/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

import org.csi.yucca.adminapi.util.DataOption;
import org.csi.yucca.adminapi.util.ManageOption;

public class SharingTenantRequest {

	private Integer idTenant;
	private String tenantcode;
	private Integer dataOptions = DataOption.READ_AND_USE.id();
	private Integer manageOptions = ManageOption.NO_RIGHT.id();
	
	public Integer getIdTenant() {
		return idTenant;
	}
	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}
	public Integer getDataOptions() {
		return dataOptions;
	}
	public void setDataOptions(Integer dataOptions) {
		this.dataOptions = dataOptions;
	}
	public Integer getManageOptions() {
		return manageOptions;
	}
	public void setManageOptions(Integer manageOptions) {
		this.manageOptions = manageOptions;
	}
	public String getTenantcode() {
		return tenantcode;
	}
	public void setTenantcode(String tenantcode) {
		this.tenantcode = tenantcode;
	}

}
