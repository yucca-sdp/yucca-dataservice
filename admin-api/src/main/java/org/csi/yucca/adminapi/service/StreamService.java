/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.request.ActionRequest;
import org.csi.yucca.adminapi.request.DatasetRequest;
import org.csi.yucca.adminapi.request.PostStreamRequest;
import org.csi.yucca.adminapi.request.PostValidateSiddhiQueriesRequest;
import org.csi.yucca.adminapi.request.StreamRequest;
import org.csi.yucca.adminapi.util.ApiUserType;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface StreamService {
	
	ServiceResponse updateStreamStatus(Integer idStatus, Integer idStream) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse createStreamDataset(PostStreamRequest request,String organizationCode, String soCode, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectStreams(Integer groupId, String organizationCode, String tenantCodeManager, String sort, JwtUser authorizedUser, Boolean includeShared) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectStreams(String sort) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectStream(String organizationCode, Integer idStream, String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse updateStream(String organizationCode, String soCode, Integer idStream, StreamRequest streamRequest, String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse updateStreamLight(String organizationCode, Integer idStream, String soCode, StreamRequest streamRequest, String tenantCodeManager, Boolean publish) throws BadRequestException, NotFoundException, Exception;
	
	byte[] selectStreamIcon(String organizationCode, Integer idStream, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	byte[] selectStreamIcon(String smartobjectCode, String streamCode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse actionOnStream( ActionRequest actionRequest, String organizationCode, String soCode, Integer idStream, ApiUserType apiUserType, JwtUser authorizedUser ) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse actionOnStream( ActionRequest actionRequest, Integer idStream, ApiUserType apiUserType) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse actionFeedback( ActionRequest actionRequest, Integer idStream) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectStreamByIdStream(Integer idStream, boolean onlyInstalled) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectStreamBySoCodeStreamCode(String soCode, String streamCode, boolean onlyInstalled) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse validateSiddhiQueries(PostValidateSiddhiQueriesRequest postValidateSiddhiQueriesRequest) throws BadRequestException, NotFoundException, Exception;
	
}
