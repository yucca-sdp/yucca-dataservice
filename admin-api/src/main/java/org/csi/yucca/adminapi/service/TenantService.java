/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.request.ActionRequest;
import org.csi.yucca.adminapi.request.ActionfeedbackOnTenantRequest;
import org.csi.yucca.adminapi.request.PostTenantRequest;
import org.csi.yucca.adminapi.request.PostTenantSocialRequest;
import org.csi.yucca.adminapi.request.PostTenantToolRequest;
import org.csi.yucca.adminapi.request.ToolsEnvironmentRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface TenantService {
	ServiceResponse updateTenantProductContacts(String tenantCode, String productCode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse updateTenantContacts() throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse updateTenantStatus(Integer idStatus, String tenantcode) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertTenantSocial(PostTenantSocialRequest request) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse insertTenant(PostTenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse deleteTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse actionOnTenant(ActionRequest actionOnTenantRequest, String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse actionfeedbackOnTenant(ActionfeedbackOnTenantRequest actionfeedbackOnTenantRequest, String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse addAdminApplication(String tenantcode, String username, String password);

	ServiceResponse subscribeAdminApiInStore(String tenantcode, String username, String password);

	ServiceResponse generetateAdminKey(String tenantcode, String username, String password);

	ServiceResponse selectTenants(String sort) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectTenantTypes() throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectMail(String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectTenantToken(String tenantCode, JwtUser authorizedUser ) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse updateTenantTool(String tenantcode, Integer idTool, PostTenantToolRequest toolRequest) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse deleteTenantTool(String tenantcode, Integer idTool) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectAllTenantTools(String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse toolsEnvironmentPrepared(String tenantcode, ToolsEnvironmentRequest request) throws BadRequestException, NotFoundException, Exception;

	void updateAdminKey(String tenantcode, String clientkey, String clientsecret);
}
