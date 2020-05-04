/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.request.PostTenantSocialRequest;
import org.csi.yucca.adminapi.request.TenantRequest;
import org.csi.yucca.adminapi.response.PostDatasetResponse;
import org.csi.yucca.adminapi.util.EmailInfo;

public interface MailService {
	
	void sendOpendataCreationInformative(PostDatasetResponse response, Tenant tenant, JwtUser authorizedUser, String userportalUrl); 
	
	void sendOpendataCreationInformative(PostDatasetResponse response, String dataSetStoreLink, String tenantCode, String user);
	
	void sendEmail(final EmailInfo emailInfo);

	void sendStreamRequestInstallationEmail(final DettaglioStream dettaglioStream);

	void sendStreamRequestUninstallationEmail(final DettaglioStream dettaglioStream);
	
	void sendTenantRequestInstallationEmail(final PostTenantSocialRequest tenantRequest);
	
	void sendTenantCreationEmail(final TenantRequest tenantRequest);
	
}
