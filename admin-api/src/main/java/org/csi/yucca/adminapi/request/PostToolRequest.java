/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PostToolRequest extends ToolRequest {

	private Integer idTool;

	public Integer getIdTool() {
		return idTool;
	}

	public void setIdTool(Integer idTool) {
		this.idTool = idTool;
	}

}
