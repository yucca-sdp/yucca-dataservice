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
public class FileStatuses {

	private FileStatus[] fileStatus;

	@JsonProperty("FileStatus")
	public FileStatus[] getFileStatus() {
		return fileStatus;
	}

	@JsonProperty("FileStatus")
	public void setFileStatus(FileStatus[] fileStatus) {
		this.fileStatus = fileStatus;
	}

}
