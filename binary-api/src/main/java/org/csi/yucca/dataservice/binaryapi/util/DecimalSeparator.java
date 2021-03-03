/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.binaryapi.util;

public enum DecimalSeparator {
	
	DOT ("."),
	COM (",");
	
	private String value;

	DecimalSeparator(String value){
		this.value = value;
	}

	public String value() {
		return value;
	}

	public DecimalSeparator other(){
		if(this == DecimalSeparator.COM) return DecimalSeparator.DOT;
		return DecimalSeparator.COM;
	}
	
	public static DecimalSeparator get(String code){
		try {
			return DecimalSeparator.valueOf(code);
		} catch (Exception e) {
			return DecimalSeparator.COM;
		}
	}
	
}
