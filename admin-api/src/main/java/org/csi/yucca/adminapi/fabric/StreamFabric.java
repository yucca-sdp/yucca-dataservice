/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.fabric;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.delegate.CepDelegate;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.FabricException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.StreamMapper;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.InternalDettaglioStream;
import org.csi.yucca.adminapi.request.ActionRequest;
import org.csi.yucca.adminapi.service.StreamService;
import org.csi.yucca.adminapi.util.Action;
import org.csi.yucca.adminapi.util.FeedbackStatus;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.Type;
import org.csi.yucca.adminapi.util.WebServiceResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Configuration
@PropertySource(value = { "classpath:adminapi.properties" })
public class StreamFabric {

	private static final Logger logger = Logger.getLogger(StreamFabric.class);

	private static StreamFabric streamFabric;

	private StreamService streamService;
	private StreamMapper streamMapper;

	public StreamFabric(StreamService streamService, StreamMapper streamMapper) {
		this.streamService = streamService;
		this.streamMapper = streamMapper;
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public static StreamFabric build(StreamService streamService, StreamMapper streamMapper) {
		if (streamFabric == null)
			streamFabric = new StreamFabric(streamService, streamMapper);
		return streamFabric;
	}

	public FabricResponse execAction(String action, DettaglioStream dettaglioStream) {

		FabricResponse fabricResponse = null;
		if (Action.INSTALLATION.code().equals(action))
			fabricResponse = execInstallation(dettaglioStream);
		else if (Action.DELETE.code().equals(action))
			fabricResponse = execUninstallation(dettaglioStream);
		
		ActionRequest actionRequest = new ActionRequest();
		actionRequest.setAction(action);
		if(FabricResponse.STATUS_SUCCESS.equals(fabricResponse.getStatus()))
			actionRequest.setStatus(FeedbackStatus.OK.code());
		else
			actionRequest.setStatus(FeedbackStatus.KO.code());
		try {
			streamService.actionFeedback(actionRequest, dettaglioStream.getIdstream());
			fabricResponse.addLogInfo("STEP LAST - Updated stream satus - INSTALLED(" + Status.INSTALLED.id() + ") ok ");
			fabricResponse.setStatus(FabricResponse.STATUS_SUCCESS);
		} catch (BadRequestException e) {
			fabricResponse.addLogError("STEP LAST - Updated stream satus - BadRequestException(" + Status.INSTALLED.id() + ") error: " + e.getMessage());
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			e.printStackTrace();
		} catch (NotFoundException e) {
			fabricResponse.addLogError("STEP LAST - Updated stream satus - BadRequestException(" + Status.INSTALLED.id() + ") error: " + e.getMessage());
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			e.printStackTrace();
		} catch (Exception e) {
			fabricResponse.addLogError("STEP LAST - Updated stream satus - BadRequestException(" + Status.INSTALLED.id() + ") error: " + e.getMessage());
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			e.printStackTrace();
		}

		return fabricResponse;
	}

	private FabricResponse execInstallation(DettaglioStream dettaglioStream) {

		FabricResponse fabricResponse = new FabricResponse("install stream " + dettaglioStream.getStreamcode());

		try {
			// update tenant status
			try {
				streamService.updateStreamStatus(Status.INSTALLATION_IN_PROGRESS.id(), dettaglioStream.getIdstream());
				fabricResponse.addLogInfo(
						"STEP 0 - Updated stream satus - " + getStreamkeyForLog(dettaglioStream) + ", INSTALLATION_IN_PROGRESS(" + Status.INSTALLATION_IN_PROGRESS.id() + ") ok ");
			} catch (BadRequestException e) {
				logger.error("[StreamFabric::execInstallation] BadRequestException error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execInstallation: BadRequestException " + e.getMessage());		
			} catch (NotFoundException e) {
				logger.error("[StreamFabric::execInstallation] NotFoundException error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execInstallation: NotFoundException " + e.getMessage());		
			} catch (Exception e) {
				logger.error("[StreamFabric::execInstallation] Exception error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execInstallation: Exception " + e.getMessage());		
			}
			//datasourceMapper.updateDataSourceStatus(Status.INSTALLATION_IN_PROGRESS.id(), dettaglioStream.getIdDataSource(),
			//		dettaglioStream.getDatasourceversion());
			
			fabricResponse.addLogDebug("Smartobject type: " + dettaglioStream.getSoTypeCode());
			fabricResponse.addLogDebug("Used Internal Count: " + dettaglioStream.getUsedInInternalCount());
			logger.info("[StreamFabric::execInstallation] dettaglioStream.getSoTypeCode(): " + dettaglioStream.getSoTypeCode());

			int step = 1;
			
			if (dettaglioStream.getSoTypeCode().equals(Type.INTERNAL.code())) {

				List<InternalDettaglioStream> internalStream = streamMapper.selectInternalStream(dettaglioStream.getIdDataSource(),
						dettaglioStream.getDatasourceversion());

				for (InternalDettaglioStream internalDettaglioStream : internalStream) {
					execStreamDefinition(fabricResponse, ""+step, internalDettaglioStream, true);
					step++;
				}
				execStreamDefinition(fabricResponse, ""+step, dettaglioStream, false);
				step++;
				for (InternalDettaglioStream internalDettaglioStream : internalStream) {
					execCreateBuilder(fabricResponse, ""+step, internalDettaglioStream);
					step++;
				}				
				
				execCreateFormatter(fabricResponse, ""+step, dettaglioStream);
				step++;
				execCreateQuery(fabricResponse, ""+step, dettaglioStream, internalStream);
				step++;
			
			}
			else if(dettaglioStream.getUsedInInternalCount()>0){
				execStreamDefinition(fabricResponse, ""+step, dettaglioStream, true);
				step++;
				execCreateBuilder(fabricResponse, ""+step, dettaglioStream);
				step++;
			}
			fabricResponse.setStatus(FabricResponse.STATUS_SUCCESS);

			// update stream status
			//datasourceMapper.updateDataSourceStatus(Status.INSTALLED.id(), dettaglioStream.getIdDataSource(),dettaglioStream.getDatasourceversion());
		} catch (FabricException e) {
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			fabricResponse.addLogError("FabricException error " + e.getMessage());
			e.printStackTrace();
		}

		return fabricResponse;

	}



	

	private FabricResponse execUninstallation(DettaglioStream dettaglioStream) {
		FabricResponse fabricResponse = new FabricResponse("uninstall stream " + dettaglioStream.getStreamcode());
		try {
			//datasourceMapper.updateDataSourceStatus(Status.UNINSTALLATION_IN_PROGRESS.id(), dettaglioStream.getIdDataSource(),
			//		dettaglioStream.getDatasourceversion());
			try {
				streamService.updateStreamStatus(Status.UNINSTALLATION_IN_PROGRESS.id(), dettaglioStream.getIdstream());
				fabricResponse.addLogInfo(
						"STEP 0 - Updated stream satus - " + getStreamkeyForLog(dettaglioStream) + ", UNINSTALLATION_IN_PROGRESS(" + Status.INSTALLATION_IN_PROGRESS.id() + ") ok ");
			} catch (BadRequestException e) {
				logger.error("[StreamFabric::execUninstallation] BadRequestException error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execUninstallation: BadRequestException " + e.getMessage());		
			} catch (NotFoundException e) {
				logger.error("[StreamFabric::execUninstallation] NotFoundException error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execUninstallation: NotFoundException " + e.getMessage());		
			} catch (Exception e) {
				logger.error("[StreamFabric::execUninstallation] Exception error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execUninstallation: Exception " + e.getMessage());		
			}

			
			fabricResponse.addLogDebug("Smartobject type: " + dettaglioStream.getSoTypeCode());
			fabricResponse.addLogDebug("Used Internal Count: " + dettaglioStream.getUsedInInternalCount());
			logger.info("[StreamFabric::execUninstallation] dettaglioStream.getSoTypeCode(): " + dettaglioStream.getSoTypeCode());
	
			int step = 1;
			if (dettaglioStream.getSoTypeCode().equals(Type.INTERNAL.code())) {
				execDeleteQuery(fabricResponse, "" + step, dettaglioStream);
				step++;
				execDeleteFormatter(fabricResponse,"" + step, dettaglioStream);
				step++;
				if(dettaglioStream.getUsedInInternalCount()>0){
					execDeleteDefinition(fabricResponse, "" + step, dettaglioStream, true);
				}
				else {
					execDeleteDefinition(fabricResponse, "" + step, dettaglioStream, false);
				}
			}
			else if(dettaglioStream.getUsedInInternalCount()>0){
				execDeleteBuilder(fabricResponse, "" + step, dettaglioStream);
				step++;
			}
			
			// update stream status
//			datasourceMapper.updateDataSourceStatus(Status.UNINSTALLATION.id(), dettaglioStream.getIdDataSource(),
//					dettaglioStream.getDatasourceversion());
//			fabricResponse.addLogInfo("STEP " + step + "- Updated stream satus - INSTALLED(" + Status.INSTALLED.id() + ") ok ");
	
			fabricResponse.setStatus(FabricResponse.STATUS_SUCCESS);
		} catch (FabricException e) {
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			fabricResponse.addLogError("FabricException error " + e.getMessage());
			e.printStackTrace();
		}
	
		return fabricResponse;

	}
	
	private String getStreamkeyForLog(DettaglioStream stream) {
		return "Stream id data source: " + stream.getIdDataSource() + ", version: " + stream.getDatasourceversion();
	}
	
	
	private void execStreamDefinition(FabricResponse fabricResponse, String step, DettaglioStream stream, boolean isInput) throws FabricException {
		try {
			for (int endpointIndex = 0; endpointIndex < CepDelegate.build().getTotalNumOfSoapEndpoint(); endpointIndex++) {
				WebServiceResponse response = CepDelegate.build().editEventStreamInfo(stream, isInput, endpointIndex);
				if (response.getStatusCode() != 200)
					throw new FabricException("Error in stream definition internal input server " + endpointIndex + " ." + getStreamkeyForLog(stream) + " - status: "
							+ response.getStatusCode() + " - soapMessage: " + response.getMessage());
				fabricResponse.addLogInfo("STEP " + step + "." + endpointIndex + " - stream definition - " + getStreamkeyForLog(stream) + ", sever " + endpointIndex + "ok");
			}
	
		} catch (KeyManagementException e) {
			logger.error("[StreamFabric::execStreamDefinition] KeyManagementException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in streamDefinition: KeyManagementException " + e.getMessage());		
		} catch (NoSuchAlgorithmException e) {
			logger.error("[StreamFabric::execStreamDefinition] NoSuchAlgorithmException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in streamDefinition: NoSuchAlgorithmException " + e.getMessage());		
		} catch (IOException e) {
			logger.error("[StreamFabric::execStreamDefinition] IOException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in streamDefinition: IOException " + e.getMessage());		
		}
	}
	
	
	private void execCreateBuilder(FabricResponse fabricResponse, String step, DettaglioStream stream) throws FabricException {
		try {
			for (int endpointIndex = 0; endpointIndex < CepDelegate.build().getTotalNumOfSoapEndpoint(); endpointIndex++) {
				WebServiceResponse response = CepDelegate.build().editActiveEventBuilderConfiguration(stream, endpointIndex);
				if (response.getStatusCode() != 200)
					throw new FabricException("Error in active builder input server " + endpointIndex +"." + getStreamkeyForLog(stream) + " - status: "
							+ response.getStatusCode() + " - soapMessage: " + response.getMessage());
				fabricResponse.addLogInfo("STEP " + step + "."+ endpointIndex+ " - active builder - " + getStreamkeyForLog(stream) + ", sever " + endpointIndex + "ok");
			}	
			
		} catch (KeyManagementException e) {
			logger.error("[StreamFabric::execStreamBuilder] KeyManagementException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execStreamBuilder: KeyManagementException " + e.getMessage());		
		} catch (NoSuchAlgorithmException e) {
			logger.error("[StreamFabric::execStreamBuilder] NoSuchAlgorithmException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execStreamBuilder: NoSuchAlgorithmException " + e.getMessage());		
		} catch (IOException e) {
			logger.error("[StreamFabric::execStreamDefinition] IOException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execStreamBuilder: IOException " + e.getMessage());		
		}		
	}
	
	private void execCreateFormatter(FabricResponse fabricResponse, String step, DettaglioStream stream) throws FabricException {
		try {
			
			for (int endpointIndex = 0; endpointIndex < CepDelegate.build().getTotalNumOfSoapEndpoint(); endpointIndex++) {
				WebServiceResponse response = CepDelegate.build().editActiveEventFormatterConfiguration(stream, endpointIndex);
				if (response.getStatusCode() != 200)
					throw new FabricException("Error in formatter input server " + endpointIndex + " ." + getStreamkeyForLog(stream) + " - status: "
							+ response.getStatusCode() + " - soapMessage: " + response.getMessage());
				fabricResponse.addLogInfo("STEP " + step + "." + endpointIndex + " - formatter - " + getStreamkeyForLog(stream) + ", sever " + endpointIndex + "ok");
			}
		} catch (KeyManagementException e) {
			logger.error("[StreamFabric::execStreamFormatter] KeyManagementException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execStreamFormatter: KeyManagementException " + e.getMessage());		
		} catch (NoSuchAlgorithmException e) {
			logger.error("[StreamFabric::execStreamFormatter] NoSuchAlgorithmException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execStreamFormatter: NoSuchAlgorithmException " + e.getMessage());		
		} catch (IOException e) {
			logger.error("[StreamFabric::execStreamFormatter] IOException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execStreamFormatter: IOException " + e.getMessage());		
		}		
	}
	
	private void execCreateQuery(FabricResponse fabricResponse, String step, DettaglioStream stream, List<InternalDettaglioStream> internalStream) throws FabricException {
		try {
			for (int endpointIndex = 0; endpointIndex < CepDelegate.build().getTotalNumOfSoapEndpoint(); endpointIndex++) {
				WebServiceResponse response = CepDelegate.build().editActiveExecutionPlanConfiguration(stream, internalStream, endpointIndex);
				if (response.getStatusCode() != 200) 
					throw new FabricException("Error in create query " + endpointIndex + " ." + getStreamkeyForLog(stream) + " - status: "
							+ response.getStatusCode() + " - soapMessage: " + response.getMessage());
				fabricResponse.addLogInfo("STEP " + step + "." + endpointIndex + " - create query - " + getStreamkeyForLog(stream) + ", sever " + endpointIndex + "ok");
			}	
		} catch (KeyManagementException e) {
			logger.error("[StreamFabric::execCreateQuery] KeyManagementException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execCreateQuery: KeyManagementException " + e.getMessage());		
		} catch (NoSuchAlgorithmException e) {
			logger.error("[StreamFabric::execCreateQuery] NoSuchAlgorithmException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execCreateQuery: NoSuchAlgorithmException " + e.getMessage());		
		} catch (IOException e) {
			logger.error("[StreamFabric::execCreateQuery] IOException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execCreateQuery: IOException " + e.getMessage());		
		}		
	}
	
	private void execDeleteQuery(FabricResponse fabricResponse, String step, DettaglioStream stream) throws FabricException {
		try {
			for (int endpointIndex = 0; endpointIndex < CepDelegate.build().getTotalNumOfSoapEndpoint(); endpointIndex++) {
				WebServiceResponse response = CepDelegate.build().undeployActiveExecutionPlanConfiguration(stream,  endpointIndex);
				if (response.getStatusCode() != 200) 
					throw new FabricException("Error in delete query " + endpointIndex + " ." + getStreamkeyForLog(stream) + " - status: "
							+ response.getStatusCode() + " - soapMessage: " + response.getMessage());
				fabricResponse.addLogInfo("STEP " + step + "." + endpointIndex + " - delete query - " + getStreamkeyForLog(stream) + ", sever " + endpointIndex + "ok");
			}	
		} catch (KeyManagementException e) {
			logger.error("[StreamFabric::execDeleteQuery] KeyManagementException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execDeleteQuery: KeyManagementException " + e.getMessage());		
		} catch (NoSuchAlgorithmException e) {
			logger.error("[StreamFabric::execDeleteQuery] NoSuchAlgorithmException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execDeleteQuery: NoSuchAlgorithmException " + e.getMessage());		
		} catch (IOException e) {
			logger.error("[StreamFabric::execDeleteQuery] IOException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execDeleteQuery: IOException " + e.getMessage());		
		}		
	}
	
	private void execDeleteFormatter(FabricResponse fabricResponse, String step, DettaglioStream stream) throws FabricException {
		try {
			
			for (int endpointIndex = 0; endpointIndex < CepDelegate.build().getTotalNumOfSoapEndpoint(); endpointIndex++) {
				WebServiceResponse response = CepDelegate.build().undeployActiveEventFormatterConfiguration(stream, endpointIndex);
				if (response.getStatusCode() != 200)
					throw new FabricException("Error in  delete formatter " + endpointIndex + " ." + getStreamkeyForLog(stream) + " - status: "
							+ response.getStatusCode() + " - soapMessage: " + response.getMessage());
				fabricResponse.addLogInfo("STEP " + step + "." + endpointIndex + " - delete formatter - " + getStreamkeyForLog(stream) + ", sever " + endpointIndex + "ok");
			}
		} catch (KeyManagementException e) {
			logger.error("[StreamFabric::execDeleteFormatter] KeyManagementException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execDeleteFormatter: KeyManagementException " + e.getMessage());		
		} catch (NoSuchAlgorithmException e) {
			logger.error("[StreamFabric::execDeleteFormatter] NoSuchAlgorithmException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execDeleteFormatter: NoSuchAlgorithmException " + e.getMessage());		
		} catch (IOException e) {
			logger.error("[StreamFabric::execDeleteFormatter] IOException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execDeleteFormatter: IOException " + e.getMessage());		
		}		
	}
	
	private void execDeleteBuilder(FabricResponse fabricResponse, String step, DettaglioStream stream) throws FabricException {
		try {
			for (int endpointIndex = 0; endpointIndex < CepDelegate.build().getTotalNumOfSoapEndpoint(); endpointIndex++) {
				WebServiceResponse response = CepDelegate.build().undeployActiveEventBuilderConfiguration(stream, endpointIndex);
				if (response.getStatusCode() != 200)
					throw new FabricException("Error in delete builder input server " + endpointIndex +"." + getStreamkeyForLog(stream) + " - status: "
							+ response.getStatusCode() + " - soapMessage: " + response.getMessage());
				fabricResponse.addLogInfo("STEP " + step + "."+ endpointIndex+ " - delete builder - " + getStreamkeyForLog(stream) + ", sever " + endpointIndex + "ok");
			}	
			
		} catch (KeyManagementException e) {
			logger.error("[StreamFabric::execDeleteBuilder] KeyManagementException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execDeleteBuilder: KeyManagementException " + e.getMessage());		
		} catch (NoSuchAlgorithmException e) {
			logger.error("[StreamFabric::execDeleteBuilder] NoSuchAlgorithmException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execDeleteBuilder: NoSuchAlgorithmException " + e.getMessage());		
		} catch (IOException e) {
			logger.error("[StreamFabric::execDeleteBuilder] IOException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in execDeleteBuilder: IOException " + e.getMessage());		
		}		
	}
	
	private void execDeleteDefinition(FabricResponse fabricResponse, String step, DettaglioStream stream, boolean isInput) throws FabricException {
		try {
			for (int endpointIndex = 0; endpointIndex < CepDelegate.build().getTotalNumOfSoapEndpoint(); endpointIndex++) {
				WebServiceResponse response = CepDelegate.build().removeEventStreamInfo(stream, isInput, endpointIndex);
				if (response.getStatusCode() != 200)
					throw new FabricException("Error in delete stream definition internal input server " + endpointIndex + " ." + getStreamkeyForLog(stream) + " - status: "
							+ response.getStatusCode() + " - soapMessage: " + response.getMessage());
				fabricResponse.addLogInfo("STEP " + step + "." + endpointIndex + " - delete stream definition - " + getStreamkeyForLog(stream) + ", sever " + endpointIndex + "ok");
			}
	
		} catch (KeyManagementException e) {
			logger.error("[StreamFabric::execDeleteDefinition] KeyManagementException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in delete streamDefinition: KeyManagementException " + e.getMessage());		
		} catch (NoSuchAlgorithmException e) {
			logger.error("[StreamFabric::execDeleteDefinition] NoSuchAlgorithmException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in delete execDeleteDefinition: NoSuchAlgorithmException " + e.getMessage());		
		} catch (IOException e) {
			logger.error("[StreamFabric::execDeleteDefinition] IOException error " + e.getMessage());
			e.printStackTrace();
			throw new FabricException("Error in delete streamDefinition: IOException " + e.getMessage());		
		}
	}
}
