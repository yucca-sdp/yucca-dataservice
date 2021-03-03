/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package it.csi.smartdata.dataapi.odata;



import it.csi.smartdata.dataapi.adminapi.SDPAdminApiOdataCast;
import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.dto.SDPDataResult;
import it.csi.smartdata.dataapi.exception.SDPPageSizeException;
import it.csi.smartdata.dataapi.util.AccountingLog;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.commons.InlineCount;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmLiteralKind;
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmSimpleType;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.UriParser;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;
import org.apache.olingo.odata2.api.uri.expression.ExpressionParserException;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetSimplePropertyUriInfo;
import org.apache.olingo.odata2.core.uri.expression.ExpressionParserInternalError;
import org.apache.olingo.odata2.core.uri.expression.FilterParserImpl;

public class SDPSingleProcessor extends ODataSingleProcessor {
	static Logger log = Logger.getLogger(SDPSingleProcessor.class.getPackage().getName());
	static Logger logAccounting= Logger.getLogger("accounting");

	private SDPAdminApiOdataCast adminApiAccess= new SDPAdminApiOdataCast();


	private String codiceApi=null;
	private String apacheUniqueId="-";
	public String getApacheUniqueId() {
		return apacheUniqueId;
	}


	public void setApacheUniqueId(String apacheUniqueId) {
		this.apacheUniqueId = apacheUniqueId;
	}

	private String baseUrl=null;
	public String getBaseUrl() {
		return baseUrl;
	}


	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}


	public String getCodiceApi() {
		return codiceApi;
	}


	public void setCodiceApi(String codiceApi) {
		this.codiceApi = codiceApi;
	}



	private int [] checkPagesData_old(Integer skip,Integer top, int resultSize) throws Exception{
		int startindex=0;
		int endindex=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();

		log.debug("[SDPSingleProcessor::checkPagesData] skipParameter="+skip);
		log.debug("[SDPSingleProcessor::checkPagesData] topParameter="+top);


		//controlli ... sollevo eccezione quando:
		// top valorizzato e > di maxsize
		// top non valorizzato e size > max
		
		
		
		
		if(top!=null && top.intValue()>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+endindex,Locale.UK);
		if(top==null && resultSize>SDPDataApiConfig.getInstance().getMaxDocumentPerPage())  throw new SDPPageSizeException("too many documents; use top parameter: max document per page = "+endindex,Locale.UK);


		//se skip e valorizzato
		if(skip!=null) {
			startindex=startindex+skip.intValue();
		}


		// a questo punto i parametri sono buoni ... valorizzo endindex in base al top se valorizzato (sempre con start index >0
		if(top!=null) endindex=top.intValue();

		endindex=startindex+endindex;

		// riporto endinx a resultsize nel caso in cui sia maggiore
		if (endindex>resultSize) endindex=resultSize;






		log.debug("[SDPSingleProcessor::checkPagesData] checkPagesData="+startindex);
		log.debug("[SDPSingleProcessor::checkPagesData] checkPagesData="+endindex);	



		int [] ret = new int[] {startindex,endindex}; 
		return ret; 

	}


	private int [] checkPagesData(Integer skip,Integer top, int resultSize) throws Exception{
		int startindex=0;
		int endindex=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();


		log.debug("[SDPSingleProcessor::checkPagesData] skipParameter="+skip);
		log.debug("[SDPSingleProcessor::checkPagesData] topParameter="+top);

		//se skip e valorizzato
		if(skip!=null) {
			startindex=startindex+skip.intValue();
		}
		
		if(skip!=null && skip.intValue()>SDPDataApiConfig.getInstance().getMaxSkipPages()) throw new SDPPageSizeException("invalid skip value: max page = "+SDPDataApiConfig.getInstance().getMaxSkipPages(),Locale.UK);
		

		//controlli ... sollevo eccezione quando:
		// top valorizzato e > di maxsize
		// top non valorizzato e size - start > max
		if(top!=null && top.intValue()>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+endindex,Locale.UK);
		if(top==null && (resultSize-startindex)>SDPDataApiConfig.getInstance().getMaxDocumentPerPage())  throw new SDPPageSizeException("too many documents; use top parameter: max document per page = "+endindex,Locale.UK);
		if(skip!=null && skip.intValue()>resultSize) throw new SDPPageSizeException("skip value out of range: max document in query result = "+resultSize,Locale.UK);




		// a questo punto i parametri sono buoni ... valorizzo endindex in base al top se valorizzato (sempre con start index >0
		if(top!=null) endindex=top.intValue();

		endindex=startindex+endindex;

		// riporto endinx a resultsize nel caso in cui sia maggiore
		if (endindex>resultSize) endindex=resultSize;






		log.debug("[SDPSingleProcessor::checkPagesData] checkPagesData="+startindex);
		log.debug("[SDPSingleProcessor::checkPagesData] checkPagesData="+endindex);	



		int [] ret = new int[] {startindex,endindex, ((top!=null) ? top.intValue() : -1 ) , ((skip!=null) ? skip.intValue() : -1 ) }; 
		return ret; 

	}	


	private int [] checkSkipTop(Integer skip,Integer top) throws Exception{
		return checkSkipTop(skip,top,true,true);
	}

	private int [] checkSkipTop(Integer skip,Integer top,boolean checkSkip,boolean checkTop) throws Exception{

		if (skip==null) skip=new Integer(-1);
		if (top==null) top= new Integer(-1);
		if(checkSkip && skip.intValue()>SDPDataApiConfig.getInstance().getMaxSkipPages()) throw new SDPPageSizeException("invalid skip value: max skip = "+SDPDataApiConfig.getInstance().getMaxSkipPages(),Locale.UK);
		if(checkTop && top.intValue()>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+SDPDataApiConfig.getInstance().getMaxDocumentPerPage(),Locale.UK);



		int [] ret = new int[] { skip.intValue() ,top.intValue() }; 
		return ret; 

	}		
	
	
	private String getNextUri(int skipOld,int topOld,String newUri,String entitiSetName,int recCount,GetEntitySetUriInfo uriInfo) throws Exception{

		String nextUri=null;
		if (topOld>0 && ((skipOld+topOld)<recCount) && SDPDataApiConfig.getInstance().isNextEnabled()) {
			int skipNew=	skipOld+topOld;
			nextUri=newUri+entitiSetName+"/?$top="+topOld+"&$skip="+skipNew;

			if (uriInfo.getOrderBy()!=null) nextUri+="&$orderby="+uriInfo.getOrderBy().getUriLiteral();

			if (uriInfo.getFilter()!=null) nextUri+="&$filter="+uriInfo.getFilter().getUriLiteral();
			if (uriInfo.getFormat()!=null) nextUri+="&$format="+uriInfo.getFormat();
			
			String select="";
			for (int k=0; uriInfo.getSelect()!=null && k<uriInfo.getSelect().size(); k++) {
				select=select+ (select.length()==0 ? "" : ",") +uriInfo.getSelect().get(k).getProperty().getName();
			}
			if (select.length()>0) nextUri+="&$select="+select;
			
			
		}	
		
		return nextUri;
	}

	@Override
	public ODataResponse readEntitySimpleProperty(final GetSimplePropertyUriInfo uriInfo,final String contentType) throws ODataException {
		throw new ODataNotImplementedException();
	}
	@Override
	public ODataResponse readEntitySimplePropertyValue(final GetSimplePropertyUriInfo uriInfo,final String contentType) throws ODataException {
		throw new ODataNotImplementedException();
	}

	/**
	 * 
	 * @param entitySet
	 * @return
	 * @throws EdmException
	 */
	private String getDataTypeStats(EdmEntitySet entitySet) throws EdmException{
		
		if ((SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS).equals(entitySet.getName())){
			return SDPAdminApiOdataCast.DATA_TYPE_SOCIAL; 
		} else if ((SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA_STATS).equals(entitySet.getName())){
			return SDPAdminApiOdataCast.DATA_TYPE_DATA;
		}
		return SDPAdminApiOdataCast.DATA_TYPE_MEASURE;
		
	}
	
	/**
	 * 
	 * @param uriInfo
	 * @param entitySet
	 * @return
	 * @throws EdmException
	 */
	private String getNameSpaceStats(GetEntitySetUriInfo uriInfo, EdmEntitySet entitySet) throws EdmException{

		String setNameStatCONST = SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS;
		
		if ((SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS).equals(entitySet.getName())) {
			setNameStatCONST = SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS;
		} else if ((SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA_STATS).equals(entitySet.getName())){
			setNameStatCONST = SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA_STATS;
		}
		
		return  uriInfo.getEntityContainer().getEntitySet(setNameStatCONST).getEntityType().getNamespace();
		
	}
	
	
	private EdmEntityType getMeasureType(GetEntitySetUriInfo uriInfo, EdmEntitySet entitySet) throws EdmException{
		
		String setNameCONST     = SDPDataApiConstants.ENTITY_SET_NAME_MEASURES;
		
		if ((SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS).equals(entitySet.getName())) {
			setNameCONST     = SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL;
		} else if ((SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA_STATS).equals(entitySet.getName())){
			setNameCONST = SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA;
		}
		
		return uriInfo.getEntityContainer().getEntitySet(setNameCONST).getEntityType();
	}

	
	private Object getUserQueryPhoenix(GetEntitySetUriInfo uriInfo, EdmEntitySet entitySet, 
			String timeGroupFilter, HashMap<String, String> mappaCampi) throws EdmException, ExceptionVisitExpression, ODataApplicationException, ExpressionParserException, ExpressionParserInternalError{
		
		EdmEntityType measureType = getMeasureType(uriInfo, entitySet);
		
		FilterExpression feStats = new FilterParserImpl(measureType).parseFilterString(timeGroupFilter, true);

		if (feStats != null) {
			SDPPhoenixExpressionVisitor evp = new SDPPhoenixExpressionVisitor();
			evp.setEntitySetName(entitySet.getName());
			evp.setMappaCampi(mappaCampi);
			
			Object userQueryPhoneix = feStats.accept(evp);
	
			log.debug("[SDPSingleProcessor::readEntitySet] userQueryPhoneix = " + userQueryPhoneix);

			return userQueryPhoneix;
		}
		
		return null;
	}
	
	private Object getUserQuerySolr(GetEntitySetUriInfo uriInfo, EdmEntitySet entitySet, 
			String timeGroupFilter, HashMap<String, String> mappaCampi) throws EdmException, ExceptionVisitExpression, ODataApplicationException, ExpressionParserException, ExpressionParserInternalError{

		EdmEntityType measureType = getMeasureType(uriInfo, entitySet);

		FilterExpression feStats = new FilterParserImpl(measureType).parseFilterString(timeGroupFilter, true);
		
		if (feStats != null) {
			
			SDPSolrExpressionVisitor evs = new SDPSolrExpressionVisitor();
			evs.setEntitySetName(entitySet.getName());
			evs.setMappaCampi(mappaCampi);
			
			Object userQuerySolr = feStats.accept(evs);

			log.debug("[SDPSingleProcessor::readEntitySet] userQuerySolr=" + userQuerySolr);
			
			return userQuerySolr;
		}
		
		return null;
	}

	/**
	 * 
	 * @param entitySet
	 * @param mappaCampi
	 * @param orderByExpression
	 * @return
	 * @throws EdmException
	 * @throws ExceptionVisitExpression
	 * @throws ODataApplicationException
	 */
	private Object getOrderQueryPhoenix( EdmEntitySet entitySet, 
										 HashMap<String, String> mappaCampi, 
										 OrderByExpression orderByExpression ) throws EdmException, ExceptionVisitExpression, ODataApplicationException{
		
		SDPPhoenixExpressionVisitor evs = new SDPPhoenixExpressionVisitor();
		evs.setEntitySetName(entitySet.getName());
		evs.setMappaCampi(mappaCampi);
		evs.setvisitorMOde(SDPPhoenixExpressionVisitor.MODE_STATS_HAVINGCLAUSE);
		
		Object orderQueryPhoenix = orderByExpression.accept(evs);
		
		log.debug("[SDPSingleProcessor::readEntitySet] orderQueryPhoenix="+orderQueryPhoenix);		
		
		return orderQueryPhoenix;
	}
	
	/**
	 * 
	 * @param entitySet
	 * @param mappaCampi
	 * @param filterExpression
	 * @return
	 * @throws EdmException
	 * @throws ExceptionVisitExpression
	 * @throws ODataApplicationException
	 */
	private Object getGroupOutQuery( EdmEntitySet entitySet, 
									 HashMap<String, String> mappaCampi, 
									 FilterExpression filterExpression ) throws EdmException, ExceptionVisitExpression, ODataApplicationException{
		
		SDPPhoenixExpressionVisitor evp = new SDPPhoenixExpressionVisitor();
		evp.setEntitySetName(entitySet.getName());
		evp.setMappaCampi(mappaCampi);
		evp.setvisitorMOde(SDPPhoenixExpressionVisitor.MODE_STATS_HAVINGCLAUSE);
		
		Object groupOutQuery = filterExpression.accept(evp);
		
		log.info("[SDPSingleProcessor::readEntitySet] havingPhoenix=" + groupOutQuery);
		
		return groupOutQuery;
	}
	
	
	/**
	 * 
	 * @param entitySet
	 * @param mappaCampi
	 * @param filterExpression
	 * @return
	 * @throws EdmException 
	 * @throws ODataApplicationException 
	 * @throws ExceptionVisitExpression 
	 */
	private Object getUserQuerySolr( EdmEntitySet entitySet, 
			 						 HashMap<String, String> mappaCampi, 
			 						 FilterExpression filterExpression ) throws EdmException, ExceptionVisitExpression, ODataApplicationException{
		
		SDPSolrExpressionVisitor evs = new SDPSolrExpressionVisitor();
		evs.setEntitySetName(entitySet.getName());
		evs.setMappaCampi(mappaCampi);
		
		Object userQuerySolr = filterExpression.accept(evs);
		
		log.debug("[SDPSingleProcessor::readEntitySet] userQuerySolr=" + userQuerySolr);
		
		return userQuerySolr;
	}
	
	/**
	 * 
	 * @param entitySet
	 * @param mappaCampi
	 * @param orderByExpression
	 * @return
	 * @throws EdmException
	 * @throws ExceptionVisitExpression
	 * @throws ODataApplicationException
	 */
	private Object getOrderQuerySolr( EdmEntitySet entitySet, 
			 						  HashMap<String, String> mappaCampi, 
			 						  OrderByExpression orderByExpression) throws EdmException, ExceptionVisitExpression, ODataApplicationException{
		
		SDPSolrExpressionVisitor evs = new SDPSolrExpressionVisitor();
		evs.setEntitySetName(entitySet.getName());
		evs.setMappaCampi(mappaCampi);
		
		Object orderQuerySolr = orderByExpression.accept(evs);
		
		log.debug("[SDPSingleProcessor::readEntitySet] orderQuerySolr="+orderQuerySolr);
		
		return orderQuerySolr;
	}
	
	
	// gian
	@Override
	public ODataResponse readEntitySet(final GetEntitySetUriInfo uriInfo,final String contentType) throws ODataException {

		AccountingLog accLog = new AccountingLog();
		
		long starTtime=0;
		long deltaTime=-1;
		
		try {
			starTtime=System.currentTimeMillis();
			
			log.debug("[SDPSingleProcessor::readEntitySet] BEGIN");
			log.debug("[SDPSingleProcessor::readEntitySet] uriInfo="     + uriInfo);
			log.debug("[SDPSingleProcessor::readEntitySet] contentType=" + contentType);
			log.debug("[SDPSingleProcessor::readEntitySet] codiceApi="   + codiceApi);
			log.debug("[SDPSingleProcessor::readEntitySet] apacheUniqueId="+apacheUniqueId);
			log.debug("[SDPSingleProcessor::readEntitySet] uriInfoDetail="+dump("uriInfo",uriInfo));
			
			accLog.setApicode(codiceApi);
			accLog.setUniqueid(apacheUniqueId);
			
			SDPAdminApiOdataCast sdpAdminApiOdataCast = new SDPAdminApiOdataCast();
			URI newUri=getContext().getPathInfo().getServiceRoot();
			
			try {
				newUri=new URI(this.baseUrl);
			} 
			catch (Exception e) {}
			
			log.debug("[SDPSingleProcessor::readEntitySet] newUri="+newUri);
			
			EdmEntitySet entitySet;
			
			ExpandSelectTreeNode expandSelectTreeNode = UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand());
			
			
// ##########################################
// PRIMA IF			
// ##########################################
			if (uriInfo.getNavigationSegments().size() == 0) {
			
				entitySet = uriInfo.getStartEntitySet();

				Object groupOutQueryPhoenix = null;
				Object groupOutQuerySolr    = null;
				
				Object userQuerySolr     = null;
				Object userQueryPhoneix  = null;
				
				Object orderQuerySolr    = null;
				Object orderQueryPhoenix = null;
				
				FilterExpression filterExpression   = uriInfo.getFilter();
				OrderByExpression orderByExpression = uriInfo.getOrderBy();
				HashMap<String, String> mappaCampi  = sdpAdminApiOdataCast.getDatasetMetadata(this.codiceApi);
				
				// SETTA USER_QUERY_SOLR
				if (filterExpression != null) {
					userQuerySolr = getUserQuerySolr(entitySet, mappaCampi, filterExpression);
				}

				// SETTA ORDER_QUERY_SOLR
				if (orderByExpression != null) {
					orderQuerySolr = getOrderQuerySolr(entitySet, mappaCampi, orderByExpression);
				}
				
				log.debug("[SDPSingleProcessor::readEntitySet] entitySet=" + entitySet.getName());
				
				accLog.setPath(entitySet.getName());				
//				accLog.setQuerString("" + groupOutQuery);
				
				// ##################
				// se sono STATS
				// ###################
				if ((SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS).equals(entitySet.getName()) || 
					(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS).equals(entitySet.getName())   ||
					(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA_STATS).equals(entitySet.getName())) {
					
					String dataType                = getDataTypeStats(entitySet);
					String nameSpace               = getNameSpaceStats(uriInfo, entitySet);
					String timeGroupByParam        = uriInfo.getCustomQueryOptions().get("timeGroupBy");
					String timeGroupOperatorsParam = uriInfo.getCustomQueryOptions().get("timeGroupOperators");
					String timeGroupFilter         = uriInfo.getCustomQueryOptions().get("timeGroupFilter");
					String bulkGroupByParam        = uriInfo.getCustomQueryOptions().get("groupBy");
					String bulkGroupOperatorsParam = uriInfo.getCustomQueryOptions().get("groupOperators");
					String bulkGroupFilter         = uriInfo.getCustomQueryOptions().get("groupFilter");
					
					// -------------------------------------------------------------
					// SETTA USER_QUERY_PHOENIX e USER_QUERY_SOLR
					if (null != timeGroupFilter && timeGroupFilter.trim().length()>0) {
						userQueryPhoneix = getUserQueryPhoenix(uriInfo, entitySet, timeGroupFilter, mappaCampi);
						userQuerySolr    = getUserQuerySolr(uriInfo, entitySet, timeGroupFilter, mappaCampi);
					} else if (null != bulkGroupFilter && bulkGroupFilter.trim().length()>0) {
						userQueryPhoneix = getUserQueryPhoenix(uriInfo, entitySet, bulkGroupFilter, mappaCampi);
						userQuerySolr    = getUserQuerySolr(uriInfo, entitySet, bulkGroupFilter, mappaCampi);
					}

					// -------------------------------------------------------------
					// SETTA ORDER_QUERY_PHOENIX
					if (orderByExpression != null) {
						orderQueryPhoenix = getOrderQueryPhoenix(entitySet, mappaCampi, orderByExpression);
						orderQuerySolr    = null; // TODO provvisorio, da gestire
					}
					
					// -------------------------------------------------------------
					// SETTA GROUP_OUT_QUERY
					if (filterExpression != null) {
						groupOutQueryPhoenix = getGroupOutQuery(entitySet, mappaCampi, filterExpression);
						groupOutQuerySolr    = null; // TODO  da gestire, per ora lasciamo null.
					}
					
					int [] skiptop = checkSkipTop(uriInfo.getSkip(), uriInfo.getTop());
					int skip       = skiptop[0];
					int top        = skiptop[1];
					
					// CHIAMA MEASURE STAT PER API
					SDPDataResult dataRes= sdpAdminApiOdataCast.getMeasuresStatsPerApi( this.codiceApi, 
							                                                            nameSpace,
							                                                            uriInfo.getEntityContainer(),
							                                                            null,
							                                                            userQueryPhoneix,
							                                                            orderQueryPhoenix,
							                                                            orderQuerySolr,
							                                                            userQuerySolr,
							                                                            -1,
							                                                            -1,
							                                                            timeGroupByParam,
							                                                            timeGroupOperatorsParam,
							                                                            bulkGroupByParam,
							                                                            bulkGroupOperatorsParam,
							                                                            groupOutQueryPhoenix,
							                                                            groupOutQuerySolr,
							                                                            dataType );
					
					
					accLog.setDataOut(dataRes.getDati().size());
					accLog.setTenantcode(dataRes.getTenant());
					accLog.setDatasetcode(dataRes.getDatasetCode());
					
					int [] limiti  = checkPagesData(uriInfo.getSkip(), uriInfo.getTop(), dataRes.getDati().size());
					int startindex = limiti[0];
					int endindex   = limiti[1];
					
					List<Map<String, Object>> misureNew = new ArrayList<Map<String,Object>>();
					for (int i=startindex;i<endindex;i++) {
						misureNew.add(dataRes.getDati().get(i));
					}
					
					ODataResponse ret= EntityProvider.writeFeed (
							contentType,
							entitySet,
							misureNew,
							EntityProviderWriteProperties.serviceRoot(
									newUri)
									.inlineCountType(InlineCount.ALLPAGES)
									.inlineCount(new Long(dataRes.getTotalCount()).intValue())
									.expandSelectTree(expandSelectTreeNode)
									.build());

					return ret;
					
				} // fine if(stats) 

// SE NON SONO STATS
				
				else if ((SDPDataApiConstants.ENTITY_SET_NAME_MEASURES).equals(entitySet.getName()) || 
						 (SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL).equals(entitySet.getName())) {

					String setNameCONST=SDPDataApiConstants.ENTITY_SET_NAME_MEASURES;
					
					if ((SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL).equals(entitySet.getName())) {
						setNameCONST=SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL;
					}
					
					String nameSpace=uriInfo.getEntityContainer().getEntitySet(setNameCONST).getEntityType().getNamespace();
					
					int [] skiptop = checkSkipTop(uriInfo.getSkip(), uriInfo.getTop(),true,false);
					int skip=skiptop[0];
					int top=skiptop[1];
					
					String dataType=SDPAdminApiOdataCast.DATA_TYPE_MEASURE;
					if ((SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL).equals(entitySet.getName())) dataType=SDPAdminApiOdataCast.DATA_TYPE_SOCIAL; 
					
					//log.info("[SDPSingleProcessor::readEntitySet] ORDINE MISURE "+((ArrayList<Object>)orderQuerySolr).get(0));
					
					
					// CHIAMA GET MEASURE PER API
					SDPDataResult dataRes= sdpAdminApiOdataCast.getMeasuresPerApi( this.codiceApi, 
																				   nameSpace,
																				   uriInfo.getEntityContainer(),
																				   null,
																				   userQuerySolr,
																				   orderQuerySolr,
																				   skip,
																				   top,
																				   dataType );
					
			
					accLog.setDataOut(dataRes.getDati().size());
					accLog.setTenantcode(dataRes.getTenant());
					accLog.setDatasetcode(dataRes.getDatasetCode());
					
					
					
//					int [] limiti=checkPagesData(uriInfo.getSkip(), uriInfo.getTop(), dataRes.getDati().size());
//					int startindex=limiti[0];
//					int endindex=limiti[1];


					List<Map<String, Object>> misureNew=new ArrayList<Map<String,Object>>();
//					for (int i=startindex;i<endindex;i++) {
//						misureNew.add(dataRes.getDati().get(i));
//					}
					for (int i=0;i<dataRes.getDati().size();i++) {
					misureNew.add(dataRes.getDati().get(i));
				}

					int skipOld=(skiptop[0] <=0 ?0 : skiptop[0]);
					int topOld=(skiptop[1] <=0 ?0 : skiptop[1]);

					String nextUri=getNextUri(skipOld, topOld, ""+newUri, entitySet.getName(), new Long(dataRes.getTotalCount()).intValue(), uriInfo);


					ODataResponse ret= EntityProvider.writeFeed (
							contentType,
							entitySet,
							misureNew,
							EntityProviderWriteProperties.serviceRoot(
									newUri)
									.inlineCountType(InlineCount.ALLPAGES)
									.inlineCount(new Long(dataRes.getTotalCount()).intValue())
									.expandSelectTree(expandSelectTreeNode).nextLink(nextUri)
									.build());

					return ret;

				} else if ((SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).equals(entitySet.getName())) {
					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).getEntityType().getNamespace();


					int [] skiptop = checkSkipTop(uriInfo.getSkip(), uriInfo.getTop(),true,false);


//					SDPDataResult dataRes=  new SDPMongoOdataCast().getMeasuresPerDataset(this.codiceApi, nameSpace,
//							uriInfo.getEntityContainer(),null,userQuery,orderQuery,
//							skiptop[0],
//							skiptop[1]);
					
					//log.info("[SDPSingleProcessor::readEntitySet] ORDINE BULK "+((ArrayList<Object>)orderQuerySolr).get(0).getClass());
					
					
					SDPDataResult dataRes=  new SDPAdminApiOdataCast().getMeasuresPerDataset(this.codiceApi, nameSpace,
							uriInfo.getEntityContainer(),null,userQuerySolr,orderQuerySolr,
							skiptop[0],
							skiptop[1]);

					accLog.setDataOut(dataRes.getDati().size());
					accLog.setTenantcode(dataRes.getTenant());
					accLog.setDatasetcode(dataRes.getDatasetCode());
					
//					int [] limiti=checkPagesData(uriInfo.getSkip(), uriInfo.getTop(),dataRes.getDati().size());
//					int startindex=limiti[0];
//					int endindex=limiti[1];
//
					List<Map<String, Object>> misureNew=new ArrayList<Map<String,Object>>();
//					for (int i=startindex;i<endindex;i++) {
//						misureNew.add(dataRes.getDati().get(i));
//					}

					for (int i=0;i<dataRes.getDati().size();i++) {
					misureNew.add(dataRes.getDati().get(i));
				}

					
					int skipOld=(skiptop[0] <=0 ?0 : skiptop[0]);
					int topOld=(skiptop[1] <=0 ?0 : skiptop[1]);

					String nextUri=getNextUri(skipOld, topOld, ""+newUri, entitySet.getName(), new Long(dataRes.getTotalCount()).intValue(), uriInfo);
					

					
					log.info("[SDPSingleProcessor::aaaaaaa] entitySet.getName()="+entitySet.getName());
					
					log.info("[SDPSingleProcessor::aaaaaaa] nextUri="+nextUri);
					log.info("[SDPSingleProcessor::aaaaaaa] dataRes.getTotalCount()="+dataRes.getTotalCount());
					log.info("[SDPSingleProcessor::aaaaaaa] misureNew.getDati().size()="+misureNew.size());
					for (int i=0;i<misureNew.size();i++) {
						Map<String, Object> oo = misureNew.get(i);
						Iterator<String> ite=oo.keySet().iterator();
						String rec=""; 
						while (ite.hasNext()) {
							String keyk=ite.next();
							rec=rec+", "+keyk+"="+oo.get(keyk);
						}
						log.info("[SDPSingleProcessor::aaaaaaa] *** REC="+rec);
						
					}
					
					
					
					ODataResponse ret= EntityProvider.writeFeed(
							contentType,
							entitySet,
							misureNew,
							EntityProviderWriteProperties.serviceRoot(
									newUri)
									.inlineCountType(InlineCount.ALLPAGES)
									.inlineCount(new Long(dataRes.getTotalCount()).intValue())
									.expandSelectTree(expandSelectTreeNode).nextLink(nextUri)
									.build()
									);

					return ret;


				} else if ((SDPDataApiConstants.ENTITY_SET_NAME_BINARY).equals(entitySet.getName())) {
					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_BINARY).getEntityType().getNamespace();


					int [] skiptop = checkSkipTop(uriInfo.getSkip(), uriInfo.getTop());


					SDPDataResult dataRes=  sdpAdminApiOdataCast.getBynaryPerDataset(this.codiceApi, nameSpace,
							uriInfo.getEntityContainer(),null,userQuerySolr,orderQuerySolr,
							null,
							skiptop[0],
							skiptop[1]);

					accLog.setDataOut(dataRes.getDati().size());
					accLog.setTenantcode(dataRes.getTenant());
					accLog.setDatasetcode(dataRes.getDatasetCode());
					
					int [] limiti=checkPagesData(uriInfo.getSkip(), uriInfo.getTop(),dataRes.getDati().size());
					int startindex=limiti[0];
					int endindex=limiti[1];

					List<Map<String, Object>> misureNew=new ArrayList<Map<String,Object>>();
					for (int i=startindex;i<endindex;i++) {
						misureNew.add(dataRes.getDati().get(i));
					}



					ODataResponse ret= EntityProvider.writeFeed(
							contentType,
							entitySet,
							misureNew,
							EntityProviderWriteProperties.serviceRoot(
									newUri)
									.inlineCountType(InlineCount.ALLPAGES)
									.inlineCount(new Long(dataRes.getTotalCount()).intValue())
									.expandSelectTree(expandSelectTreeNode)
									.build()
							);

					return ret;


				}
				throw new ODataNotFoundException(ODataNotFoundException.ENTITY);







			} 
			
//################################################à
//################################################à
//################################################à
			
			
			else if (uriInfo.getNavigationSegments().size() == 1) {
				// navigation first level, simplified example for illustration
				// purposes only
				entitySet = uriInfo.getTargetEntitySet();
				EdmEntitySet startEntity=uriInfo.getStartEntitySet();
				//EdmNavigationProperty navigationProperty = getContext().getNavigationProperty();


				EdmEntitySet targetEntity=uriInfo.getNavigationSegments().get(0).getEntitySet();
				EdmNavigationProperty prpnav=uriInfo.getNavigationSegments().get(0).getNavigationProperty();

				
				
				Object userQuerySolr = null;
				Object orderQuerySolr=null;
				HashMap<String, String> mappaCampi=new SDPAdminApiOdataCast().getDatasetMetadata (this.codiceApi);

				FilterExpression fe = uriInfo.getFilter();
				OrderByExpression oe=uriInfo.getOrderBy();
				if (fe != null) {
					
					SDPSolrExpressionVisitor evs = new SDPSolrExpressionVisitor();
					evs.setEntitySetName(entitySet.getName());
					evs.setMappaCampi(mappaCampi);
					userQuerySolr = fe.accept(evs);
					log.debug("[SDPSingleProcessor::readEntitySet] userQuery="+userQuerySolr);
					
					
				}
				if (oe != null) {
					
	                SDPSolrExpressionVisitor evs = new SDPSolrExpressionVisitor();
						evs.setEntitySetName(entitySet.getName());
						evs.setMappaCampi(mappaCampi);
						orderQuerySolr = oe.accept(evs);
						log.debug("[SDPSingleProcessor::readEntitySet] orderQuerySolr="+orderQuerySolr);					
				}
				log.debug("[SDPSingleProcessor::readEntitySet] entitySet="+targetEntity.getName());
				
				
				accLog.setPath(entitySet.getName());				
				accLog.setQuerString(""+userQuerySolr);


				if  (SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA.equals(startEntity.getName())) {
					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).getEntityType().getNamespace();

					String id = getKeyValue(uriInfo.getKeyPredicates().get(0));

					SDPDataResult dataRes  = sdpAdminApiOdataCast.getMeasuresPerDataset(this.codiceApi, nameSpace,
							uriInfo.getEntityContainer(),id,null,null,-1,-1);


					if  (SDPDataApiConstants.ENTITY_SET_NAME_BINARY.equals(targetEntity.getName())) {
						log.info("[SDPSingleProcessor::readEntitySet] ENTITY_SET_NAME_BINARY INIT");
						ArrayList<String> elencoIdBinary=new ArrayList<String> ();
						for (int i=0;i<dataRes.getDati().size();i++) {
							if (dataRes.getDati().get(i).containsKey("____binaryIdsArray")) {
								ArrayList<String> curarr=(ArrayList<String>)dataRes.getDati().get(i).get("____binaryIdsArray");
								for (int j=0; curarr!=null && j<curarr.size(); j++) {
									elencoIdBinary.add(curarr.get(j));
								}
							}
						}
						
						String nameSpaceTarget=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_BINARY).getEntityType().getNamespace();


						int [] skiptop = checkSkipTop(uriInfo.getSkip(), uriInfo.getTop());


						SDPDataResult dataResTarget=  sdpAdminApiOdataCast.getBynaryPerDataset(this.codiceApi, nameSpaceTarget,
								uriInfo.getEntityContainer(),null,userQuerySolr,orderQuerySolr,
								elencoIdBinary,
								skiptop[0],
								skiptop[1]);

						int [] limiti=checkPagesData(uriInfo.getSkip(), uriInfo.getTop(),dataResTarget.getDati().size());
						int startindex=limiti[0];
						int endindex=limiti[1];

						List<Map<String, Object>> misureNew=new ArrayList<Map<String,Object>>();
						for (int i=startindex;i<endindex;i++) {
							misureNew.add(dataResTarget.getDati().get(i));
						}

						accLog.setDataOut(dataRes.getDati().size());
						accLog.setTenantcode(dataRes.getTenant());
						accLog.setDatasetcode(dataRes.getDatasetCode());


						ODataResponse ret= EntityProvider.writeFeed(
								contentType,
								entitySet,
								misureNew,
								EntityProviderWriteProperties.serviceRoot(
										newUri)
										.inlineCountType(InlineCount.ALLPAGES)
										.inlineCount(new Long(dataRes.getTotalCount()).intValue())
										.expandSelectTree(expandSelectTreeNode)
										.build()
								);
						log.info("[SDPSingleProcessor::readEntitySet] ENTITY_SET_NAME_BINARY END");

						return ret;						
						
					}

					log.debug("[SDPSingleProcessor::readEntitySet] ENaaaaaaD");

				} else if  (SDPDataApiConstants.ENTITY_SET_NAME_BINARY.equals(startEntity.getName())) {
					
					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_BINARY).getEntityType().getNamespace();

					String id = getKeyValue(uriInfo.getKeyPredicates().get(0));

					SDPDataResult dataRes  = sdpAdminApiOdataCast.getBynaryPerDataset(this.codiceApi, nameSpace,
							uriInfo.getEntityContainer(),id,null,null,null,-1,-1);
					

					ArrayList<String> elencoIdBinary=new ArrayList<String>();
					for (int i=0; dataRes!=null && dataRes.getDati()!=null && i<dataRes.getDati().size(); i++) {
						Map<String, Object> cur=dataRes.getDati().get(i);
						if (cur.containsKey("idBinary")) elencoIdBinary.add((String) cur.get("idBinary"));
					}
					
					
					if  (SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA.equals(targetEntity.getName())) {

						
					}

					
				}
				/*
				 * if (ENTITY_SET_NAME_CARS.equals(entitySet.getName())) { int
				 * manufacturerKey = getKeyValue(uriInfo.getKeyPredicates().get(0));
				 * 
				 * List<Map<String, Object>> cars = new ArrayList<Map<String,
				 * Object>>(); cars.addAll(dataStore.getCarsFor(manufacturerKey));
				 * 
				 * return EntityProvider.writeFeed(contentType, entitySet, cars,
				 * EntityProviderWriteProperties.serviceRoot(
				 * getContext().getPathInfo().getServiceRoot()).build()); }
				 */

				throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
			}
			
			
			
			throw new ODataNotImplementedException();


		} catch (Exception e) {
			log.error("[SDPSingleProcessor::readEntitySet] " , e);
			accLog.setErrore(e.toString());
			
			if (e instanceof ODataException) throw (ODataException)e;
			throw new ODataException(e);
		} finally {
			try {
				deltaTime=System.currentTimeMillis()-starTtime;
				accLog.setElapsed(deltaTime);
			} catch (Exception e) {}
			logAccounting.info(accLog.toString());				
			
			log.debug("[SDPSingleProcessor::readEntitySet] END");

		}







		//		
		//		
		//		EdmEntitySet entitySet;
		//		if (uriInfo.getNavigationSegments().size() == 0) {
		//			entitySet = uriInfo.getStartEntitySet();
		//
		//			Object userQuery=null;
		//
		//			FilterExpression fe = uriInfo.getFilter();
		//			if (fe != null) {
		//				SDPExpressionVisitor ev = new SDPExpressionVisitor();
		//				ev.setEntitySetName(entitySet.getName());
		//				userQuery = fe.accept(ev);
		//				log.debug("expression:\n" + ev.getOut());
		//
		//			}
		//
		//
		//			if ((SDPDataApiConstants.ENTITY_SET_NAME_SMARTOBJECT).equals(entitySet.getName())) {
		//				return EntityProvider.writeFeed(
		//						contentType,
		//						entitySet,
		//						getSmartObj(),
		//						EntityProviderWriteProperties.serviceRoot(
		//								getContext().getPathInfo().getServiceRoot())
		//								.build());
		//			} else 			if ((SDPDataApiConstants.ENTITY_SET_NAME_STREAMS).equals(entitySet.getName())) {
		//				String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES).getEntityType().getNamespace();
		//
		//				List<Map<String, Object>> streams= new MongoDataAccess().getStreamsPerApi(this.codiceApi, nameSpace,
		//						uriInfo.getEntityContainer());
		//
		//
		//				ODataResponse ret= EntityProvider.writeFeed(
		//						contentType,
		//						entitySet,
		//						streams,
		//						EntityProviderWriteProperties.serviceRoot(
		//								newUri)
		//								.build());
		//
		//				return ret;
		//
		//			} else if ((SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).equals(entitySet.getName())) {
		//				String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).getEntityType().getNamespace();
		//
		//				List<Map<String, Object>> misure= new SDPMongoOdataCast().getMeasuresPerDataset(this.codiceApi, nameSpace,
		//						uriInfo.getEntityContainer(),null,userQuery);
		//
		//				int startindex=0;
		//				int endindex=misure.size();
		//
		//				if(uriInfo.getSkip()!=null) startindex=startindex+uriInfo.getSkip().intValue();
		//				if(uriInfo.getTop()!=null) endindex=startindex+uriInfo.getTop().intValue();
		//
		//				List<Map<String, Object>> misureNew=new ArrayList<Map<String,Object>>();
		//				for (int i=startindex;i<endindex;i++) {
		//					misureNew.add(misure.get(i));
		//				}
		//
		//
		//
		//
		//				ODataResponse ret= EntityProvider.writeFeed(
		//						contentType,
		//						entitySet,
		//						misureNew,
		//						EntityProviderWriteProperties.serviceRoot(
		//								newUri)
		//								.build());
		//
		//				return ret;
		//
		//
		//			}
		//
		//			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
		//
		//		} else if (uriInfo.getNavigationSegments().size() == 1) {
		//
		//			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
		//		}
		//
		//		throw new ODataNotImplementedException();

	}

	@Override
	public ODataResponse readEntity(final GetEntityUriInfo uriInfo,final String contentType) throws ODataException {
		AccountingLog accLog=new AccountingLog(); 
		long starTtime=0;
		long deltaTime=-1;

		try {
			starTtime=System.currentTimeMillis();
			log.debug("[SDPSingleProcessor::readEntity] BEGIN");
			log.debug("[SDPSingleProcessor::readEntity] uriInfo="+uriInfo);
			log.debug("[SDPSingleProcessor::readEntity] contentType="+contentType);
			log.debug("[SDPSingleProcessor::readEntity] codiceApi="+codiceApi);
			log.debug("[SDPSingleProcessor::readEntity] uriInfoDetail="+dump("uriInfo",uriInfo));
			
			accLog.setApicode(codiceApi);
accLog.setUniqueid(apacheUniqueId);
			
			
			URI newUri=getContext().getPathInfo().getServiceRoot();
			try {
				newUri=new URI(this.baseUrl);
			} catch (Exception e) {}
			log.debug("[SDPSingleProcessor::readEntitySet] newUri="+newUri);
			ExpandSelectTreeNode expandSelectTreeNode = UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand());

			if (uriInfo.getNavigationSegments().size() == 0) {
				EdmEntitySet entitySet = uriInfo.getStartEntitySet();

				if  (SDPDataApiConstants.ENTITY_SET_NAME_MEASURES.equals(entitySet.getName())) {

					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES).getEntityType().getNamespace();

					
		
					String id = getKeyValue(uriInfo.getKeyPredicates().get(0));
					accLog.setPath(entitySet.getName());				
					accLog.setQuerString("objectid="+id);

					String dataType=SDPAdminApiOdataCast.DATA_TYPE_MEASURE;
					if ((SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL).equals(entitySet.getName())) dataType=SDPAdminApiOdataCast.DATA_TYPE_SOCIAL; 

					SDPDataResult dataRes=  adminApiAccess.getMeasuresPerApi(this.codiceApi, nameSpace,
							uriInfo.getEntityContainer(),id,null,null,-1,-1,dataType);


					//Map<String, Object> data = dataRes.getDati().get(0);
					Map<String, Object> data = ( dataRes.getDati()!= null && dataRes.getDati().size()>0 ) ? dataRes.getDati().get(0) :null ;


					if (data != null) {
						URI serviceRoot = getContext().getPathInfo()
								.getServiceRoot();
						ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties
								.serviceRoot(newUri);

						accLog.setDataOut(dataRes.getDati().size());
		accLog.setTenantcode(dataRes.getTenant());
		accLog.setDatasetcode(dataRes.getDatasetCode());
						
						
						return EntityProvider.writeEntry(contentType, entitySet,
								data, propertiesBuilder.expandSelectTree(expandSelectTreeNode).build());
					} else {
						throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
					}
				} else if ((SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).equals(entitySet.getName())) {
					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).getEntityType().getNamespace();

					String id = getKeyValue(uriInfo.getKeyPredicates().get(0));
					accLog.setPath(entitySet.getName());				
					accLog.setQuerString("objectid="+id);

					SDPDataResult dataRes  = adminApiAccess.getMeasuresPerDataset(this.codiceApi, nameSpace,
							uriInfo.getEntityContainer(),id,null,null,-1,-1);


					Map<String, Object> data = ( dataRes.getDati()!= null && dataRes.getDati().size()>0 ) ? dataRes.getDati().get(0) : null ;

					if (data != null) {
						URI serviceRoot = getContext().getPathInfo()
								.getServiceRoot();
						ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties
								.serviceRoot(newUri);
						accLog.setDataOut(dataRes.getDati().size());
		accLog.setTenantcode(dataRes.getTenant());
		accLog.setDatasetcode(dataRes.getDatasetCode());

						return EntityProvider.writeEntry(contentType, entitySet,
								data, propertiesBuilder.expandSelectTree(expandSelectTreeNode).build());
					} else {
						throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
					}
				} else if ((SDPDataApiConstants.ENTITY_SET_NAME_BINARY).equals(entitySet.getName())) {
					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_BINARY).getEntityType().getNamespace();

					String id = getKeyValue(uriInfo.getKeyPredicates().get(0));
					accLog.setPath(entitySet.getName());				
					accLog.setQuerString("objectid="+id);

					SDPDataResult dataRes  = adminApiAccess.getBynaryPerDataset(this.codiceApi, nameSpace,
							uriInfo.getEntityContainer(),id,null,null,null,-1,-1);


					Map<String, Object> data = ( dataRes.getDati()!= null && dataRes.getDati().size()>0 ) ? dataRes.getDati().get(0) : null ;

					if (data != null) {
						URI serviceRoot = getContext().getPathInfo()
								.getServiceRoot();
						ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties
								.serviceRoot(newUri);
						accLog.setDataOut(dataRes.getDati().size());
		accLog.setTenantcode(dataRes.getTenant());
		accLog.setDatasetcode(dataRes.getDatasetCode());

						return EntityProvider.writeEntry(contentType, entitySet,
								data, propertiesBuilder.expandSelectTree(expandSelectTreeNode).build());
					} else {
						throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
					}
				} 
				/**
				 * else if
				 * (ENTITY_SET_NAME_MANUFACTURERS.equals(entitySet.getName())) { int
				 * id = getKeyValue(uriInfo.getKeyPredicates().get(0)); Map<String,
				 * Object> data = dataStore.getManufacturer(id);
				 * 
				 * if (data != null) { URI serviceRoot =
				 * getContext().getPathInfo().getServiceRoot();
				 * ODataEntityProviderPropertiesBuilder propertiesBuilder =
				 * EntityProviderWriteProperties.serviceRoot(serviceRoot);
				 * 
				 * return EntityProvider.writeEntry(contentType, entitySet, data,
				 * propertiesBuilder.build()); } }
				 **/
			}
			throw new ODataNotImplementedException();			



		} 
		catch (Exception e) {
			log.error("[SDPSingleProcessor::readEntity] " + e);
			accLog.setErrore(e.toString());

			if (e instanceof ODataException) throw (ODataException)e;
			throw new ODataException(e);
		} 
		finally {
			
			try {
				deltaTime=System.currentTimeMillis()-starTtime;		
				accLog.setElapsed(deltaTime);
			} 
			catch (Exception e) {}
			
			logAccounting.info(accLog.toString());				
			
			log.debug("[SDPSingleProcessor::readEntity] END");

		}






	}


	private String getKeyValue(final KeyPredicate key) throws ODataException {
		EdmProperty property = key.getProperty();
		EdmSimpleType type = (EdmSimpleType) property.getType();
		return type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT,
				property.getFacets(), String.class);
	}

	private String dump(String prefix,Object o) {
		StringBuilder sb = new StringBuilder();
		if (o == null)
			sb.append("null");
		else {
			Class<?> cl = o.getClass();

			for (Method m : cl.getMethods()) {
				String methodName = m.getName();
				if (methodName.startsWith("get") && m.getParameterTypes().length == 0 &&
						!methodName.equals("getClass")) {

					sb.append("\n");
					if (prefix != null)
						sb.append(prefix).append(".");
					sb.append(methodName.substring(3)).append(" ");
					int l = methodName.length();
					for (int i = 3; i < 25 - l ; i++)
						sb.append(".");
					sb.append(": ");
					try {
						Object val = m.invoke(o, (Object[]) null);
						sb.append(val);
						if (val != null)
							sb.append(" (").append(sb.getClass().getName()).append(")");
					} catch (Exception e) {
						sb.append("got exception ").append(e);
					}
				}
			}
		}

		return sb.toString();
	}


}
