/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.DettaglioDataset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BackofficeDatasetDettaglioResponse extends DatasetDettaglioResponse {

	private String solrcollectionname;
	private String phoenixschemaname;
	private String phoenixtablename;
	
	
	public BackofficeDatasetDettaglioResponse() {
		super();
	}

	public BackofficeDatasetDettaglioResponse(DettaglioDataset dettaglioDataset) {
		super(dettaglioDataset);
		this.solrcollectionname = dettaglioDataset.getSolrcollectionname();
		this.phoenixschemaname = dettaglioDataset.getPhoenixschemaname();
		this.phoenixtablename = dettaglioDataset.getPhoenixtablename();
	}

	public String getSolrcollectionname() {
		return solrcollectionname;
	}

	public void setSolrcollectionname(String solrcollectionname) {
		this.solrcollectionname = solrcollectionname;
	}

	public String getPhoenixschemaname() {
		return phoenixschemaname;
	}

	public void setPhoenixschemaname(String phoenixschemaname) {
		this.phoenixschemaname = phoenixschemaname;
	}

	public String getPhoenixtablename() {
		return phoenixtablename;
	}

	public void setPhoenixtablename(String phoenixtablename) {
		this.phoenixtablename = phoenixtablename;
	}

}
