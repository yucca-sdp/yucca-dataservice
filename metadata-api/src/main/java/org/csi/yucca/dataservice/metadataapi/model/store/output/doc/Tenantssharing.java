/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

import java.util.List;

public class Tenantssharing {

	private List<Tenantsharing> tenantsharing;

	public Tenantssharing() {
	}

	public List<Tenantsharing> getTenantsharing() {
		return tenantsharing;
	}

	public void setTenantsharing(List<Tenantsharing> tenantsharing) {
		this.tenantsharing = tenantsharing;
	}

}
