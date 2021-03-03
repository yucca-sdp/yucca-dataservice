/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

public class SupplyType {

	private Integer idSupplyType;
	private String supplytype;
	private String description;
	

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
