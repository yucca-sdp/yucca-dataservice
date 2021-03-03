/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Dataset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class DatasetTypeResponse extends Response{

	private Integer idDatasetType;
	private String datasetType;
	private String description;
	
	public DatasetTypeResponse() {
		super();
	}

	public DatasetTypeResponse(Dataset dataset) {
		super();
		this.idDatasetType = dataset.getIdDatasetType();
		this.datasetType = dataset.getDatasetType();
		this.description = dataset.getDatasetTypeDescription();
	}
	
	
	public Integer getIdDatasetType() {
		return idDatasetType;
	}
	public void setIdDatasetType(Integer idDatasetType) {
		this.idDatasetType = idDatasetType;
	}
	public String getDatasetType() {
		return datasetType;
	}
	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
}
