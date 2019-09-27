/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

import java.util.List;

public class SearchEngineJsonSo {
	private String model;
	private List<SearchEngineJsonSoPosition> position;

	public SearchEngineJsonSo() {
		super();
	}

	public List<SearchEngineJsonSoPosition> getPosition() {
		return position;
	}

	public void setPositions(List<SearchEngineJsonSoPosition> position) {
		this.position = position;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

}
