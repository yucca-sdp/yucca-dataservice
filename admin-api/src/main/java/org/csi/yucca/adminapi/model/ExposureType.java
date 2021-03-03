/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

public class ExposureType {

	private int idExposureType;
	private String exposuretype;
	private String description;
	
	public int getIdExposureType() {
		return idExposureType;
	}
	public void setIdExposureType(int idExposureType) {
		this.idExposureType = idExposureType;
	}
	public String getExposuretype() {
		return exposuretype;
	}
	public void setExposuretype(String exposuretype) {
		this.exposuretype = exposuretype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
