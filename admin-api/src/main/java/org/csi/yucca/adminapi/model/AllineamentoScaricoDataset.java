/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

public class AllineamentoScaricoDataset {

	private Integer idOrganization;

	private Integer idDataset;
	
	private Integer datasetVersion;
	
	private String lastMongoObjectId;

	public Integer getIdOrganization() {
		return idOrganization;
	}

	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	}

	public Integer getIdDataset() {
		return idDataset;
	}

	public void setIdDataset(Integer idDataset) {
		this.idDataset = idDataset;
	}

	public Integer getDatasetVersion() {
		return datasetVersion;
	}

	public void setDatasetVersion(Integer datasetVersion) {
		this.datasetVersion = datasetVersion;
	}

	public String getLastMongoObjectId() {
		return lastMongoObjectId;
	}

	public void setLastMongoObjectId(String lastMongoObjectId) {
		this.lastMongoObjectId = lastMongoObjectId;
	}
}