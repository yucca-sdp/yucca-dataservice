/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AllineamentoScaricoDatasetResponse extends Response {
	
	private Integer idDataset;
	private Integer datasetVersion;
	private String lastMongoObjectId;
	private Integer idOrganization;
	
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
	public Integer getIdOrganization() {
		return idOrganization;
	}
	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	} 
	
}
