/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SupplyTypeResponse extends Response{
	
	private Integer idSupplyType;
	private String supplytype;
	private String description;

	@JsonIgnore
	public boolean isEmpty(){
		return this.idSupplyType == null && this.supplytype == null && this.description == null;
	}
	
	public SupplyTypeResponse(DettaglioSmartobject smartobject) {
		super();
		this.idSupplyType = smartobject.getIdSupplyType();
		this.supplytype = smartobject.getSupplytype();
		this.description = smartobject.getDescriptionSupplytype();
	}

	public SupplyTypeResponse() {
		super();
	}
	
	public Integer getIdSupplyType() {
		return idSupplyType;
	}
	public void setIdSupplyType(Integer idSupplyType) {
		this.idSupplyType = idSupplyType;
	}
	public String getSupplytype() {
		return supplytype;
	}
	public void setSupplytype(String supplytype) {
		this.supplytype = supplytype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
