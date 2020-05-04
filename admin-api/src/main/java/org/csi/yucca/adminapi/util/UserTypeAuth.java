/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.util;

public enum UserTypeAuth {

	DEFAULT (1, "default"),
	SOCIAL  (2, "social"),
	ADMIN   (3, "admin");
	
	private int id;
	private String description;
	
	UserTypeAuth(int id, String description){
		this.id = id;
		this.description = description;
	}

	public int id() {
		return id;
	}

	public String description() {
		return description;
	}
}
