/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

public class DataSourceGroupTypeResponse extends Response{

	private Integer idDatasourcegroupType;
	private String name;
	private String description;
	
	public DataSourceGroupTypeResponse(Integer idDatasourcegroupType, String name,
			String description) {
		this.idDatasourcegroupType = idDatasourcegroupType;
		this.name = name;
		this.description = description;
	}
	
	public DataSourceGroupTypeResponse() {
		super();
	}
	public Integer getIdDatasourcegroupType() {
		return idDatasourcegroupType;
	}
	public void setIdDatasourcegroupType(Integer idDatasourcegroupType) {
		this.idDatasourcegroupType = idDatasourcegroupType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
