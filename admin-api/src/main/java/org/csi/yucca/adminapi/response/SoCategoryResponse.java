/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.ISoCategory;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SoCategoryResponse extends Response{
	
	private Integer idSoCategory;
	private String socategorycode;
	private String description;
	
	@JsonIgnore
	public boolean isEmpty(){
		return this.idSoCategory == null && this.socategorycode == null && this.description == null;
	}

	public SoCategoryResponse(ISoCategory iSoCategory) {
		super();
		
		if (iSoCategory != null) {
			this.idSoCategory = iSoCategory.getIdSoCategory();
			this.socategorycode = iSoCategory.getSmartObjectCategoryCode();
			this.description = iSoCategory.getSmartObjectCategoryDescription();			
		}
	}
	
	public SoCategoryResponse(DettaglioSmartobject smartobject) {
		super();
		this.idSoCategory = smartobject.getIdSoCategory();
		this.socategorycode = smartobject.getSocategorycode();
		this.description = smartobject.getDescriptionSoCategory();
	}
	
	public SoCategoryResponse() {
		super();
	}
	
	public Integer getIdSoCategory() {
		return idSoCategory;
	}
	public void setIdSoCategory(Integer idSoCategory) {
		this.idSoCategory = idSoCategory;
	}
	public String getSocategorycode() {
		return socategorycode;
	}
	public void setSocategorycode(String socategorycode) {
		this.socategorycode = socategorycode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
}
