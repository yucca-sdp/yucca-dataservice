/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

import java.io.Serializable;

/**
 * 
 * @author Pietro Cannalire
 *
 */
public class Tool implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1731928956755346190L;
	
	protected Integer idTool;	
	protected String name;
	protected String toolversion;
	
	public Integer getIdTool() {
		return idTool;
	}
	public void setId_tool(Integer idTool) {
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
	
	@Override
	public boolean equals(Object tool) {
		return (this.idTool.equals(((Tool) tool).getIdTool()));
	
	}

}
