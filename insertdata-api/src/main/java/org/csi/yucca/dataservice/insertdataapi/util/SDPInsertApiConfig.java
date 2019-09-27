/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SDPInsertApiConfig {

	// Generic properties, useful for all
	public static final String ADMIN_API_URL = "ADMIN_API_URL";
	public static final int MAX_DOCUMENTS_IN_REQUEST = 100000;
	public static final int MAX_DOCUMENTS_IN_REQUEST_REALTIME = 1000;
	//

	// Enabler for services
	public static final String ENABLE_DATA_MASSIVE_SERVICE_HTTP = "ENABLE_DATA_MASSIVE_SERVICE_HTTP";
	public static final String ENABLE_STREAM_MASSIVE_SERVICE_HTTP = "ENABLE_STREAM_MASSIVE_SERVICE_HTTP";
	public static final String ENABLE_MEDIA_MASSIVE_SERVICE_HTTP = "ENABLE_MEDIA_MASSIVE_SERVICE_HTTP";
	public static final String ENABLE_STREAM_REALTIME_SERVICE_JMS = "ENABLE_STREAM_REALTIME_SERVICE_JMS";
	public static final String ENABLE_STREAM_VALIDATION_SERVICE_HTTP = "ENABLE_STREAM_VALIDATION_SERVICE_HTTP";
	public static final String ENABLE_STREAM_VALIDATION_SERVICE_JMS = "ENABLE_STREAM_VALIDATION_SERVICE_JMS";
	public static final String ENABLE_SOCIAL_TWITTER_SERVICE_CRON = "ENABLE_SOCIAL_TWITTER_SERVICE_CRON";

	public static final String ENABLE_AUTHENTICATION_FILTER_HTTP = "ENABLE_AUTHENTICATION_FILTER_HTTP";

	// for ENABLE_DATA_MASSIVE_SERVICE_HTTP,ENABLE_STREAM_MASSIVE_SERVICE_HTTP,
	// ENABLE_MEDIA_MASSIVE_SERVICE_HTTP,ENABLE_STREAM_REALTIME_SERVICE_JMS
	public static final String PHOENIX_URL = "PHOENIX_URL";
	public static final String SOLR_INDEXER_ENABLED = "SOLR_INDEXER_ENABLED";
	public static final String SOLR_URL = "SOLR_URL";
	public static final String SOLR_TYPE_ACCESS = "SOLR_TYPE_ACCESS";
	public static final String SOLR_USERNAME = "SOLR_USERNAME";
	public static final String SOLR_PASSWORD = "SOLR_PASSWORD";
	public static final String SOLR_SECURITY_DOMAIN_NAME = "SOLR_SECURITY_DOMAIN_NAME";

	// for ENABLE_DATA_MASSIVE_SERVICE_HTTP (to delete)
	public static final String KNOX_SDNET_ULR = "KNOX_SDNET_ULR";
	public static final String KNOX_SDNET_USERNAME = "KNOX_SDNET_USERNAME";
	public static final String KNOX_SDNET_PASSWORD = "KNOX_SDNET_PASSWORD";
	public static final String MAIL_SERVER = "MAIL_SERVER";
	public static final String MAIL_TO_ADDRESS = "MAIL_TO_ADDRESS";
	public static final String MAIL_FROM_ADDRESS = "MAIL_FROM_ADDRESS";
	public static final String DELETE_MAIL_SUBJECT_404 = "DELETE_MAIL_SUBJECT_404";
	public static final String DELETE_MAIL_BODY_404 = "DELETE_MAIL_BODY_404";
	public static final String DELETE_MAIL_SUBJECT_500 = "DELETE_MAIL_SUBJECT_500";
	public static final String DELETE_MAIL_BODY_500 = "DELETE_MAIL_BODY_500";
	public static final String DELETE_MAIL_SUBJECT_200 = "DELETE_MAIL_SUBJECT_200";
	public static final String DELETE_MAIL_BODY_200 = "DELETE_MAIL_BODY_200";

	// only for ENABLE_STREAM_REALTIME_SERVICE_JMS,
	// ENABLE_STREAM_VALIDATION_SERVICE_JMS
	// ENABLE_STREAM_VALIDATION_SERVICE_JMS (websocket)
	public static final String JMS_MB_EXTERNAL_URL = "JMS_MB_EXTERNAL_URL";
	public static final String JMS_MB_EXTERNAL_USERNAME = "JMS_MB_EXTERNAL_USERNAME";
	public static final String JMS_MB_EXTERNAL_PASSWORD = "JMS_MB_EXTERNAL_PASSWORD";

	public static final String JMS_MB_INTERNAL_URL = "JMS_MB_INTERNAL_URL";
	public static final String JMS_MB_INTERNAL_USERNAME = "JMS_MB_INTERNAL_USERNAME";
	public static final String JMS_MB_INTERNAL_PASSWORD = "JMS_MB_INTERNAL_PASSWORD";

	public static final String RBAC_USER_STORE_WEBSERVICE_URL = "RBAC_USER_STORE_WEBSERVICE_URL";
	public static final String RBAC_WEBSERVICE_USER = "RBAC_WEBSERVICE_USER";
	public static final String RBAC_WEBSERVICE_PASSWORD = "RBAC_WEBSERVICE_PASSWORD";

	public static SDPInsertApiConfig instance = null;
	private static int anno_init = 0;
	private static int mese_init = 0;
	private static int giorno_init = 0;

	private HashMap<String, String> params = new HashMap<String, String>();

	private static boolean singletonToRefresh() {
		int curAnno = Calendar.getInstance().get(Calendar.YEAR);
		int curMese = Calendar.getInstance().get(Calendar.MONTH);
		int curGiorno = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (curAnno > anno_init)
			return true;
		else if (curMese > mese_init)
			return true;
		else if (curGiorno > giorno_init)
			return true;
		return false;
	}

	public synchronized static SDPInsertApiConfig getInstance() {
		if (instance == null || singletonToRefresh()) {
			instance = new SDPInsertApiConfig();
			anno_init = Calendar.getInstance().get(Calendar.YEAR);
			mese_init = Calendar.getInstance().get(Calendar.MONTH);
			giorno_init = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		}
		return instance;
	}

	private SDPInsertApiConfig() {
		ResourceBundle rb = ResourceBundle.getBundle("InsertdataApiConfig");
		params = new HashMap<String, String>();

		params.put(ENABLE_DATA_MASSIVE_SERVICE_HTTP, rb.getString(ENABLE_DATA_MASSIVE_SERVICE_HTTP));
		params.put(ENABLE_STREAM_MASSIVE_SERVICE_HTTP, rb.getString(ENABLE_STREAM_MASSIVE_SERVICE_HTTP));
		params.put(ENABLE_MEDIA_MASSIVE_SERVICE_HTTP, rb.getString(ENABLE_MEDIA_MASSIVE_SERVICE_HTTP));
		params.put(ENABLE_STREAM_REALTIME_SERVICE_JMS, rb.getString(ENABLE_STREAM_REALTIME_SERVICE_JMS));
		params.put(ENABLE_STREAM_VALIDATION_SERVICE_HTTP, rb.getString(ENABLE_STREAM_VALIDATION_SERVICE_HTTP));
		params.put(ENABLE_STREAM_VALIDATION_SERVICE_JMS, rb.getString(ENABLE_STREAM_VALIDATION_SERVICE_JMS));
		params.put(ENABLE_SOCIAL_TWITTER_SERVICE_CRON, rb.getString(ENABLE_SOCIAL_TWITTER_SERVICE_CRON));

		params.put(ENABLE_AUTHENTICATION_FILTER_HTTP, rb.getString(ENABLE_AUTHENTICATION_FILTER_HTTP));

		params.put(PHOENIX_URL, rb.getString(PHOENIX_URL));
		params.put(SOLR_URL, rb.getString(SOLR_URL));

		params.put(SOLR_TYPE_ACCESS, rb.getString(SOLR_TYPE_ACCESS));
		params.put(SOLR_USERNAME, rb.getString(SOLR_USERNAME));
		params.put(SOLR_PASSWORD, rb.getString(SOLR_PASSWORD));
		params.put(SOLR_SECURITY_DOMAIN_NAME, rb.getString(SOLR_SECURITY_DOMAIN_NAME));

		params.put(JMS_MB_INTERNAL_URL, rb.getString(JMS_MB_INTERNAL_URL));
		params.put(JMS_MB_INTERNAL_USERNAME, rb.getString(JMS_MB_INTERNAL_USERNAME));
		params.put(JMS_MB_INTERNAL_PASSWORD, rb.getString(JMS_MB_INTERNAL_PASSWORD));

		params.put(JMS_MB_EXTERNAL_URL, rb.getString(JMS_MB_EXTERNAL_URL));
		params.put(JMS_MB_EXTERNAL_USERNAME, rb.getString(JMS_MB_EXTERNAL_USERNAME));
		params.put(JMS_MB_EXTERNAL_PASSWORD, rb.getString(JMS_MB_EXTERNAL_PASSWORD));

		params.put(SOLR_INDEXER_ENABLED, rb.getString(SOLR_INDEXER_ENABLED));

		params.put(KNOX_SDNET_ULR, rb.getString(KNOX_SDNET_ULR));
		params.put(KNOX_SDNET_USERNAME, rb.getString(KNOX_SDNET_USERNAME));
		params.put(KNOX_SDNET_PASSWORD, rb.getString(KNOX_SDNET_PASSWORD));

		params.put(MAIL_SERVER, rb.getString(MAIL_SERVER));
		params.put(MAIL_TO_ADDRESS, rb.getString(MAIL_TO_ADDRESS));
		params.put(MAIL_FROM_ADDRESS, rb.getString(MAIL_FROM_ADDRESS));
		params.put(DELETE_MAIL_SUBJECT_404, rb.getString(DELETE_MAIL_SUBJECT_404));
		params.put(DELETE_MAIL_BODY_404, rb.getString(DELETE_MAIL_BODY_404));
		params.put(DELETE_MAIL_SUBJECT_500, rb.getString(DELETE_MAIL_SUBJECT_500));
		params.put(DELETE_MAIL_BODY_500, rb.getString(DELETE_MAIL_BODY_500));
		params.put(DELETE_MAIL_SUBJECT_200, rb.getString(DELETE_MAIL_SUBJECT_200));
		params.put(DELETE_MAIL_BODY_200, rb.getString(DELETE_MAIL_BODY_200));

		params.put(ADMIN_API_URL, rb.getString(ADMIN_API_URL));
		
		params.put(RBAC_USER_STORE_WEBSERVICE_URL, rb.getString(RBAC_USER_STORE_WEBSERVICE_URL));
		params.put(RBAC_WEBSERVICE_USER, rb.getString(RBAC_WEBSERVICE_USER));
		params.put(RBAC_WEBSERVICE_PASSWORD, rb.getString(RBAC_WEBSERVICE_PASSWORD));

	}

	public boolean isServiceEnable(String service) {
		String serviceEnable = params.get(service);
		if (serviceEnable != null) {
			try {
				return Boolean.parseBoolean(serviceEnable);
			} catch (Exception e) {
				return false;
			}
		} else
			return false;
	}

	public String getSolrSecurityDomainName() {
		return (params.get(SOLR_SECURITY_DOMAIN_NAME) != null ? params.get(SOLR_SECURITY_DOMAIN_NAME) : "");
	}

	public String getSolrUrl() {
		return params.get(SOLR_URL);
	}

	public String getSolrTypeAccess() {
		return params.get(SOLR_TYPE_ACCESS);
	}

	public String getSolrUsername() {
		return params.get(SOLR_USERNAME);
	}

	public String getSolrPassword() {
		return params.get(SOLR_PASSWORD);
	}

	public String getPhoenixUrl() {
		return params.get(PHOENIX_URL);
	}

	public String getJMSMbInternalUrl() {
		return params.get(JMS_MB_INTERNAL_URL);
	}

	public String getJMSMbInternalUsername() {
		return params.get(JMS_MB_INTERNAL_USERNAME);
	}

	public String getJMSMbInternalPassword() {
		return params.get(JMS_MB_INTERNAL_PASSWORD);
	}

	public String getJMSMbExternalUrl() {
		return params.get(JMS_MB_EXTERNAL_URL);
	}

	public String getJMSMbExternalUsername() {
		return params.get(JMS_MB_EXTERNAL_USERNAME);
	}

	public String getJMSMbExternalPassword() {
		return params.get(JMS_MB_EXTERNAL_PASSWORD);
	}

	public boolean isSolrIndexerEnabled() {
		String solrIndexerParam = params.get(SOLR_INDEXER_ENABLED);
		if (solrIndexerParam != null) {
			try {
				return Boolean.parseBoolean(solrIndexerParam);
			} catch (Exception e) {
				return false;
			}
		} else
			return false;
	}

	public String getKnoxSdnetUlr() {
		return params.get(KNOX_SDNET_ULR);
	}

	public String getKnoxSdnetUsername() {
		return params.get(KNOX_SDNET_USERNAME);
	}

	public String getKnoxSdnetPassword() {
		return params.get(KNOX_SDNET_PASSWORD);
	}

	public String getMailServer() {
		return params.get(MAIL_SERVER);
	}

	public String getMailToAddress() {
		return params.get(MAIL_TO_ADDRESS);
	}

	public String getMailFromAddress() {
		return params.get(MAIL_FROM_ADDRESS);
	}

	public String getDeleteMailSubject404() {
		return params.get(DELETE_MAIL_SUBJECT_404);
	}

	public String getDeleteMailBody404() {
		return params.get(DELETE_MAIL_BODY_404);
	}

	public String getDeleteMailSubject500() {
		return params.get(DELETE_MAIL_SUBJECT_500);
	}

	public String getDeleteMailBody500() {
		return params.get(DELETE_MAIL_BODY_500);
	}

	public String getDeleteMailSubject200() {
		return params.get(DELETE_MAIL_SUBJECT_200);
	}

	public String getDeleteMailBody200() {
		return params.get(DELETE_MAIL_BODY_200);
	}

	public String getAdminApiUrl() {
		return params.get(ADMIN_API_URL);
	}
	
	public String getRbacUserStoreWebserviceUrl() {
		return params.get(RBAC_USER_STORE_WEBSERVICE_URL);
	}
	
	public String getRbacWebserviceUser() {
		return params.get(RBAC_WEBSERVICE_USER);
	}
	public String getRbacWebservicePassword() {
		return params.get(RBAC_WEBSERVICE_PASSWORD);
	}
}
