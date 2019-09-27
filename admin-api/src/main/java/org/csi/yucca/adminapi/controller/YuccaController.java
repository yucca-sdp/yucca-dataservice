/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.conf.JwtFilter;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.ConflictException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.exception.YuccaException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.response.Response;
import org.csi.yucca.adminapi.util.ApiCallable;
import org.csi.yucca.adminapi.util.ApiExecutable;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class YuccaController {
	
	private static final Logger logger = Logger.getLogger(YuccaController.class);
	
	protected JwtUser getAuthorizedUser(HttpServletRequest request){
		return (JwtUser)request.getAttribute(JwtFilter.JWT_USER_REQUEST_ATTRIBUTE_KEY);
	}
	
	public ResponseEntity<Object> buildErrorResponse(YuccaException exception){
		return new ResponseEntity<Object>(new Response(exception.errors(), exception.getArg()).args(exception.getArgs()), 
				exception.getHttpStatus());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity<Object> buildResponse(ServiceResponse serviceResponse){
		if (serviceResponse.isImage)
		{
			final HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.IMAGE_PNG);
			return new ResponseEntity(serviceResponse.getObject(), headers, HttpStatus.CREATED); 
		}
		else {
			return new ResponseEntity<Object>(serviceResponse.getObject(), serviceResponse.getHttpStatus());
		}
	}

	@SuppressWarnings("rawtypes")
	public ResponseEntity buildResponse(){
		return new ResponseEntity(HttpStatus.OK);
	}
	
	public ResponseEntity<Object> internalServerError(Exception exception){
		return new ResponseEntity<Object>(new Response(Errors.INTERNAL_SERVER_ERROR, exception.toString()), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleBindException(Exception be) {
		logger.error("Exception before Controller: " + be, be);
		return new ResponseEntity<Object>(new Response(Errors.PARAMETER_TYPE_ERROR, be.getMessage()), 
				HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 
	 * @param apiCallable
	 * @param logger
	 * @return
	 */
	public ResponseEntity<Object> run(ApiCallable apiCallable, Logger logger){
		
		ServiceResponse serviceResponse = null;
		
		try {
			
			serviceResponse = apiCallable.call();
			
		} 
		catch (UnauthorizedException unauthorizedException) {
			logger.error("UnauthorizedException: " + unauthorizedException, unauthorizedException);
			return buildErrorResponse(unauthorizedException);
		}
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException.getMessage(), badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.warn("NotFoundException: " + notFoundException.getMessage());			
			return buildErrorResponse(notFoundException);
		}
		catch (ConflictException conflictException) {
			logger.error("ConflictException: " + conflictException, conflictException);			
			return buildErrorResponse(conflictException);
		}
		catch (YuccaException yuccaException) {
			logger.error("YuccaException: " + yuccaException, yuccaException);			
			return buildErrorResponse(yuccaException);
		}
		catch (Exception e) {
			logger.error("Internal Server Error: " + e, e);
			return internalServerError(e);
		}
		
		return buildResponse(serviceResponse);
		
	}	
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> run(ApiExecutable apiExecutable, Logger logger){
		
		try {
			
			apiExecutable.call();
			
		} 
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException.getMessage(), badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.warn("NotFoundException: " + notFoundException.getMessage());			
			return buildErrorResponse(notFoundException);
		}
		catch (ConflictException conflictException) {
			logger.error("ConflictException: " + conflictException, conflictException);			
			return buildErrorResponse(conflictException);
		}
		catch (Exception e) {
			logger.error("Internal Server Error: " + e, e);
			return internalServerError(e);
		}
		
		return buildResponse();
	}	
	
	
}
