/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.phoenix;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.phoenix.iterate.TableResultIterator;
import org.apache.phoenix.jdbc.PhoenixConnection;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.metadata.SDPInsertMedataFactory;
import org.csi.yucca.dataservice.insertdataapi.model.output.CollectionConfDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsert;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetInfo;
import org.csi.yucca.dataservice.insertdataapi.model.output.FieldsDto;
import org.csi.yucca.dataservice.insertdataapi.util.DateUtil;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

import com.mongodb.BulkWriteResult;

import net.minidev.json.JSONObject;

public class SDPInsertApiPhoenixDataAccess {

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	// private static String takeNvlValues(Object obj) {
	// if (null==obj) return null;
	// else return obj.toString();
	// }

	private static ThreadLocal<PhoenixConnection> threadLocalConnection = new ThreadLocal<>();
	private static ThreadLocal<PhoenixConnection> threadLocalConnectionHdp3 = new ThreadLocal<>();
	
	public SDPInsertApiPhoenixDataAccess() throws ClassNotFoundException {
		// Class.forName("org.apache.phoenix.queryserver.client.Driver");
	}

	public int insertBulk(String schema, String table, DatasetBulkInsert dati,String hdpVersion) {
		// String riga=null;
		// DBObject dbObject = null;
		final int batchSize = 1000;
		int count = 0;
		PhoenixConnection conn = null;
		BulkWriteResult result = null;
		try {
			// System.out.println("###########################################");
			if (hdpVersion!= null && !hdpVersion.equals("")) {
				conn = threadLocalConnectionHdp3.get();  
				if (conn == null)
				{
					log.debug("[SDPInsertApiPhoenixDataAccess::insertBulk] No connection for thread ["+Thread.currentThread().getName()+"]");
					conn = (PhoenixConnection) DriverManager.getConnection(SDPInsertApiConfig.getInstance().getPhoenixHdp3Url());
					threadLocalConnectionHdp3.set(conn);
					log.debug("[SDPInsertApiPhoenixDataAccess::insertBulk] Saved connection for thread["+Thread.currentThread().getName()+"]");
				}
				else {
					log.debug("[SDPInsertApiPhoenixDataAccess::insertBulk] Connection found for thread ["+Thread.currentThread().getName()+"]");
				}
			}else {
				conn = threadLocalConnection.get();  
				if (conn == null)
				{
					log.debug("[SDPInsertApiPhoenixDataAccess::insertBulk] No connection for thread ["+Thread.currentThread().getName()+"]");
					conn = (PhoenixConnection) DriverManager.getConnection(SDPInsertApiConfig.getInstance().getPhoenixUrl());
					threadLocalConnection.set(conn);
					log.debug("[SDPInsertApiPhoenixDataAccess::insertBulk] Saved connection for thread["+Thread.currentThread().getName()+"]");
				}
				else {
					log.debug("[SDPInsertApiPhoenixDataAccess::insertBulk] Connection found for thread ["+Thread.currentThread().getName()+"]");
				}

			}
			
			// try {conn.commit();} catch (Exception e)
			// {log.warn("[SDPInsertApiPhoenixDataAccess:insertBulk] Invalid Connection..... Exception catched");}

			conn.setAutoCommit(false);

			// String schema = "DB_"+tenant.toUpperCase();

			String campiSQL = "iddataset_l, datasetversion_l, id ";
			String valuesSql = "(?, ?, ?  ";
			if (dati.getDatasetType().equals("streamDataset")) {
				campiSQL = campiSQL + ", time_dt, sensor_s, streamcode_s ";
				valuesSql = valuesSql + ",? ,? ,? ";

			} else if (dati.getDatasetType().equals("socialDataset")) {
				campiSQL = campiSQL + ", time_dt, sensor_s, streamcode_s ";
				valuesSql = valuesSql + ",? ,? ,? ";

			} else if (dati.getDatasetType().equals("binaryDataset")) {
			} else {
			}

			Iterator<Entry<String, FieldsDto>> fieldIter = dati.getFieldsType().entrySet().iterator();

			while (fieldIter.hasNext()) {
				Entry<String, FieldsDto> field = fieldIter.next();
				String nome = field.getKey().toUpperCase();
				String tipo = (field.getValue()).getFieldType();

				if ("int".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					campiSQL += "_I\" INTEGER";
				} else if ("long".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					campiSQL += "_L\" BIGINT";
				} else if ("double".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					campiSQL += "_D\" DOUBLE";
				} else if ("float".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					campiSQL += "_F\" FLOAT";
				} else if ("string".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					campiSQL += "_S\" VARCHAR";
				} else if ("boolean".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					//campiSQL += "_b TINYINT";
					campiSQL += "_B\" BOOLEAN";
				} else if ("datetime".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					campiSQL += "_DT\" TIMESTAMP";
				} else if ("date".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					campiSQL += "_DT\" TIMESTAMP";
				} else if ("longitude".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					campiSQL += "_D\" DOUBLE";
				} else if ("latitude".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					campiSQL += "_D\" DOUBLE";
				} else if ("binary".equalsIgnoreCase(tipo)) {
					campiSQL += ",\"" + nome;
					campiSQL += "_S\" VARCHAR";
				}

				valuesSql += ",?";
			}

			String sql = "UPSERT INTO " + schema + "." + table + " (" + campiSQL + ")  VALUES  " + valuesSql + ") ";
			log.debug("insertBulk sql " + sql);

			PreparedStatement stmt = conn.prepareStatement(sql);
			Iterator<JSONObject> iter = dati.getJsonRowsToInsert().iterator();
			while (iter.hasNext()) {
				JSONObject json = iter.next();

				stmt.setInt(1, (Integer.parseInt(Long.toString(dati.getIdDataset()))));
				stmt.setInt(2, (Integer.parseInt(Long.toString(dati.getDatasetVersion()))));
				stmt.setString(3, json.get("objectid").toString());

				int pos = 4;
				if (!dati.getDatasetType().equals("bulkDataset") && !dati.getDatasetType().equals("binaryDataset")) {
					stmt.setTimestamp(4, new Timestamp(DateUtil.multiParseDate(json.get("time").toString()).getTime()));
					stmt.setString(5, dati.getSensor());
					stmt.setString(6, dati.getStream());
					pos = 7;
				}
				fieldIter = dati.getFieldsType().entrySet().iterator();
				while (fieldIter.hasNext()) {
					Entry<String, FieldsDto> field = fieldIter.next();
					String nome = field.getKey();
					String tipo = (field.getValue()).getFieldType();

					Object value = json.get(nome);

					log.debug("insertBulk param nome: " + nome + " -  tipo: " + tipo + " - value: " + value);

					if ("int".equalsIgnoreCase(tipo)) {
						if (null == value)
							stmt.setNull(pos, java.sql.Types.INTEGER);
						else
							stmt.setInt(pos, Integer.parseInt(value.toString()));
					} else if ("long".equalsIgnoreCase(tipo)) {
						if (null == value)
							stmt.setNull(pos, java.sql.Types.BIGINT);
						else
							stmt.setLong(pos, Long.parseLong(value.toString()));
					} else if ("double".equalsIgnoreCase(tipo)) {
						if (null == value || Double.isNaN(Double.parseDouble(value.toString())) || Double.isInfinite(Double.parseDouble(value.toString())))
							stmt.setNull(pos, java.sql.Types.DOUBLE);
						else
							stmt.setDouble(pos, Double.parseDouble(value.toString()));
					} else if ("float".equalsIgnoreCase(tipo)) {
						if (null == value || Float.isNaN(Float.parseFloat(value.toString())) || Float.isInfinite(Float.parseFloat(value.toString())))
							stmt.setNull(pos, java.sql.Types.FLOAT);
						else
							stmt.setFloat(pos, (Float.parseFloat(value.toString())));
					} else if ("string".equalsIgnoreCase(tipo)) {
						if (null == value)
							stmt.setNull(pos, java.sql.Types.VARCHAR);
						else
							stmt.setString(pos, value.toString());
					} else if ("binary".equalsIgnoreCase(tipo)) {
						if (null == value)
							stmt.setNull(pos, java.sql.Types.VARCHAR);
						else
							stmt.setString(pos, value.toString());
					} else if ("boolean".equalsIgnoreCase(tipo)) {
						if (null == value)
							//stmt.setNull(pos, java.sql.Types.TINYINT);
							stmt.setNull(pos, java.sql.Types.BOOLEAN);
						else
							//stmt.setInt(pos, Boolean.parseBoolean(value.toString()) ? 1 : 0);
							stmt.setBoolean(pos, Boolean.parseBoolean(value.toString()) );
					} else if ("datetime".equalsIgnoreCase(tipo)) {
						if (null == value)
							stmt.setNull(pos, java.sql.Types.TIMESTAMP);
						else {
							Date date = DateUtil.multiParseDate(value.toString());
							if (date == null)
								stmt.setNull(pos, java.sql.Types.TIMESTAMP);
							else
								stmt.setTimestamp(pos, new Timestamp(date.getTime()));
						}
					} else if ("date".equalsIgnoreCase(tipo)) {
						if (null == value)
							stmt.setNull(pos, java.sql.Types.TIMESTAMP);
						else {
							Date date = DateUtil.multiParseDate(value.toString());
							if (date == null)
								stmt.setNull(pos, java.sql.Types.TIMESTAMP);
							else
								stmt.setTimestamp(pos, new Timestamp(date.getTime()));
						}
					} else if ("longitude".equalsIgnoreCase(tipo)) {
						if (null == value || Double.isNaN(Double.parseDouble(value.toString())) || Double.isInfinite(Double.parseDouble(value.toString())))
							stmt.setNull(pos, java.sql.Types.DOUBLE);
						else
							stmt.setDouble(pos, Double.parseDouble(value.toString()));
					} else if ("latitude".equalsIgnoreCase(tipo)) {
						if (null == value || Double.isNaN(Double.parseDouble(value.toString())) || Double.isInfinite(Double.parseDouble(value.toString())))
							stmt.setNull(pos, java.sql.Types.DOUBLE);
						else
							stmt.setDouble(pos, Double.parseDouble(value.toString()));
					}

					pos++;
				}

				stmt.addBatch();
				
				if(++count % batchSize == 0) {
					stmt.executeBatch();
				}

			}
			try {
				stmt.executeBatch();
				conn.commit();
			} catch (ArrayIndexOutOfBoundsException ae) {
				log.fatal("[SDPInsertApiPhoenixDataAccess::insertBulk][table:"+schema+"."+table+"][datasetCode:"+dati.getDatasetCode()+"][datasetVersion+"+dati.getDatasetVersion()+"] ArrayIndexOutOfBoundsException I will RETRY", ae);
				if (stmt != null)
					log.info("stmt closed?"+stmt.isClosed());
				try{
					log.fatal("[SDPInsertApiPhoenixDataAccess::insertBulk][table:"+schema+"."+table+"][datasetCode:"+dati.getDatasetCode()+"][datasetVersion+"+dati.getDatasetVersion()+"]  start RETRY");
					stmt.executeBatch();
					conn.commit();
				} catch (Throwable e) {
					log.fatal("[SDPInsertApiPhoenixDataAccess::insertBulk][table:"+schema+"."+table+"][datasetCode:"+dati.getDatasetCode()+"][datasetVersion+"+dati.getDatasetVersion()+"]  RETRY FAILEDD", ae);
					try {
						conn.rollback();
					} catch (SQLException e1) {
						log.fatal("[SDPInsertApiPhoenixDataAccess::insertBulk][table:"+schema+"."+table+"][datasetCode:"+dati.getDatasetCode()+"][datasetVersion+"+dati.getDatasetVersion()+"] ROLLBACK FAILED", e1);
					}
					throw new InsertApiRuntimeException(e);
				}
				log.fatal("[SDPInsertApiPhoenixDataAccess::insertBulk][table:"+schema+"."+table+"][datasetCode:"+dati.getDatasetCode()+"][datasetVersion+"+dati.getDatasetVersion()+"] RETRY SUCCESS");
				return result == null ? -1 : result.getInsertedCount();
			} catch (Exception e) {
				log.fatal("[SDPInsertApiPhoenixDataAccess::insertBulk][table:"+schema+"."+table+"][datasetCode:"+dati.getDatasetCode()+"][datasetVersion+"+dati.getDatasetVersion()+"] Excption", e);
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.fatal("[SDPInsertApiPhoenixDataAccess::insertBulk][table:"+schema+"."+table+"][datasetCode:"+dati.getDatasetCode()+"][datasetVersion+"+dati.getDatasetVersion()+"] ROLLBACK FAILED", e1);
				}
				throw new InsertApiRuntimeException(e);
			} finally {
				try {
					stmt.clearBatch();
					stmt.clearParameters();
					stmt.close();
//					conn.close();
				} catch (SQLException e1) {
					log.fatal("[SDPInsertApiPhoenixDataAccess::insertBulk][table:"+schema+"."+table+"][datasetCode:"+dati.getDatasetCode()+"][datasetVersion+"+dati.getDatasetVersion()+"] CLOSED FAILED", e1);
				}
			}

		} catch (SQLException e) {
			log.error("Phoenix runtime exception", e);
			throw new InsertApiRuntimeException(e);
		} catch (InsertApiRuntimeException e1) {
			log.error("Phoenix runtime exception", e1);
			throw e1;
		}
		return result == null ? -1 : result.getInsertedCount();

	}

	public int deleteData(DatasetInfo infoDataset, String tenant, Long idDataset, Long datasetVersion) {
		log.debug("[SDPInsertApiPhoenixDataAccess::deleteData]     deleteData " + infoDataset);

		int DELETE_LIMIT = 50000;
		int totalDeletedRows = 0;
		int deletedRows = -1;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(SDPInsertApiConfig.getInstance().getPhoenixUrl());

			conn.setAutoCommit(false);

			
			log.info("[SDPInsertApiPhoenixDataAccess::deleteData]     infoDataset " + infoDataset);
			log.info("[SDPInsertApiPhoenixDataAccess::deleteData]     tenant " + tenant);
			log.info("[SDPInsertApiPhoenixDataAccess::deleteData]     idDataset " + idDataset);
			log.info("[SDPInsertApiPhoenixDataAccess::deleteData]     datasetVersion " + datasetVersion);
			log.info("[SDPInsertApiPhoenixDataAccess::deleteData]     SDPInsertMedataFactory " + SDPInsertMedataFactory.getSDPInsertMetadataApiAccess());
			if (datasetVersion==null)
				datasetVersion = -1L;
			
			CollectionConfDto confDto = SDPInsertMedataFactory.getSDPInsertMetadataApiAccess()
					.getCollectionInfo(tenant, idDataset, datasetVersion, infoDataset.getDatasetSubType());
			log.info("[SDPInsertApiPhoenixDataAccess::deleteData]     SDPInsertMedataFactory Done");
			
			String schema = confDto.getPhoenixSchemaName();
			String table = confDto.getPhoenixTableName();


			String sql = "DELETE FROM " + schema + "." + table + " WHERE iddataset_l=?";

			if (datasetVersion != null && datasetVersion > 0)
				sql += " AND datasetversion_l=? ";

			sql += " LIMIT ?";

			log.info("[SDPInsertApiPhoenixDataAccess::deleteData]     sql " + sql);
			log.info("[SDPInsertApiPhoenixDataAccess::deleteData]     idDataset " + idDataset);
			log.info("[SDPInsertApiPhoenixDataAccess::deleteData]     datasetVersion " + datasetVersion);
			int counter = 0;
			try {
				while (deletedRows != 0) {
					counter++;
					log.info("[SDPInsertApiPhoenixDataAccess::deleteData]    loop - counter " + counter);
					log.info("[SDPInsertApiPhoenixDataAccess::deleteData]    loop - deletedRows " + deletedRows);
					log.info("[SDPInsertApiPhoenixDataAccess::deleteData]    loop - totalDeletedRows " + totalDeletedRows);

					PreparedStatement stmt = conn.prepareStatement(sql);
					try {
						stmt.setInt(1, (Integer.parseInt(Long.toString(idDataset))));
						if (datasetVersion != null && datasetVersion > 0) {
							stmt.setInt(2, (Integer.parseInt(Long.toString(datasetVersion))));
							stmt.setInt(3, DELETE_LIMIT);
						} else {
							stmt.setInt(2, DELETE_LIMIT);
						}

						stmt.execute();
						deletedRows = stmt.getUpdateCount();
						conn.commit();
						log.info("[SDPInsertApiPhoenixDataAccess::deleteData]    loop - deletedRows dopo " + deletedRows);
						totalDeletedRows += deletedRows;
					} finally {
						stmt.clearParameters();
						stmt.close();
					}
				}
			} catch (Exception e) {
				log.error("Insert Phoenix Error", e);
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.error("Impossible to rollback", e1);
				}
				throw new InsertApiRuntimeException(e);
			} finally {
				try {
					conn.close();
				} catch (SQLException e1) {
					log.error("Impossible to close", e1);
				}
			}

		} catch (SQLException e) {
			log.error("Phoenix runtime exception", e);
			throw new InsertApiRuntimeException(e);
		} catch (InsertApiRuntimeException e1) {
			log.error("Phoenix runtime exception", e1);
			throw e1;
		}
		return totalDeletedRows;

	}

}
