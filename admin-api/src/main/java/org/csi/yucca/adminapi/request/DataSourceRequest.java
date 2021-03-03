/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

public class DataSourceRequest {

	private Integer idDataset;

	private Integer idStream;
	
	private Integer idDatasource; 

	private Integer datasourceversion;
	
	public Integer getIdDatasource() {
		return idDatasource;
	}
	public void setIdDatasource(Integer idDatasource) {
		this.idDatasource = idDatasource;
	}
	public Integer getDatasourceversion() {
		return datasourceversion;
	}
	public void setDatasourceversion(Integer datasourceversion) {
		this.datasourceversion = datasourceversion;
	}
	public Integer getIdDataset() {
		return idDataset;
	}
	public void setIdDataset(Integer idDataset) {
		this.idDataset = idDataset;
	}
	public Integer getIdStream() {
		return idStream;
	}
	public void setIdStream(Integer idStream) {
		this.idStream = idStream;
	}
}
