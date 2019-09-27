/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate.beans.hdfs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileStatusesContainer {

	private FileStatuses fileStatuses;

	@JsonProperty("FileStatuses")
	public FileStatuses getFileStatuses() {
		return fileStatuses;
	}

	@JsonProperty("FileStatuses")
	public void setFileStatuses(FileStatuses fileStatuses) {
		this.fileStatuses = fileStatuses;
	}

}
