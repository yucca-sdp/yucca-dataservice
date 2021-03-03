/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.util;

public enum Languages {
	
	IT("langIT"),
	EN("langEN");
	
	private String value;
	
	Languages(String value){
		this.value = value;
	}
	
	public String value(){
		return this.value;
	}
	
}
