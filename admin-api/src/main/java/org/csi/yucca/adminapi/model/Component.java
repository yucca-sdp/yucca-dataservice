/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

public class Component {

	private String jdbcNativeType;
	private String hiveType;
	private Integer idComponent;
	private String name;
	private String alias;
	private Integer inorder;
	private Double tolerance;
	private Integer sinceVersion;
	private Integer idPhenomenon;
	private Integer idDataType;
	private Integer idMeasureUnit;
	private Integer idDataSource;
	private Integer datasourceversion;
	private Integer iskey;
	private Integer sourcecolumn;
	private String sourcecolumnname;
	private Integer required;
	private String foreignkey;
	private Integer isgroupable;
	
	public String getForeignkey() {
		return foreignkey;
	}

	public void setForeignkey(String foreignkey) {
		this.foreignkey = foreignkey;
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

	public Integer getSinceVersion() {
		return sinceVersion;
	}

	public void setSinceVersion(Integer sinceVersion) {
		this.sinceVersion = sinceVersion;
	}

	public Integer getIdPhenomenon() {
		return idPhenomenon;
	}

	public void setIdPhenomenon(Integer idPhenomenon) {
		this.idPhenomenon = idPhenomenon;
	}

	public Integer getIdDataType() {
		return idDataType;
	}

	public void setIdDataType(Integer idDataType) {
		this.idDataType = idDataType;
	}

	public Integer getIdMeasureUnit() {
		return idMeasureUnit;
	}

	public void setIdMeasureUnit(Integer idMeasureUnit) {
		this.idMeasureUnit = idMeasureUnit;
	}

	public Integer getIdDataSource() {
		return idDataSource;
	}

	public void setIdDataSource(Integer idDataSource) {
		this.idDataSource = idDataSource;
	}

	public Integer getDatasourceversion() {
		return datasourceversion;
	}

	public void setDatasourceversion(Integer datasourceversion) {
		this.datasourceversion = datasourceversion;
	}

	public Integer getIskey() {
		return iskey;
	}

	public void setIskey(Integer iskey) {
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

	public Integer getRequired() {
		return required;
	}

	public void setRequired(Integer required) {
		this.required = required;
	}
	
	// --------------------------------------

	public Integer getIsgroupable() {
		return isgroupable;
	}

	public void setIsgroupable(Integer isgroupable) {
		this.isgroupable = isgroupable;
	}

	public void setIsgroupable(Boolean isgroupable) {
		this.isgroupable = isgroupable == null ? 0: ( isgroupable ? 1 : 0);
	}

	public Component isgroupable(Integer isgroupable) {
		this.isgroupable = isgroupable;
		return this;
	}
	
	public Component idComponent(Integer idComponent) {
		this.idComponent = idComponent;
		return this;
	}

	public Component name(String name) {
		this.name = name;
		return this;
	}

	public Component alias(String alias) {
		this.alias = alias;
		return this;
	}

	public Component inorder(Integer inorder) {
		this.inorder = inorder;
		return this;
	}

	public Component tolerance(Double tolerance) {
		this.tolerance = tolerance;
		return this;
	}

	public Component sinceVersion(Integer sinceVersion) {
		this.sinceVersion = sinceVersion;
		return this;
	}

	public Component idPhenomenon(Integer idPhenomenon) {
		this.idPhenomenon = idPhenomenon;
		return this;
	}

	public Component idDataType(Integer idDataType) {
		this.idDataType = idDataType;
		return this;
	}

	public Component idMeasureUnit(Integer idMeasureUnit) {
		this.idMeasureUnit = idMeasureUnit;
		return this;
	}

	public Component idDataSource(Integer idDataSource) {
		this.idDataSource = idDataSource;
		return this;
	}

	public Component datasourceversion(Integer datasourceversion) {
		this.datasourceversion = datasourceversion;
		return this;
	}

	public Component iskey(Integer iskey) {
		this.iskey = iskey;
		return this;
	}

	public Component sourcecolumn(Integer sourcecolumn) {
		this.sourcecolumn = sourcecolumn;
		return this;
	}

	public Component sourcecolumnname(String sourcecolumnname) {
		this.sourcecolumnname = sourcecolumnname;
		return this;
	}

	public Component required(Integer required) {
		this.required = required;
		return this;
	}
	
	public void setIskey(Boolean iskey) {
		this.iskey = iskey==null? 0: (iskey?1:0);
	}

	public void setRequired(Boolean required) {
		this.required = required==null? 0: (required?1:0);
	}

	
	public void iskey(Boolean iskey) {
		this.iskey = iskey==null? 0: (iskey?1:0);
	}

	public void required(Boolean required) {
		this.required = required==null? 0: (required?1:0);
	}

	public String getJdbcNativeType() {
		return jdbcNativeType;
	}

	public void setJdbcNativeType(String jdbcNativeType) {
		this.jdbcNativeType = jdbcNativeType;
	}

	public String getHiveType() {
		return hiveType;
	}

	public void setHiveType(String hiveType) {
		this.hiveType = hiveType;
	}


}
