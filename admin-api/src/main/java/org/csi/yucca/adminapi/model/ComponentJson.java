/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ComponentJson {

	private String jdbcNativeType;
	private String hiveType;
	private Integer id_component;
	private String name;
	private String alias;
	private Integer inorder;
	private Double tolerance;
	private Integer since_version;
	// private Integer id_data_type;
	private Integer id_data_source;
	private Integer datasourceversion;
	private Integer iskey;
	private Integer isgroupable;
	private Integer sourcecolumn;
	private String sourcecolumnname;
	private Integer required;
	private String foreignkey;
	private Integer idPhenomenon;
	private String phenomenonname;
	private String phenomenoncetegory;
	private Integer idDataType;
	private String datatypecode;
	private String datatypedescription;
	// private Integer dt_id_data_type;
	// private String dt_datatypecode;
	// private String dt_description;
	private Integer idMeasureUnit;
	private String measureunit;
	private String measureunitcategory;
	
	private static ObjectMapper mapper = new ObjectMapper();

	
	public String getForeignkey() {
		return foreignkey;
	}

	public void setForeignkey(String foreignkey) {
		this.foreignkey = foreignkey;
	}

	public Integer getId_component() {
		return id_component;
	}

	public void setId_component(Integer id_component) {
		this.id_component = id_component;
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

	public Integer getSince_version() {
		return since_version;
	}

	public void setSince_version(Integer since_version) {
		this.since_version = since_version;
	}

	public Integer getIdPhenomenon() {
		return idPhenomenon;
	}

	public void setIdPhenomenon(Integer idPhenomenon) {
		this.idPhenomenon = idPhenomenon;
	}

	// public Integer getId_data_type() {
	// return id_data_type;
	// }
	//
	// public void setId_data_type(Integer id_data_type) {
	// this.id_data_type = id_data_type;
	// }

	public Integer getIdMeasureUnit() {
		return idMeasureUnit;
	}

	public void setIdMeasureUnit(Integer idMeasureUnit) {
		this.idMeasureUnit = idMeasureUnit;
	}

	public Integer getId_data_source() {
		return id_data_source;
	}

	public void setId_data_source(Integer id_data_source) {
		this.id_data_source = id_data_source;
	}

	public Integer getDatasourceversion() {
		return datasourceversion;
	}

	public Integer getIsgroupable() {
		return isgroupable;
	}

	public void setIsgroupable(Integer isgroupable) {
		this.isgroupable = isgroupable;
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

	public String getPhenomenonname() {
		return phenomenonname;
	}

	public void setPhenomenonname(String phenomenonname) {
		this.phenomenonname = phenomenonname;
	}

	public String getPhenomenoncetegory() {
		return phenomenoncetegory;
	}

	public void setPhenomenoncetegory(String phenomenoncetegory) {
		this.phenomenoncetegory = phenomenoncetegory;
	}

	// public Integer getDt_id_data_type() {
	// return dt_id_data_type;
	// }
	//
	// public void setDt_id_data_type(Integer dt_id_data_type) {
	// this.dt_id_data_type = dt_id_data_type;
	// }
	//
	// public String getDt_datatypecode() {
	// return dt_datatypecode;
	// }
	//
	// public void setDt_datatypecode(String dt_datatypecode) {
	// this.dt_datatypecode = dt_datatypecode;
	// }
	//
	// public String getDt_description() {
	// return dt_description;
	// }
	//
	// public void setDt_description(String dt_description) {
	// this.dt_description = dt_description;
	// }

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

	public Integer getIdDataType() {
		return idDataType;
	}

	public void setIdDataType(Integer idDataType) {
		this.idDataType = idDataType;
	}

	public String getDatatypecode() {
		return datatypecode;
	}

	public void setDatatypecode(String datatypecode) {
		this.datatypecode = datatypecode;
	}

	public String getDatatypedescription() {
		return datatypedescription;
	}

	public void setDatatypedescription(String datatypedescription) {
		this.datatypedescription = datatypedescription;
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

	public String getJdbcnativetype() {
		return jdbcNativeType;
	}

	public void setJdbcnativetype(String jdbcnativetype) {
		this.jdbcNativeType = jdbcnativetype;
	}

	public String getHivetype() {
		return hiveType;
	}

	public void setHivetype(String hivetype) {
		this.hiveType = hivetype;
	}

	
	public void setDataType(DataType dataType) {
		if (dataType != null) {
			this.idDataType = dataType.getIdDataType();
			this.datatypecode = dataType.getDatatypecode();
			this.datatypedescription = dataType.getDescription();
		}
	}

	public String toJson() throws JsonProcessingException {
		return mapper.writeValueAsString(this);
	}

}
