/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

public class ToolRequest {

	protected String name;
	protected String toolversion;
	
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
