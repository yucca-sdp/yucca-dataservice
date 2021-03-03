/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package it.csi.smartdata.dataapi.adminapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.client.db.BackofficeDettaglioClientDB;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.ComponentResponse;

import it.csi.smartdata.dataapi.adminapi.edmprovider.LookupOdataProvider;
import it.csi.smartdata.dataapi.adminapi.edmprovider.SchemaOdataProvider;
import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.dto.SDPDataResult;
import it.csi.smartdata.dataapi.exception.SDPCustomQueryOptionException;
import it.csi.smartdata.dataapi.exception.SDPOrderBySizeException;
import it.csi.smartdata.dataapi.exception.SDPPageSizeException;
import it.csi.smartdata.dataapi.odata.SDPOdataFilterExpression;
import it.csi.smartdata.dataapi.odata.SDPPhoenixExpression;
import it.csi.smartdata.dataapi.solr.CloudSolrSingleton;
import it.csi.smartdata.dataapi.solr.KnoxSolrSingleton;

public class SDPAdminApiOdataCast {

	static Logger log = Logger.getLogger(SDPAdminApiOdataCast.class.getPackage().getName());

	public static final String DATA_TYPE_MEASURE = "measures";
	public static final String DATA_TYPE_DATA = "data";
	public static final String DATA_TYPE_SOCIAL = "social";
	private String codiceApi = null;
	private BackofficeDettaglioApiResponse configObject = null;
	private SolrClient serverHdp2 = null;
	private SolrClient serverHdp3 = null;

	/**
	 * 
	 * @param indiceArray
	 * @param campiGroupBy
	 * @param campiStats
	 * @param buckets
	 * @param misura
	 * @param count
	 * @param ret
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static ArrayList<Map<String, Object>> ricorsione(int indiceArray, String[] campiGroupBy,
			String[] campiStats, List<SimpleOrderedMap<Object>> buckets, HashMap<String, Object> misura, long count,
			ArrayList<Map<String, Object>> ret) {
		HashMap<String, Object> misuraOrig = new HashMap<String, Object>();
		misuraOrig.putAll(misura);

		long countOrig = count;
		if (buckets != null) {
			for (SimpleOrderedMap<Object> bucket : buckets) {
				Object val2 = bucket.get("val");
				long countlocal = count;
				countlocal = 0;
				if (bucket.get("count") instanceof Long) {
					countlocal += ((Long) bucket.get("count")).longValue();
				} else if (bucket.get("count") instanceof Integer) {
					countlocal += ((Integer) bucket.get("count")).intValue();
				}

				if (null != val2)
					misura.put(campiGroupBy[indiceArray], val2); // TODO .cercare il tipo
				if (indiceArray == campiGroupBy.length - 1) {
					// ESTRARRE STATISTICHE
					for (int i = 0; i < campiStats.length; i++) {
						// Double valore=(Double)bucket.get(campiStats[i].toLowerCase());
						// misura.put(campiStats[i], valore);
						misura.put(campiStats[i] + "_sts", bucket.get(campiStats[i] + "_sts"));
					}

					misura.put("count", countlocal);
					ret.add(misura);
					misura = new HashMap<String, Object>();
					misura.putAll(misuraOrig);
					count = countOrig;

				} else if (indiceArray < campiGroupBy.length - 1) {
					List<SimpleOrderedMap<Object>> nestedBuckets = (List<SimpleOrderedMap<Object>>) bucket
							.findRecursive(campiGroupBy[indiceArray + 1].toLowerCase(), "buckets");
					if (null == nestedBuckets || nestedBuckets.size() <= 0) {
						SimpleOrderedMap<Object> missing = (SimpleOrderedMap) bucket
								.findRecursive(campiGroupBy[indiceArray + 1].toLowerCase(), "missing");
						if (null != missing) {
							nestedBuckets = new ArrayList<SimpleOrderedMap<Object>>();
							nestedBuckets.add(missing);
						}
					}

					ret = ricorsione(indiceArray + 1, campiGroupBy, campiStats, nestedBuckets, misura, countlocal,
							(ArrayList<Map<String, Object>>) ret);
				}
			}
		}

		return ret;
	}

	/**
	 * 
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @param datatType
	 * @param userQuery
	 */
	private void logGetMeasuresStatsPerStreamSolr(String nameSpace, EdmEntityContainer entityContainer,
			String internalId, String datatType, Object userQuery) {
		log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamSolr] BEGIN");
		log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamSolr] nameSpace=" + nameSpace);
		log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamSolr] entityContainer=" + entityContainer);
		log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamSolr] internalId=" + internalId);
		log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamSolr] datatType=" + datatType);
		log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamSolr] userQuery=" + userQuery);
	}

	/**
	 * 
	 * @return
	 */
	private HashMap<String, String> getCampoTipoMetadato() {

		HashMap<String, String> campoTipoMetadato = new HashMap<String, String>();

		for (ComponentResponse componentResponse : configObject.getDettaglioStreamDatasetResponse().getComponents()) {
			String nome = componentResponse.getName();
			String tipo = componentResponse.getDataType().getDatatypecode();
			campoTipoMetadato.put(nome, tipo);
		}

		return campoTipoMetadato;
	}

	/**
	 * 
	 * @param internalId
	 * @param userQuery
	 * @return
	 */
	private String getQueryTotSolr(String internalId, Object userQuery) {
		String queryTotSolr = "(iddataset_l:"
				+ configObject.getDettaglioStreamDatasetResponse().getDataset().getIddataset().toString() + ")";
		queryTotSolr += " AND (datasetversion_l : [ 0 TO * ])";

		if (null != internalId) {
			queryTotSolr += "AND (id : " + internalId + ")";
		}

		if (null != userQuery) {

			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] userQuery=" + userQuery);

			if (userQuery instanceof SDPOdataFilterExpression) {
				queryTotSolr += "AND (" + userQuery + ")";
			} else {
				queryTotSolr += "AND " + userQuery;
			}
		}

		return queryTotSolr;
	}

	/**
	 * 
	 * @param timeGroupByParam
	 * @return
	 */
	private String[] getCampiGroupBy(String timeGroupByParam) {

		String[] campiGroupBy = null;
		if (timeGroupByParam != null && timeGroupByParam.indexOf(",") > 0) {
			campiGroupBy = timeGroupByParam.split(",");
		}

		return campiGroupBy;
	}

	/**
	 * 
	 * @param campiGroupBy
	 * @param timeGroupByParam
	 * @return
	 */
	private String getTimeGroupByParam(String[] campiGroupBy, String timeGroupByParam) {
		if (campiGroupBy != null) {
			return campiGroupBy[0];
		}
		return timeGroupByParam;

	}

	/**
	 * 
	 * @param timeGroupByParam
	 * @return
	 */
	private String getFirstBucket(String timeGroupByParam) {

		if ("retweetparentid".equals(timeGroupByParam)) {
			return "retweetparentid";
		}

		if ("iduser".equals(timeGroupByParam)) {
			return "iduser";
		}

		return "timegroup";

	}

	/**
	 * 
	 * @param timeGroupByParam
	 * @param campiGroupBy
	 * @param campoTipoMetadato
	 * @param timeGroupOperatorsParam
	 * @param compPropsTot
	 * @param st
	 * @param campiStats
	 * @return
	 * @throws SDPCustomQueryOptionException
	 */
	private String getJsonFacetWithTime(String timeGroupByParam, String[] campiGroupBy,
			HashMap<String, String> campoTipoMetadato, String timeGroupOperatorsParam, List<Property> compPropsTot,
			StringTokenizer st, String[] campiStats) throws SDPCustomQueryOptionException {

		String jsonFacet = "{ timegroup : { ";
		jsonFacet += "start : \"1900-01-01T00:00:00Z\",";
		jsonFacet += "end : \"3000-01-01T00:00:00Z\",";
		jsonFacet += "mincount : 1,";
		jsonFacet += "type   :   range, field   : time_dt,";

		int nfacet = 0;

		if ("year".equals(timeGroupByParam)) {
			jsonFacet += "gap : \"+1YEAR\", ";
			nfacet++;
		} else if ("month_year".equals(timeGroupByParam)) {
			jsonFacet += "gap : \"+1MONTH\", ";
			nfacet++;
		} else if ("dayofmonth_month_year".equals(timeGroupByParam)) {
			jsonFacet += "gap : \"+1DAY\", ";
			nfacet++;
		} else if ("hour_dayofmonth_month_year".equals(timeGroupByParam)) {
			jsonFacet += "gap : \"+1HOUR\", ";
			nfacet++;
		} else if ("minute_hour_dayofmonth_month_year".equals(timeGroupByParam)) {
			jsonFacet += "gap : \"+1MINUTE\", ";
			nfacet++;

		} else if ("month".equals(timeGroupByParam)) {
			throw new SDPCustomQueryOptionException("invalid timeGroupBy value", Locale.UK);
		} else if ("dayofmonth_month".equals(timeGroupByParam)) {
			throw new SDPCustomQueryOptionException("invalid timeGroupBy value", Locale.UK);
		} else if ("dayofweek_month".equals(timeGroupByParam)) {
			throw new SDPCustomQueryOptionException("invalid timeGroupBy value", Locale.UK);
		} else if ("dayofweek".equals(timeGroupByParam)) {
			throw new SDPCustomQueryOptionException("invalid timeGroupBy value", Locale.UK);
		} else if ("hour_dayofweek".equals(timeGroupByParam)) {
			throw new SDPCustomQueryOptionException("invalid timeGroupBy value", Locale.UK);
		} else if ("hour".equals(timeGroupByParam)) {
			throw new SDPCustomQueryOptionException("invalid timeGroupBy value", Locale.UK);
		} else if ("retweetparentid".equals(timeGroupByParam)) {

			if (!("socialDataset".equalsIgnoreCase(configObject.getDettaglioStreamDatasetResponse().getDataset()
					.getDatasetSubtype().getDatasetSubtype()))) {
				throw new SDPCustomQueryOptionException(
						"invalid timeGroupBy value: retweetparentid aggregations is aveailable only for social dataset",
						Locale.UK);
			}
			jsonFacet = "{ retweetparentid : { type:terms,field: retweetparentid_l,   ";
			nfacet++;
		} else if ("iduser".equals(timeGroupByParam)) {
			if (!("socialDataset".equalsIgnoreCase(configObject.getDettaglioStreamDatasetResponse().getDataset()
					.getDatasetSubtype().getDatasetSubtype()))) {
				throw new SDPCustomQueryOptionException(
						"invalid timeGroupBy value: iduser aggregations is aveailable only for social dataset",
						Locale.UK);
			}
			jsonFacet = "{ iduser : { type:terms,field: userid_l,";
			nfacet++;

		} else {
			throw new SDPCustomQueryOptionException("invalid timeGroupBy value", Locale.UK);
		}

		jsonFacet += " missing:true, limit : 1000, ";

		for (int kk = 1; campiGroupBy != null && kk < campiGroupBy.length; kk++) {
			String campoCompleto = campiGroupBy[kk];
			String suffisso = SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(campoTipoMetadato.get(campiGroupBy[kk]));
			if (null != suffisso)
				campoCompleto = campiGroupBy[kk] + suffisso;
			else
				throw new SDPCustomQueryOptionException("invalid timeGroupBy value " + campiGroupBy[kk], Locale.UK);

			jsonFacet += "facet: { " + campiGroupBy[kk].toLowerCase()
					+ " : { missing:true, limit:1000, type:terms,field: " + campoCompleto.toLowerCase() + ",";

			nfacet++;
		}

		String lastFacet = "";

		HashMap<String, String> campoOperazione = new HashMap<String, String>();

		int cntSts = 0;
		while (st.hasMoreTokens()) {

			String curOperator = st.nextToken();
			StringTokenizer stDue = new StringTokenizer(curOperator, ",", false);

			if (stDue.countTokens() != 2) {
				throw new SDPCustomQueryOptionException("invalid timeGroupOperators value: '" + curOperator + "'",
						Locale.UK);
			}

			String op = stDue.nextToken();
			String field = stDue.nextToken();

			if (!hasField(compPropsTot, field)) {
				throw new SDPCustomQueryOptionException(
						"invalid timeGroupOperators filed '" + field + "' in '" + curOperator + "' not fund in edm",
						Locale.UK);
			}

			String opPhoenix = null;

			if ("avg".equals(op)) {
				opPhoenix = "avg";
			} else if ("sum".equals(op)) {
				opPhoenix = "sum";
			} else if ("max".equals(op)) {
				opPhoenix = "max";
			} else if ("min".equals(op)) {
				opPhoenix = "min";
			} else {
				throw new SDPCustomQueryOptionException(
						"invalid timeGroupOperators invalid operation '" + op + "' in '" + curOperator + "'",
						Locale.UK);
			}

			if (campoOperazione.containsKey(field)) {
				throw new SDPCustomQueryOptionException(
						"invalid timeGroupOperators filed '" + field + "' present in more than one operation",
						Locale.UK);
			}

			campoOperazione.put(field, opPhoenix);

			String campoCompleto = field
					+ SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(campoTipoMetadato.get(field));

			campiStats[cntSts] = field;

			cntSts++;

			if (lastFacet.length() > 0) {
				lastFacet += ",";
			}

			lastFacet += field + "_sts : \"" + opPhoenix + "(" + campoCompleto.toLowerCase() + ")\"";
		}

		jsonFacet += "facet: {" + lastFacet + "}";

		for (int kk = 0; kk < nfacet; kk++) {
			jsonFacet += " } }";
		}

		log.info("[SDPDataApiMongoAccess::getJsonFacetWithTime] ****** jsonFacet =" + jsonFacet);

		return jsonFacet;
	}

	/**
	 * 
	 * @param timeGroupByParam
	 * @param campiGroupBy
	 * @param campoTipoMetadato
	 * @param timeGroupOperatorsParam
	 * @param compPropsTot
	 * @param st
	 * @param campiStats
	 * @return
	 * @throws SDPCustomQueryOptionException
	 */
	private String getJsonFacetBulk(String[] campiGroupBy, HashMap<String, String> campoTipoMetadato,
			String timeGroupOperatorsParam, List<Property> compPropsTot, StringTokenizer st, String[] campiStats)
			throws SDPCustomQueryOptionException {

		int nfacet = 0;
		if (campiGroupBy == null || campiGroupBy.length == 0) {
			throw new SDPCustomQueryOptionException("invalid groupBy value", Locale.UK);
		}

		String campoCompletoIniziale = campiGroupBy[0];
		String suffissoIniziale = SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX
				.get(campoTipoMetadato.get(campiGroupBy[0]));
		if (null != suffissoIniziale)
			campoCompletoIniziale = campiGroupBy[0] + suffissoIniziale;
		else
			throw new SDPCustomQueryOptionException("invalid groupBy value " + campiGroupBy[0], Locale.UK);

		String jsonFacet = "{ " + campiGroupBy[0].toLowerCase() + " : { missing:true, limit:1000, type:terms,field: "
				+ campoCompletoIniziale.toLowerCase() + ",";
		nfacet++;

		for (int kk = 1; campiGroupBy != null && kk < campiGroupBy.length; kk++) {
			String campoCompleto = campiGroupBy[kk];
			String suffisso = SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(campoTipoMetadato.get(campiGroupBy[kk]));
			if (null != suffisso)
				campoCompleto = campiGroupBy[kk] + suffisso;
			else
				throw new SDPCustomQueryOptionException("invalid groupBy value " + campiGroupBy[kk], Locale.UK);

			jsonFacet += "facet: { " + campiGroupBy[kk].toLowerCase()
					+ " : { missing:true, limit:1000, type:terms,field: " + campoCompleto.toLowerCase() + ",";

			nfacet++;
		}

		String lastFacet = "";

		HashMap<String, String> campoOperazione = new HashMap<String, String>();

		int cntSts = 0;
		while (st.hasMoreTokens()) {

			String curOperator = st.nextToken();
			StringTokenizer stDue = new StringTokenizer(curOperator, ",", false);

			if (stDue.countTokens() != 2) {
				throw new SDPCustomQueryOptionException("invalid groupOperators value: '" + curOperator + "'",
						Locale.UK);
			}

			String op = stDue.nextToken();
			String field = stDue.nextToken();

			if (!hasField(compPropsTot, field)) {
				throw new SDPCustomQueryOptionException(
						"invalid groupOperators filed '" + field + "' in '" + curOperator + "' not fund in edm",
						Locale.UK);
			}

			String opPhoenix = null;

			if ("avg".equals(op)) {
				opPhoenix = "avg";
			} else if ("sum".equals(op)) {
				opPhoenix = "sum";
			} else if ("max".equals(op)) {
				opPhoenix = "max";
			} else if ("min".equals(op)) {
				opPhoenix = "min";
			} else {
				throw new SDPCustomQueryOptionException(
						"invalid groupOperators invalid operation '" + op + "' in '" + curOperator + "'", Locale.UK);
			}

			if (campoOperazione.containsKey(field)) {
				throw new SDPCustomQueryOptionException(
						"invalid groupOperators filed '" + field + "' present in more than one operation", Locale.UK);
			}

			campoOperazione.put(field, opPhoenix);

			String campoCompleto = field
					+ SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(campoTipoMetadato.get(field));

			campiStats[cntSts] = field;

			cntSts++;

			if (lastFacet.length() > 0) {
				lastFacet += ",";
			}

			lastFacet += field + "_sts : \"" + opPhoenix + "(" + campoCompleto.toLowerCase() + ")\"";
		}

		jsonFacet += "facet: {" + lastFacet + "}";

		for (int kk = 0; kk < nfacet; kk++) {
			jsonFacet += " } }";
		}

		log.info("[SDPDataApiMongoAccess::getJsonFacetBulk] ****** jsonFacet =" + jsonFacet);

		return jsonFacet;
	}

	/**
	 * 
	 * @param compPropsTot
	 * @param elencoCampiGroup
	 * @param misuraDefault
	 * @return
	 */
	private int setMisureDefaultStatsSolr(List<Property> compPropsTot, Map<String, Object> misuraDefault) {

		int cnt = 0;

		List<ComponentResponse> components = configObject.getDettaglioStreamDatasetResponse().getComponents();

		for (ComponentResponse component : components) {

			if (component.getIsgroupable()) {

				String campoGroup = component.getName();

				for (int i = 0; i < compPropsTot.size(); i++) {

					String chiaveEdm = compPropsTot.get(i).getName();

					if (campoGroup.equals(chiaveEdm)) {
						if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Boolean)) {
							misuraDefault.put(chiaveEdm, Boolean.valueOf(false));
						} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.String)) {
							misuraDefault.put(chiaveEdm, "");
						} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int32)) {
							misuraDefault.put(chiaveEdm, -1);
						} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int64)) {
							misuraDefault.put(chiaveEdm, Long.parseLong("-1"));
						} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Double)) {
							misuraDefault.put(chiaveEdm, Double.parseDouble("-1"));
						} else if (((SimpleProperty) compPropsTot.get(i)).getType()
								.equals(EdmSimpleTypeKind.DateTimeOffset)) {
						} else if (((SimpleProperty) compPropsTot.get(i)).getType()
								.equals(EdmSimpleTypeKind.DateTime)) {
							misuraDefault.put(chiaveEdm, new Date());
						} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Decimal)) {
							misuraDefault.put(chiaveEdm, Double.parseDouble("-1"));
						}
					}
				}

				cnt++;
			}
		}

		return cnt;
	}

	@SuppressWarnings("unused")
	private int setMisureDefaultStatsSolr_old(List<Property> compPropsTot, String elencoCampiGroup,
			Map<String, Object> misuraDefault) {

		int cnt = 0;

		StringTokenizer stG = new StringTokenizer(elencoCampiGroup != null ? elencoCampiGroup : "", "|", false);

		while (stG.hasMoreElements()) {

			String campoGroup = stG.nextToken();

			for (int i = 0; i < compPropsTot.size(); i++) {

				String chiaveEdm = compPropsTot.get(i).getName();

				if (campoGroup.equals(chiaveEdm)) {
					if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Boolean)) {
						misuraDefault.put(chiaveEdm, Boolean.valueOf(false));
					} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.String)) {
						misuraDefault.put(chiaveEdm, "");
					} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int32)) {
						misuraDefault.put(chiaveEdm, -1);
					} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int64)) {
						misuraDefault.put(chiaveEdm, Long.parseLong("-1"));
					} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Double)) {
						misuraDefault.put(chiaveEdm, Double.parseDouble("-1"));
					} else if (((SimpleProperty) compPropsTot.get(i)).getType()
							.equals(EdmSimpleTypeKind.DateTimeOffset)) {
					} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTime)) {
						misuraDefault.put(chiaveEdm, new Date());
					} else if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Decimal)) {
						misuraDefault.put(chiaveEdm, Double.parseDouble("-1"));
					}
				}
			}

			cnt++;
		}

		return cnt;
	}

	/**
	 * 
	 * @param starTtime
	 * @param message
	 */
	private void logExecutionTime(long starTtime, String message) {

		long deltaTime = -1;
		try {
			deltaTime = System.currentTimeMillis() - starTtime;
		} catch (Exception e) {
		}

		log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamSolr] PERFORMANCE total data query executed in --> "
				+ deltaTime);

	}

	/**
	 * 
	 * @param internalId
	 * @param userQuery
	 * @param jsonFacet
	 * @return
	 */
	private SolrQuery getSolrQuery(String internalId, Object userQuery, String jsonFacet) {

		String queryTotSolr = getQueryTotSolr(internalId, userQuery);

		log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamSolr] ****** queryTotSolr =" + queryTotSolr);

		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("*:*");
		solrQuery.setFilterQueries(queryTotSolr);
		solrQuery.setRows(0);
		solrQuery.add("json.facet", jsonFacet);

		return solrQuery;
	}

	/**
	 * 
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @param datatType
	 * @param userQuery
	 * @param userOrderBy
	 * @param skip
	 * @param limit
	 * @param timeGroupByParam
	 * @param timeGroupOperatorsParam
	 * @param groupOutQuery
	 * @param elencoCampiGroup
	 * @return
	 * @throws ODataException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private SDPDataResult getBulkStatsPerStreamSolr(String nameSpace, EdmEntityContainer entityContainer,
			String internalId, String datatType, Object userQuery, Object userOrderBy, int skip, int limit,
			String bulkGroupByParam, String bulkGroupOperatorsParam, Object groupOutQuery, String hdpVersion)
			throws ODataException {

		log.debug("[SDPAdminApiOdataData::getBulkStatsPerStreamSolr] " + "BEGIN");

		logGetMeasuresStatsPerStreamSolr(nameSpace, entityContainer, internalId, datatType, userQuery);

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

		int cnt = 0;

		initDbObject(codiceApi);

		try {

			long starTtimetot = System.currentTimeMillis();
			List<Property> compPropsTot = Util.convertFromComponentResponseToSimpleProperty(
					configObject.getDettaglioStreamDatasetResponse().getComponents(), nameSpace);
			String collection = configObject.getDettaglioStreamDatasetResponse().getDataset().getSolrcollectionname();
			HashMap<String, String> campoTipoMetadato = getCampoTipoMetadato();
			String[] campiGroupBy = getCampiGroupBy(bulkGroupByParam);
			if (campiGroupBy == null) {
				campiGroupBy = new String[] { bulkGroupByParam };
			}

//			timeGroupByParam            = getTimeGroupByParam(campiGroupBy, timeGroupByParam);

			if (null == bulkGroupOperatorsParam || bulkGroupOperatorsParam.trim().length() <= 0) {
				throw new SDPCustomQueryOptionException("invalid groupOperators value", Locale.UK);
			}

			StringTokenizer stringTokenizer = new StringTokenizer(bulkGroupOperatorsParam, ";", false);
			String[] campiStats = new String[stringTokenizer.countTokens()];

			String jsonFacet = getJsonFacetBulk(campiGroupBy, campoTipoMetadato, bulkGroupOperatorsParam, compPropsTot,
					stringTokenizer, campiStats);

			SolrQuery solrQuery = getSolrQuery(internalId, userQuery, jsonFacet);

			long starTtime = System.currentTimeMillis();
			QueryResponse queryResponse;
			if (hdpVersion != null && !hdpVersion.equals("")) {
				queryResponse = serverHdp3.query(collection, solrQuery);
			} else {
				queryResponse = serverHdp2.query(collection, solrQuery);
			}
			logExecutionTime(starTtime,
					"[SDPDataApiMongoAccess::getBulkStatsPerStreamSolr] PERFORMANCE total data query executed in --> ");

			log.debug("[SDPDataApiMongoAccess::getBulkStatsPerStreamSolr] RESPONSE --> "
					+ queryResponse.getResponse().toString());

			NamedList<Object> bucketList = queryResponse.getResponse();

			starTtime = System.currentTimeMillis();

			ArrayList<Map<String, Object>> retTmp = new ArrayList<Map<String, Object>>();

			Map<String, Object> misuraDefault = new HashMap<String, Object>();

			cnt = setMisureDefaultStatsSolr(compPropsTot, misuraDefault);

			Map<String, Object> misura = new HashMap<String, Object>();
			misura.putAll(misuraDefault);

			if (queryResponse.getResponse().get("facets") != null) {

				// notice "findRecursive" usage to get the buckets list
				List<SimpleOrderedMap<Object>> buckets = (List<SimpleOrderedMap<Object>>) bucketList
						.findRecursive("facets", campiGroupBy[0].toLowerCase(), "buckets");

				if (buckets != null) {
					retTmp = ricorsione(0, campiGroupBy, campiStats, buckets, (HashMap<String, Object>) misura, cnt,
							retTmp);
				}
			}

			ret.addAll(retTmp);

			logExecutionTime(starTtime, "[SDPDataApiMongoAccess::getBulkStatsPerStreamSolr] PERFORMANCE  FETCH TIME =");

			logExecutionTime(starTtimetot,
					"[SDPDataApiMongoAccess::getBulkStatsPerStreamSolr] PERFORMANCE TOTAL METHOD ELAPSED =");

		} catch (Exception e) {

			if (e instanceof SDPCustomQueryOptionException) {
				log.error("[SDPDataApiMongoAccess::getBulkStatsPerStreamSolr] rethrow", e);
				throw (SDPCustomQueryOptionException) e;
			} else {
				log.error("[SDPDataApiMongoAccess::getBulkStatsPerStreamSolr] INGORED", e);
			}

		} finally {
			log.debug("[SDPDataApiMongoAccess::getBulkStatsPerStreamSolr] END");
		}

		SDPDataResult outres = new SDPDataResult(ret, cnt);

		log.debug("[SDPAdminApiOdataData::getBulkStatsPerStreamSolr] " + "END");

		return outres;
	}

	/**
	 * 
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @param datatType
	 * @param userQuery
	 * @param userOrderBy
	 * @param skip
	 * @param limit
	 * @param timeGroupByParam
	 * @param timeGroupOperatorsParam
	 * @param groupOutQuery
	 * @param elencoCampiGroup
	 * @return
	 * @throws ODataException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private SDPDataResult getMeasuresStatsPerStreamSolr(String nameSpace, EdmEntityContainer entityContainer,
			String internalId, String datatType, Object userQuery, Object userOrderBy, int skip, int limit,
			String timeGroupByParam, String timeGroupOperatorsParam, Object groupOutQuery, String hdpVersion)
			throws ODataException {

		log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamSolr] " + "BEGIN");

		logGetMeasuresStatsPerStreamSolr(nameSpace, entityContainer, internalId, datatType, userQuery);

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

		int cnt = 0;

		initDbObject(codiceApi);

		try {

			long starTtimetot = System.currentTimeMillis();
			List<Property> compPropsTot = Util.convertFromComponentResponseToSimpleProperty(
					configObject.getDettaglioStreamDatasetResponse().getComponents(), nameSpace);
			String collection = configObject.getDettaglioStreamDatasetResponse().getDataset().getSolrcollectionname();
			HashMap<String, String> campoTipoMetadato = getCampoTipoMetadato();
			String[] campiGroupBy = getCampiGroupBy(timeGroupByParam);
			timeGroupByParam = getTimeGroupByParam(campiGroupBy, timeGroupByParam);

			if (null == timeGroupOperatorsParam || timeGroupOperatorsParam.trim().length() <= 0) {
				throw new SDPCustomQueryOptionException("invalid timeGroupOperators value", Locale.UK);
			}

			StringTokenizer stringTokenizer = new StringTokenizer(timeGroupOperatorsParam, ";", false);
			String[] campiStats = new String[stringTokenizer.countTokens()];

			String jsonFacet = getJsonFacetWithTime(timeGroupByParam, campiGroupBy, campoTipoMetadato,
					timeGroupOperatorsParam, compPropsTot, stringTokenizer, campiStats);

			SolrQuery solrQuery = getSolrQuery(internalId, userQuery, jsonFacet);

			long starTtime = System.currentTimeMillis();

			QueryResponse queryResponse;
			if (hdpVersion != null && !hdpVersion.equals("")) {
				queryResponse = serverHdp3.query(collection, solrQuery);
			} else {
				queryResponse = serverHdp2.query(collection, solrQuery);
			}

			logExecutionTime(starTtime,
					"[SDPDataApiMongoAccess::getMeasuresStatsPerStreamSolr] PERFORMANCE total data query executed in --> ");

			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamSolr] RESPONSE --> "
					+ queryResponse.getResponse().toString());

			NamedList<Object> bucketList = queryResponse.getResponse();

			starTtime = System.currentTimeMillis();

			ArrayList<Map<String, Object>> retTmp = new ArrayList<Map<String, Object>>();

			Map<String, Object> misuraDefault = new HashMap<String, Object>();

			cnt = setMisureDefaultStatsSolr(compPropsTot, misuraDefault);

			String firstBucket = getFirstBucket(timeGroupByParam);

			if (queryResponse.getResponse().get("facets") != null) {

				// notice "findRecursive" usage to get the buckets list
				List<SimpleOrderedMap<Object>> buckets = (List<SimpleOrderedMap<Object>>) bucketList
						.findRecursive("facets", firstBucket, "buckets");

				if (buckets != null) {

					for (SimpleOrderedMap<Object> bucket : buckets) {
						long count = 0;
						Map<String, Object> misura = new HashMap<String, Object>();
						int year = -1;
						int month = -1;
						int day = -1;
						int hour = -1;
						int min = -1;
						long iduser = -1;
						long retweetparentid = -1;
						if (timeGroupByParam.equals("iduser")) {
							iduser = Long.parseLong(((Object) bucket.get("val")).toString());
						} else if (timeGroupByParam.equals("retweetparentid")) {
							retweetparentid = Long.parseLong(((Object) bucket.get("val")).toString());

						} else {
							Date date = (Date) bucket.get("val");

							SimpleDateFormat df = new SimpleDateFormat("yyyy");
							SimpleDateFormat dfa = new SimpleDateFormat("MM");
							SimpleDateFormat dfb = new SimpleDateFormat("dd");
							SimpleDateFormat dfc = new SimpleDateFormat("hh");
							SimpleDateFormat dfd = new SimpleDateFormat("mm");

							year = Integer.parseInt(df.format(date));
							month = ("year".equals(timeGroupByParam) ? -1 : Integer.parseInt(dfa.format(date)));
							day = ("month_year".equals(timeGroupByParam) || "year".equals(timeGroupByParam) ? -1
									: Integer.parseInt(dfb.format(date)));
							hour = ("dayofmonth_month_year".equals(timeGroupByParam)
									|| "month_year".equals(timeGroupByParam) || "year".equals(timeGroupByParam) ? -1
											: Integer.parseInt(dfc.format(date)));
							min = ("hour_dayofmonth_month_year".equals(timeGroupByParam)
									|| "dayofmonth_month_year".equals(timeGroupByParam)
									|| "month_year".equals(timeGroupByParam) || "year".equals(timeGroupByParam) ? -1
											: Integer.parseInt(dfd.format(date)));
						}

						if (bucket.get("count") instanceof Long) {
							count += ((Long) bucket.get("count")).longValue();
						} else if (bucket.get("count") instanceof Integer) {
							count += ((Integer) bucket.get("count")).intValue();
						}

						misura.put("dayofmonth", day);
						misura.put("month", month);
						misura.put("year", year);
						misura.put("hour", hour);
						misura.put("minute", min);
						misura.put("dayofweek", -1);
						misura.put("retweetparentid", retweetparentid);
						misura.put("iduser", iduser);

						misura.putAll(misuraDefault);

						if (null != campiGroupBy && campiGroupBy.length > 1) {

							List<SimpleOrderedMap<Object>> nestedBuckets = (List<SimpleOrderedMap<Object>>) bucket
									.findRecursive(campiGroupBy[1].toLowerCase(), "buckets");

							if (null == nestedBuckets || nestedBuckets.size() <= 0) {
								SimpleOrderedMap<Object> missing = (SimpleOrderedMap) bucket
										.findRecursive(campiGroupBy[1].toLowerCase(), "missing");
								if (null != missing) {
									nestedBuckets = new ArrayList<SimpleOrderedMap<Object>>();
									nestedBuckets.add(missing);
								}
							}

							retTmp = ricorsione(1, campiGroupBy, campiStats, nestedBuckets,
									(HashMap<String, Object>) misura, count, retTmp);
						} else {

							for (int i = 0; i < campiStats.length; i++) {
								String valore = ((Object) bucket.get(campiStats[i] + "_sts")).toString();
								misura.put(campiStats[i] + "_sts", Double.parseDouble(valore.replace(',', '.')));
							}

							misura.put("count", count);
							retTmp.add(misura);
						}

						cnt++;
					}
				}
			}

			ret.addAll(retTmp);

			logExecutionTime(starTtime,
					"[SDPDataApiMongoAccess::getMeasuresStatsPerStreamSolr] PERFORMANCE  FETCH TIME =");

			logExecutionTime(starTtimetot,
					"[SDPDataApiMongoAccess::getMeasuresStatsPerStreamSolr] PERFORMANCE TOTAL METHOD ELAPSED =");

		} catch (Exception e) {

			if (e instanceof SDPCustomQueryOptionException) {
				log.error("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamSolr] rethrow", e);
				throw (SDPCustomQueryOptionException) e;
			} else {
				log.error("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamSolr] INGORED", e);
			}

		} finally {
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamSolr] END");
		}

		SDPDataResult outres = new SDPDataResult(ret, cnt);

		log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamSolr] " + "END");

		return outres;
	}

	/**
	 * 
	 */
	public SDPAdminApiOdataCast() {
		if ("KNOX".equalsIgnoreCase(SDPDataApiConfig.getInstance().getSolrTypeAccessHdp2()))
			serverHdp2 = KnoxSolrSingleton.getServerHdp2();
		else
			serverHdp2 = CloudSolrSingleton.getServerHdp2();

		if ("KNOX".equalsIgnoreCase(SDPDataApiConfig.getInstance().getSolrTypeAccessHdp3()))
			serverHdp3 = KnoxSolrSingleton.getServerHdp3();
		else
			serverHdp3 = CloudSolrSingleton.getServerHdp3();
	}

	/**
	 * 
	 * @param codiceApi
	 * @throws ODataException
	 */
	private void initDbObject(String codiceApi) throws ODataException {

		if (null == configObject || !codiceApi.equals(this.codiceApi)) {

			this.codiceApi = codiceApi;

			try {
				log.info("[SDPAdminApiOdataCast::initDbObject] Calling for codiceApi:" + codiceApi);
				this.configObject = BackofficeDettaglioClientDB.getBackofficeDettaglioApi(codiceApi, log.getName());
				log.info("[SDPAdminApiOdataCast::initDbObject] Calling for codiceApi:" + codiceApi + "..done!");
			} catch (AdminApiClientException e) {
				log.error("[SDPAdminApiOdataCast::initDbObject] Error", e);
				throw new ODataException(e);
			}
		}
	}

	/**
	 * 
	 * @param edmFQName
	 * @param codiceApi
	 * @return
	 * @throws ODataException
	 */
	public EntityType getEntityType(FullQualifiedName edmFQName, String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getEntityType(edmFQName, configObject);
	}

	/**
	 * 
	 * @param edmFQName
	 * @param codiceApi
	 * @return
	 * @throws ODataException
	 */
	public ComplexType getComplexType(FullQualifiedName edmFQName, String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getComplexType(edmFQName, configObject);
	}

	/**
	 * 
	 * @param entityContainer
	 * @param association
	 * @param sourceEntitySetName
	 * @param sourceEntitySetRole
	 * @param codiceApi
	 * @return
	 * @throws ODataException
	 */
	public AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association,
			String sourceEntitySetName, String sourceEntitySetRole, String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getAssociationSet(entityContainer, association, sourceEntitySetName, sourceEntitySetRole,
				configObject);
	}

	/**
	 * 
	 * @param entityContainer
	 * @param name
	 * @param codiceApi
	 * @return
	 * @throws ODataException
	 */
	public EntitySet getEntitySet(String entityContainer, String name, String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getEntitySet(entityContainer, name, configObject);
	}

	/**
	 * 
	 * @param edmFQName
	 * @param codiceApi
	 * @return
	 * @throws ODataException
	 */
	public Association getAssociation(FullQualifiedName edmFQName, String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getAssociation(edmFQName, configObject);
	}

	/**
	 * 
	 * @param name
	 * @param codiceApi
	 * @return
	 * @throws ODataException
	 */
	public EntityContainerInfo getEntityContainerInfo(String name, String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getEntityContainerInfo(name, configObject);
	}

	/**
	 * 
	 * @param codiceApi
	 * @return
	 * @throws ODataException
	 * @throws Exception
	 */
	public List<Schema> getSchemasInternal(String codiceApi) throws ODataException, Exception {
		try {
			log.debug("[SDPMongoOdataCast::getSchemasInternal] BEGIN");
			log.info("[SDPMongoOdataCast::getSchemasInternal] codiceApi=" + codiceApi);

			List<Schema> schemas = new ArrayList<Schema>();
			initDbObject(codiceApi);
			String nameSpace = configObject.getEntitynamespace();

			SchemaOdataProvider provider = new LookupOdataProvider();
			Schema schema = new Schema();
			schema.setNamespace(nameSpace);
			schema.setEntityTypes(provider.getEntityTypes(configObject));
			schema.setEntityContainers(provider.getEntityContainers(configObject));
			schema.setAssociations(provider.getAssociations(configObject));
			schema.setComplexTypes(provider.getComplexTypes(configObject));
			schemas.add(schema);

			return schemas;
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getSchemasInternal] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getSchemasInternal] END");
		}
	}

	/**
	 * 
	 * @param codiceApi
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> getDatasetMetadata(String codiceApi) throws Exception {
		initDbObject(codiceApi);

		List<ComponentResponse> elencoCampi = configObject.getDettaglioStreamDatasetResponse().getComponents();
		HashMap<String, String> mappaCampi = new HashMap<String, String>();

		for (int i = 0; i < elencoCampi.size(); i++) {
			ComponentResponse cur = elencoCampi.get(i);
			String nome = cur.getName();
			String tipo = cur.getDataType().getDatatypecode();
			mappaCampi.put(nome, tipo);

		}

		return mappaCampi;
	}

	/**
	 * DATI
	 * 
	 * @param codiceApi
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @return
	 */
	public SDPDataResult getMeasuresPerApi(String codiceApi, String nameSpace, EdmEntityContainer entityContainer,
			String internalId, Object userQuery, Object userOrderBy, int skip, int limit, String dataType)
			throws Exception {

		try {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] BEGIN");
			log.info("[SDPAdminApiOdataData::getMeasuresPerApi] codiceApi = " + codiceApi);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] nameSpace = " + nameSpace);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] entityContainer = " + entityContainer);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] internalId = " + internalId);
			log.info("[SDPAdminApiOdataData::getMeasuresPerApi] userQuery = " + userQuery);

			initDbObject(codiceApi);
			List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
			int totCnt = 0;

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * elencodataset potrebbe contenere pi? elementi dello stesso dataset in
			 * versione differente ad es: idDataset= 1, datasetVersion=1,
			 * [campo1:int,camp2:string,campo3:date] idDataset= 1, datasetVersion=2,
			 * [campo1:int,camp2:string,campo3:date,campo11:long] idDataset= 3,
			 * datasetVersion=1, [campo1:log]
			 * 
			 * deve diventare idDataset= 1, datasetVersion=1,2, [campo1:int,camp2
			 * :string,campo3:date,campo1:int,camp2:string,campo3 :date,campo11:long]
			 * idDataset= 3, datasetVersion=1 [campo1:log]
			 * 
			 * 
			 * si dovrebbe trasformare List<DBObject> elencoDataset in un array di oggetti
			 * di questo tipo:
			 * 
			 * idDataset array di datasetVersion array dei campi ottenuto come join dei
			 * campi delle varie versioni di quel dataset parte di config (presa da una
			 * versione a caso) info presa da una versione a caso
			 */

			String dsCodes = configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetcode();
			String tenantsCodes = configObject.getDettaglioStreamDatasetResponse().getTenantManager().getTenantcode();

			String nameSpaceStream = configObject.getEntitynamespace();
//			String tenantStrean = ((DBObject) elencoDataset.get(i).get(
//					"configData")).get("tenantCode").toString();

			SDPDataResult cur = getMeasuresPerStreamNewLimitSolr(codiceApi, nameSpaceStream, entityContainer,
					internalId, dataType, userQuery, userOrderBy, skip, limit);

			List<Map<String, Object>> misureCur = cur.getDati();

			for (int k = 0; misureCur != null && k < misureCur.size(); k++) {
				ret.add(misureCur.get(k));
			}
			totCnt += cur.getTotalCount();

			return new SDPDataResult(ret, totCnt, tenantsCodes, dsCodes);

		} catch (SDPOrderBySizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerApi] SDPOrderBySizeException" + e);
			throw (SDPOrderBySizeException) e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerDataset] SDPPageSizeException" + e);
			throw (SDPPageSizeException) e;
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerApi] " + e);
			throw e;
		} finally {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] END");

		}
	}

	/**
	 * 
	 * @param components
	 * @return
	 */
	private boolean atLeastOneComponentIsGroupable(List<ComponentResponse> components) {
		if (components != null) {
			for (ComponentResponse componentResponse : components) {
				if (componentResponse.getIsgroupable()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param codiceApi
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @param userQuery
	 * @param userOrderBy
	 * @param skip
	 * @param limit
	 * @param timeGroupByParam
	 * @param timeGroupOperatorsParam
	 * @param groupOutQuery
	 * @param dataType
	 * @return
	 * @throws Exception
	 */
	public SDPDataResult getMeasuresStatsPerApi(String codiceApi, String nameSpace, EdmEntityContainer entityContainer,
			String internalId, // null
			Object userQueryPhoneix, // userQueryPhoneix
			Object orderQueryPhoenix, Object orderQuerySolr, Object userQuerySolr, int skip, // -1
			int limit, // -1
			String timeGroupByParam, String timeGroupOperatorsParam, String bulkGroupByParam,
			String bulkGroupOperatorsParam, Object groupOutQueryPhoenix, // userQuery
			Object groupOutQuerySolr, String dataType) throws Exception {

		// TODO YUCCA-74 odata evoluzione

		try {

			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerApi] BEGIN");
			log.info("[SDPAdminApiOdataData::getMeasuresStatsPerApi] codiceApi = " + codiceApi);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerApi] nameSpace = " + nameSpace);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerApi] entityContainer = " + entityContainer);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerApi] internalId = " + internalId);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerApi] userQuerySolr = " + userQuerySolr);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerApi] userQueryPhoneix = " + userQueryPhoneix);

			initDbObject(codiceApi);
			List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
			int totCnt = 0;

			String nameSpaceStream = configObject.getEntitynamespace();

			SDPDataResult cur = null;

			if (isBulkDataset(configObject.getDettaglioStreamDatasetResponse())) {
				String hdpVersion = configObject.getDettaglioStreamDatasetResponse().getDataset().getHdpVersion();
				cur = getBulkStatsPerStreamSolr(nameSpaceStream, entityContainer, internalId, dataType, userQuerySolr,
						orderQuerySolr, skip, limit, bulkGroupByParam, bulkGroupOperatorsParam, groupOutQuerySolr,
						hdpVersion);
			} else if (atLeastOneComponentIsGroupable(
					configObject.getDettaglioStreamDatasetResponse().getComponents())) {
				String hdpVersion = configObject.getDettaglioStreamDatasetResponse().getDataset().getHdpVersion();
				cur = getMeasuresStatsPerStreamSolr(nameSpaceStream, entityContainer, internalId, dataType,
						userQuerySolr, orderQuerySolr, skip, limit, timeGroupByParam, timeGroupOperatorsParam,
						groupOutQuerySolr, hdpVersion);
			} else {

				cur = getMeasuresStatsPerStreamPhoenix(nameSpaceStream, entityContainer, internalId, dataType,
						userQueryPhoneix, orderQueryPhoenix, skip, limit, timeGroupByParam, timeGroupOperatorsParam,
						groupOutQueryPhoenix);
			}

			List<Map<String, Object>> misureCur = cur.getDati();

			for (int k = 0; misureCur != null && k < misureCur.size(); k++) {
				ret.add(misureCur.get(k));
			}
			totCnt += cur.getTotalCount();

			return new SDPDataResult(ret, totCnt,
					configObject.getDettaglioStreamDatasetResponse().getTenantManager().getTenantcode(),
					configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetcode());
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getMeasuresStatsPerApi] " + e);
			throw e;
		} finally {
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerApi] END");

		}
	}

	private boolean isBulkDataset(BackofficeDettaglioStreamDatasetResponse dettaglioStreamDatasetResponse) {
		return dettaglioStreamDatasetResponse.getDataset().getDatasetSubtype().getDatasetSubtype().toLowerCase()
				.equals("bulkdataset");
	}

	/**
	 * 
	 * @param codiceApi
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @param userQuery
	 * @param userOrderBy
	 * @param skip
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public SDPDataResult getMeasuresPerDataset(String codiceApi, String nameSpace, EdmEntityContainer entityContainer,
			String internalId, Object userQuery, Object userOrderBy, int skip, int limit) throws Exception {

		// TODO YUCCA-74 odata evoluzione
		try {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] BEGIN");
			log.info("[SDPAdminApiOdataData::getMeasuresPerDataset] codiceApi = " + codiceApi);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] nameSpace = " + nameSpace);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] entityContainer = " + entityContainer);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] internalId =" + internalId);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] userQuery = " + userQuery);

			initDbObject(codiceApi);
			List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
//	
//			List<DBObject> elencoDataset = orderNestDS(mongoDataAccess
//					.getDatasetPerApi(codiceApi));

			int totCnt = 0;

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * elencodataset potrebbe contenere più elementi dello stesso dataset in
			 * versione differente ad es: idDataset= 1, datasetVersion=1,
			 * [campo1:int,camp2:string,campo3:date] idDataset= 1, datasetVersion=2,
			 * [campo1:int,camp2:string,campo3:date,campo11:long] idDataset= 3,
			 * datasetVersion=1, [campo1:log]
			 * 
			 * deve diventare idDataset= 1, datasetVersion=1,2, [campo1:int,camp2
			 * :string,campo3:date,campo1:int,camp2:string,campo3 :date,campo11:long]
			 * idDataset= 3, datasetVersion=1 [campo1:log]
			 * 
			 * 
			 * si dovrebbe trasformare List<DBObject> elencoDataset in un array di oggetti
			 * di questo tipo:
			 * 
			 * idDataset array di datasetVersion array dei campi ottenuto come join dei
			 * campi delle varie versioni di quel dataset parte di config (presa da una
			 * versione a caso) info presa da una versione a caso
			 */

//			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] Dataset.size = "
//					+ elencoDataset.size());
//			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] elencoDataset = "
//					+ elencoDataset);

//			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] Dataset = "
//					+ ((DBObject) elencoDataset.get(i)));
			// TODO log a debug
			String nameSpaceStrean = configObject.getEntitynamespace();

			SDPDataResult cur = getMeasuresPerStreamNewLimitSolr(codiceApi, nameSpaceStrean, entityContainer,
					internalId, DATA_TYPE_DATA, userQuery, userOrderBy, skip, limit);

			List<Map<String, Object>> misureCur = cur.getDati();
			for (int k = 0; misureCur != null && k < misureCur.size(); k++) {
				ret.add(misureCur.get(k));
			}
			totCnt += cur.getTotalCount();

			return new SDPDataResult(ret, totCnt,
					configObject.getDettaglioStreamDatasetResponse().getTenantManager().getTenantcode(),
					configObject.getApicode());
		} catch (SDPOrderBySizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerDataset] SDPOrderBySizeException" + e);
			throw (SDPOrderBySizeException) e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerDataset] SDPPageSizeException" + e);
			throw (SDPPageSizeException) e;
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerDataset] " + e);
			throw e;
		} finally {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] END");

		}
	}

	/**
	 * 
	 * @param codiceApi
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @param userQuery
	 * @param userOrderBy
	 * @param elencoIdBinary
	 * @param skip
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public SDPDataResult getBynaryPerDataset(String codiceApi, String nameSpace, EdmEntityContainer entityContainer,
			String internalId, Object userQuery, Object userOrderBy, ArrayList<String> elencoIdBinary, int skip,
			int limit) throws Exception {

		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPAdminApiOdataData::getBynaryPerDataset] BEGIN");
			log.info("[SDPAdminApiOdataData::getBynaryPerDataset] codiceApi=" + codiceApi);
			log.debug("[SDPAdminApiOdataData::getBynaryPerDataset] nameSpace=" + nameSpace);
			log.debug("[SDPAdminApiOdataData::getBynaryPerDataset] entityContainer=" + entityContainer);
			log.debug("[SDPAdminApiOdataData::getBynaryPerDataset] internalId=" + internalId);
			log.info("[SDPAdminApiOdataData::getBynaryPerDataset] userQuery=" + userQuery);

			initDbObject(codiceApi);
			List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

			int totCnt = 0;

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * elencodataset potrebbe contenere più elementi dello stesso dataset in
			 * versione differente ad es: idDataset= 1, datasetVersion=1,
			 * [campo1:int,camp2:string,campo3:date] idDataset= 1, datasetVersion=2,
			 * [campo1:int,camp2:string,campo3:date,campo11:long] idDataset= 3,
			 * datasetVersion=1, [campo1:log]
			 * 
			 * deve diventare idDataset= 1, datasetVersion=1,2, [campo1:int,camp2
			 * :string,campo3:date,campo1:int,camp2:string,campo3 :date,campo11:long]
			 * idDataset= 3, datasetVersion=1 [campo1:log]
			 * 
			 * 
			 * si dovrebbe trasformare List<DBObject> elencoDataset in un array di oggetti
			 * di questo tipo:
			 * 
			 * idDataset array di datasetVersion array dei campi ottenuto come join dei
			 * campi delle varie versioni di quel dataset parte di config (presa da una
			 * versione a caso) info presa da una versione a caso
			 */

			String nameSpaceStream = configObject.getEntitynamespace();

			SDPDataResult cur = getBinary(nameSpaceStream, entityContainer, internalId, DATA_TYPE_DATA, userQuery,
					userOrderBy, elencoIdBinary, codiceApi, skip, limit);
			List<Map<String, Object>> misureCur = cur.getDati();
			for (int k = 0; misureCur != null && k < misureCur.size(); k++) {
				ret.add(misureCur.get(k));
			}
			totCnt += cur.getTotalCount();

			return new SDPDataResult(ret, totCnt,
					configObject.getDettaglioStreamDatasetResponse().getTenantManager().getTenantcode(),
					configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetcode());
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getBynaryPerDataset] " + e);
			throw e;
		} finally {
			log.debug("[SDPAdminApiOdataData::getBynaryPerDataset] END");

		}
	}

	/**
	 * 
	 * @param codiceApi
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @param datatType
	 * @param userQuery
	 * @param userOrderBy
	 * @param skipI
	 * @param limitI
	 * @return
	 * @throws ODataException
	 */
	@SuppressWarnings("unchecked")
	private SDPDataResult getMeasuresPerStreamNewLimitSolr(String codiceApi, String nameSpace,
			EdmEntityContainer entityContainer, String internalId, String datatType, Object userQuery,
			Object userOrderBy, int skipI, int limitI) throws ODataException {
		String collection = null;
		String idDataset = null;
//		String datasetCode=null;
//		String datasetToFindVersion=null;
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		long cnt = 0;
		long skipL = skipI;
		long limitL = limitI;

		initDbObject(codiceApi);

		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] BEGIN");
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] codiceApi=" + codiceApi);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] nameSpace=" + nameSpace);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] entityContainer=" + entityContainer);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] internalId=" + internalId);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] datatType=" + datatType);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] userQuery=" + userQuery);
//			String solrCollection= codiceTenant;
//			String codiceTenantOrig=codiceTenant;

			List<Property> compPropsTot = new ArrayList<Property>();
//			List<Property> compPropsCur=new ArrayList<Property>();			

			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] limit_init --> " + skipL);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] skip_init --> " + skipL);

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			// - modificare eventualmente la logica di recupero di collencion,host, port, db
			// specifici per il dataset
			// - modificare eventualmente la logica di recupero dell'idDataset
			// INVARIATO!!

			idDataset = configObject.getDettaglioStreamDatasetResponse().getDataset().getIddataset().toString();
//			datasetCode=configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetcode();
			String streamSubtype = configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetSubtype()
					.getDatasetSubtype();

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sara un array da mettere in in INUTILE
			 */

//			datasetToFindVersion=takeNvlValues(streamMetadata.get("datasetVersion"));

			collection = configObject.getDettaglioStreamDatasetResponse().getDataset().getSolrcollectionname();

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			// - modificare eventualmente la logica di recupero dell'elenco dei campi che
			// contiene il join di info.fuields di tutte le versioni di quel dataset
			List<ComponentResponse> eleCapmpi = configObject.getDettaglioStreamDatasetResponse().getComponents();
			compPropsTot = Util.convertFromComponentResponseToSimpleProperty(eleCapmpi, nameSpace);
			Map<String, String> campoTipoMetadato = Util.convertFromComponentResponseToMap(eleCapmpi);

			String queryTotSolr = "(iddataset_l:" + idDataset + ")";
			String queryTotCntSolr = "(iddataset_l:" + idDataset + ")";

			queryTotSolr += " AND (datasetversion_l : [ 0 TO * ])";

			if (null != internalId) {
				queryTotSolr += "AND (id : " + internalId + ")";
				queryTotCntSolr += "AND (id : " + internalId + ")";

			}
			if (null != userQuery) {
				log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] userQuery=" + userQuery);
				if (userQuery instanceof SDPOdataFilterExpression) {
					queryTotSolr += "AND (" + userQuery + ")";
					queryTotCntSolr += "AND (" + userQuery + ")";
				} else {
					queryTotSolr += "AND " + userQuery;
					queryTotCntSolr += "AND " + userQuery;
				}

				// query.append("$and", userQuery);
			}

			String query = queryTotSolr;

			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] total data query =" + query);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] collection =" + collection);

			// yucca-1080
			// queryTotSolr=queryTotSolr.toLowerCase().replaceAll("iddataset_l",
			// "idDataset_l").replaceAll("datasetversion_l", "datasetVersion_l");
			// queryTotCntSolr=queryTotCntSolr.toLowerCase().replaceAll("iddataset_l",
			// "idDataset_l").replaceAll("datasetversion_l", "datasetVersion_l");

			// CloudSolrClient solrServer = CloudSolrSingleton.getServer();
			String hdpVersion = configObject.getDettaglioStreamDatasetResponse().getDataset().getHdpVersion();
			SolrClient solrServer;
			if (hdpVersion != null && !hdpVersion.equals("")) {
				solrServer = serverHdp3;
			} else {
				solrServer = serverHdp2;
			}
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(queryTotCntSolr);
			solrQuery.setRows(1);

			long starTtime = 0;
			long deltaTime = -1;

			starTtime = System.currentTimeMillis();
			QueryResponse rsp = solrServer.query(collection, solrQuery);
			SolrDocumentList aaa = (SolrDocumentList) rsp.getResponse().get("response");
			cnt = aaa.getNumFound();
			try {
				deltaTime = System.currentTimeMillis() - starTtime;
			} catch (Exception e) {
			}
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] total data query COUNT executed in --> "
					+ deltaTime);

			starTtime = 0;
			deltaTime = -1;

			int maxdocPerPage = SDPDataApiConfig.getInstance().getMaxDocumentPerPage();
			try {
				// TODO add maxodatapage
				// maxdocPerPage=Integer.parseInt(MongoTenantDbSingleton.getInstance().getMaxDocPerPage(codiceTenantOrig));
				maxdocPerPage = configObject.getMaxOdataResultperpage();
				log.info(
						"[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] max doc per page from configuration --> "
								+ maxdocPerPage);
			} catch (Exception e) {

			}

			/** nuovi controlli skip e limit **/
			if (skipL < 0)
				skipL = 0;

			// controlli sui valoris massimi ammessi
			if (skipL > 0 && skipL > SDPDataApiConfig.getInstance().getMaxSkipPages())
				throw new SDPPageSizeException(
						"invalid skip value: max skip = " + SDPDataApiConfig.getInstance().getMaxSkipPages(),
						Locale.UK);
			if (limitL > 0 && limitL > maxdocPerPage)
				throw new SDPPageSizeException("invalid top value: max document per page = " + maxdocPerPage,
						Locale.UK);

			// se lo skip porta oltre il numero di risultati eccezione
			if (skipL > cnt)
				throw new SDPPageSizeException("skip value out of range: max document in query result = " + cnt,
						Locale.UK);

			if (limitL < 0) {

				// se limit non valorizzato si restituisce tutto il resultset (limit=cnt) e si
				// solleva eccezione se il resulset supera il numero massimo di risultati per
				// pagina
				if ((cnt > maxdocPerPage))
					throw new SDPPageSizeException(
							"too many documents; use top parameter: max document per page = " + maxdocPerPage,
							Locale.UK);
				limitL = cnt;
			}

			if (limitL > 0 && limitL > (cnt - skipL))
				limitL = cnt - skipL;

			ArrayList<SortClause> orderSolr = null;

			if (null != userOrderBy) {
				// log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] ORDINE
				// "+((ArrayList<Object>)userOrderBy).get(0));

				boolean orderByAllowed = false;
				if (cnt < SDPDataApiConstants.SDP_MAX_DOC_FOR_ORDERBY) {
					orderByAllowed = true;
				} else if ((DATA_TYPE_MEASURE.equals(datatType) || DATA_TYPE_SOCIAL.equals(datatType))
						&& ((ArrayList<SortClause>) userOrderBy).size() <= 1) {

					SortClause elemOrder = (SortClause) ((ArrayList<SortClause>) userOrderBy).get(0);
					if (elemOrder.getItem().equalsIgnoreCase("time_dt"))
						orderByAllowed = true;
					if (elemOrder.getItem().equalsIgnoreCase("retweetParentId")
							&& ("socialDataset".equalsIgnoreCase(streamSubtype)))
						orderByAllowed = true;
					if (elemOrder.getItem().equalsIgnoreCase("userId")
							&& ("socialDataset".equalsIgnoreCase(streamSubtype)))
						orderByAllowed = true;
					if (elemOrder.getItem().equalsIgnoreCase("id"))
						orderByAllowed = true;
				} else if (DATA_TYPE_DATA.equals(datatType) && ((ArrayList<SortClause>) userOrderBy).size() <= 1) {
					// SDPMongoOrderElement
					// elemOrder=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(0);
					SortClause elemOrder = (SortClause) ((ArrayList<SortClause>) userOrderBy).get(0);
					if (elemOrder.getItem().equalsIgnoreCase("id"))
						orderByAllowed = true;
				}

				if (!orderByAllowed)
					throw new SDPOrderBySizeException("too many documents for order clause;", Locale.UK);

				for (int kkk = 0; kkk < ((ArrayList<String>) userOrderBy).size(); kkk++) {
					if (null == orderSolr)
						orderSolr = new ArrayList<SortClause>();
					// yucca-1080
					SortClause cc = ((ArrayList<SortClause>) userOrderBy).get(kkk);
					orderSolr.add(new SortClause(cc.getItem().toLowerCase(), cc.getOrder()));
					// orderSolr.add(((ArrayList<SortClause>)userOrderBy).get(kkk));
				}

				starTtime = System.currentTimeMillis();
				// cursor = collMisure.find(query).sort(dbObjUserOrder).skip(skip).limit(limit);
				try {
					deltaTime = System.currentTimeMillis() - starTtime;
				} catch (Exception e) {
				}
			} else {
				starTtime = System.currentTimeMillis();
				// cursor = collMisure.find(query).skip(skip).limit(limit);
				try {
					deltaTime = System.currentTimeMillis() - starTtime;
				} catch (Exception e) {
				}

			}

			solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(queryTotSolr);
			solrQuery.setRows(new Integer(new Long(limitL).intValue()));
			solrQuery.setStart(new Integer(new Long(skipL).intValue()));
			if (null != orderSolr)
				solrQuery.setSorts(orderSolr);

			starTtime = System.currentTimeMillis();
			rsp = solrServer.query(collection, solrQuery);
			SolrDocumentList results = rsp.getResults();

			try {
				deltaTime = System.currentTimeMillis() - starTtime;
			} catch (Exception e) {
			}
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] total data query executed in --> "
					+ deltaTime);
			// log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] count -->
			// "+cursor.count());
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] orderby =" + orderSolr);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] limit --> " + limitL);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] skip --> " + skipL);

			SolrDocument curSolrDoc = null;
			try {
				for (int j = 0; j < results.size(); ++j) {
					curSolrDoc = results.get(j);
					log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] curSolrDoc --> "
							+ curSolrDoc.toString());

					String internalID = curSolrDoc.get("id").toString();
					// String datasetVersion=takeNvlValues(curSolrDoc.get("datasetVersion_l"));
					String datasetVersion = Util.takeNvlValues(curSolrDoc.get("datasetversion_l"));
					Map<String, Object> misura = new HashMap<String, Object>();
					misura.put("internalId", internalID);

					if (DATA_TYPE_MEASURE.equals(datatType) || DATA_TYPE_SOCIAL.equals(datatType)) {
						String streamId = curSolrDoc.get("streamcode_s").toString();
						String sensorId = curSolrDoc.get("sensor_s").toString();
						misura.put("streamCode", streamId);
						misura.put("sensor", sensorId);

						java.util.Date sddd = (java.util.Date) curSolrDoc.get("time_dt");

						misura.put("time", sddd);
					}
					// String iddataset=takeNvlValues(curSolrDoc.get("idDataset_l"));
					String iddataset = Util.takeNvlValues(curSolrDoc.get("iddataset_l"));
					if (null != iddataset)
						misura.put("idDataset", Integer.parseInt(iddataset));
					if (null != datasetVersion)
						misura.put("datasetVersion", Integer.parseInt(datasetVersion));

					ArrayList<String> elencoBinaryId = new ArrayList<String>();
					for (int i = 0; i < compPropsTot.size(); i++) {

						String chiave = compPropsTot.get(i).getName();
						String chiaveL = Util.getPropertyName(compPropsTot.get(i));

						chiaveL = compPropsTot.get(i).getName() + SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX
								.get(campoTipoMetadato.get(compPropsTot.get(i).getName()));

						//
						// if (curSolrDoc.keySet().contains(chiaveL) ) {
						// Object oo = curSolrDoc.get(chiaveL);
						//
						// String valore=takeNvlValues(curSolrDoc.get(chiaveL));
						//
						if (curSolrDoc.keySet().contains(chiaveL.toLowerCase())) {
							Object oo = curSolrDoc.get(chiaveL.toLowerCase());

							String valore = Util.takeNvlValues(curSolrDoc.get(chiaveL.toLowerCase()));
							log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] valore --> " + valore);
							log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] compPropsTot --> "
									+ ((SimpleProperty) compPropsTot.get(i)).getType());
							if (null != valore) {
								if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Boolean)) {
									misura.put(chiave, Boolean.valueOf(valore));
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.String)) {
									misura.put(chiave, valore);
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Int32)) {
									misura.put(chiave, Integer.parseInt(valore));
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Int64)) {
									misura.put(chiave, Long.parseLong(valore));
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Double)) {
									misura.put(chiave, Double.parseDouble(valore));
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.DateTimeOffset)) {
									// Object dataObj=obj.get(chiave);
									java.util.Date dtSolr = (java.util.Date) oo;
									misura.put(chiave, dtSolr);
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.DateTime)) {
//									//Sun Oct 19 07:01:17 CET 1969  TODO chiedere a Fabrizio
//									//EEE MMM dd HH:mm:ss zzz yyyy
//									Object dataObj=obj.get(chiave);// ?
//									//System.out.println("------------------------------"+dataObj.getClass().getName());
//									misura.put(chiave, dataObj);
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Decimal)) {
									// comppnenti.put(chiave, Float.parseFloat(valore));
									misura.put(chiave, Double.parseDouble(valore));
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Binary)) {
									Map<String, Object> mappaBinaryRef = new HashMap<String, Object>();
									mappaBinaryRef.put("idBinary", (String) valore);
									misura.put(chiave, mappaBinaryRef);
									elencoBinaryId.add((String) valore);

								}
							}
						}
					}
					if (elencoBinaryId.size() > 0)
						misura.put("____binaryIdsArray", elencoBinaryId);

					ret.add(misura);
				}

				try {
					deltaTime = System.currentTimeMillis() - starTtime;
				} catch (Exception e) {
				}
				log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] total fetch in --> " + deltaTime);

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				// cursor.close();
			}

		} catch (SDPOrderBySizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] SDPOrderBySizeException", e);
			throw (SDPOrderBySizeException) e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] SDPPageSizeException", e);
			throw (SDPPageSizeException) e;
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] GenericException", e);
			log.error("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] INGORED" + e);
		} finally {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] END");
		}

		SDPDataResult outres = new SDPDataResult(ret, cnt);
		return outres;
	}

	/**
	 * 
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @param datatType
	 * @param userQuery
	 * @param userOrderBy
	 * @param skip
	 * @param limit
	 * @param timeGroupByParam
	 * @param timeGroupOperatorsParam
	 * @param groupOutQuery
	 * @return
	 * @throws ODataException
	 */
	private SDPDataResult getMeasuresStatsPerStreamPhoenix(String nameSpace, EdmEntityContainer entityContainer,
			String internalId, String datatType, Object userQuery, Object userOrderBy, int skip, int limit,
			String timeGroupByParam, String timeGroupOperatorsParam, Object groupOutQuery) throws ODataException {
//		String collection=null;
		// String sensore=null;
		// String stream=null;
		String idDataset = null;
//		String datasetCode=null;
//		String datasetToFindVersion=null;
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		int cnt = 0;

		Connection conn = null;
		initDbObject(codiceApi);

		try {
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] BEGIN");
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] nameSpace=" + nameSpace);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] entityContainer=" + entityContainer);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] internalId=" + internalId);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] datatType=" + datatType);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] userQuery=" + userQuery);

			List<Property> compPropsTot = new ArrayList<Property>();
			// gian
			compPropsTot = Util.convertFromComponentResponseToSimpleProperty(
					configObject.getDettaglioStreamDatasetResponse().getComponents(), nameSpace);

//			List<Property> compPropsCur=new ArrayList<Property>();			

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			// - modificare eventualmente la logica di recupero di collencion,host, port, db
			// specifici per il dataset
			// - modificare eventualmente la logica di recupero dell'idDataset
			// INVARIATO

			idDataset = configObject.getDettaglioStreamDatasetResponse().getDataset().getIddataset().toString();
//			datasetCode=configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetcode();
			String streamSubtype = configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetSubtype()
					.getDatasetSubtype();
			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sara un array da mettere in in
			 */

			// idDataset=takeNvlValues(
			// ((DBObject)streamMetadata.get("configData")).get("idDataset") );

			String schema = configObject.getDettaglioStreamDatasetResponse().getDataset().getPhoenixschemaname();
			String table = configObject.getDettaglioStreamDatasetResponse().getDataset().getPhoenixtablename();

			List<ComponentResponse> eleCapmpi = configObject.getDettaglioStreamDatasetResponse().getComponents();
			HashMap<String, String> campoTipoMetadato = new HashMap<String, String>();

			String campiPh = null;
			for (ComponentResponse componentResponse : eleCapmpi) {
				String nome = componentResponse.getName();
				String tipo = componentResponse.getDataType().getDatatypecode();
				campoTipoMetadato.put(nome, tipo);

//				if (campiPh == null) campiPh=nome+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo)+ " " + SDPDataApiConstants.SDP_DATATYPE_PHOENIXTYPES.get(tipo);
//				else campiPh+=","+nome+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo)+ " " + SDPDataApiConstants.SDP_DATATYPE_PHOENIXTYPES.get(tipo);
				String nomeTot = "\"" + (nome + SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo)).toUpperCase()
						+ "\"" + " " + SDPDataApiConstants.SDP_DATATYPE_PHOENIXTYPES.get(tipo);
				if (campiPh == null)
					campiPh = nomeTot;
				else
					campiPh += "," + nomeTot;
			}

			conn = DriverManager.getConnection(SDPDataApiConfig.getInstance().getPhoenixUrl());
			String queryBaseSolr = "( iddataset_l = ? and datasetversion_l>=0 ) ";

			String sql = " FROM " + schema + "." + table + " (" + campiPh + ")  WHERE  " + queryBaseSolr;
			if (null != internalId)
				sql += " AND (objectid=?)";
			if (null != userQuery)
				sql += " AND (" + ((SDPPhoenixExpression) userQuery).toString() + ")";

			String groupby = "";
			String groupbysleect = "";

			if ("year".equals(timeGroupByParam)) {
				groupby = " YEAR(time_dt)";
				groupbysleect = " YEAR(time_dt) as year, -1 as month, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek ";
			} else if ("month_year".equals(timeGroupByParam)) {
				groupby = " YEAR(time_dt), MONTH(time_dt)";
				groupbysleect = " YEAR(time_dt) as year , MONTH(time_dt) as month, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek";
			} else if ("dayofmonth_month_year".equals(timeGroupByParam)) {
				groupby = " YEAR(time_dt), MONTH(time_dt), DAYOFMONTH(time_dt) ";
				groupbysleect = " YEAR(time_dt) as year, MONTH(time_dt) as month , DAYOFMONTH(time_dt) as dayofmonth ,  -1 as hour, -1 as minute, -1 as dayofweek";
			} else if ("hour_dayofmonth_month_year".equals(timeGroupByParam)) {
				groupby = " YEAR(time_dt), MONTH(time_dt), DAYOFMONTH(time_dt), hour(to_time(''||TIME_DT, 'yyyy-MM-dd HH:mm:sss.SSS','GMT'))  ";
				groupbysleect = " YEAR(time_dt) as year, MONTH(time_dt) as month , DAYOFMONTH(time_dt) as dayofmonth, hour(to_time(''||TIME_DT, 'yyyy-MM-dd HH:mm:sss.SSS','GMT'))  as hour ,  -1 as minute, -1 as dayofweek";
			} else if ("minute_hour_dayofmonth_month_year".equals(timeGroupByParam)) {
				groupby = " YEAR(time_dt), MONTH(time_dt), DAYOFMONTH(time_dt), hour(to_time(''||TIME_DT, 'yyyy-MM-dd HH:mm:sss.SSS','GMT')) , MINUTE(time_dt)";
				groupbysleect = " YEAR(time_dt) as year, MONTH(time_dt) as month , DAYOFMONTH(time_dt) as dayofmonth, hour(to_time(''||TIME_DT, 'yyyy-MM-dd HH:mm:sss.SSS','GMT'))  as hour , MINUTE(time_dt) as minute , -1 as dayofweek";
			} else if ("month".equals(timeGroupByParam)) {
				// YUCCA-388
				groupby = " MONTH(time_dt)";
				groupbysleect = " MONTH(time_dt) as month , -1 as year, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek ";
			} else if ("dayofmonth_month".equals(timeGroupByParam)) {
				groupby = " MONTH(time_dt), DAYOFMONTH(time_dt)";
				groupbysleect = " MONTH(time_dt) as month , DAYOFMONTH(time_dt) as dayfomonth  -1 as year, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek ";
			} else if ("dayofweek_month".equals(timeGroupByParam)) {
				////// groupby = " MONTH(time_dt), DAYOFMONTH(time_dt)";
				groupby = " MONTH(time_dt), DAYOFWEEK(time_dt)";
				groupbysleect = " MONTH(time_dt) as month , -1 as dayfomonth,  -1 as year, -1 as dayofmonth, -1 as hour, -1 as minute, DAYOFWEEK(time_dt) as dayofweek ";
			} else if ("dayofweek".equals(timeGroupByParam)) {
				groupby = " DAYOFWEEK(time_dt)";
				groupbysleect = " -1 as month , -1 as dayfomonth,  -1 as year, -1 as dayofmonth, -1 as hour, -1 as minute, DAYOFWEEK(time_dt) as dayofweek ";
			} else if ("hour_dayofweek".equals(timeGroupByParam)) {
				groupby = " DAYOFWEEK(time_dt),hour(to_time(''||TIME_DT, 'yyyy-MM-dd HH:mm:sss.SSS','GMT')) ";
				groupbysleect = " -1 as month , -1 as dayfomonth,  -1 as year, -1 as dayofmonth, hour(to_time(''||TIME_DT, 'yyyy-MM-dd HH:mm:sss.SSS','GMT'))  as hour, -1 as minute, DAYOFWEEK(time_dt) as dayofweek ";
			} else if ("hour".equals(timeGroupByParam)) {
				// YUCCA-388
				groupby = "  hour(to_time(''||TIME_DT, 'yyyy-MM-dd HH:mm:sss.SSS','GMT'))  ";
				groupbysleect = "  hour(to_time(''||TIME_DT, 'yyyy-MM-dd HH:mm:sss.SSS','GMT'))  as hour, -1 as year, -1 as month,  -1 as dayofmonth,  -1 as minute, -1 as dayofweek";
			} else if ("retweetparentid".equals(timeGroupByParam)) {
				if (!("socialDataset".equalsIgnoreCase(streamSubtype)))
					throw new SDPCustomQueryOptionException(
							"invalid timeGroupBy value: retweetparentid aggregations is aveailable only for social dataset",
							Locale.UK);
				groupby = "  retweetParentId_l";
				groupbysleect = "  retweetParentId_l as retweetParentId, -1 as year, -1 as month, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek";
			} else if ("iduser".equals(timeGroupByParam)) {
				// YUCCA-388
				if (!("socialDataset".equalsIgnoreCase(streamSubtype)))
					throw new SDPCustomQueryOptionException(
							"invalid timeGroupBy value: iduser aggregations is aveailable only for social dataset",
							Locale.UK);
				groupby = "  userId_l ";
				groupbysleect = "  userId_l as userId , -1 as year, -1 as month, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek";
			} else {
				throw new SDPCustomQueryOptionException("invalid timeGroupBy value", Locale.UK);
			}

			if (groupbysleect.indexOf("userId_l") == -1 && "socialDataset".equalsIgnoreCase(streamSubtype)) {
				groupbysleect += ", -1 as userId_l";
			}
			if (groupbysleect.indexOf("retweetParentId_l") == -1 && "socialDataset".equalsIgnoreCase(streamSubtype)) {
				groupbysleect += ", -1 as retweetParentId_l";
			}

			// operazioni statistiche
			if (null == timeGroupOperatorsParam || timeGroupOperatorsParam.trim().length() <= 0)
				throw new SDPCustomQueryOptionException("invalid timeGroupOperators value", Locale.UK);
			StringTokenizer st = new StringTokenizer(timeGroupOperatorsParam, ";", false);
			HashMap<String, String> campoOperazione = new HashMap<String, String>();
			while (st.hasMoreTokens()) {
				String curOperator = st.nextToken();
				StringTokenizer stDue = new StringTokenizer(curOperator, ",", false);
				if (stDue.countTokens() != 2)
					throw new SDPCustomQueryOptionException("invalid timeGroupOperators value: '" + curOperator + "'",
							Locale.UK);
				String op = stDue.nextToken();
				String field = stDue.nextToken();
				if (!hasField(compPropsTot, field)) {
					throw new SDPCustomQueryOptionException(
							"invalid timeGroupOperators filed '" + field + "' in '" + curOperator + "' not fund in edm",
							Locale.UK);
				}
				String opPhoenix = null;
				boolean extraOp = false;
				if ("avg".equals(op))
					opPhoenix = "avg";
				else if ("first".equals(op)) {
					opPhoenix = "FIRST_VALUE";
					extraOp = true;
				} else if ("last".equals(op)) {
					opPhoenix = "LAST_VALUE";
					extraOp = true;
				} else if ("sum".equals(op))
					opPhoenix = "sum";
				else if ("max".equals(op))
					opPhoenix = "max";
				else if ("min".equals(op))
					opPhoenix = "min";
				else
					throw new SDPCustomQueryOptionException(
							"invalid timeGroupOperators invalid operation '" + op + "' in '" + curOperator + "'",
							Locale.UK);

				if (campoOperazione.containsKey(field))
					throw new SDPCustomQueryOptionException(
							"invalid timeGroupOperators filed '" + field + "' present in more than one operation",
							Locale.UK);

				campoOperazione.put(field, opPhoenix);

				String campoCompleto = field
						+ SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(campoTipoMetadato.get(field));
				campoCompleto = "\"" + campoCompleto.toUpperCase() + "\"";
				groupbysleect += ", " + opPhoenix + "(";
				groupbysleect += campoCompleto;
				groupbysleect += ")";
				if (extraOp) {
					groupbysleect += " WITHIN GROUP (ORDER BY " + campoCompleto + " asc) ";
				}
				// groupbysleect+= " as " + field +"_sts";
				groupbysleect += " as \"" + (field + "_sts").toUpperCase() + "\"";

			}

			sql = groupbysleect + ", count(1) as totale " + sql + " GROUP BY " + groupby;
			sql = "select * from (select " + sql + ")";

			if (null != groupOutQuery)
				sql += " where " + ((SDPPhoenixExpression) groupOutQuery).toString();

			if (null != userOrderBy)
				sql += " ORDER BY " + (String) userOrderBy;
			log.info("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] sqlPhoenix=" + sql);

			int strtINdex = 2;
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, new Double(idDataset).intValue());
			if (null != internalId) {
				stmt.setString(2, internalId);
				strtINdex = 3;
			}
			if (null != userQuery) {
				for (int i = 0; i < ((SDPPhoenixExpression) userQuery).getParameters().size(); i++) {
					Object curpar = ((SDPPhoenixExpression) userQuery).getParameters().get(i);
					stmt.setObject(strtINdex, curpar);
					strtINdex++;
				}
			}
			if (null != groupOutQuery) {
				for (int i = 0; i < ((SDPPhoenixExpression) groupOutQuery).getParameters().size(); i++) {
					Object curpar = ((SDPPhoenixExpression) groupOutQuery).getParameters().get(i);

					stmt.setObject(strtINdex, curpar);
					strtINdex++;
				}
			}

			long starTtime = 0;
			long deltaTime = -1;
			starTtime = System.currentTimeMillis();
			ResultSet rs = stmt.executeQuery();

			// Cursor cursor =collMisure.aggregate(pipeline,aggregationOptions);

			try {
				deltaTime = System.currentTimeMillis() - starTtime;
			} catch (Exception e) {
			}
			log.info("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] QUERY TIME =" + deltaTime);

			starTtime = System.currentTimeMillis();
			deltaTime = -1;

//			int cntRet=1;
			cnt = 0;

			while (rs.next()) {
				// System.out.println("num: "+cntRet+ "------------"
				// +rs.getString("iddataset_l"));
				// DBObject obj=result;
				String giorno = rs.getString("dayofmonth");
				String mese = rs.getString("month");
				String anno = rs.getString("year");
				String ora = rs.getString("hour");
				// YUCCA-346
				String minuto = rs.getString("minute");

				// YUCCA-388
				String dayofweek = rs.getString("dayofweek");
				String retweetparentid = null;
				String iduser = null;
				if ("socialDataset".equalsIgnoreCase(streamSubtype)) {
					retweetparentid = rs.getString("retweetParentId_l");
					iduser = rs.getString("userId_l");
				}

				String count = rs.getString("totale");

				Integer dayOfweekInt = (dayofweek == null ? -1 : new Integer(dayofweek));
				if (dayOfweekInt > 0) {
					dayOfweekInt++;
					if (dayOfweekInt > 7)
						dayOfweekInt = 1;
				}

				Map<String, Object> misura = new HashMap<String, Object>();
				misura.put("dayofmonth", (giorno == null ? -1 : new Integer(giorno)));
				misura.put("month", (mese == null ? -1 : new Integer(mese)));
				misura.put("year", (anno == null ? -1 : new Integer(anno)));
				misura.put("hour", (ora == null ? -1 : new Integer(ora)));
				// YUCCA-346
				misura.put("minute", (minuto == null ? -1 : new Integer(minuto)));
				// YUCCA-388
				misura.put("dayofweek", dayOfweekInt);
				misura.put("retweetparentid", (retweetparentid == null ? -1 : new Long(retweetparentid)));
				misura.put("iduser", (iduser == null ? -1 : new Long(iduser)));
				misura.put("count", (count == null ? 0 : new Integer(count)));
				for (int i = 0; i < compPropsTot.size(); i++) {
					String chiave = compPropsTot.get(i).getName();
					String chiaveEdm = chiave + "_sts";
					if (campoOperazione.get(chiave) != null) {
						String valore = rs.getString(chiaveEdm);
						if (null != valore) {
							if (((SimpleProperty) compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Boolean)) {
								misura.put(chiaveEdm, Boolean.valueOf(valore));
							} else if (((SimpleProperty) compPropsTot.get(i)).getType()
									.equals(EdmSimpleTypeKind.String)) {
								misura.put(chiaveEdm, valore);
							} else if (((SimpleProperty) compPropsTot.get(i)).getType()
									.equals(EdmSimpleTypeKind.Int32)) {
								// misura.put(chiaveEdm, Integer.parseInt(valore));
								misura.put(chiaveEdm, rs.getInt(chiaveEdm));
							} else if (((SimpleProperty) compPropsTot.get(i)).getType()
									.equals(EdmSimpleTypeKind.Int64)) {
								// misura.put(chiaveEdm, Long.parseLong(valore.replace(',','.')));
								misura.put(chiaveEdm, rs.getLong(chiaveEdm));
							} else if (((SimpleProperty) compPropsTot.get(i)).getType()
									.equals(EdmSimpleTypeKind.Double)) {
								// misura.put(chiaveEdm, Double.parseDouble(valore.replace(',','.')));
								misura.put(chiaveEdm, rs.getDouble(chiaveEdm));
							} else if (((SimpleProperty) compPropsTot.get(i)).getType()
									.equals(EdmSimpleTypeKind.DateTimeOffset)) {
								// Object dataObj=obj.get(chiave);
								// misura.put(chiave, dataObj);
							} else if (((SimpleProperty) compPropsTot.get(i)).getType()
									.equals(EdmSimpleTypeKind.DateTime)) {
								// Object dataObj=obj.get(chiave);
								// misura.put(chiave, dataObj);
							} else if (((SimpleProperty) compPropsTot.get(i)).getType()
									.equals(EdmSimpleTypeKind.Decimal)) {
								// misura.put(chiaveEdm, Double.parseDouble(valore.replace(',','.')));
								misura.put(chiaveEdm, rs.getDouble(chiaveEdm));

							}
						}

					}
				}
				cnt++;
				ret.add(misura);
			}

			try {
				deltaTime = System.currentTimeMillis() - starTtime;
			} catch (Exception e) {
			}
			log.info("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] FETCH TIME =" + deltaTime);

		} catch (Exception e) {
			if (e instanceof SDPCustomQueryOptionException) {
				log.error("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] rethrow", e);
				throw (SDPCustomQueryOptionException) e;
			} else
				log.error("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] INGORED", e);
		} finally {
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] END");
		}

		SDPDataResult outres = new SDPDataResult(ret, cnt);
		return outres;
	}

	/**
	 * 
	 * @param compPropsTot
	 * @param fieldName
	 * @return
	 */
	private boolean hasField(List<Property> compPropsTot, String fieldName) {
		for (int i = 0; compPropsTot != null && i < compPropsTot.size(); i++) {
			String chiave = compPropsTot.get(i).getName();
			if (chiave != null && chiave.equals(fieldName))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @param datatType
	 * @param userQuery
	 * @param userOrderBy
	 * @param elencoIdBinary
	 * @param codiceApi
	 * @param skipI
	 * @param limitI
	 * @return
	 * @throws ODataException
	 */
	@SuppressWarnings("unchecked")
	public SDPDataResult getBinary(String nameSpace, EdmEntityContainer entityContainer, String internalId,
			String datatType, Object userQuery, Object userOrderBy, ArrayList<String> elencoIdBinary, String codiceApi,
			int skipI, int limitI) throws ODataException {

		// potrebbe no nsubire modifiche verificare solo info.binaryIdDataset e
		// info.binaryDatasetVersion in base a come viene modificato streamMetadata

		initDbObject(codiceApi);

		String collection = null;
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		long cnt = -1;
		long skipL = skipI;
		long limitL = limitI;
		try {
			log.info("[SDPAdminApiOdataData::getBinary] BEGIN");
			log.debug("[SDPAdminApiOdataData::getBinary] nameSpace=" + nameSpace);
			log.debug("[SDPAdminApiOdataData::getBinary] entityContainer=" + entityContainer);
			log.debug("[SDPAdminApiOdataData::getBinary] internalId=" + internalId);
			log.debug("[SDPAdminApiOdataData::getBinary] datatType=" + datatType);
			log.debug("[SDPAdminApiOdataData::getBinary] userQuery=" + userQuery);

			collection = configObject.getDettaglioStreamDatasetResponse().getBinarydataset().getSolrcollectionname();

			Integer idDatasetBinary = configObject.getDettaglioStreamDatasetResponse().getBinarydataset()
					.getIddataset();
			Integer binaryDatasetVersion = configObject.getDettaglioStreamDatasetResponse().getDataset()
					.getDatasourceversionBinary();

			List<Property> compPropsTot = new ArrayList<Property>();

			compPropsTot.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String)
					.setFacets(new Facets().setNullable(false)));
			compPropsTot.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32)
					.setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64)
					.setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("idBinary").setType(EdmSimpleTypeKind.String)
					.setFacets(new Facets().setNullable(false)));
			compPropsTot.add(new SimpleProperty().setName("filenameBinary").setType(EdmSimpleTypeKind.String)
					.setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("aliasNameBinary").setType(EdmSimpleTypeKind.String)
					.setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("sizeBinary").setType(EdmSimpleTypeKind.Int64)
					.setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("contentTypeBinary").setType(EdmSimpleTypeKind.String)
					.setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("urlDownloadBinary").setType(EdmSimpleTypeKind.String)
					.setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("metadataBinary").setType(EdmSimpleTypeKind.String)
					.setFacets(new Facets().setNullable(true)));

			HashMap<String, String> campoTipoMetadato = new HashMap<String, String>();
			campoTipoMetadato.put("internalId", "id");
			campoTipoMetadato.put("datasetVersion", "datasetVersion_l");
			campoTipoMetadato.put("idDataset", "idDataset_l");
			campoTipoMetadato.put("idBinary", "idBinary_s");
			campoTipoMetadato.put("filenameBinary", "filenameBinary_s");
			campoTipoMetadato.put("aliasNameBinary", "aliasNameBinary_s");
			campoTipoMetadato.put("sizeBinary", "sizeBinary_l");
			campoTipoMetadato.put("contentTypeBinary", "contentTypeBinary_s");
			campoTipoMetadato.put("urlDownloadBinary", "urlDownloadBinary_s");
			campoTipoMetadato.put("metadataBinary", "metadataBinary_s");

			if (collection == null)
				return null;

			String queryTotSolr = "(iddataset_l:" + idDatasetBinary + " AND datasetversion_l : " + binaryDatasetVersion
					+ " ";
			if (null != internalId) {
				queryTotSolr += "AND id : " + internalId;
			}
			queryTotSolr += ")";
			if (null != userQuery) {
				log.debug("[SDPAdminApiOdataData::getBinary] userQuery=" + userQuery);
				if (userQuery instanceof SDPOdataFilterExpression) {
					queryTotSolr += "AND (" + userQuery + ")";
				} else {
					queryTotSolr += "AND " + userQuery;
				}
			}
			// '(iddataset_l:5185 AND datasetversion_l : 1 ) AND (idbinary_s : OR
			// GTFS_BUS_IT_PIE_PARTIAL.zip))
			String inClause = null;
			for (int kki = 0; null != elencoIdBinary && kki < elencoIdBinary.size(); kki++) {
				if (inClause == null)
					inClause = "(" + elencoIdBinary.get(kki);
				else
					inClause += " OR " + elencoIdBinary.get(kki);
			}
			String query = queryTotSolr;
			if (inClause != null)
				query += " AND (idbinary_s : " + inClause + "))";

			log.info("[SDPAdminApiOdataData::getBinary] total data query =" + query);
			if (skipL < 0)
				skipL = 0;
			if (limitL < 0)
				limitL = SDPDataApiConfig.getInstance().getMaxDocumentPerPage();

			// per ordinamento su max TODO
			limitL = SDPDataApiConfig.getInstance().getMaxDocumentPerPage()
					+ SDPDataApiConfig.getInstance().getMaxSkipPages();
			skipL = 0;

			ArrayList<SortClause> orderSolr = null;

			for (int kkk = 0; userOrderBy != null && kkk < ((ArrayList<String>) userOrderBy).size(); kkk++) {
				if (null == orderSolr)
					orderSolr = new ArrayList<SortClause>();
				orderSolr.add(((ArrayList<SortClause>) userOrderBy).get(kkk));
			}
			String hdpVersion = configObject.getDettaglioStreamDatasetResponse().getDataset().getHdpVersion();
			SolrClient solrServer;
			if (hdpVersion != null && !hdpVersion.equals("")) {
				solrServer = serverHdp3;
			} else {
				solrServer = serverHdp2;
			}

			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(query);
			solrQuery.setRows(new Integer(new Long(limitL).intValue()));
			solrQuery.setStart(new Integer(new Long(skipL).intValue()));
			if (null != orderSolr)
				solrQuery.setSorts(orderSolr);

			QueryResponse rsp = solrServer.query(collection, solrQuery);
			SolrDocumentList results = rsp.getResults();
			SolrDocument curSolrDoc = null;

			cnt = results.getNumFound();

			try {
				for (int j = 0; j < results.size(); ++j) {
					curSolrDoc = results.get(j);
					String internalID = curSolrDoc.get("id").toString();
					String datasetVersion = Util.takeNvlValues(curSolrDoc.get("datasetversion_l"));
					Map<String, Object> misura = new HashMap<String, Object>();
					misura.put("internalId", internalID);
					String iddataset = Util.takeNvlValues(curSolrDoc.get("iddataset_l"));
					if (null != iddataset)
						misura.put("idDataset", Integer.parseInt(iddataset));
					if (null != datasetVersion)
						misura.put("datasetVersion", Integer.parseInt(datasetVersion));

					for (int i = 0; i < compPropsTot.size(); i++) {

						String chiave = compPropsTot.get(i).getName();
						String chiaveL = Util.getPropertyName(compPropsTot.get(i));

						chiaveL = campoTipoMetadato.get(compPropsTot.get(i).getName());

						if (curSolrDoc.keySet().contains(chiaveL.toLowerCase())) {
							Object oo = curSolrDoc.get(chiaveL.toLowerCase());

							String valore = Util.takeNvlValues(curSolrDoc.get(chiaveL.toLowerCase()));
							if (null != valore) {
								if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Boolean)) {
									misura.put(chiave, Boolean.valueOf(valore));
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.String)) {
									misura.put(chiave, valore);
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Int32)) {
									misura.put(chiave, Integer.parseInt(valore));
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Int64)) {
									misura.put(chiave, Long.parseLong(valore));
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Double)) {
									misura.put(chiave, Double.parseDouble(valore));
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.DateTimeOffset)) {
									// Object dataObj=obj.get(chiave);
									java.util.Date dtSolr = (java.util.Date) oo;
									misura.put(chiave, dtSolr);
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.DateTime)) {
									// Sun Oct 19 07:01:17 CET 1969
									// EEE MMM dd HH:mm:ss zzz yyyy
									// Object dataObj=obj.get(chiave);
									// TODO ??? chiedere a Fabrizio
									// System.out.println("------------------------------"+dataObj.getClass().getName());
									// misura.put(chiave, dataObj);
									// SimpleDateFormat dateFormat = new
									// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
									// Date data = dateFormat.parse(valore);
									// misura.put(chiave, data);
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Decimal)) {
									// comppnenti.put(chiave, Float.parseFloat(valore));
									misura.put(chiave, Double.parseDouble(valore));
								} else if (((SimpleProperty) compPropsTot.get(i)).getType()
										.equals(EdmSimpleTypeKind.Binary)) {
									Map<String, Object> mappaBinaryRef = new HashMap<String, Object>();
									mappaBinaryRef.put("idBinary", (String) valore);
									misura.put(chiave, mappaBinaryRef);
								}
							}
						}
					}
					String path = "/api/" + codiceApi + "/attachment/" + idDatasetBinary + "/" + binaryDatasetVersion
							+ "/" + misura.get("idBinary");
					misura.put("urlDownloadBinary", path);
					ret.add(misura);
				}

				log.info("[SDPAdminApiOdataData::getBinary] total fetch in --> nodata");

			} catch (Exception e) {
				throw e;
			} finally {
				// cursor.close();
			}
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getBinary] INGORED" + e);
		} finally {
			log.debug("[SDPAdminApiOdataData::getBinary] END");
		}
		SDPDataResult outres = new SDPDataResult(ret, cnt);
		return outres;
	}

}
