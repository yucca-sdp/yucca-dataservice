/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.util;

public enum DataType {
	
	INT (1,   "int", "int"),
	LONG  (2,   "long",  "long"),
	DOUBLE   (3,   "double",   "double"),
	FLOAT   (4,   "float",   "float"),
	STRING   (5,   "string",   "string"),
	BOOLEAN   (6,   "boolean",   "boolean"),
	DATE_TIME   (7,   "dateTime",   "dateTime"),
	LONGITUDE   (8,   "longitude",   "longitude"),
	LATITUDE   (9,   "latitude",   "latitude"),
	BINARY   (10,   "binary",   "binary");
	
	private Integer id;
	private String code;
	private String description;
	
	DataType(Integer id, String code, String description){
		this.id = id;
		this.code = code;
		this.description = description;
	}

	public Integer id() {
		return id;
	}

	public String code() {
		return code;
	}

	public String description() {
		return description;
	}
	
	public void checkValue(String value)throws Exception{
		if(this == INT){
			Integer.parseInt(value);
		}
		else if(this == LONG){
			Long.valueOf(value);
		}
		else if(this == DOUBLE || this == LONGITUDE || this == LATITUDE){
			Double.valueOf(value);
		}
		else if(this == FLOAT ){
			Float.valueOf(value);
		}
		
	}
	
	public static org.csi.yucca.adminapi.model.DataType getFromId(Integer idDataType){
		org.csi.yucca.adminapi.model.DataType result = null;
		for (DataType dt : DataType.values()) {
			if(idDataType == dt.id()){
				result = new org.csi.yucca.adminapi.model.DataType(dt.id(), dt.code(), dt.description());
			}
		}
		return result;
	}
	
//	public static String getHiveTypeFromId(Integer idDataType){
//		org.csi.yucca.adminapi.model.DataType result = null;
//		for (DataType dt : DataType.values()) {
//			if(idDataType == dt.id()){
//				result = new org.csi.yucca.adminapi.model.DataType(dt.id(), dt.code(), dt.description());
//			}
//		}
//		return result;
//	}
	
	
	
}