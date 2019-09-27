/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.binaryapi.model.metadata;

import org.csi.yucca.dataservice.binaryapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class ArchiveInfo extends AbstractEntity {
	public String archiveRevision;
	private String archiveDate;

	public ArchiveInfo() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getArchiveRevision() {
		return archiveRevision;
	}

	public void setArchiveRevision(String archiveRevision) {
		this.archiveRevision = archiveRevision;
	}

	public String getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(String archiveDate) {
		this.archiveDate = archiveDate;
	}

}
