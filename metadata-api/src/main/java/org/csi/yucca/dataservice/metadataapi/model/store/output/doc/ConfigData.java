/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

public class ConfigData {

	private Long idTenant;
	private String tenantCode;
	private String collection;
	private String database;
	private String type;
	private String subtype;
	private String entityNameSpace;
	private String datasetStatus;
	private Integer current;
	private Archive archive;
	private Integer deleted;

	public ConfigData() {

	}

	public Long getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Long idTenant) {
		this.idTenant = idTenant;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getEntityNameSpace() {
		return entityNameSpace;
	}

	public void setEntityNameSpace(String entityNameSpace) {
		this.entityNameSpace = entityNameSpace;
	}

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getDatasetStatus() {
		return datasetStatus;
	}

	public void setDatasetStatus(String datasetStatus) {
		this.datasetStatus = datasetStatus;
	}

	public Archive getArchive() {
		return archive;
	}

	public void setArchive(Archive archive) {
		this.archive = archive;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
