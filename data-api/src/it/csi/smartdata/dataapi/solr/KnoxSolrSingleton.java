/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package it.csi.smartdata.dataapi.solr;
import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
public class KnoxSolrSingleton {
private SolrClient serverHdp2;
private SolrClient serverHdp3;
	
	private KnoxSolrSingleton() {
		
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		if (SDPDataApiConfig.instance.getSolrUsername()!=null)
		{
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
					SDPDataApiConfig.instance.getSolrKnoxUsername(), SDPDataApiConfig.instance.getSolrKnoxPassword());
			
			provider.setCredentials(new AuthScope(AuthScope.ANY), credentials);
			
			clientBuilder.setDefaultCredentialsProvider(provider);
		}
		clientBuilder.setMaxConnTotal(128);
		clientBuilder.setMaxConnPerRoute(128);
		
		
		try {
			serverHdp2 = new HttpSolrClient(SDPDataApiConfig.getInstance().getSolrUrl(),
				clientBuilder.build());
		} catch (Exception e) {
			//TODO log
			e.printStackTrace();
		}
		
		
		
		
		HttpClientBuilder clientBuilderhdp3 = HttpClientBuilder.create();
		if (SDPDataApiConfig.instance.getSolrUsername()!=null)
		{
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
					SDPDataApiConfig.instance.getSolrKnoxUsername(), SDPDataApiConfig.instance.getSolrKnoxPassword());
			
			provider.setCredentials(new AuthScope(AuthScope.ANY), credentials);
			
			clientBuilderhdp3.setDefaultCredentialsProvider(provider);
		}
		clientBuilderhdp3.setMaxConnTotal(128);
		clientBuilderhdp3.setMaxConnPerRoute(128);
		
		
		try {
			serverHdp3 = new HttpSolrClient(SDPDataApiConfig.getInstance().getSolrHdp3Url(),
					clientBuilderhdp3.build());
		} catch (Exception e) {
			//TODO log
			e.printStackTrace();
		}
		
    }
	
		  /**
		   * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
		   * or the first access to SingletonHolder.INSTANCE, not before.
		   */
	  static class SingletonHolder { 
	    static final KnoxSolrSingleton INSTANCE = new KnoxSolrSingleton();
	  }

	  public static SolrClient getServerHdp2() {
		    return SingletonHolder.INSTANCE.serverHdp2;
		  }
	  
	  public static SolrClient getServerHdp3() {
		    return SingletonHolder.INSTANCE.serverHdp3;
		  }
}
