/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.ComponentResponse;
import org.csi.yucca.adminapi.util.DatasetSubtype;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
//

//
public class HiveDelegate {

	// quando creo dataset o nuova versione
	// Ad un salvataggio di un dataset effettuare la seguente operazione:
	//
	//
	// - verificare che esista il db di stage per il tenant manager

	// verifica che ci sia il db "stg_" + organizationCode + "_" +
	// tenantCode.replaceAll("-", "_")).toLowerCase();
	// - SE SI :
	// * * creare una tabella external (o ricrearla) con le colonne dell'ultima
	// versione che punti alla cartella
	//
	// * * creare o modificare su ranger le policy di accesso alla cartella hive
	// per il tenant manager e quelli che sono delegati
	// * * forzare lo scarico CSV

	private static final Logger logger = Logger.getLogger(HiveDelegate.class);

	private static HiveDelegate hiveDelegate;

	public static Map<String, String> yuccaHiveTypesMap = null;

	@Value("${hive.jdbc.user}")
	private String hiveUser;

	@Value("${hive.jdbc.password}")
	private String hivePassword;

	@Value("${hive.jdbc.url}")
	private String hiveUrl;

	public HiveDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		logger.info("[HiveDelegate::HiveDelegate]  store url " + hiveUrl);
	}

	public static HiveDelegate build() {
		if (hiveDelegate == null)
			hiveDelegate = new HiveDelegate();
		return hiveDelegate;
	}

	private Connection getHiveConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.apache.hive.jdbc.HiveDriver");
		Connection connection = DriverManager.getConnection(hiveUrl, hiveUser, hivePassword);
		return connection;
	}

	public static String getHiveStageAreaFromOrganizationTenant(String organizationCode, String tenantCode) {
		return ("stg_" + organizationCode + "_" + tenantCode.replaceAll("-", "_")).toLowerCase();
	}

	private String getDbName(String organizationCode, String tenantCode) {
		return getHiveStageAreaFromOrganizationTenant(organizationCode, tenantCode);
	}

//	public String getOrganizationStageArea(String organizationCode, String tenantCode) throws ClassNotFoundException, SQLException {
//		DatabaseMetaData meta = getHiveConnection().getMetaData();
//		String[] types = { "TABLE" };
//		ResultSet tablesResultSet = meta.getTables(null, null, "%", types);
//		String result = null;
//		String hiveStageArea = getHiveStageAreaFromOrganizationTenant(organizationCode, tenantCode);
//		while (tablesResultSet.next()) {
//			if (tablesResultSet.getString("TABLE_SCHEM").equalsIgnoreCase(hiveStageArea))
//				result = tablesResultSet.getString("TABLE_SCHEM");
//		}
//		return result;
//	}

	public void createExternalTable(String tableName, String organizationCode, String tenantCode, BackofficeDettaglioStreamDatasetResponse datasource)
			throws SQLException, ClassNotFoundException {
		logger.info("[HiveDelegate::createExternalTable] START " + datasource.getDataset().getDatasetcode());
		Connection hiveConnection = getHiveConnection();
		// drop existing table

		try {
			String hiveDropCommand = "drop table " + getDbName(organizationCode, tenantCode) + "." + tableName;
			logger.info("[HiveDelegate::createExternalTable] hiveDropCommand " + hiveDropCommand);
			Statement stmtDrop = hiveConnection.createStatement();
			stmtDrop.execute(hiveDropCommand.toString());

		} catch (Exception e) {
			logger.error("[HiveDelegate::createExternalTable] error in drop " + e.getMessage());
			e.printStackTrace();
		}

		Statement stmt = null;
		try {
			StringBuffer hiveCreateCommand = new StringBuffer();
			String location = HdfsDelegate.build().getHdfsDir(organizationCode, datasource);
	
			logger.info("[HiveDelegate::createExternalTable] location " + location);
			hiveCreateCommand.append("create external table " + getDbName(organizationCode, tenantCode) + "." + tableName + "(");
			if ((datasource.getDataset().getDatasetSubtype().getIdDatasetSubtype()).equals(DatasetSubtype.STREAM.id()) || (datasource.getDataset().getDatasetSubtype().getIdDatasetSubtype()).equals(DatasetSubtype.SOCIAL.id()))
				hiveCreateCommand.append("`TIME` " + getHiveTypeFromYuccaType("dateTime")+",");
			int counter = 0;
			for (ComponentResponse column : datasource.getComponents()) {				
				hiveCreateCommand.append("`" + column.getName() + "` " + getHiveTypeFromYuccaType(column.getDataType().getDatatypecode()));
				counter++;
				if (counter < datasource.getComponents().size())
					hiveCreateCommand.append(",");
			}
			hiveCreateCommand.append(")");
			hiveCreateCommand.append(" row format serde 'org.apache.hadoop.hive.serde2.OpenCSVSerde' ");
			hiveCreateCommand.append(" with serdeproperties (");
			hiveCreateCommand.append("\"separatorChar\" = \",\",");
			hiveCreateCommand.append("\"quoteChar\" = \"\\\"\",");
			hiveCreateCommand.append("\"escapeChar\" = \"\\\\\"");
			hiveCreateCommand.append(")");
			hiveCreateCommand.append("stored as textfile  location '" + location + "/' tblproperties (\"skip.header.line.count\"=\"1\")");
	
			logger.info("[HiveDelegate::createExternalTable] hiveCreateCommand " + hiveCreateCommand);
	
			stmt = hiveConnection.createStatement();
			
			stmt.execute(hiveCreateCommand.toString());
		}finally {
			if(stmt !=null)
				stmt.close();
			if(hiveConnection!=null)
				hiveConnection.close();
		}

	}

	public static String getHiveTypeFromYuccaType(String yuccaType) {

		if (yuccaHiveTypesMap == null) {
			yuccaHiveTypesMap = new HashMap<String, String>();
			yuccaHiveTypesMap.put("int", "INT");
			yuccaHiveTypesMap.put("long", "BIGINT");
			yuccaHiveTypesMap.put("longitude", "DOUBLE");
			yuccaHiveTypesMap.put("latitude", "DOUBLE");
			yuccaHiveTypesMap.put("double", "DOUBLE");
			yuccaHiveTypesMap.put("float", "FLOAT");
			yuccaHiveTypesMap.put("string", "STRING");
			yuccaHiveTypesMap.put("dateTime", "TIMESTAMP");
			yuccaHiveTypesMap.put("boolean", "BOOLEAN");
			yuccaHiveTypesMap.put("binary", "BINARY");
		}
		return yuccaHiveTypesMap.get(yuccaType);
	}

}
