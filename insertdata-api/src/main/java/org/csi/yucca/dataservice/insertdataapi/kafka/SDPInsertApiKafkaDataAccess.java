/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.kafka;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsert;
import org.csi.yucca.dataservice.insertdataapi.model.output.FieldsDto;
import org.csi.yucca.dataservice.insertdataapi.util.DateUtil;

import com.mongodb.BulkWriteResult;
import com.mongodb.util.JSON;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONArray;

public class SDPInsertApiKafkaDataAccess {

	private static final Logger log = Logger.getLogger("org.csi.yucca.datainsert");

	public int insertBulk(String collection, DatasetBulkInsert dati, String tenant) throws Exception {
		int insertedCount = 0;
		try {
			Collection<SolrInputDocument> list = new ArrayList<SolrInputDocument>();
			Iterator<Entry<String, FieldsDto>> fieldIter = null;
			Iterator<JSONObject> iter = dati.getJsonRowsToInsert().iterator();
			List<String> outs = new LinkedList<String>();
			while (iter.hasNext()) {
				// SolrInputDocument doc = new SolrInputDocument();
				JSONObject json = iter.next();
				log.info("json input " + json);
				JSONObject out = new JSONObject();

				out.put("id", json.get("objectid").toString());
				out.put("iddataset_l", (Integer.parseInt(Long.toString(dati.getIdDataset()))));
				out.put("datasetversion_l", (Integer.parseInt(Long.toString(dati.getDatasetVersion()))));

				if (!dati.getDatasetType().equals("bulkDataset") && !dati.getDatasetType().equals("binaryDataset")) {
					out.put("time_dt", DateUtil.convertToStd(json.get("time").toString()));
					out.put("sensor_s", dati.getSensor());
					out.put("streamcode_s", dati.getStream());
				}

				fieldIter = dati.getFieldsType().entrySet().iterator();
				while (fieldIter.hasNext()) {
					Entry<String, FieldsDto> field = fieldIter.next();
					String nome = field.getKey().toLowerCase();
					String tipo = (field.getValue()).getFieldType();
					Object value = json.get(field.getKey());

					// if(null != value) {
					if ("int".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_i", null);
						else
							out.put(nome + "_i", Integer.parseInt(value.toString()));
					} else if ("long".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_l", null);
						else
							out.put(nome + "_l", Long.parseLong(value.toString()));
					} else if ("double".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_d", null);
						else
							out.put(nome + "_d", Double.parseDouble(value.toString()));
					} else if ("float".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_f", null);
						else
							out.put(nome + "_f", (Float.parseFloat(value.toString())));
					} else if ("string".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_s", null);
						else
							out.put(nome + "_s", value.toString());
					} else if ("binary".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_s", null);
						else
							out.put(nome + "_s", value.toString());
					} else if ("boolean".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_b", null);
						else
							out.put(nome + "_b", Boolean.parseBoolean(value.toString()));
					} else if ("datetime".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_dt", null);
						else
							out.put(nome + "_dt", DateUtil.convertToStd(value.toString()));
					} else if ("date".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_dt", null);
						else
							out.put(nome + "_dt", DateUtil.convertToStd(value.toString()));
					} else if ("longitude".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_d", null);
						else
							out.put(nome + "_d", Double.parseDouble(value.toString()));
					} else if ("latitude".equalsIgnoreCase(tipo)) {
						if (null == value)
							out.put(nome + "_d", null);
						else
							out.put(nome + "_d", Double.parseDouble(value.toString()));
					}

					// }
				}

				log.info("json output " + out);
				if (dati.getJsonRowsToInsert().size() == 1) {
					log.info("kafka send single - tenant: " + tenant + " - datasetcode: " + dati.getDatasetCode()
							+ " - datasetversion " + dati.getDatasetVersion() + " - collection: " + collection);

					KafkaWriterImplSingleton.getInstance().send(tenant, dati.getDatasetCode(),
							(int) (dati.getDatasetVersion()), collection, out.toString());
				} else
					outs.add(out.toString());

				insertedCount++;
			}
			if (dati.getJsonRowsToInsert().size() > 1) {
				log.info("kafka send multi - tenant: " + tenant + " - datasetcode: " + dati.getDatasetCode()
				+ " - datasetversion " + dati.getDatasetVersion() + " - collection: " + collection);
				KafkaWriterImplSingleton.getInstance().send(tenant, dati.getDatasetCode(),
						(int) (dati.getDatasetVersion()), collection, outs);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return insertedCount;
	}
}
