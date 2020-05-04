/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

public class DataSourceGroupResponse extends Response{

	private Integer idTenant;
	private Integer datasourcegroupversion;
	private String name;
	private Long idDatasourcegroup;
	private String color;
	private DataSourceGroupTypeResponse type;
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public DataSourceGroupTypeResponse getType() {
		return type;
	}
	public void setType(DataSourceGroupTypeResponse type) {
		this.type = type;
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
	public Integer getDatasourcegroupversion() {
		return datasourcegroupversion;
	}
	public void setDatasourcegroupversion(Integer datasourcegroupversion) {
		this.datasourcegroupversion = datasourcegroupversion;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getIdDatasourcegroup() {
		return idDatasourcegroup;
	}
	public void setIdDatasourcegroup(Long idDatasourcegroup) {
		this.idDatasourcegroup = idDatasourcegroup;
	}
}
