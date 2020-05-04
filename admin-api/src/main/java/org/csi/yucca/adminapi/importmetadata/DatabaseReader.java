/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.importmetadata;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.model.Component;
import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.model.DataType;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.response.DettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.util.Constants;
import org.csi.yucca.adminapi.util.TenantType;
import org.csi.yucca.adminapi.util.Util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabaseReader {

	private String organizationCode;
	private String tenantCode;
	private String dbType;
	private String dbUrl;
	private String dbName;
	private String username;
	private String password;
	private String dbSchema;
	private String domain;
	private String subdomain;

	private List<DettaglioDataset> existingMedatataList;
	private DatabaseConfiguration databaseConfiguation;

	List<Dataset> dataset = new LinkedList<Dataset>();
	Map<String, List<String>> columnWarnings = new HashMap<String, List<String>>();

	Map<String, String> fkMap;
	static Logger log = Logger.getLogger(DatabaseReader.class);

	ObjectMapper mapper = new ObjectMapper();
	private Integer tenantType;
	private String databaseProductVersion = null;

	private ServletContext servletContext;
	private boolean importFromBackoffice;

	public DatabaseReader(ServletContext servletContext, String organizationCode, String tenantCode, String dbType, String dbUrl, String dbName, String username, String password,
			List<DettaglioDataset> existingMedatataList, String hiveUser, String hivePassword, String hiveUrl, Integer tenantType) throws ImportDatabaseException {
		super();
		log.debug("[DatabaseReader::DatabaseReader] START - dbType:  " + dbType + ", dbUrl: " + dbUrl + ", dbName: " + dbName + ", username: " + username);
		this.importFromBackoffice = false;
		this.servletContext = servletContext;
		this.existingMedatataList = existingMedatataList;
		this.organizationCode = organizationCode;
		this.tenantCode = tenantCode;
		this.dbType = dbType;
		this.dbUrl = dbUrl;
		this.dbName = dbName;

		if (username != null && username.contains(":")) {
			String[] usernameSchemaDB = username.split(":");
			this.username = usernameSchemaDB[0];
			this.dbSchema = usernameSchemaDB[1].toUpperCase();
		} else {
			this.username = username;
			//if (dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE) || dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE_8))
			if (!dbType.equals(DatabaseConfiguration.DB_TYPE_HIVE)) 
				this.dbSchema = username.toUpperCase();
		}
		this.password = password;

		if (DatabaseConfiguration.DB_TYPE_HIVE.equals(dbType)) {
			// this.dbUrl = "yucca_datalake";

			this.dbName = getHiveDbName(tenantType, tenantCode, organizationCode);
			// area
			this.username = hiveUser;
			this.password = hivePassword;
			this.dbUrl = hiveUrl;
			this.dbSchema =  this.dbName;
		}
		this.tenantType = tenantType;

		databaseConfiguation = DatabaseConfiguration.getDatabaseConfiguration(dbType);

		if (databaseConfiguation == null)
			throw new ImportDatabaseException(ImportDatabaseException.INVALID_DB_TYPE,
					"Database type used: " + dbType + " - Database type supported: MYSQL, ORACLE, POSTGRESQL, HIVE");

	}
	
	public DatabaseReader(ServletContext servletContext, String organizationCode,String dbType, String dbUrl, String dbName, String username, String password,List<DettaglioDataset> existingMedatataList,
			 String hiveUser, String hivePassword, String hiveUrl,String domain,String subdomain) throws ImportDatabaseException {
		super();
		log.debug("[DatabaseReader::DatabaseReader] START - dbType:  " + dbType + ", dbUrl: " + dbUrl + ", dbName: " + dbName + ", username: " + username);
		this.importFromBackoffice = true;
		this.servletContext = servletContext;
		this.organizationCode = organizationCode;
		this.existingMedatataList = existingMedatataList;
		this.dbType = dbType;
		this.dbUrl = dbUrl;
		this.dbName = dbName;
		this.domain = domain;
		this.subdomain = subdomain;

		if (username != null && username.contains(":")) {
			String[] usernameSchemaDB = username.split(":");
			this.username = usernameSchemaDB[0];
			this.dbSchema = usernameSchemaDB[1].toUpperCase();
		} else {
			this.username = username;
			if (dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE) || dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE_8))
				this.dbSchema = username.toUpperCase();
		}
		this.password = password;

		if (DatabaseConfiguration.DB_TYPE_HIVE.equals(dbType)) {
			// this.dbUrl = "yucca_datalake";

			this.dbName = getHiveDbName(domain, subdomain, organizationCode);
			// area
			this.username = hiveUser;
			this.password = hivePassword;
			this.dbUrl = hiveUrl;
			this.dbSchema =  this.dbName;
		}

		databaseConfiguation = DatabaseConfiguration.getDatabaseConfiguration(dbType);

		if (databaseConfiguation == null)
			throw new ImportDatabaseException(ImportDatabaseException.INVALID_DB_TYPE,
					"Database type used: " + dbType + " - Database type supported: MYSQL, ORACLE, POSTGRESQL, HIVE");

	}

	public static String getHiveDbName(Integer tenantType, String tenantCode, String organizationCode) {
		// String startDbName = tenantType != null && TenantType.DEVELOP.id() ==
		// tenantType.intValue() ? "svilstg_" : "stg_";

		if (tenantType != null && TenantType.DEVELOP.id() == tenantType.intValue())
			return ("svilstg_" + tenantCode.replaceAll("-", "_")).toLowerCase();
		return ("stg_" + organizationCode + "_" + tenantCode.replaceAll("-", "_")).toLowerCase();
	}
	
	public static String getHiveDbName(String domain, String subdomain, String organizationCode) {
		return ("db_" + organizationCode.toLowerCase() + "_" + domain.toLowerCase() + "_" + subdomain.toLowerCase());
	}

	// URLClassLoader oracleClassLoader = null;
	private Connection getConnection() throws ClassNotFoundException, ImportDatabaseException {
		log.info("[DatabaseReader::getConnection] START modificato");
		Connection connection = null;
		boolean debugLocal = false;
		if (!debugLocal && (dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE) || dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE_8))) {
			log.info("[DatabaseReader::getConnection] DB-ORACLE");

			try {
				log.info("[DatabaseReader::getConnection] jar ok");
				String jdbcJar = dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE_8) ? "classes12.zip" : "ojdbc6.jar";
				URL[] cp = new URL[] { servletContext.getResource("/extLib/" + jdbcJar) };
				URLClassLoader oracleClassLoader = new URLClassLoader(cp, ClassLoader.getSystemClassLoader());
				log.info("[DatabaseReader::getConnection] oracleClassLoader ok");
				Class<?> drvClass = oracleClassLoader.loadClass("oracle.jdbc.OracleDriver");
				log.info("[DatabaseReader::getConnection] drvClass ok");
				Driver oracledriver = (Driver) drvClass.newInstance();
				log.info("[DatabaseReader::getConnection] oracledriver ok");

				Properties props = new Properties();
				props.setProperty("user", username);
				props.setProperty("password", password);
				connection = oracledriver.connect(databaseConfiguation.getConnectionUrl(dbUrl, dbName), props);

			} catch (Exception e) {
				log.info("[DatabaseReader::getConnection] ERROR " + e.getMessage());

				e.printStackTrace();
				throw new ImportDatabaseException(ImportDatabaseException.CONNECTION_FAILED, e.getMessage());
			} finally {
				// if (ora8loader != null)
				// try {
				// ora8loader.close();
				// } catch (IOException e) {
				// log.info("[DatabaseReader::getConnection] ERROR " +
				// e.getMessage());
				// e.printStackTrace();
				// }
			}
		} else {
			Class.forName(databaseConfiguation.getDbDriver());
			log.debug("[DatabaseReader::getConnection] Driver Loaded.");
			try {
				connection = DriverManager.getConnection(databaseConfiguation.getConnectionUrl(dbUrl, dbName), username, password);
				if (dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE) || dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE_8)) {
					((oracle.jdbc.driver.OracleConnection) connection).setIncludeSynonyms(true);
					((oracle.jdbc.driver.OracleConnection) connection).setRemarksReporting(true);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new ImportDatabaseException(ImportDatabaseException.CONNECTION_FAILED, e.getMessage());
			}
		}
		return connection;
	}

	public String loadSchema() throws ClassNotFoundException, ImportDatabaseException, SQLException, IOException {

		log.debug("[DatabaseReader::loadSchema] START");
		Connection conn = getConnection();
		log.debug("[DatabaseReader::loadSchema]  Got Connection.");

		Map<String, DettaglioDataset> existingMetadataMap = mapExistingMetadata();
		log.debug("[DatabaseReader::loadSchema]  existing metadata loaded.");

		DatabaseMetaData meta = conn.getMetaData();
		databaseProductVersion = meta.getDatabaseProductVersion();

		String[] types = { "TABLE", "VIEW", "SYNONYM" };
		List<DatabaseTableDataset> tables = new LinkedList<DatabaseTableDataset>();

		ResultSet tablesResultSet = null;
		// ClassLoader thatLoader =
		// Thread.currentThread().getContextClassLoader();
		// Thread.currentThread().setContextClassLoader(ora8loader);
		// try {
		String hiveStageArea = "";
		if (tenantType !=  null && tenantCode != null)
		 hiveStageArea = getHiveDbName(tenantType, tenantCode, organizationCode);// ("stg_"
		// +
		// organizationCode
		// +
		// "_"
		// +
		// tenantCode.replaceAll("-",
		// "_")).toLowerCase();
		if (domain !=  null && subdomain != null)
			hiveStageArea = getHiveDbName(domain, subdomain, organizationCode);

		log.info("[DatabaseReader::loadSchema] getTables - dbSchema: " + dbSchema);
		if (dbType.equals(DatabaseConfiguration.DB_TYPE_HIVE) && hiveStageArea != "" )
			tablesResultSet = meta.getTables(null, hiveStageArea, "%",types);
		else
			tablesResultSet = meta.getTables(null, "%", "%",types);
		// } finally {
		// Thread.currentThread().setContextClassLoader(thatLoader);
		// }

		if (dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE) || dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE_8)) {
			loadFk(meta, null);
			loadComponentsMetadata(conn);

		}

		// Map<String, String> pkMap = loadPk(meta);

		//
		// TABLE_CAT String => table catalog (may be null)
		// TABLE_SCHEM String => table schema (may be null)
		// TABLE_NAME String => table name
		// TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW",
		// "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
		// "SYNONYM".
		// REMARKS String => explanatory comment on the table
		// TYPE_CAT String => the types catalog (may be null)
		// TYPE_SCHEM String => the types schema (may be null)
		// TYPE_NAME String => type name (may be null)
		// SELF_REFERENCING_COL_NAME String => name of the designated
		// "identifier" column of a typed table (may be null)
		// REF_GENERATION String => specifies how values in
		// SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER",
		// "DERIVED". (may be null)

		while (tablesResultSet.next()) {
			
			String tableSchema = tablesResultSet.getString("TABLE_SCHEM");
			if(dbSchema==null || tableSchema == null || tableSchema.equalsIgnoreCase(dbSchema)){

				String tableName = tablesResultSet.getString("TABLE_NAME");
				String tableCat = tablesResultSet.getString("TABLE_CAT");
				String tableType = tablesResultSet.getString("TABLE_TYPE");
				String metadataColumnComment = "REMARKS";
				if ((dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE) || dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE_8)) && databaseProductVersion != null
						&& databaseProductVersion.toUpperCase().contains("ORACLE8"))
					metadataColumnComment = "table_remarks";
	
				String tableComment = tablesResultSet.getString(metadataColumnComment);
				log.debug("[DatabaseReader::loadSchema] tableName " + tableName);
	
				DatabaseTableDataset table = new DatabaseTableDataset();
				table.setTableName(tableName);
	
				boolean checkColumOracle = !(dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE) || dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE_8))
						|| ((dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE) || dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE_8))
								&& componentsMetadata.get(tableName) != null);
	
				// System.out.println("tableType:" + tableType + ", tableSchema: " +
				// tableSchema + ", tableName: " + tableName + " tableCat: " +
				// tableCat);
				if (!tableName.equals("TOAD_PLAN_TABLE") && !tableName.equals("PLAN_TABLE") && checkColumOracle
						&& ((dbType.equals(DatabaseConfiguration.DB_TYPE_HIVE) && tableSchema.toLowerCase().equals(hiveStageArea))
								|| (/*
									 * (tableSchema == null ||
									 * username.toUpperCase().equalsIgnoreCase(
									 * tableSchema)) &&
									 */ (tableCat == null || dbName.toUpperCase().equalsIgnoreCase(tableCat))))) {
					// printResultSetColumns(tablesResultSet);
					// System.out.println("tableType:" + tableType +
					// ", tableSchema: " + tableSchema + ", tableName: " + tableName
					// + " tableCat: " + tableCat);
					ComponentJson[] components = new ComponentJson[0];
	
					// ****************************************
					// DB DIVERSO DA ORACLE
					// ****************************************
					if (!dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE) || dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE_8)) {
	
						try {
							loadFk(meta, tableName);
						} catch (Exception e) {
							log.error("[DatabaseReader::loadSchema] error while loading fk  of table " + tableName + " - message: " + e.getMessage());
							e.printStackTrace();
							table.addWarning("Error loading foreign keys: " + e.getMessage());
						}
	
						try {
							components = loadColumns(meta, tableName, tableSchema);
						} catch (Exception e) {
							log.error("[DatabaseReader::loadSchema] error while loading fk  of table " + tableName + " - message: " + e.getMessage());
							e.printStackTrace();
							table.addWarning("Error loading foreign keys: " + e.getMessage());
						}
	
					}
	
					// ****************************************
					// DB ORACLE
					// ****************************************
					else {
						try {
							components = loadColumnsOracle(conn, tableName, tableSchema);
						} catch (Exception e) {
							log.error("[DatabaseReader::loadSchema] error while loading fk  of table " + tableName + " - message: " + e.getMessage());
							e.printStackTrace();
							table.addWarning("Error loading foreign keys: " + e.getMessage());
						}
					}
	
					for (ComponentJson component : components) {
						if (fkMap.containsKey(tableName + "." + component.getSourcecolumnname())) {
							component.setForeignkey(fkMap.get(tableName + "." + component.getSourcecolumnname()));
						}
					}
	
					DettaglioDataset metadata = existingMetadataMap.get(tableName);
	
					if (metadata == null) {
						table.setStatus(DatabaseTableDataset.DATABASE_TABLE_DATASET_STATUS_NEW);
						table.setTableType(tableType);
						metadata = new DettaglioDataset();
						metadata.setDatasetname(tableName);
						String description = "Imported from " + dbName + " " + tableName + (tableComment != null ? " - " + tableComment : "");
						metadata.setDescription(description);
	
						// String componentsJson =
						// mapper.writeValueAsString(components);
						metadata.setComponents(components);
	
						metadata.getComponents();
	
						if(importFromBackoffice) {
							metadata.setDbhiveschema(dbSchema);
							metadata.setDbhivetable(tableName);
						}
						else {
							metadata.setJdbcdbname(dbName);
							if (DatabaseConfiguration.DB_TYPE_HIVE.equals(dbType)) {
								metadata.setJdbcdburl("yucca_datalake");
							} else {
								metadata.setJdbcdburl(dbUrl);
							}
							metadata.setJdbcdbtype(dbType);
							metadata.setJdbctablename(tableName);
							if (dbSchema != null)
								metadata.setJdbcdbschema(dbSchema);
							else
								metadata.setJdbcdbschema(tableSchema);
						}
	
					} else {
						table.setStatus(DatabaseTableDataset.DATABASE_TABLE_DATASET_STATUS_EXISTING);
						table.setTableType(tableType);
	
						List<ComponentJson> newComponents = new LinkedList<ComponentJson>();
						ComponentJson[] existingComponents = metadata.getComponents();
						for (int i = 0; i < components.length; i++) {
							ComponentJson existingComponent = findExistingComponentFromSourceColumnName(components[i].getSourcecolumnname(), existingComponents);
							if (existingComponent == null) {
								newComponents.add(components[i]);
							} else {
								if (existingComponent.getSourcecolumn() == null)
									existingComponent.setSourcecolumn(i + 1);
								// update info from db (like new jdbcnative and
								// hive)
								existingComponent.setJdbcNativeType(components[i].getJdbcNativeType());
								existingComponent.setHiveType(components[i].getHiveType());
	
								components[i] = existingComponent;
							}
						}
	
						if (newComponents.size() > 0) {
							// Component[] newComponentsArray = new
							// Component[newComponents.size() +
							// metadata.getInfo().getComponents().length];
							// int counter = 0;
							// for (Component component :
							// metadata.getInfo().getComponents()) {
							// newComponentsArray[counter] = component;
							// counter++;
							// }
							// for (Component component : newComponents) {
							// newComponentsArray[counter] = component;
							// counter++;
							// }
							// String componentsJson =
							// mapper.writeValueAsString(components);
	
							metadata.setComponents(components);
							table.setNewComponents(newComponents);
						}
					}
	
					loadPk(meta, tableName, components);
	
					if (columnWarnings.containsKey(table.getTableName())) {
						for (String warning : columnWarnings.get(table.getTableName())) {
							table.addWarning(warning);
						}
					}
					try {
						table.setDataset(new DettaglioStreamDatasetResponse(metadata));
						tables.add(table);
					} catch (Exception e) {
						e.printStackTrace();
						log.error("[DatabaseReader::loadSchema] error while adding  of table " + tableName + " - message: " + e.getMessage());
					}
				}
			}
		}

		String json = mapper.writeValueAsString(tables);

		conn.close();

		return json;
	}

	private ComponentJson findExistingComponentFromSourceColumnName(String sourcecolumnname, ComponentJson[] existingComponents) {
		ComponentJson foundedComponent = null;
		if (existingComponents != null) {
			for (ComponentJson component : existingComponents) {
				if (component.getSourcecolumnname() != null && sourcecolumnname != null && component.getSourcecolumnname().equals(sourcecolumnname)) {
					foundedComponent = component;
					break;
				}

			}
		}
		return foundedComponent;
	}

	private Map<String, DettaglioDataset> mapExistingMetadata() {
		log.debug("[DatabaseReader::loadExistingMetadata] START");

		Map<String, DettaglioDataset> existingMedatata = new HashMap<String, DettaglioDataset>();
		if (existingMedatataList != null) {
			if (existingMedatataList != null && existingMedatataList.size() > 0) {

				for (DettaglioDataset dettaglioDataset : existingMedatataList) {
					String key = importFromBackoffice?dettaglioDataset.getDbhivetable():dettaglioDataset.getJdbctablename();
					existingMedatata.put(key, dettaglioDataset);
				}
			}
		}

		return existingMedatata;

	}

	private ComponentJson[] loadColumns(DatabaseMetaData metaData, String tableName, String tableSchema) throws SQLException {
		List<ComponentJson> components = new LinkedList<ComponentJson>();

		ResultSet columnsResultSet = null;
		try {
			// https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html#getColumns-java.lang.String-java.lang.String-java.lang.String-java.lang.String-
			columnsResultSet = metaData.getColumns(null, tableSchema, tableName, null);
			int columnCounter = 1;
			while (columnsResultSet.next()) {
				ComponentJson component = new ComponentJson();
				String columnName = columnsResultSet.getString("COLUMN_NAME");
				// component.setName(Util.camelcasefy(columnName, "_"));
				component.setName(columnName);
				if (columnsResultSet.getString("REMARKS") != null)
					component.setAlias(columnsResultSet.getString("REMARKS"));
				else
					component.setAlias(columnName.replace("_", " "));

				String columnType = columnsResultSet.getString("TYPE_NAME");
				Integer precision = null;
				if (columnsResultSet.getString("COLUMN_SIZE") != null)
					precision = Integer.parseInt(columnsResultSet.getString("COLUMN_SIZE"));
				Integer scale = null;
				if (columnsResultSet.getString("DECIMAL_DIGITS") != null)
					scale = Integer.parseInt(columnsResultSet.getString("DECIMAL_DIGITS"));

				String jdbcNativeType = columnType;
				String lengthPrecisionScale = "";

				if (precision != null && precision > 0) {
					lengthPrecisionScale += "(" + precision;
					if (scale != null && scale > 0)
						lengthPrecisionScale += "," + scale;
					lengthPrecisionScale += ")";
				}
				jdbcNativeType += lengthPrecisionScale;

				if (columnType != null) {
					String hiveType = databaseConfiguation.getHiveType(columnType.toUpperCase());
					if (hiveType != null && (hiveType.equals("DECIMAL") || hiveType.equals("CHAR")))
						hiveType += lengthPrecisionScale;
					if (columnType.equals("CHAR") ) {
						if (precision == 0) hiveType = "CHAR(255)";
						else if (precision < 255) hiveType = "CHAR("+precision+")";
						else hiveType = "STRING";
					} 
					component.setHiveType(hiveType);
				}

				DataType dataType = null;

				if (columnType != null) {
					dataType = org.csi.yucca.adminapi.util.DataType.getFromId(databaseConfiguation.getTypesMap().get(columnType));
				} else {

					if (!columnWarnings.containsKey(tableName)) {
						columnWarnings.put(tableName, new LinkedList<String>());
					}

					columnWarnings.get(tableName).add("Unkonwn data type for column " + columnName + ": " + columnType);

					log.warn("[DatabaseReader::loadColumns] unkonwn data type  " + columnType + " for table " + tableName + " column " + columnName);

					dataType = org.csi.yucca.adminapi.util.DataType.getFromId(Constants.ADMINAPI_DATA_TYPE_STRING);
				}

				component.setJdbcNativeType(jdbcNativeType);
				component.setDataType(dataType);

				component.setInorder(columnCounter);
				component.setSourcecolumn(columnCounter);
				component.setSourcecolumnname(columnName);

				components.add(component);
				columnCounter++;
			}

			ComponentJson[] componentsArr = new ComponentJson[components.size()];
			componentsArr = components.toArray(componentsArr);

			return componentsArr;
		} finally {
			if (columnsResultSet != null)
				columnsResultSet.close();
		}

	}

	private Map<String, List<String>> componentsMetadata = new HashMap<String, List<String>>();

	private PreparedStatement getStatement(Connection conn) throws SQLException {
		String query = " select a.table_name, a.column_name, a.data_type,a.data_precision, a.data_scale, a.data_length,  b.comments  "
				+ " from all_tab_columns a, all_col_comments b  WHERE a.table_name  = b.table_name AND  a.column_name = b.column_name";

		if (dbSchema != null) {
			query += " AND  a.owner=? AND b.owner=a.owner ";
		}
		
		query +=  "union select a.table_name, a.column_name, a.data_type,a.data_precision, a.data_scale, a.data_length, b.comments "    
				  + " from all_tab_columns a, all_col_comments b, all_synonyms s " + 
				  "WHERE a.table_name  = b.table_name AND  a.column_name = b.column_name and s.TABLE_NAME=b.TABLE_NAME ";
		if (dbSchema != null) {
			query += " AND  s.owner=? AND b.owner=a.owner ";
		}

		PreparedStatement statement = conn.prepareStatement(query);

		if (dbSchema != null) {
			statement.setString(1, dbSchema.toUpperCase());
			statement.setString(2, dbSchema.toUpperCase());
		}

		return statement;
	}

	private void loadComponentsMetadata(Connection conn) throws SQLException {

		PreparedStatement statement = getStatement(conn);

		ResultSet rs = null;

		try {

			rs = statement.executeQuery();

			while (rs.next()) {
				String tableName = rs.getString("table_name");
				String columnName = rs.getString("column_name");
				String columnComment = rs.getString("comments");
				String columnType = rs.getString("data_type");
				Integer precision = rs.getInt("data_precision");
				Integer scale = rs.getInt("data_scale");
				Integer length = rs.getInt("data_length");

				if (columnComment == null || columnComment.trim().equals("")) {
					columnComment = columnName.replace("_", " ");
				} else if (columnComment.length() > 500) {
					columnComment = columnComment.substring(0, 500);
				}

				Integer idDataType = Constants.ADMINAPI_DATA_TYPE_STRING;
				if (columnType != null && databaseConfiguation.getTypesMap().get(columnType) != null)
					idDataType = databaseConfiguation.getTypesMap().get(columnType);
				else {

					if (!columnWarnings.containsKey(tableName)) {
						columnWarnings.put(tableName, new LinkedList<String>());
					}

					columnWarnings.get(tableName).add("Unkonwn data type for column " + columnName + ": " + columnType);

					log.warn("[DatabaseReader::loadComponentsMetadata] unkonwn data type  " + columnType + " for table " + tableName + " column " + columnName);
				}

				if (!componentsMetadata.containsKey(tableName)) {
					componentsMetadata.put(tableName, new LinkedList<String>());
				}

				componentsMetadata.get(tableName).add(columnName + "|" + columnComment + "|" + columnType + "|" + precision + "|" + scale + "|" + length);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	/**
	 * 
	 * @param columnType
	 * @param scale
	 * @param precision
	 * @return
	 */
	private String getHiveToOracleType(String columnType, Integer scale, Integer precision) {
		if ("NUMBER".equals(columnType) && scale == 0 && precision == 0) {
			// DECIMAL(38,10)
			return "DECIMAL(38,10)";
		}
		
		if ("NUMBER".equals(columnType) && scale == 0 && precision < 10) {
			// INT
			return databaseConfiguation.getHiveType("NUMBER_INT");
		}

		if ("NUMBER".equals(columnType) && scale == 0 && precision >= 10 && precision < 19) {
			// BIGINT
			return databaseConfiguation.getHiveType("NUMBER_BIGINT");
		}

		if ("NUMBER".equals(columnType) && scale > 0 && precision >= 19) {
			// DECIMAL
			return databaseConfiguation.getHiveType("NUMBER_DECIMAL");
		}
		
		if ("NUMBER".equals(columnType) && scale < 0 ) {
			// DECIMAL
			return databaseConfiguation.getHiveType("NUMBER_DECIMAL");
		}

		return databaseConfiguation.getHiveType("NUMBER_BIGINT");
	}

	/**
	 * 
	 * @param conn
	 * @param tableName
	 * @param tableSchema
	 * @return
	 * @throws SQLException
	 */
	private ComponentJson[] loadColumnsOracle(Connection conn, String tableName, String tableSchema) throws SQLException {

		List<ComponentJson> components = new LinkedList<ComponentJson>();

		try {

			List<String> columns = componentsMetadata.get(tableName);

			int counter = 0;

			// add(columnName + "|" + columnComment + "|" + columnType + "|" +
			// precision + "|" + scale);
			for (String columnData : columns) {

				String[] columnDataSplitted = columnData.split("[|]");

				// String columnName = columnDataSplitted[0];
				// String columnComment = columnDataSplitted[1];
				String columnType = columnDataSplitted[2];
				Integer precision = Integer.parseInt(columnDataSplitted[3]);
				Integer scale = Integer.parseInt(columnDataSplitted[4]);
				Integer length = Integer.parseInt(columnDataSplitted[5]);

				ComponentJson component = new ComponentJson();

				// component.setName(Util.camelcasefy(columnDataSplitted[0],
				// "_"));
				component.setName(columnDataSplitted[0]);
				component.setAlias(columnDataSplitted[1]);

				DataType dataType = org.csi.yucca.adminapi.util.DataType.getFromId(databaseConfiguation.getTypesMap().get(columnType));
				
				if ("NUMBER".equals(columnType) && scale < 0 ) {
					// LONG
					dataType = org.csi.yucca.adminapi.util.DataType.getFromId(databaseConfiguation.getTypesMap().get("LONG"));
				}
				
				if ("NUMBER".equals(columnType) && scale == 0 && precision == 0 ) {
					// LONG
					dataType = org.csi.yucca.adminapi.util.DataType.getFromId(databaseConfiguation.getTypesMap().get("DOUBLE"));
				}

				
				component.setDataType(dataType);
				String jdbcNativeType = columnType;
				
				String lengthPrecisionScale = "";
				if (columnType.equals("VARCHAR2") && length != null) {
					lengthPrecisionScale += "(" + length + ")";

				} else if (precision != null && precision > 0) {
					lengthPrecisionScale += "(" + precision;
					if (scale != null && scale > 0)
						lengthPrecisionScale += "," + scale;
					lengthPrecisionScale += ")";
				}
				jdbcNativeType += lengthPrecisionScale;

				component.setJdbcNativeType(jdbcNativeType);

				if (columnType != null) {
					String hiveType = getHiveToOracleType(columnType, scale, precision);
					if (hiveType != null && (hiveType.equals("DECIMAL") ))//|| hiveType.equals("CHAR")))
						hiveType += lengthPrecisionScale;
					if (columnType.equals("CHAR") ) {
						if (length == 0) hiveType = "CHAR(255)";
						else if (length < 255) hiveType = "CHAR("+length+")";
						else hiveType = "STRING";
					} 				
						
					component.setHiveType(hiveType);
				}
				component.setSourcecolumn(counter);
				component.setInorder(counter);
				component.setSourcecolumnname(columnDataSplitted[0]);
				components.add(component);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ComponentJson[] componentsArr = new ComponentJson[components.size()];
		componentsArr = components.toArray(componentsArr);

		return componentsArr;

	}

	/**
	 * 
	 * @param conn
	 * @param tableName
	 * @param tableSchema
	 * @return
	 * @throws SQLException
	 */
	private Component[] loadColumnsFromMetadata(Connection conn, String tableName, String tableSchema) throws SQLException {
		List<Component> components = new LinkedList<Component>();
		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement("SELECT * from " + tableName + " WHERE 1 = 0");
			ResultSetMetaData statementMetaData = statement.getMetaData();
			for (int i = 1; i < statementMetaData.getColumnCount() + 1; i++) {
				Component component = new Component();
				String columnName = Util.camelcasefy(statementMetaData.getColumnName(i), "_");
				component.setName(columnName);

				if (statementMetaData.getColumnLabel(i) != null)
					component.setAlias(statementMetaData.getColumnLabel(i));
				else
					component.setAlias(columnName.replace("_", " "));

				String columnType = statementMetaData.getColumnTypeName(i);
				if (columnType != null && databaseConfiguation.getTypesMap().get(columnType) != null)
					component.setIdDataType(databaseConfiguation.getTypesMap().get(columnType));
				else {
					log.warn("[DatabaseReader::loadColumns] unkonwn data type  " + columnType);
					component.setIdDataType(Constants.ADMINAPI_DATA_TYPE_STRING);
				}
				component.setSourcecolumn(i - 1);
				component.setInorder(i - 1);

				component.setSourcecolumnname(columnName);

				components.add(component);

			}

			Component[] componentsArr = new Component[components.size()];
			componentsArr = components.toArray(componentsArr);

			return componentsArr;
		} finally {
			if (statement != null)
				statement.close();
		}

	}

	private void loadFk(DatabaseMetaData metaData, String table) throws SQLException {
		ResultSet foreignKeys = null;
		try {
			fkMap = new HashMap<String, String>();

			foreignKeys = metaData.getImportedKeys(null, null, table);
			while (foreignKeys.next()) {
				String fkTableName = foreignKeys.getString("FKTABLE_NAME");
				String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
				String pkTableName = foreignKeys.getString("PKTABLE_NAME");
				String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
				// System.out.println("--" + fkTableName + "." + fkColumnName +
				// " -> " + pkTableName + "." + pkColumnName);
				fkMap.put(fkTableName + "." + fkColumnName, pkTableName + "." + pkColumnName);
			}
		} finally {
			if (foreignKeys != null)
				foreignKeys.close();
		}

	}

	private void loadPk(DatabaseMetaData metaData, String tableName, ComponentJson[] components) throws SQLException {
		ResultSet primaryKeys = null;
		try {
			primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
			while (primaryKeys.next()) {
				String pkColumnName = primaryKeys.getString("COLUMN_NAME");
				for (ComponentJson component : components) {
					if (component.getSourcecolumnname().equals(pkColumnName))
						component.setIskey(Util.booleanToInt(true));
				}
			}
		} finally {
			if (primaryKeys != null)
				primaryKeys.close();
		}
	}

	public List<String> printResultSetColumns(ResultSet rs) {
		List<String> columnsName = new LinkedList<String>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			// The column count starts from 1
			for (int i = 1; i <= columnCount; i++) {
				String name = rsmd.getColumnName(i);
				// System.out.println("" + name + ": " + rs.getObject(name));
				columnsName.add(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return columnsName;
	}

	public static void main(String[] args) {

		String organizationCode = "csi";
		String tenantCode = "csi_symon";
		String dbType = DatabaseConfiguration.DB_TYPE_HIVE;
		String dbUrl = "";
		String dbName = "";
		String username = "";
		String password = "";
		List<DettaglioDataset> existingMedatataList = null;
		String hiveUser = "", hivePassword = "", 
		hiveUrl = "";
		Integer tenantType = null;
		ServletContext servletContext = null;
		try {
			DatabaseReader databaseReader = new DatabaseReader(servletContext, organizationCode, tenantCode, dbType, dbUrl, dbName, username, password, existingMedatataList,
					hiveUser, hivePassword, hiveUrl, tenantType);
			String schema = databaseReader.loadSchema();
			System.out.println("schema " + schema);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
