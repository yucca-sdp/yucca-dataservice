/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.binaryapi.knoxapi.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileStatusContainer {
	
	@Expose
	@SerializedName("FileStatus")
	private FileStatus fileStatus;

	public FileStatus getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(FileStatus fileStatus) {
		this.fileStatus = fileStatus;
	}
	
	
}
