/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.csi.yucca.dataservice.insertdataapi.model.output.ErrorOutput;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class InsertApiBaseException extends WebApplicationException{

	
	private static final long serialVersionUID = 1L;
	
	
	public static final String ERROR_CODE_AUTHENTICATION_FAILED = "E002";
	

	public static final String ERROR_CODE_STREAM_NOT_FOUND="E011";
	public static final String ERROR_CODE_INVALID_JSON="E012";
	public static final String ERROR_CODE_INVALID_COMPONENTS="E013";
	public static final String ERROR_CODE_DATASET_DATASETVERSION_INVALID="E014";
	public static final String ERROR_CODE_DATASET_MAXRECORDS="E015";

	
	public static final String ERROR_CODE_INPUT_SENSOR_MANCANTE="E016";
	public static final String ERROR_CODE_INPUT_STREAM_MANCANTE="E017";
	public static final String ERROR_CODE_INPUT_DATA_NOTARRAY="E018";
	public static final String ERROR_CODE_INPUT_DUPLICATE="E019";
	public static final String ERROR_CODE_INPUT_INVALID_DATA_VALUE="E020";
	
	
	
	
	private String errorName=null;
	private String errorCode=null;
	private String output=null;
	private String errorMessage=null;
	
	public void setOutput(String output) {
		this.output = output;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private String getMessageFromCode(String errorCode) {
		if (ERROR_CODE_STREAM_NOT_FOUND.equals(errorCode)) return "Stream unknown";
		else if (ERROR_CODE_INVALID_JSON.equals(errorCode)) return "Json validation failed";
		else if (ERROR_CODE_INVALID_COMPONENTS.equals(errorCode)) return "Json components are not coherent with stream definition";
		else if (ERROR_CODE_DATASET_DATASETVERSION_INVALID.equals(errorCode)) return "Dataset or dataset version validation failed";
		else if (ERROR_CODE_DATASET_MAXRECORDS.equals(errorCode)) return "Input size validation failed";
		else if (ERROR_CODE_INPUT_SENSOR_MANCANTE.equals(errorCode)) return "Input validation failed: missing sensor information";
		else if (ERROR_CODE_INPUT_STREAM_MANCANTE.equals(errorCode)) return "Input validation failed: missing stream/application information";
		else if (ERROR_CODE_INPUT_DATA_NOTARRAY.equals(errorCode)) return "Input validation failed: input object must be an array";
		else if (ERROR_CODE_INPUT_DUPLICATE.equals(errorCode)) return "Input validation failed: duplicate input block ";
		else if (ERROR_CODE_INPUT_INVALID_DATA_VALUE.equals(errorCode)) return "Invalid data value ";
		else if (ERROR_CODE_AUTHENTICATION_FAILED.equals(errorCode)) return "Authentication Failed ";
		else return "unknown error";
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	
	@Override
	public javax.ws.rs.core.Response getResponse() {
		ResponseBuilder response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		response.entity(new ErrorOutput(errorName, errorCode, output, errorMessage));
		return response.build();
	};
	
	public InsertApiBaseException(String errorCode, Throwable cause, String additionalMessage) {
		super(cause);
		this.errorCode=errorCode;
		this.errorName=getMessageFromCode(errorCode)+(additionalMessage != null ? additionalMessage : "" ) ;
	}

	public InsertApiBaseException(String errorCode, String additionalMessage) {
		super();
		this.errorCode=errorCode;
		this.errorName=getMessageFromCode(errorCode)+(additionalMessage != null ? additionalMessage : "" ) ;
	}

	public InsertApiBaseException(String errorCode, Throwable cause) {
		super(cause);
		this.errorCode=errorCode;
		this.errorName=getMessageFromCode(errorCode);
	}

	public InsertApiBaseException(String errorCode) {
		super();
		this.errorCode=errorCode;
		this.errorName=getMessageFromCode(errorCode);
	}
	
	public String toJson() {
		String result = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			result = mapper.writeValueAsString(this.getResponse().getEntity());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
}
