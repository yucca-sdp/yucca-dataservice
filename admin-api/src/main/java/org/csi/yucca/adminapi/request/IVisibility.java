/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

import java.util.List;

public interface IVisibility {
	String getVisibility();
	LicenseRequest getLicense();
	OpenDataRequest getOpendata(); 
	List<SharingTenantRequest> getSharingTenants(); 
	String getCopyright();
}
