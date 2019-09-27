/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.jms.JMSConnectionUtils;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsert;
import org.csi.yucca.dataservice.insertdataapi.util.AccountingLogValidation;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

/**
 * 
 * This service receives realtime http data (authentication is demanded to
 * external API), validates them and write to internal ActiveMQ. Errors are sent
 * as response HTTP and external ActiveMQ
 * 
 */
@Path("/realtime")
public class StreamValidationService {

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");
	private static final Log logAccounting = LogFactory.getLog("sdpaccounting");
	private Connection connectionExternal;
	private Connection connectionInternal;
	private Session sessionProducerExternal;
	private Session sessionProducerInternal;
	MessageProducer producerInternal;
	MessageProducer producerExternal;


	@PreDestroy
	public void preDestroy() {
		try {
			log.info("[StreamValidationService::StreamValidationService] Starting closing JMS connections");
			producerExternal.close();
			producerInternal.close();
			sessionProducerExternal.close();
			sessionProducerInternal.close();
			connectionExternal.close();
			connectionInternal.close();
			log.info("[StreamValidationService::StreamValidationService] JMS connections CLOSED");
		} catch (JMSException e) {
			log.error("[StreamValidationService::StreamValidationService] Error closing JMS connections", e);
		}
	}

	@Context
	public void setServletContext(ServletContext context) {
		try {
			initConnectionsPools();
		} catch (JMSException e) {
			log.error("[StreamValidationService::StreamValidationService] Error creating JMS connections", e);
			throw new WebApplicationException("Impossibile inizializzare il validatore " + e.getMessage());
		}

	}

	private void initConnectionsPools() throws JMSException {
		log.info("[StreamValidationService::StreamValidationService] Begin constructor: create connection pool factory");
		PooledConnectionFactory connectionFactoryInternal = JMSConnectionUtils.createInternalActiveMQConnection();
		PooledConnectionFactory connectionFactoryExternal = JMSConnectionUtils.createExternalActiveMQConnection();

		connectionExternal = connectionFactoryExternal.createConnection();
		connectionInternal = connectionFactoryInternal.createConnection();

		sessionProducerExternal = connectionExternal.createSession(false, Session.AUTO_ACKNOWLEDGE);
		sessionProducerInternal = connectionInternal.createSession(false, Session.AUTO_ACKNOWLEDGE);

		producerExternal = sessionProducerExternal.createProducer(null);
		producerInternal = sessionProducerInternal.createProducer(null);
		log.info("[StreamValidationService::StreamValidationService] Internal producer DONE");
	}

	@POST
	@Path("/input/{codTenant}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response dataInsert(String jsonData, @PathParam(value = "codTenant") String codTenant, @HeaderParam(value = "UNIQUE_ID") String uniqueid,
			@HeaderParam(value = "X-Forwarded-For") String forwardfor, @HeaderParam(value = "Authorization") String authInfo)
			throws InsertApiBaseException, InsertApiRuntimeException, JMSException {
		if (connectionExternal == null || connectionInternal == null || sessionProducerExternal == null || sessionProducerInternal == null || producerExternal == null
				|| producerInternal == null)
			initConnectionsPools();
		// tenant zero to stop
		dataRealtimeValidationInternalInsert(jsonData, codTenant, uniqueid, forwardfor, authInfo, producerInternal, sessionProducerInternal, producerExternal,
				sessionProducerExternal, "http");
		return Response.status(Status.ACCEPTED).build(); // out is not used
															// (realtime http
															// endpoint returns
															// empty body)
	}
	
	public Response dataInsertMQTT(String jsonData, String codTenant, String uniqueid,String forwardfor,String authInfo)
			throws InsertApiBaseException, InsertApiRuntimeException, JMSException {
		if (connectionExternal == null || connectionInternal == null || sessionProducerExternal == null || sessionProducerInternal == null || producerExternal == null
				|| producerInternal == null)
			initConnectionsPools();
		dataRealtimeValidationInternalInsert(jsonData, codTenant, uniqueid, forwardfor, authInfo, producerInternal, sessionProducerInternal, producerExternal,
				sessionProducerExternal, "mqtt");
		return Response.status(Status.ACCEPTED).build(); // out is not used
															// (realtime http
															// endpoint returns
															// empty body)
	}


	public JSONObject getSmartobject_StreamFromJson(String codTenant, String jsonData) throws Exception {
		return InsertApiLogic.getSmartobject_StreamFromJson(codTenant, jsonData);
	}

	/*
	 * This method validate messages in the form of: { "stream" : "flussoProva",
	 * "sensor" : "550e8400-e29b-41d4-a716-446655440002", "values" : [{ "time" :
	 * "2014-05-13T17:08:58+0200", "components" : { "temp" : "17.4", "wind" :
	 * "50" } }, { ………… ………… ………… ………… }, { "time" : "2014-05-13T17:08:58+0200",
	 * "components" : { "temp" : "17.4", "wind" : "50" } } ] }
	 * 
	 * You cannot send data to many different streams in a single call (you can
	 * do this in dataInsert method)
	 * 
	 */
	private void dataRealtimeValidationInternalInsert(String jsonData, String codTenant, String uniqueid, String forwardfor, String authInfo, MessageProducer producerInternal,
			Session sessionProducerInternal, MessageProducer producerExternal, Session sessionProducerExterna, String protocol) throws InsertApiBaseException {
		long starTtime = 0;
		long deltaTime = -1;
		AccountingLogValidation accLog = new AccountingLogValidation();

		starTtime = System.currentTimeMillis();
		accLog.setTenant(codTenant);
		
		accLog.setUniqueid(uniqueid + "");
		//accLog.setConnid(connid);
		
		accLog.setProtocol(protocol);
		//accLog.setOperation(operation);


		accLog.setIporigin(forwardfor);
		


		try {
			if (!AbstractService.validationJsonFormat(jsonData)) {
				throw new InsertApiBaseException("E012");
			}

			log.debug("[StreamValidationService::dataRealtimeValidationInternalInsert] BEGIN Parsing and validation ..");
			DatasetBulkInsert streamInput = new InsertApiLogic().parseSingleJsonInputStream(codTenant, jsonData);

			List<String> datiToIns = convertDataToIns(jsonData);
			
			log.debug("[InsertApi::dataRealtimeValidationInternalInsert] END Parsing and validation. Elapsed[" + (System.currentTimeMillis() - starTtime) + "]");
			String topic = "input." + codTenant + "." + streamInput.getSensor() + "_" + streamInput.getStream();
			accLog.setDestination(topic);
			for (String datoToIns : datiToIns) {
				inserimentoInActiveMq(topic, producerInternal, sessionProducerInternal, datoToIns);
			}
			log.debug("[InsertApi::dataRealtimeValidationInternalInsert] END inserimentoGeneralizzato. Elapsed[" + (System.currentTimeMillis() - starTtime) + "]");
			int inDataCount = 0;

			log.debug("[StreamValidationService::dataRealtimeValidationInternalInsert] report inserimento: ");
			accLog.setSensor_stream(streamInput.getSensor() + "|" + streamInput.getStream());
			accLog.setError(streamInput.getStatus());
			inDataCount += streamInput.getNumRowToInsFromJson();

			log.debug("[StreamValidationService::dataRealtimeValidationInternalInsert] inDataCount --> " + inDataCount);
			accLog.setNumevents(inDataCount);

		} catch (InsertApiBaseException insEx) {
			log.warn("[InsertApi::dataRealtimeValidationInternalInsert] InsertApiBaseException " + insEx.getErrorCode() + " - " + insEx.getErrorName());
			accLog.setError(insEx.getErrorCode() + " - " + insEx.getErrorName());
			String topic = "output." + codTenant + ".errors";
			accLog.setDestination(topic);
			insEx.setOutput(topic);
			insEx.setErrorMessage(jsonData);
			String errorJson = insEx.toJson();
			try {
				inserimentoInActiveMq(topic, producerExternal, sessionProducerExternal, errorJson); // TODO
																									// change
			} catch (Exception e) {
				log.fatal("[StreamValidationService::dataRealtimeValidationInternalInsert] Error 1 " + e.getMessage());
				e.printStackTrace();
			}
			throw insEx;
		} catch (Exception e) {
			log.fatal("[InsertApi::dataRealtimeValidationInternalInsert] GenericException WITH TENANTCODE[" + codTenant + "] WITH DATA [" + jsonData + "]" + e);
			e.printStackTrace();
			String topic = "output." + codTenant + ".errors";

			try {
				inserimentoInActiveMq(topic, producerExternal, sessionProducerExternal, "{\"errorName\":\"Internal Server Error\"}"); // TODO
																																		// change
			} catch (Exception e1) {
				log.fatal("[StreamValidationService::dataRealtimeValidationInternalInsert] Error 2 " + e1.getMessage());
				e.printStackTrace();
			}
			throw new InsertApiRuntimeException(e);
		} finally {
			try {
				deltaTime = System.currentTimeMillis() - starTtime;
				accLog.setElapsed(deltaTime);

			} catch (Exception e) {
			}
			logAccounting.info(accLog.toString());
			log.info("[InsertApi::dataRealtimeValidationInternalInsert] END --> elapsed: " + deltaTime);
		}

		// response.setStatus(Status.ACCEPTED.getStatusCode());
		return;

	}
	
	public static List<String> convertDataToIns(String jsonData) throws ParseException {
		//jsonData è fatto cosi' { 
//		"stream": "simple",
//		"application": "sender",
//		"values": [ 
//		{ 
//		"time": "2014-05-13T17:08:58Z", 
//		"components": {     
//		  "booleano" : true,
//		    "stringa" : "todel",
//		  "numero":2.2
//		 } 
//		}
//		]
//		}
		//{"stream":"streamtstcsp","sensor":"cea99c00-c178-50fa-9a24-d32b8910dcd5","values":{"time":"2014-05-13T17:08:58Z","components":{"c1":1}}}
		List<String> datiToIns = new LinkedList<String>();
		JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
		JSONObject parsedJsonData = (JSONObject) parser.parse(jsonData);
	//	JSONObject parsedJsonData = JsonPath.read(jsonData, "$[0]");
		JSONArray  values = (JSONArray)parsedJsonData.get("values");
		for (int i = 0; i < values.size(); i++) {
			JSONObject jsonDataToSend = new JSONObject();
			jsonDataToSend.put("stream",parsedJsonData.get("stream"));
			if(parsedJsonData.get("application")!=null)
				jsonDataToSend.put("application",parsedJsonData.get("application"));
			else if(parsedJsonData.get("sensor")!=null)
				jsonDataToSend.put("sensor",parsedJsonData.get("sensor"));
			jsonDataToSend.put("values",values.get(i));
			datiToIns.add(jsonDataToSend.toJSONString());
		}
		return datiToIns;
	}

	public static void main(String[] args) {
		String input1 = "{ \"stream\": \"simple\",\"application\": \"sender\",\"values\": [ { \"time\": \"2014-05-13T17:08:58Z\", \"components\": { \"booleano\" : true, \"stringa\" : \"todel\", \"numero\":2.2 } }]}";
		String input = "{ \"stream\": \"streamtstcsp\",\"sensor\": \"cea99c00-c178-50fa-9a24-d32b8910dcd5\",\"values\": [ { \"time\": \"2014-05-13T17:08:58Z\", \"components\": { \"c1\" : 1}} , { \"time\": \"2015-05-13T17:08:58Z\", \"components\": { \"c1\" : 2 } }]}";
		try {
			List<String> outputs = convertDataToIns(input);
			for (String output : outputs) {
				System.out.println(output);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void inserimentoInActiveMq(String topic, MessageProducer producerInternal, Session sessionProducerInternal, String jsonData) throws Exception {
		// int cnt = 0;
		// boolean indiceDaCReare=true;
		try {
			Long startTimeX = System.currentTimeMillis();
			log.debug("[InsertApiLogic::insertInternalActiveMq] BEGIN Insert code ..." + topic);

			TextMessage message = sessionProducerInternal.createTextMessage();
			message.setText(jsonData);
			Topic topicCreated = sessionProducerInternal.createTopic(topic);
			log.info("[InsertApiLogic::insertInternalActiveMq] createTopic  Elapsed[" + (System.currentTimeMillis() - startTimeX) + "]");
			producerInternal.send(topicCreated, message);
			log.info("[InsertApiLogic::insertInternalActiveMq] send END  Elapsed[" + (System.currentTimeMillis() - startTimeX) + "]");
		} catch (InsertApiRuntimeException e1) {
			log.error("[InsertApiLogic::insertManager] InsertApiRuntimeException ", e1);
			e1.printStackTrace();
			throw e1;
		} catch (Throwable e2) {
			log.error("[InsertApiLogic::insertManager] UnknownException, presume redelivery " + e2);
			throw new InsertApiRuntimeException(e2);
		}
		long startTimeX = System.currentTimeMillis();
		// mongoAccess.updateStatusRecordArray(tenant, idRequest, "end_ins",
		// datiToIns);
		log.debug("[InsertApiLogic::insertManager] END updateStatus  Elapsed[" + (System.currentTimeMillis() - startTimeX) + "]");
		return;

	}

	// connectionExternal = connectionFactoryExternal.createConnection();
	//
	// Session sessionProducer = connectionExternal.createSession(false,
	// Session.AUTO_ACKNOWLEDGE);
	//
	// Destination destinationProducer =
	// sessionProducer.createTopic(VIRTUAL_QUEUE_PRODUCER_INSERTAPI_OUTPUT + "."
	// + codTenant + "." + smartobjectStream);
	// log.debug("[JMSMessageListener::forwardMessage] Connected to queue:" +
	// destinationProducer.toString());
	// MessageProducer producer =
	// sessionProducer.createProducer(destinationProducer);
	// message.clearProperties();
	// message.clearBody();
	// message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
	// message.setText(correctedMsg.toJSONString());

}
