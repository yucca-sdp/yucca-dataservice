/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

public class DataType {
	
	private Integer idDataType;
	private String datatypecode;
	private String description;
	
	public DataType(Integer idDataType, String datatypecode, String description) {
		super();
		this.idDataType = idDataType;
		this.datatypecode = datatypecode;
		this.description = description;
	}
	
	public DataType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdDataType() {
		return idDataType;
	}
	public void setIdDataType(Integer idDataType) {
		this.idDataType = idDataType;
	}
	public String getDatatypecode() {
		return datatypecode;
	}
	public void setDatatypecode(String datatypecode) {
		this.datatypecode = datatypecode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
