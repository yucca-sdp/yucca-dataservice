/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model.builder;

import org.csi.yucca.adminapi.model.DataSourceGroup;

public class DataSourceGroupBuilder {

	private Integer idTenant;
	private String name;
	private Integer idDatasourcegroupType;
	private String color;
	private Long idDatasourcegroup;
	private Integer datasourcegroupversion;
	private String status;

	
	public DataSourceGroupBuilder color(String color){
		this.color = color;
		return this;
	}
	
	public DataSourceGroupBuilder idDatasourcegroupType(Integer idDatasourcegroupType){
		this.idDatasourcegroupType = idDatasourcegroupType;
		return this;
	}

	public DataSourceGroupBuilder idTenant(Integer idTenant){
		this.idTenant = idTenant;
		return this;
	}
	
	public DataSourceGroupBuilder name(String name){
		this.name = name;
		return this;
	}
	
	public DataSourceGroupBuilder status(String status){
		this.status = status;
		return this;
	}

	public DataSourceGroupBuilder datasourcegroupversion(Integer datasourcegroupversion){
		this.datasourcegroupversion = datasourcegroupversion;
		return this;
	}

	public DataSourceGroupBuilder idDatasourcegroup(Long idDatasourcegroup){
		this.idDatasourcegroup = idDatasourcegroup;
		return this;
	}
	
	public DataSourceGroup build(){
		DataSourceGroup model = new DataSourceGroup();
		model.setIdTenant(idTenant);
		model.setName(name);
		model.setIdDatasourcegroupType(idDatasourcegroupType);
		model.setColor(color);
		model.setIdDatasourcegroup(idDatasourcegroup);
		model.setDatasourcegroupversion(datasourcegroupversion);
		model.setStatus(status);
		return model;
	}
}