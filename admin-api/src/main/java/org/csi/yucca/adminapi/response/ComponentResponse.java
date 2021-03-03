/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.util.Util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ComponentResponse extends Response {

	private Integer idComponent;
	private String name;
	private String alias;
	private Integer inorder;
	private Double tolerance;
	private PhenomenonResponse phenomenon;
	private DataTypeResponse dataType;
	private MeasureUnitResponse measureUnit;
	private Boolean required;
	private Integer sinceVersion;
	private Boolean iskey;
	private Boolean isgroupable;
	private Integer sourcecolumn;
	private String sourcecolumnname;
	private String foreignkey;
	private String hivetype;
	private String jdbcnativetype;
	
	public ComponentResponse(ComponentJson componentJson) {
		super();
		this.idComponent = componentJson.getId_component();
		this.name = componentJson.getName();
		this.alias = componentJson.getAlias();
		this.inorder = componentJson.getInorder();
		this.tolerance = componentJson.getTolerance();
		this.phenomenon = new PhenomenonResponse(componentJson);
		this.dataType = new DataTypeResponse(componentJson);
		this.measureUnit = new MeasureUnitResponse(componentJson);
		this.required = Util.intToBoolean(componentJson.getRequired());
		this.sinceVersion = componentJson.getSince_version();
		this.iskey = Util.intToBoolean(componentJson.getIskey());
		this.isgroupable = Util.intToBoolean(componentJson.getIsgroupable());
		this.sourcecolumn = componentJson.getSourcecolumn();
		this.sourcecolumnname = componentJson.getSourcecolumnname();
		this.foreignkey = componentJson.getForeignkey();
		this.hivetype = componentJson.getHiveType();
		this.jdbcnativetype = componentJson.getJdbcNativeType();
	}
	
	public ComponentResponse idDataType(Integer idDataType){
		DataTypeResponse dataTypeResponse = new DataTypeResponse();
		dataTypeResponse.setIdDataType(idDataType);
		this.dataType = dataTypeResponse;
		return this;
	}
	
	public ComponentResponse name(String name){
		this.name = name;
		return this;
	}

	public ComponentResponse idComponent(Integer idComponent){
		this.idComponent = idComponent;
		return this;
	}
	
	public ComponentResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdComponent() {
		return idComponent;
	}

	public void setIdComponent(Integer idComponent) {
		this.idComponent = idComponent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public String getForeignkey() {
		return foreignkey;
	}

	public void setForeignkey(String foreignkey) {
		this.foreignkey = foreignkey;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getInorder() {
		return inorder;
	}

	public void setInorder(Integer inorder) {
		this.inorder = inorder;
	}

	public Double getTolerance() {
		return tolerance;
	}

	public void setTolerance(Double tolerance) {
		this.tolerance = tolerance;
	}

	public PhenomenonResponse getPhenomenon() {
		return phenomenon;
	}

	public void setPhenomenon(PhenomenonResponse phenomenon) {
		this.phenomenon = phenomenon;
	}

	public DataTypeResponse getDataType() {
		return dataType;
	}

	public void setDataType(DataTypeResponse dataType) {
		this.dataType = dataType;
	}

	public MeasureUnitResponse getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(MeasureUnitResponse measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Integer getSinceVersion() {
		return sinceVersion;
	}

	public void setSinceVersion(Integer sinceVersion) {
		this.sinceVersion = sinceVersion;
	}

	public Boolean getIsgroupable() {
		return isgroupable;
	}

	public void setIsgroupable(Boolean isgroupable) {
		this.isgroupable = isgroupable;
	}

	public Boolean getIskey() {
		return iskey;
	}

	public void setIskey(Boolean iskey) {
		this.iskey = iskey;
	}

	public Integer getSourcecolumn() {
		return sourcecolumn;
	}

	public void setSourcecolumn(Integer sourcecolumn) {
		this.sourcecolumn = sourcecolumn;
	}

	public String getSourcecolumnname() {
		return sourcecolumnname;
	}

	public void setSourcecolumnname(String sourcecolumnname) {
		this.sourcecolumnname = sourcecolumnname;
	}

	public String getHivetype() {
		return hivetype;
	}

	public void setHivetype(String hivetype) {
		this.hivetype = hivetype;
	}

	public String getJdbcnativetype() {
		return jdbcnativetype;
	}

	public void setJdbcnativetype(String jdbcnativetype) {
		this.jdbcnativetype = jdbcnativetype;
	}
	
	

}
