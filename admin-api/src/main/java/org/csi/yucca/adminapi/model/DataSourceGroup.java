/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

public class DataSourceGroup {
	
	private Integer idTenant;
	private Integer datasourcegroupversion;
	private String name;
	private Long idDatasourcegroup;
	private String color;

	private Integer idDatasourcegroupType;
	private String nameDatasourcegroupType;
	private String descriptionDatasourcegroupType;
	private String status;
	
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
	public Integer getIdDatasourcegroupType() {
		return idDatasourcegroupType;
	}
	public void setIdDatasourcegroupType(Integer idDatasourcegroupType) {
		this.idDatasourcegroupType = idDatasourcegroupType;
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
	public String getNameDatasourcegroupType() {
		return nameDatasourcegroupType;
	}
	public void setNameDatasourcegroupType(String nameDatasourcegroupType) {
		this.nameDatasourcegroupType = nameDatasourcegroupType;
	}
	public String getDescriptionDatasourcegroupType() {
		return descriptionDatasourcegroupType;
	}
	public void setDescriptionDatasourcegroupType(String descriptionDatasourcegroupType) {
		this.descriptionDatasourcegroupType = descriptionDatasourcegroupType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
