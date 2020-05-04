/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.model.MeasureUnit;
import org.csi.yucca.adminapi.util.Errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MeasureUnitResponse extends Response{
	
	private Integer idMeasureUnit;
	private String measureunit;
	private String measureunitcategory;
	
	public MeasureUnitResponse(ComponentJson componentJson) {
		super();
		if (componentJson != null) {
			this.idMeasureUnit = componentJson.getIdMeasureUnit();
			this.measureunit = componentJson.getMeasureunit()!=null?String.valueOf(componentJson.getMeasureunit()):null;
			this.measureunitcategory = componentJson.getMeasureunitcategory();			
		}
	}
	
	public MeasureUnitResponse(MeasureUnit measureUnit) {
		super();
		this.idMeasureUnit = measureUnit.getIdMeasureUnit();
		this.measureunit = measureUnit.getMeasureunit();
		this.measureunitcategory = measureUnit.getMeasureunitcategory();
	}
	
	public MeasureUnitResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MeasureUnitResponse(Errors errors, String arg) {
		super(errors, arg);
		// TODO Auto-generated constructor stub
	}
	public Integer getIdMeasureUnit() {
		return idMeasureUnit;
	}
	public void setIdMeasureUnit(Integer idMeasureUnit) {
		this.idMeasureUnit = idMeasureUnit;
	}
	public String getMeasureunit() {
		return measureunit;
	}
	public void setMeasureunit(String measureunit) {
		this.measureunit = measureunit;
	}
	public String getMeasureunitcategory() {
		return measureunitcategory;
	}
	public void setMeasureunitcategory(String measureunitcategory) {
		this.measureunitcategory = measureunitcategory;
	}
}
