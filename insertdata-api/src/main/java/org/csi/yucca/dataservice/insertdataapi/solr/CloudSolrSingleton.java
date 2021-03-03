/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.solr;


import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class CloudSolrSingleton {

	private SolrClient server;

	private CloudSolrSingleton(String url) {
		
		if (SDPInsertApiConfig.getInstance().getSolrSecurityDomainName() != null && SDPInsertApiConfig.getInstance().getSolrSecurityDomainName().trim().length()>0 && 
				!(SDPInsertApiConfig.getInstance().getSolrSecurityDomainName().equals("NO_SECURITY"))) {
		HttpClientUtil.setConfigurer( new  Krb5HttpClientConfigurer(SDPInsertApiConfig.getInstance().getSolrSecurityDomainName()));
		
		}

		server = new CloudSolrClient(url);
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder { 
		private static final CloudSolrSingleton INSTANCE_HDP2 = new CloudSolrSingleton(SDPInsertApiConfig.getInstance().getSolrUrl());
		private static final CloudSolrSingleton INSTANCE_HDP3 = new CloudSolrSingleton(SDPInsertApiConfig.getInstance().getSolrHdp3Url());
	}

	public static SolrClient getServerHdp2() {
		return SingletonHolder.INSTANCE_HDP2.server;
	}
	public static SolrClient getServerHdp3() {
		return SingletonHolder.INSTANCE_HDP3.server;
	}
	
	
	
	

}
