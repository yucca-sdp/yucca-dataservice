/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.importmetadata.dbConf;

import org.csi.yucca.adminapi.importmetadata.DatabaseConfiguration;
import org.csi.yucca.adminapi.util.Constants;


public class OracleConfiguration extends DatabaseConfiguration {

	@Override
	protected void initTypesMap() {
		typesMap.put("VARCHAR2", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("NVARCHAR2", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("NUMBER", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("FLOAT", Constants.ADMINAPI_DATA_TYPE_FLOAT);
		typesMap.put("LONG", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("DATE", Constants.ADMINAPI_DATA_TYPE_DATETIME);
		typesMap.put("BINARY_FLOAT", Constants.ADMINAPI_DATA_TYPE_FLOAT);
		typesMap.put("BINARY_DOUBLE", Constants.ADMINAPI_DATA_TYPE_DOUBLE);
		typesMap.put("TIMESTAMP", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("INTERVAL YEAR", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("INTERVAL DAY", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("RAW", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("LONG RAW", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("ROWID", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("UROWID", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("CHAR", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("NCHAR", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("CLOB", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("NCLOB", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("BLOB", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("BFILE", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("DOUBLE", Constants.ADMINAPI_DATA_TYPE_DOUBLE);
		
		hiveTypesMap.put("NUMBER",     "DECIMAL");  // si precisione
		hiveTypesMap.put("NUMBER_INT",     "INT");
		hiveTypesMap.put("NUMBER_BIGINT",  "BIGINT");
		hiveTypesMap.put("NUMBER_DECIMAL", "DECIMAL"); // si precisione
		hiveTypesMap.put("BINARY_DOUBLE",  "DOUBLE");
		hiveTypesMap.put("BINARY_FLOAT",   "FLOAT");
		hiveTypesMap.put("CHAR",           "CHAR"); // si precisione
		hiveTypesMap.put("NCHAR",          "CHAR");// si precisione
		hiveTypesMap.put("VARCHAR2",       "STRING"); 
		hiveTypesMap.put("NVARCHAR2",      "STRING");
		hiveTypesMap.put("DATE",           "TIMESTAMP");
		hiveTypesMap.put("TIMESTAMP",      "TIMESTAMP");
		hiveTypesMap.put("RAW",            "BINARY");
		hiveTypesMap.put("FLOAT",          "FLOAT");

	}

	@Override
	protected String getConnectionUrl(String hostname, String dbname) {
		return "jdbc:oracle:thin:@" + hostname + ":" + dbname; // jdbc:oracle:thin:@localhost:1521:caspian;
	}

	@Override
	protected void initDbDriver() {
		dbDriver = "oracle.jdbc.driver.OracleDriver";
	}

}
