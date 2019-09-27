/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Config {

	public static final String METADATAAPI_BASE_URL = "METADATAAPI_BASE_URL";

	public static final String STORE_BASE_URL = "STORE_BASE_URL";
	public static final String USERPORTAL_BASE_URL = "USERPORTAL_BASE_URL";

//	public static final String SERVICE_BASE_URL = "SERVICE_BASE_URL";
//	public static final String MANAGEMENT_BASE_URL = "MANAGEMENT_BASE_URL";
	public static final String API_ADMIN_URL = "API_ADMIN_URL";
	
	public static final String EXPOSED_API_BASE_URL = "EXPOSED_API_BASE_URL";

	public static final String OAUTH_BASE_URL = "OAUTH_BASE_URL";
	public static final String OAUTH_USERNAME = "OAUTH_USERNAME";
	public static final String OAUTH_PASSWORD = "OAUTH_PASSWORD";
	public static final String OAUTH_ROLES_WEBSERVICE_URL = "OAUTH_ROLES_WEBSERVICE_URL";
	
	public static final String SEARCH_ENGINE_BASE_URL = "SEARCH_ENGINE_BASE_URL";
	public static final String SEARCH_ENGINE_COLLECTION = "SEARCH_ENGINE_COLLECTION";
	public static final String SOLR_TYPE_ACCESS = "SOLR_TYPE_ACCESS";
	public static final String SOLR_USERNAME = "SOLR_USERNAME";
	public static final String SOLR_PASSWORD = "SOLR_PASSWORD";
	public static final String SOLR_SECURITY_DOMAIN_NAME = "SOLR_SECURITY_DOMAIN_NAME";

	private static Map<String, String> params = null;
	private static Config instance = null;

	private Config() {

		params = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("MetadataApiConfig");
		params.put(METADATAAPI_BASE_URL, rb.getString(METADATAAPI_BASE_URL));
		params.put(STORE_BASE_URL, rb.getString(STORE_BASE_URL));
		params.put(USERPORTAL_BASE_URL, rb.getString(USERPORTAL_BASE_URL));
//		params.put(SERVICE_BASE_URL, rb.getString(SERVICE_BASE_URL));
//		params.put(MANAGEMENT_BASE_URL, rb.getString(MANAGEMENT_BASE_URL));
		params.put(API_ADMIN_URL, rb.getString(API_ADMIN_URL));
		params.put(EXPOSED_API_BASE_URL, rb.getString(EXPOSED_API_BASE_URL));
		params.put(OAUTH_BASE_URL, rb.getString(OAUTH_BASE_URL));
		params.put(OAUTH_USERNAME, rb.getString(OAUTH_USERNAME));
		params.put(SEARCH_ENGINE_BASE_URL, rb.getString(SEARCH_ENGINE_BASE_URL));
		params.put(OAUTH_ROLES_WEBSERVICE_URL,rb.getString(OAUTH_ROLES_WEBSERVICE_URL));

		
		
		params.put(SOLR_USERNAME,rb.getString(SOLR_USERNAME));
		params.put(SOLR_SECURITY_DOMAIN_NAME,rb.getString(SOLR_SECURITY_DOMAIN_NAME));
		params.put(SOLR_TYPE_ACCESS,rb.getString(SOLR_TYPE_ACCESS));
		params.put(SEARCH_ENGINE_COLLECTION,rb.getString(SEARCH_ENGINE_COLLECTION));
		
		
		ResourceBundle rbSecret = ResourceBundle.getBundle("MetadataApiSecret");
		params.put(OAUTH_PASSWORD, rbSecret.getString(OAUTH_PASSWORD));
		params.put(SOLR_PASSWORD, rbSecret.getString(SOLR_PASSWORD));
	}

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	public String getMetadataapiBaseUrl() {
		return params.get(METADATAAPI_BASE_URL);
	}

	public String getStoreBaseUrl() {
		return params.get(STORE_BASE_URL);
	}

	public String getUserportalBaseUrl() {
		return params.get(USERPORTAL_BASE_URL);
	}

	public String getOauthBaseUrl() {
		return params.get(OAUTH_BASE_URL);
	}

	public String getOauthUsername() {
		return params.get(OAUTH_USERNAME);
	}

	public String getOauthPassword() {
		return params.get(OAUTH_PASSWORD);
	}
	public String getOauthRolesWebserviceUrl()
	{
		return params.get(OAUTH_ROLES_WEBSERVICE_URL);
	}
//	public String getServiceBaseUrl() {
//		return params.get(SERVICE_BASE_URL);
//	}
//
//	public String getManagementBaseUrl() {
//		return params.get(MANAGEMENT_BASE_URL);
//	}

	public String getApiAdminUrl() {
		return params.get(API_ADMIN_URL);
	}

	public String getExposedApiBaseUrl() {
		return params.get(EXPOSED_API_BASE_URL);
	}

	public String getSearchEngineBaseUrl() {
		return params.get(SEARCH_ENGINE_BASE_URL);
	}
	
	public String getSearchEngineCollection() {
		return params.get(SEARCH_ENGINE_COLLECTION);
	}
	

	public String getSolrSecurityDomainName() {
		return (params.get(SOLR_SECURITY_DOMAIN_NAME) != null ? params.get(SOLR_SECURITY_DOMAIN_NAME) : "");
	}
	
	public String getSolrTypeAccess(){
		return params.get(SOLR_TYPE_ACCESS);
	}
	
	public String getSolrUsername(){
		return params.get(SOLR_USERNAME);
	}
	
	public String getSolrPassword(){
		return params.get(SOLR_PASSWORD);
	}
	
	
	
}
