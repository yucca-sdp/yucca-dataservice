/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

public interface ITenant {
	Integer getIdTenant();
	String getTenantCode();
	String getTenantDescription();
}
