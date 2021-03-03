/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

public class PostDataSourceGroupRequest {

	private Integer idTenant;
	private String name;
	private Integer idDatasourcegroupType;
	private String color;
	private Long idDatasourcegroup;
	private Integer datasourcegroupversion;
	
	public Integer getDatasourcegroupversion() {
		return datasourcegroupversion;
	}
	public void setDatasourcegroupversion(Integer datasourcegroupversion) {
		this.datasourcegroupversion = datasourcegroupversion;
	}
	public Long getIdDatasourcegroup() {
		return idDatasourcegroup;
	}
	public void setIdDatasourcegroup(Long idDatasourcegroup) {
		this.idDatasourcegroup = idDatasourcegroup;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getIdTenant() {
		return idTenant;
	}
	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIdDatasourcegroupType() {
		return idDatasourcegroupType;
	}
	public void setIdDatasourcegroupType(Integer idDatasourcegroupType) {
		this.idDatasourcegroupType = idDatasourcegroupType;
	}
}