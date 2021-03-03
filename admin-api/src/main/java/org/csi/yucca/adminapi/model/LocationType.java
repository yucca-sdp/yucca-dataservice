/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

public class LocationType {
	
	private Integer idLocationType;
	private String locationtype;
	private String description;
	public Integer getIdLocationType() {
		return idLocationType;
	}
	public void setIdLocationType(Integer idLocationType) {
		this.idLocationType = idLocationType;
	}
	public String getLocationtype() {
		return locationtype;
	}
	public void setLocationtype(String locationtype) {
		this.locationtype = locationtype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
