/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.util;

public enum Action {
	
	REQUEST_INSTALLATION   ("req_install"),
	REQUEST_UNINSTALLATION ("req_uninstall"),
	NEW_VERSION            ("new_version"),
	INSTALLATION           ("install"),
	UNINSTALLATION         ("uninstall"),
	DELETE                 ("delete"),
	MIGRATE                ("migrate"),
	UPGRADE                ("upgrade"),
	CONSOLIDATE            ("consolidate"),
	DISMISS				   ("dismiss"),
	RESTORE                ("restore");
	
	private String code;
	
	Action(String code){
		this.code = code;
	}

	public String code() {
		return code;
	}

}
