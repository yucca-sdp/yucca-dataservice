/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.solr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.csi.yucca.dataservice.insertdataapi.metadata.SDPInsertMedataFactory;
import org.csi.yucca.dataservice.insertdataapi.model.output.CollectionConfDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsert;
import org.csi.yucca.dataservice.insertdataapi.model.output.FieldsDto;
import org.csi.yucca.dataservice.insertdataapi.solr.KnoxSolrSingleton.TEHttpSolrClient;
import org.csi.yucca.dataservice.insertdataapi.util.DateUtil;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

import com.mongodb.BulkWriteResult;

import net.minidev.json.JSONObject;

public class SDPInsertApiSolrDataAccess {

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	SolrClient server = null;

	public SDPInsertApiSolrDataAccess() throws ClassNotFoundException {
		
		if ("KNOX".equalsIgnoreCase(SDPInsertApiConfig.instance.getSolrTypeAccess()))
		{
			server = KnoxSolrSingleton.getServer();
		}
		else {
			server = CloudSolrSingleton.getServer();
		}
		
	}

//	public static void main(String[] args) throws SolrServerException, IOException {
//
//		SolrClient server2 = CloudSolrSingleton.getServer();
//		server2.setDefaultCollection("tst_csp_data");
//		SolrInputDocument doc = new SolrInputDocument();
//		doc.addField("id", "ppppp");
//		doc.addField("name", "A lovely summer holiday");
//		server2.add(doc);
//		server2.commit();
//
//		server2.setParser(new XMLResponseParser());
//		SolrQuery parameters = new SolrQuery();
//
//		parameters.set("q", "*:*");
//		parameters.set("qt", "/select");
//		parameters.set("collection", "tst_csp_data");
//		QueryResponse response;
//		try {
//			response = server2.query(parameters);
//			SolrDocumentList list = response.getResults();
//			System.out.println(list.size());
//		} catch (SolrServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public int insertBulk(String collection, DatasetBulkInsert dati) throws Exception {
		BulkWriteResult result = null;
		try {



			Collection<SolrInputDocument> list = new ArrayList<SolrInputDocument>();
			Iterator<Entry<String, FieldsDto>> fieldIter = null;
			Iterator<JSONObject> iter = dati.getJsonRowsToInsert().iterator();
			while (iter.hasNext()) {
				SolrInputDocument doc = new SolrInputDocument();
				JSONObject json = iter.next();

				doc.setField("id", json.get("objectid").toString());
				doc.setField("idDataset_l", (Integer.parseInt(Long.toString(dati.getIdDataset()))));
				doc.setField("datasetVersion_l", (Integer.parseInt(Long.toString(dati.getDatasetVersion()))));

				if (!dati.getDatasetType().equals("bulkDataset") && !dati.getDatasetType().equals("binaryDataset")) {
					doc.setField("time_dt", DateUtil.convertToStd(json.get("time").toString()));
					doc.setField("sensor_s", dati.getSensor());
					doc.setField("streamCode_s", dati.getStream());
				}

				fieldIter = dati.getFieldsType().entrySet().iterator();
				while (fieldIter.hasNext()) {
					Entry<String, FieldsDto> field = fieldIter.next();
					String nome = field.getKey();
					String tipo = (field.getValue()).getFieldType();
					Object value = json.get(nome);

					if ("int".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_i", null);
						else
							doc.setField(nome + "_i", Integer.parseInt(value.toString()));
					} else if ("long".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_l", null);
						else
							doc.setField(nome + "_l", Long.parseLong(value.toString()));
					} else if ("double".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_d", null);
						else
							doc.setField(nome + "_d", Double.parseDouble(value.toString()));
					} else if ("float".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_f", null);
						else
							doc.setField(nome + "_f", (Float.parseFloat(value.toString())));
					} else if ("string".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_s", null);
						else
							doc.setField(nome + "_s", value.toString());
					} else if ("binary".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_s", null);
						else
							doc.setField(nome + "_s", value.toString());
					} else if ("boolean".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_b", null);
						else
							doc.setField(nome + "_b", Boolean.parseBoolean(value.toString()));
					} else if ("datetime".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_dt", null);
						else
							doc.setField(nome + "_dt", DateUtil.convertToStd(value.toString()));
					} else if ("date".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_dt", null);
						else
							doc.setField(nome + "_dt", DateUtil.convertToStd(value.toString()));
					} else if ("longitude".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_d", null);
						else
							doc.setField(nome + "_d", Double.parseDouble(value.toString()));
					} else if ("latitude".equalsIgnoreCase(tipo)) {
						if (null == value)
							doc.setField(nome + "_d", null);
						else
							doc.setField(nome + "_d", Double.parseDouble(value.toString()));
					}

				}

				list.add(doc);
			}
			try {
				
				if (server instanceof CloudSolrClient) {
					((CloudSolrClient)server).setDefaultCollection(collection);
				}
				if (server instanceof TEHttpSolrClient) {
					((CloudSolrClient)server).setDefaultCollection(collection);
					
				}
				server.add(collection, list);
				// server.commit(collection);
				list.clear();
			} catch (Exception e) {
				log.error("Insert Phoenix Error", e);
				throw new Exception(e);
			} finally {
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result == null ? -1 : result.getInsertedCount();

	}

	public int deleteData(String datasetType, String tenant, Long idDataset, Long datasetVersion) throws Exception {

		if (datasetVersion==null)
			datasetVersion = -1L;
		
		CollectionConfDto conf = SDPInsertMedataFactory.getSDPInsertMetadataApiAccess().getCollectionInfo(tenant, idDataset, datasetVersion, datasetType);

		String query = "iddataset_l:" + idDataset;
		if (datasetVersion != null && datasetVersion > 0)
			query += " AND datasetversion_l:" + datasetVersion;
		UpdateResponse updateResponse =server.deleteByQuery(conf.getSolrCollectionName(), query,1000);
		
		//UpdateResponse updateResponse = server.commit();
		log.info("[SDPInsertApiSolrDataAccess::deleteData] updateResponse " + updateResponse);
		return updateResponse.getStatus();

	}

}
