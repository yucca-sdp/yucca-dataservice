/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.model.output.v01;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;
public class Dataset {
	
	public static final String DATASET_TYPE_BULK = "bulkDataset";
	public static final String DATASET_TYPE_BINARY= "binaryDataset";
	public static final String DATASET_TYPE_STREAM = "streamDataset";
	public static final String DATASET_TYPE_SOCIAL = "socialDataset";

	private Long datasetId;
	private String code;  // codice dataset
	private String datasetType;
	private DatasetColumn[] columns;
	private String importFileType;

	public Dataset() {

	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public DatasetColumn[] getColumns() {
		return columns;
	}

	public void setColumns(DatasetColumn[] columns) {
		this.columns = columns;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}

	public Long getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(Long datasetId) {
		this.datasetId = datasetId;
	}

	public String getImportFileType() {
		return importFileType;
	}

	public void setImportFileType(String importFileType) {
		this.importFileType = importFileType;
	}


}
