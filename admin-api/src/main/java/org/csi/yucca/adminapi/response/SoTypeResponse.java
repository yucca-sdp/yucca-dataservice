/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.ISoType;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SoTypeResponse extends Response{

	private Integer idSoType;
	private String sotypecode;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String description;

	public SoTypeResponse(ISoType iSoType) {
		super();
		this.idSoType = iSoType.getIdSoType();
		this.sotypecode = iSoType.getSoTypeCode();
	}
	
	public SoTypeResponse(DettaglioSmartobject smartobject) {
		super();
		this.idSoType = smartobject.getIdSoType();
		this.sotypecode = smartobject.getSotypecode();
		this.description = smartobject.getDescriptionSoType();
	}

	public SoTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Integer getIdSoType() {
		return idSoType;
	}
	public void setIdSoType(Integer idSoType) {
		this.idSoType = idSoType;
	}
	public String getSotypecode() {
		return sotypecode;
	}
	public void setSotypecode(String sotypecode) {
		this.sotypecode = sotypecode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
