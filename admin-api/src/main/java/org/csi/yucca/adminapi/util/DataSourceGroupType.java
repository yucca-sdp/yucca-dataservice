/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.util;

public enum DataSourceGroupType {
	
	USER_DEFINED(1);
	
	private Integer id;
	
	DataSourceGroupType(int id){
		this.id = id;
	}

	public Integer id() {
		return id;
	}

}
