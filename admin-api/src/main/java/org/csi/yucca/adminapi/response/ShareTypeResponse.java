/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;
import org.csi.yucca.adminapi.model.join.TenantManagement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ShareTypeResponse extends Response{

	private Integer idShareType;

	private String description;
	
	public ShareTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ShareTypeResponse(TenantManagement tenantManagement) {
		super();
		this.idShareType = tenantManagement.getIdShareType();
		this.description = tenantManagement.getSharetypedescription();
	}

	public ShareTypeResponse(DettaglioTenantBackoffice dettaglioTenant) {
		super();
		this.idShareType = dettaglioTenant.getIdShareType();
		this.description = dettaglioTenant.getSharetypedescription();
	}

	public Integer getIdShareType() {
		return idShareType;
	}

	public void setIdShareType(Integer idShareType) {
		this.idShareType = idShareType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
