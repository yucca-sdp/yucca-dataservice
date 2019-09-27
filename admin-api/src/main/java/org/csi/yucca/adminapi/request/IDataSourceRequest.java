/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

import java.util.List;

import org.csi.yucca.adminapi.request.OpenDataRequest;

public interface IDataSourceRequest {
	String getDisclaimer();
	String getName();
	Integer getIdSubdomain();
	String getVisibility();
	boolean getUnpublished();
	String getRequestername();
	String getRequestersurname();
	String getRequestermail();
	Boolean getPrivacyacceptance();
	String getIcon();
	OpenDataRequest getOpendata();
	String getCopyright();
	String getExternalreference();
	List<SharingTenantRequest> getSharingTenants();
}
