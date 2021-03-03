/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.TenantsType;
import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;
import org.csi.yucca.adminapi.model.join.TenantManagement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TenantTypeResponse extends Response{

	private Integer idTenantType;

	private String tenanttypecode;

	private String description;
	
	public TenantTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TenantTypeResponse(TenantManagement tenantManagement) {
		super();
		this.idTenantType = tenantManagement.getIdTenantType();
		this.tenanttypecode = tenantManagement.getTenanttypecode();
		this.description = tenantManagement.getTenanttypedescription();
	}

	public TenantTypeResponse(DettaglioTenantBackoffice dettaglioTenant) {
		super();
		this.idTenantType = dettaglioTenant.getIdTenantType();
		this.tenanttypecode = dettaglioTenant.getTenanttypecode();
		this.description = dettaglioTenant.getTenanttypedescription();
	}
	
	public TenantTypeResponse(TenantsType tenantType) {
		super();
		this.idTenantType = tenantType.getIdTenantType();
		this.tenanttypecode = tenantType.getTenanttypecode();
		this.description = tenantType.getDescription();
	}

	public Integer getIdTenantType() {
		return idTenantType;
	}

	public void setIdTenantType(Integer idTenantType) {
		this.idTenantType = idTenantType;
	}

	public String getTenanttypecode() {
		return tenanttypecode;
	}

	public void setTenanttypecode(String tenanttypecode) {
		this.tenanttypecode = tenanttypecode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
