/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

public class SearchEngineJsonFieldElement {

	private String componentName;
	private String componentAlias;
	private String dataType;
	private Double tolerance;
	private String measureUnit;
	private String measureUnitCategory;
	private String phenomenon;
	private String phenomenonCategory;
	private String sinceVersion;

	public SearchEngineJsonFieldElement() {
		super();
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentAlias() {
		return componentAlias;
	}

	public void setComponentAlias(String componentAlias) {
		this.componentAlias = componentAlias;
	}

	public Double getTolerance() {
		return tolerance;
	}

	public void setTolerance(Double tolerance) {
		this.tolerance = tolerance;
	}

	public String getMeasureUnitCategory() {
		return measureUnitCategory;
	}

	public void setMeasureUnitCategory(String measureUnitCategory) {
		this.measureUnitCategory = measureUnitCategory;
	}

	public String getPhenomenon() {
		return phenomenon;
	}

	public void setPhenomenon(String phenomenon) {
		this.phenomenon = phenomenon;
	}

	public String getPhenomenonCategory() {
		return phenomenonCategory;
	}

	public void setPhenomenonCategory(String phenomenonCategory) {
		this.phenomenonCategory = phenomenonCategory;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getSinceVersion() {
		return sinceVersion;
	}

	public void setSinceVersion(String sinceVersion) {
		this.sinceVersion = sinceVersion;
	}

	
}
