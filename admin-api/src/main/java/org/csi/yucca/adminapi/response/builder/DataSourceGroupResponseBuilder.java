/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response.builder;

import org.csi.yucca.adminapi.model.DataSourceGroup;
import org.csi.yucca.adminapi.response.DataSourceGroupResponse;
import org.csi.yucca.adminapi.response.DataSourceGroupTypeResponse;

public class DataSourceGroupResponseBuilder{

	private Integer idTenant;
	private Integer datasourcegroupversion;
	private String name;
	private Integer idDatasourcegroupType;
	private Long idDatasourcegroup;
	private String color;
	private String nameDatasourcegroupType;
	private String descriptionDatasourcegroupType;
	private String status;
	
	public DataSourceGroupResponseBuilder status(String status){
		this.status = status;
		return this;
	}
	
	public DataSourceGroupResponseBuilder nameDatasourcegroupType(String nameDatasourcegroupType){
		this.nameDatasourcegroupType = nameDatasourcegroupType;
		return this;
	}

	public DataSourceGroupResponseBuilder descriptionDatasourcegroupType(String descriptionDatasourcegroupType){
		this.descriptionDatasourcegroupType = descriptionDatasourcegroupType;
		return this;
	}
	
	public DataSourceGroupResponseBuilder idDatasourcegroup(Long idDatasourcegroup){
		this.idDatasourcegroup = idDatasourcegroup;
		return this;
	}

	public DataSourceGroupResponseBuilder idTenant(Integer idTenant){
		this.idTenant = idTenant;
		return this;
	}

	public DataSourceGroupResponseBuilder datasourcegroupversion(Integer datasourcegroupversion){
		this.datasourcegroupversion = datasourcegroupversion;
		return this;
	}

	public DataSourceGroupResponseBuilder idDatasourcegroupType(Integer idDatasourcegroupType){
		this.idDatasourcegroupType = idDatasourcegroupType;
		return this;
	}
	
	public DataSourceGroupResponseBuilder name(String name){
		this.name = name;
		return this;
	}

	public DataSourceGroupResponseBuilder color(String color){
		this.color = color;
		return this;
	}
	
	public DataSourceGroupResponseBuilder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DataSourceGroupResponseBuilder(DataSourceGroup model) {
		super();

		this.idTenant = model.getIdTenant();
		this.datasourcegroupversion = model.getDatasourcegroupversion();
		this.name = model.getName();
		this.idDatasourcegroupType = model.getIdDatasourcegroupType();
		this.idDatasourcegroup = model.getIdDatasourcegroup();
		this.color = model.getColor();
		this.descriptionDatasourcegroupType = model.getDescriptionDatasourcegroupType();
		this.nameDatasourcegroupType = model.getNameDatasourcegroupType();
		this.status = model.getStatus();
	}

	public DataSourceGroupResponse build(){
		DataSourceGroupResponse response = new DataSourceGroupResponse();
		
		response.setIdTenant(this.idTenant);
		response.setDatasourcegroupversion(this.datasourcegroupversion);
		response.setName(this.name);
		response.setType( new DataSourceGroupTypeResponse(this.idDatasourcegroupType, 
				this.nameDatasourcegroupType, this.descriptionDatasourcegroupType));
		response.setIdDatasourcegroup(this.idDatasourcegroup);
		response.setColor(this.color);
		response.setStatus(this.status);
		
		return response;
	}

}
