/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model;

/**
 * 
 * @author Pietro Cannalire
 *
 */
public class TenantTool extends Tool {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4965430172090625602L;
	
	private Boolean enabled;

	public Boolean getEnabled() {
		return enabled;	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
		
	@Override
	public boolean equals(Object tool) {
		return (this.getIdTool().equals(((Tool) tool).getIdTool())
				&& this.enabled.equals(((TenantTool) tool).getEnabled()));
	
	}

}
