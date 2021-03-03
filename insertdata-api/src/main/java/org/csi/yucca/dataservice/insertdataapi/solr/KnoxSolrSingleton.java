/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.solr;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.util.Base64;
import org.apache.solr.common.util.NamedList;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class KnoxSolrSingleton {

	private SolrClient server;

	private KnoxSolrSingleton(String username, String password, String url) {

		System.out.println("ELIMINARE KnoxSolrSingleton username " + username);
		System.out.println("ELIMINARE KnoxSolrSingleton password " + password);
		System.out.println("ELIMINARE KnoxSolrSingleton url " + url);

//		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
//		if (SDPInsertApiConfig.instance.getSolrUsername()!=null)
//		{
//			CredentialsProvider provider = new BasicCredentialsProvider();
//			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
//					SDPInsertApiConfig.instance.getSolrUsername(), SDPInsertApiConfig.instance.getSolrPassword());
//			provider.setCredentials(AuthScope.ANY, credentials);
//			clientBuilder.setDefaultCredentialsProvider(provider);
//		}
//		clientBuilder.setMaxConnTotal(128);
//
//		//server = new HttpSolrClient(SDPInsertApiConfig.getInstance().getSolrUrl(),clientBuilder.build());
//		server = new TEHttpSolrClient(SDPInsertApiConfig.getInstance().getSolrUrl());

//		SSLContextBuilder builder = new SSLContextBuilder();
//		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
//		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

		// CloseableHttpClient client = HttpClientBuilder.create().build();
		//CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		if (username != null) {
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
			provider.setCredentials(AuthScope.ANY, credentials);
			clientBuilder.setDefaultCredentialsProvider(provider);
		}
		clientBuilder.setMaxConnTotal(128);

		//server = new HttpSolrClient(SDPInsertApiConfig.getInstance().getSolrUrl(), clientBuilder.build());
		 server = new TEHttpSolrClient(url, username, password);
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	static class SingletonHolder {
		static final KnoxSolrSingleton INSTANCE_HDP2 = new KnoxSolrSingleton(
				SDPInsertApiConfig.getInstance().getKnoxSdnetUsername(),
				SDPInsertApiConfig.getInstance().getKnoxSdnetPassword(), SDPInsertApiConfig.getInstance().getSolrUrl());
		static final KnoxSolrSingleton INSTANCE_HDP3 = new KnoxSolrSingleton(
				SDPInsertApiConfig.getInstance().getKnoxSdnetUsernameHdp3(),
				SDPInsertApiConfig.getInstance().getKnoxSdnetPasswordHdp3(),
				SDPInsertApiConfig.getInstance().getSolrHdp3Url());
	}

	public static SolrClient getServerHdp2() {
		return SingletonHolder.INSTANCE_HDP2.server;
	}

	public static SolrClient getServerHdp3() {
		return SingletonHolder.INSTANCE_HDP3.server;
	}

	public class TEHttpSolrClient extends HttpSolrClient {

		private String defaultCollection = null;

		public void setDefaultCollection(String defaultCollection) {
			this.defaultCollection = defaultCollection;
		}

		private final String UTF_8 = StandardCharsets.UTF_8.name();
		private String username;
		private String password;

		public TEHttpSolrClient(String baseURL, String username, String password) {
			super(baseURL);
			this.username = username;
			this.password = password;
		}

		@Override
		public NamedList<Object> request(final SolrRequest request, String collection)
				throws SolrServerException, IOException {
			ResponseParser responseParser = request.getResponseParser();
			if (responseParser == null) {
				responseParser = this.parser;
			}

			if (collection == null && this.defaultCollection != null)
				collection = this.defaultCollection;
			return request(request, responseParser, collection);
		}

		public NamedList<Object> request(final SolrRequest request, final ResponseParser processor, String collection)
				throws SolrServerException, IOException {

			HttpRequestBase method = createMethod(request, collection);

			String userPass = username + ":"
					+ password;
			String encoded = Base64.byteArrayToBase64(userPass.getBytes(UTF_8));
			// below line will make sure that it sends authorization token every time in all
			// your requests
			method.setHeader(new BasicHeader("Authorization", "Basic " + encoded));

			try {
				return executeMethod(method, processor);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SolrServerException(e.getMessage());
			}

		}
	}

}
