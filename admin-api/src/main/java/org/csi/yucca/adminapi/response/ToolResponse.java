/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ToolResponse {

	protected Integer idTool;
	protected String name;
	protected String toolversion;
	
	public Integer getIdTool() {
		return idTool;
	}
	public void setIdTool(Integer idTool) {
		this.idTool = idTool;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getToolversion() {
		return toolversion;
	}
	public void setToolversion(String version) {
		this.toolversion = version;
	}
}
