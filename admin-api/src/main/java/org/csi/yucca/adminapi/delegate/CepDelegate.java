/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.InternalDettaglioStream;
import org.csi.yucca.adminapi.util.DataType;
import org.csi.yucca.adminapi.util.SoType;
import org.csi.yucca.adminapi.util.Type;
import org.csi.yucca.adminapi.util.WebServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Service
@PropertySource(value = { "classpath:adminapi.properties" })
public class CepDelegate {

	@Value("${cep.soap.endpoints}")
	private String cepSoapEndpoints;
	private String[] cepSoapEndpoint;

//	@Value("${cep.soap.endpoint2}")
//	private String cepSoapEndpoint2;

	@Value("${cep.soap.endpoint.user}")
	private String cepSoapEndpointUser;

	@Value("${cep.soap.endpoint.password}")
	private String cepSoapEndpointPassword;

	private static CepDelegate cepDelegate;

	private static final Logger logger = Logger.getLogger(CepDelegate.class);

//	public static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
//	public static final String SOAP_ACTION_HEADER_KEY = "SOAPAction";

//	public static final String VALIDATE_SIDDHI_QUERIES_SOAP_ACTION = "urn:validateSiddhiQueries";
//	public static final String VALIDATE_SIDDHI_QUERIES_CONTENT_TYPE = "text/xml";

//	public WebServiceResponse callWebService( String wsURL, String username, String password, 
//            String xmlInput, String soapAction, String contentType)throws NoSuchAlgorithmException, KeyManagementException, IOException {
//		
//		HttpClientBuilder clientBuilder = HttpClients.custom();
//		
//		CredentialsProvider provider = new BasicCredentialsProvider();
//		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
//		provider.setCredentials(AuthScope.ANY, credentials);
//		clientBuilder.setDefaultCredentialsProvider(provider);
//		CloseableHttpClient client = clientBuilder.build();
//		HttpPost post = new HttpPost(wsURL);
//		HttpEntity str = new StringEntity(xmlInput);
//		post.setEntity(str);post.setHeader(CONTENT_TYPE_HEADER_KEY, contentType);
//		post.setHeader(SOAP_ACTION_HEADER_KEY, soapAction);
//		
//		CloseableHttpResponse closeableHttpResponse = client.execute(post);
//		
//		return new WebServiceResponse(closeableHttpResponse);
//	}

	public CepDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		logger.info("[CepDelegate::CepDelegate]  cep Soap Endpoint " + cepSoapEndpoint);
	}

	public void initSoapEndpoints() {
		cepSoapEndpoint = cepSoapEndpoints.split("\\|");
	}

	public static CepDelegate build() {
		if (cepDelegate == null) {
			cepDelegate = new CepDelegate();
			cepDelegate.initSoapEndpoints();
		}
		return cepDelegate;
	}

	public int getTotalNumOfSoapEndpoint() {
		return cepSoapEndpoint.length;
	}

	/**
	 * 
	 * @param queryExpressions
	 * @param inputStreamDefiniitons
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public WebServiceResponse validateSiddhiQueriesWebService(String queryExpressions, List<String> inputStreamDefiniitons)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {

		// StringBuilder sb = new StringBuilder ();
		logger.info("[CepDelegate::validateSiddhiQueriesWebService]  queryExpressions " + queryExpressions);

		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://admin.processor.event.carbon.wso2.org\">";
		xmlInput += "   <soapenv:Header/>";
		xmlInput += "   <soapenv:Body>";

		xmlInput += "      <ser:validateSiddhiQueries>";
		for (String element : inputStreamDefiniitons) {
			xmlInput += "		<ser:inputStreamDefiniitons>" + element + "</ser:inputStreamDefiniitons>";
		}
		xmlInput += "         <ser:queryExpressions>" + queryExpressions + "</ser:queryExpressions>";
		xmlInput += "      </ser:validateSiddhiQueries>";

		xmlInput += "   </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";
		System.out.println("xmlInput" + xmlInput);

		return WebServiceDelegate.callWebService(this.cepSoapEndpoint[0]+"/services/EventProcessorAdminService", this.cepSoapEndpointUser, this.cepSoapEndpointPassword, xmlInput,
				"urn:validateSiddhiQueries", "text/xml");
	}

	public WebServiceResponse editEventStreamInfo(DettaglioStream stream, boolean isInput, int endpointIndex)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {

		logger.info("[CepDelegate::editEventStreamInfo]  START");

		String action = "editEventStreamInfo";
		String streamKey = stream.getTenantCode() + "__" + stream.getSmartObjectCode().replaceAll("\\-", "_") + "__" + stream.getStreamcode();


		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:int=\"http://internal.admin.manager.stream.event.carbon.wso2.org\" xmlns:xsd=\"http://internal.admin.manager.stream.event.carbon.wso2.org/xsd\">";
		xmlInput += "   <soapenv:Header/>";
		xmlInput += "   <soapenv:Body>";
		xmlInput += "     <int:editEventStreamInfo>";
		xmlInput += "      <int:oldStreamId>" + (isInput ? "input" : "output") + "__" + streamKey + ":1.0.0</int:oldStreamId>";
		xmlInput += "      <int:eventStreamName>" + (isInput ? "input" : "output") + "__" + streamKey + "</int:eventStreamName>";

		xmlInput += "      <int:eventStreamVersion>1.0.0</int:eventStreamVersion>";
		xmlInput += "      <int:metaEventStreamAttributeDtos>";
		xmlInput += "         <xsd:attributeName>source</xsd:attributeName>";
		xmlInput += "         <xsd:attributeType>string</xsd:attributeType>";
		xmlInput += "      </int:metaEventStreamAttributeDtos>";
		xmlInput += "      <int:payloadEventStreamAttributeDtos>";
		xmlInput += "         <xsd:attributeName>time</xsd:attributeName>";
		xmlInput += "         <xsd:attributeType>string</xsd:attributeType>";
		xmlInput += "      </int:payloadEventStreamAttributeDtos>";

		for (ComponentJson c : stream.getComponents()) {
			xmlInput += "      <int:payloadEventStreamAttributeDtos>";
			xmlInput += "      <xsd:attributeName>" + c.getName() + "</xsd:attributeName>";
			if (c.getDatatypecode().equals(DataType.DATE_TIME.code()))
				xmlInput += "      <xsd:attributeType>string</xsd:attributeType>";
			else if (c.getDatatypecode().equals(DataType.LATITUDE.code()) || c.getDatatypecode().equals(DataType.LONGITUDE.code()))
				xmlInput += "      <xsd:attributeType>double</xsd:attributeType>";
			else
				xmlInput += "      <xsd:attributeType>" + c.getDatatypecode().toLowerCase() + "</xsd:attributeType>";
			xmlInput += "      </int:payloadEventStreamAttributeDtos>";
		}

		xmlInput += "      <int:eventStreamDescription>" + (isInput ? "input" : "output") + "__" + streamKey + "</int:eventStreamDescription>";
		xmlInput += "      <int:eventStreamNickName>" + (isInput ? "input" : "output") + "__" + streamKey + "</int:eventStreamNickName>";

		xmlInput += "    </int:editEventStreamInfo>";
		xmlInput += "  </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";
		System.out.println("xmlInput" + xmlInput);
		return WebServiceDelegate.callWebService(cepSoapEndpoint[endpointIndex]+"/services/EventStreamAdminService", this.cepSoapEndpointUser, this.cepSoapEndpointPassword, xmlInput, action,
				"text/xml");
	}

	public WebServiceResponse editActiveEventBuilderConfiguration(DettaglioStream stream, int endpointIndex)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {

		logger.info("[CepDelegate::editActiveEventBuilderConfiguration]  START");

		String action = "editActiveEventBuilderConfiguration";
		String streamKey = stream.getTenantCode() + "__" + stream.getSmartObjectCode().replaceAll("\\-", "_") + "__" + stream.getStreamcode();

		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:int=\"http://internal.admin.builder.event.carbon.wso2.org\" xmlns:xsd=\"http://internal.admin.builder.event.carbon.wso2.org/xsd\">";
		xmlInput += "   <soapenv:Header/>";
		xmlInput += "   <soapenv:Body>";
		xmlInput += "     <int:editActiveEventBuilderConfiguration>";

		xmlInput += "       <int:originalEventBuilderName>" + streamKey + "__EventBuilder</int:originalEventBuilderName>";

		xmlInput += "       <int:eventBuilderConfigXml><![CDATA[";
		xmlInput += "       	<eventBuilder name=\"" + stream.getTenantCode() + "__" + stream.getSmartObjectCode().replaceAll("\\-", "_") + "__" + stream.getStreamcode()
				+ "__EventBuilder\" statistics=\"disable\" trace=\"disable\" xmlns=\"http://wso2.org/carbon/eventbuilder\">";
		xmlInput += "           	<from eventAdaptorName=\"JMSMBInternalInputEventAdaptor.xml\" eventAdaptorType=\"jms\">";
		int mbNum = endpointIndex+1;
		xmlInput += "               	<property name=\"transport.jms.Destination\">VirtualQueueConsumer.mb" + mbNum + ".input." + stream.getTenantCode()
				+ "." + stream.getSmartObjectCode() + "_" + stream.getStreamcode() + "</property>";
		xmlInput += "           	</from>";
		xmlInput += "           	<mapping customMapping=\"enable\" type=\"json\">";
		String jsonPath = "";
		if (stream.getIdSoType().equals(SoType.APPLICATION.id()))
			jsonPath = "application";
		else if (stream.getIdSoType().equals(SoType.DEVICE.id()) || stream.getIdSoType().equals(SoType.FEED_TWEET.id()))
			jsonPath = "sensor";
		xmlInput += "            <property><from jsonPath=\"" + jsonPath + "\" /><to name=\"meta_source\" type=\"string\" /></property>";
		xmlInput += "            <property><from jsonPath=\"values.time\" /><to name=\"time\" type=\"string\" /></property>";
		for (ComponentJson c : stream.getComponents()) {

			xmlInput += "            <property><from jsonPath=\"values.components." + c.getName() + "\" />";
			if (c.getDatatypecode().equals(DataType.DATE_TIME.code()))
				xmlInput += "      <to name=\"" + c.getName() + "\" type=\"string\" />";
			else if (c.getDatatypecode().equals(DataType.LATITUDE.code()) || c.getDatatypecode().equals(DataType.LONGITUDE.code()))
				xmlInput += "      <to name=\"" + c.getName() + "\" type=\"double\" />";
			else
				xmlInput += "      <to name=\"" + c.getName() + "\" type=\"" + c.getDatatypecode() + "\" />";
			xmlInput += "            </property>";

		}
		xmlInput += "          </mapping>";
		xmlInput += "          <to streamName=\"input__" + stream.getTenantCode() + "__" + stream.getSmartObjectCode().replaceAll("\\-", "_") + "__" + stream.getStreamcode()
				+ "\" version=\"1.0.0\" />";
		xmlInput += "        </eventBuilder>]]>";

		xmlInput += "       </int:eventBuilderConfigXml>";
		xmlInput += "     </int:editActiveEventBuilderConfiguration>";
		xmlInput += "  </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";
		System.out.println("xmlInput" + xmlInput);

		return WebServiceDelegate.callWebService(cepSoapEndpoint[endpointIndex]+"/services/EventBuilderAdminService", this.cepSoapEndpointUser, this.cepSoapEndpointPassword, xmlInput, action,
				"text/xml");
	}

	public WebServiceResponse editActiveEventFormatterConfiguration(DettaglioStream stream, int endpointIndex)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {

		logger.info("[CepDelegate::editActiveEventFormatterConfiguration]  START");

		String streamKey = stream.getTenantCode() + "__" + stream.getSmartObjectCode().replaceAll("\\-", "_") + "__" + stream.getStreamcode();

		String action = "editActiveEventFormatterConfiguration";
		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:int=\"http://internal.admin.formatter.event.carbon.wso2.org\" xmlns:xsd=\"http://internal.admin.formatter.event.carbon.wso2.org/xsd\">";

		xmlInput += "   <soapenv:Header/>";
		xmlInput += "   <soapenv:Body>";
		xmlInput += "     <int:editActiveEventFormatterConfiguration>";

//		xmlInput += "       <int:originalEventBuilderName>" + stream.getTenantCode() + "__" + stream.getSmartObjectCode() + "__" + stream.getStreamcode()
//				+ "__EventBuilder</int:originalEventBuilderName>";

		xmlInput += "         <int:eventFormatterConfiguration><![CDATA[";
		xmlInput += "           <eventFormatter name=\"output__" + streamKey + "__InternalEventFormatter\" ";
		xmlInput += "             statistics=\"disable\" trace=\"disable\" xmlns=\"http://wso2.org/carbon/eventformatter\">";
		xmlInput += "               <from streamName=\"output__" + streamKey + "\" version=\"1.0.0\"/>";
		xmlInput += "                 <mapping customMapping=\"enable\" type=\"json\">";
		xmlInput += "                  <inline>{ \"stream\": \"" + stream.getStreamcode() + "\",";
		String sensor = "\"sensor\": \"internal\"";
		if (stream.getSoTypeCode().equals(SoType.APPLICATION.code()))
			sensor = "\"application\": \"{{meta_source}}\"";
		else if (stream.getSoTypeCode().equals(SoType.DEVICE.code()) || stream.getSoTypeCode().equals(SoType.FEED_TWEET.code()))
			sensor = "\"sensor\": \"{{meta_source}}\"";
		xmlInput += "  " + sensor + " ";

		xmlInput += "  ,\"values\": [{\"time\": \"{{time}}\", \"components\":{";

		for (ComponentJson c : stream.getComponents())
			xmlInput += " \"" + c.getName() + "\":\"{{" + c.getName() + "}}\",";

		xmlInput = xmlInput.substring(0, xmlInput.length() - 1);
		xmlInput += " }}]}   </inline>";
		xmlInput += "                 </mapping>";

		xmlInput += "               <to eventAdaptorName=\"JMSMBInternalOutputEventAdaptor.xml\" eventAdaptorType=\"jms\">";
		xmlInput += "                  <property name=\"transport.jms.Destination\">input." + stream.getTenantCode() + "." + stream.getSmartObjectCode().replaceAll("\\-", "_") + "_"
				+ stream.getStreamcode() + "</property>";
		xmlInput += "               </to>";
		xmlInput += "             </eventFormatter>";
		xmlInput += "           ]]>";
		xmlInput += "        </int:eventFormatterConfiguration>";
		xmlInput += "     <int:eventFormatterName>output__" + streamKey + "__InternalEventFormatter</int:eventFormatterName>";
		xmlInput += "     </int:editActiveEventFormatterConfiguration>";
		xmlInput += "  </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";
		System.out.println("xmlInput" + xmlInput);

		return WebServiceDelegate.callWebService(cepSoapEndpoint[endpointIndex]+"/services/EventFormatterAdminService", this.cepSoapEndpointUser, this.cepSoapEndpointPassword, xmlInput, action,
				"text/xml");
	}

	public WebServiceResponse editActiveExecutionPlanConfiguration(DettaglioStream stream, List<InternalDettaglioStream> internalStreams, int endpointIndex)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {

		logger.info("[CepDelegate::editActiveExecutionPlanConfiguration]  START");

		String streamKey = stream.getTenantCode() + "__" + stream.getSmartObjectCode().replaceAll("\\-", "_") + "__" + stream.getStreamcode();

		String action = "editActiveExecutionPlanConfiguration";
		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://admin.processor.event.carbon.wso2.org\" xmlns:xsd=\"http://admin.processor.event.carbon.wso2.org/xsd\">";
		xmlInput += "   <soapenv:Header/>";
		xmlInput += "   <soapenv:Body>";
		xmlInput += "     <adm:editActiveExecutionPlanConfiguration>";
		xmlInput += "              <adm:configuration><![CDATA[";
		xmlInput += "              <executionPlan  name=\"process__" + streamKey + "__ExecutionPlan\" statistics=\"disable\"";
		xmlInput += "       trace=\"disable\" xmlns=\"http://wso2.org/carbon/eventprocessor\">";
		xmlInput += "       <description>Execution plan for " + streamKey + "</description>";
		xmlInput += "       <siddhiConfiguration>";
		xmlInput += "         <property name=\"siddhi.enable.distributed.processing\">" + (getTotalNumOfSoapEndpoint() > 1 ? "RedundantNode" : "false")
				+ "</property>";
		xmlInput += "         <property name=\"siddhi.persistence.snapshot.time.interval.minutes\">0</property>";
		xmlInput += "       </siddhiConfiguration>";
		xmlInput += "       <importedStreams>";
		if (!stream.getSoTypeCode().equals(Type.INTERNAL.code()))
			xmlInput += "         <stream as=\"input\" name=\"input__" + streamKey + "\" version=\"1.0.0\"/>";
		else {
			for (InternalDettaglioStream s : internalStreams) {
				xmlInput += "         <stream as=\"" + s.getAliasName() + "\" name=\"input__" + s.getTenantCode() + "__" + s.getSmartObjectCode().replaceAll("\\-", "_") + "__"
						+ s.getStreamcode() + "\" version=\"1.0.0\"/>";
			}
		}
		xmlInput += "       </importedStreams>";
		xmlInput += "       <queryExpressions><![CDATA[";
		if (!stream.getSoTypeCode().equals(Type.INTERNAL.code())) {
			xmlInput += "     from input";
			xmlInput += "     insert into outputStream;";
		} else {
			xmlInput += "     " + stream.getInternalquery();
		}
		xmlInput += "     ]]]]><![CDATA[></queryExpressions>";
		xmlInput += "       <exportedStreams>";
		xmlInput += "         <stream name=\"output__" + streamKey + "\" valueOf=\"outputStream\" version=\"1.0.0\"/>";
		xmlInput += "       </exportedStreams>";
		xmlInput += "     </executionPlan>]]></adm:configuration>";
		xmlInput += "              <!--Optional:-->";
		xmlInput += "              <adm:name>process__" + streamKey + "__ExecutionPlan</adm:name>";
		xmlInput += "     </adm:editActiveExecutionPlanConfiguration>";
		xmlInput += "  </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";
		System.out.println("xmlInput" + xmlInput);

		return WebServiceDelegate.callWebService(cepSoapEndpoint[endpointIndex]+"/services/EventProcessorAdminService", this.cepSoapEndpointUser, this.cepSoapEndpointPassword, xmlInput, action,
				"text/xml");
	}

	public WebServiceResponse undeployActiveExecutionPlanConfiguration(DettaglioStream stream, int endpointIndex)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {

		logger.info("[CepDelegate::undeployActiveExecutionPlanConfiguration]  START");

		String streamKey = stream.getTenantCode() + "__" + stream.getSmartObjectCode() + "__" + stream.getStreamcode();

		String action = "undeployActiveExecutionPlanConfiguration";
		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://admin.processor.event.carbon.wso2.org\" xmlns:xsd=\"http://admin.processor.event.carbon.wso2.org/xsd\">";
		xmlInput += "   <soapenv:Header/>";
		xmlInput += "   <soapenv:Body>";
		xmlInput += "     <adm:undeployActiveExecutionPlanConfiguration>";
		xmlInput += "        <adm:name>process__" + streamKey + "__ExecutionPlan</adm:name>";
		xmlInput += "     </adm:undeployActiveExecutionPlanConfiguration>";
		xmlInput += "  </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";
		System.out.println("xmlInput" + xmlInput);

		return WebServiceDelegate.callWebService(cepSoapEndpoint[endpointIndex]+"/services/EventProcessorAdminService", this.cepSoapEndpointUser, this.cepSoapEndpointPassword, xmlInput, action,
				"text/xml");
	}

	public WebServiceResponse undeployActiveEventFormatterConfiguration(DettaglioStream stream, int endpointIndex)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {

		logger.info("[CepDelegate::undeployActiveEventFormatterConfiguration]  START");

		String streamKey = stream.getTenantCode() + "__" + stream.getSmartObjectCode() + "__" + stream.getStreamcode();

		String action = "undeployActiveEventFormatterConfiguration";
		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:int=\"http://internal.admin.formatter.event.carbon.wso2.org\" xmlns:xsd=\"http://internal.admin.formatter.event.carbon.wso2.org/xsd\">";

		xmlInput += "   <soapenv:Header/>";
		xmlInput += "   <soapenv:Body>";
		xmlInput += "     <int:undeployActiveEventFormatterConfiguration>";
		xmlInput += "     <int:eventFormatterName>output__" + streamKey + "__InternalEventFormatter</int:eventFormatterName>";
		xmlInput += "     </int:undeployActiveEventFormatterConfiguration>";
		xmlInput += "  </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";
		System.out.println("xmlInput" + xmlInput);

		return WebServiceDelegate.callWebService(cepSoapEndpoint[endpointIndex]+"/services/EventFormatterAdminService", this.cepSoapEndpointUser, this.cepSoapEndpointPassword, xmlInput, action,
				"text/xml");
	}

	public WebServiceResponse undeployActiveEventBuilderConfiguration(DettaglioStream stream, int endpointIndex)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {

		logger.info("[CepDelegate::undeployActiveEventBuilderConfiguration]  START");

		String action = "undeployActiveEventBuilderConfiguration";

		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:int=\"http://internal.admin.builder.event.carbon.wso2.org\" xmlns:xsd=\"http://internal.admin.builder.event.carbon.wso2.org/xsd\">";
		xmlInput += "   <soapenv:Header/>";
		xmlInput += "   <soapenv:Body>";
		xmlInput += "     <int:undeployActiveEventBuilderConfiguration>";

		xmlInput += "       <int:eventBuilderName>" + stream.getTenantCode() + "__" + stream.getSmartObjectCode().replaceAll("\\-", "_") + "__" + stream.getStreamcode()
				+ "__EventBuilder</int:eventBuilderName>";

		xmlInput += "     </int:undeployActiveEventBuilderConfiguration>";
		xmlInput += "  </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";
		System.out.println("xmlInput" + xmlInput);

		return WebServiceDelegate.callWebService(cepSoapEndpoint[endpointIndex]+"/services/EventBuilderAdminService", this.cepSoapEndpointUser, this.cepSoapEndpointPassword, xmlInput, action,
				"text/xml");
	}

	public WebServiceResponse removeEventStreamInfo(DettaglioStream stream, boolean isInput, int endpointIndex)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {

		logger.info("[CepDelegate::removeEventStreamInfo]  START");

		String action = "removeEventStreamInfo";

		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:int=\"http://internal.admin.manager.stream.event.carbon.wso2.org\" xmlns:xsd=\"http://internal.admin.manager.stream.event.carbon.wso2.org/xsd\">";
		xmlInput += "   <soapenv:Header/>";
		xmlInput += "   <soapenv:Body>";
		xmlInput += "     <int:removeEventStreamInfo>";
		xmlInput += "      <int:eventStreamName>" + (isInput ? "input" : "output") + "__" + stream.getTenantCode() + "__" + stream.getSmartObjectCode() + "__"
				+ stream.getStreamcode() + "</int:eventStreamName>";

		xmlInput += "      <int:eventStreamVersion>1.0.0</int:eventStreamVersion>";
		xmlInput += "    </int:removeEventStreamInfo>";
		xmlInput += "  </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";
		System.out.println("xmlInput" + xmlInput);

		return WebServiceDelegate.callWebService(cepSoapEndpoint[endpointIndex]+"/services/EventStreamAdminService", this.cepSoapEndpointUser, this.cepSoapEndpointPassword, xmlInput, action,
				"text/xml");
	}
}
