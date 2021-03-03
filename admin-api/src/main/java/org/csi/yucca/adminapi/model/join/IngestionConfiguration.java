/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model.join;

import java.sql.Timestamp;

public class IngestionConfiguration {
	
	
	private String jdbcNativeType;
	private String hiveType;
	private String table;
	private String column;
	private String comments;
	private String datasetCode;
	private String domain;
	private String subdomain;
	private String visibility;
	private Integer opendata;
	private Timestamp registrationDate;
	private String dbName;
	private String dbSchema;
	private String dbUrl;
	private Integer columnIndex;
	private String dbhiveschema;
	private String dbhivetable;
	
	public String getDbhiveschema() {
		return dbhiveschema;
	}
	public void setDbhiveschema(String dbhiveschema) {
		this.dbhiveschema = dbhiveschema;
	}
	public String getDbhivetable() {
		return dbhivetable;
	}
	public void setDbhivetable(String dbhivetable) {
		this.dbhivetable = dbhivetable;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDatasetCode() {
		return datasetCode;
	}
	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getSubdomain() {
		return subdomain;
	}
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public Integer getOpendata() {
		return opendata;
	}
	public void setOpendata(Integer opendata) {
		this.opendata = opendata;
	}
	public Timestamp getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Timestamp registrationDate) {
		this.registrationDate = registrationDate;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbSchema() {
		return dbSchema;
	}
	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public Integer getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(Integer columnIndex) {
		this.columnIndex = columnIndex;
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
