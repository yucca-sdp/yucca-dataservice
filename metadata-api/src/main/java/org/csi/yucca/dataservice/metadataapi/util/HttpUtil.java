/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpMethod;
//import org.apache.commons.httpclient.UsernamePasswordCredentials;
//import org.apache.commons.httpclient.auth.AuthScope;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;

public class HttpUtil {

	static Logger log = Logger.getLogger(HttpUtil.class);

	private static HttpUtil instance;

	private HttpUtil() {
	};

	public static HttpUtil getInstance() {
		if (instance == null)
			instance = new HttpUtil();
		return instance;
	}

	public String doGet(String targetUrl, String contentType, String characterEncoding, Map<String, String> parameters) {
		return makeCall("GET", targetUrl, contentType, characterEncoding, parameters);
	}

	public String doPost(String targetUrl, String contentType, String characterEncoding, Map<String, String> parameters) {
		return makeCall("POST", targetUrl, contentType, characterEncoding, parameters);
	}

	private String makeCall(String method, String targetUrl, String contentType, String characterEncoding, Map<String, String> parameters) {
		log.debug("[AbstractService::doPost] START");
		String result = "";
		int resultCode = -1;
		try {

			HttpRequestBase httpRequestBase = prepareCall(method, targetUrl, contentType, characterEncoding, parameters);

			// auth
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(Config.getInstance().getSolrUsername(), Config.getInstance().getSolrPassword()));
			CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

			CloseableHttpResponse response = httpClient.execute(httpRequestBase);

			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String dataLine = null;
			while ((dataLine = reader.readLine()) != null) {
				buffer.append(dataLine);
			}
			result = buffer.toString();

			// HttpClient httpclient = new HttpClient();
			// httpclient.getState().setCredentials(new
			// AuthScope(AuthScope.ANY),
			// new
			// UsernamePasswordCredentials(Config.getInstance().getSolrUsername(),
			// Config.getInstance().getSolrPassword()));
			// httpMethod.setDoAuthentication(true);
			// try {
			// resultCode = httpclient.executeMethod(httpMethod);
			// log.debug("[AbstractService::doPost] - get result: " +
			// resultCode);
			// result = httpMethod.getResponseBodyAsString();
			// } finally {
			// httpMethod.releaseConnection();
			// }

		} catch (IOException e) {
			log.error("[AbstractService::doPost] ERROR IOException: " + e.getMessage());
			ErrorResponse error = new ErrorResponse();
			error.setErrorCode("" + resultCode);
			error.setMessage(e.getMessage());
			result = error.toJson();
		} finally {
			log.debug("[AbstractService::doPost] END");
		}
		return result;
	}

	private HttpRequestBase prepareCall(String method, String targetUrl, String contentType, String characterEncoding, Map<String, String> parameters) {

		if (contentType == null)
			contentType = "application/json";
		if (characterEncoding == null)
			characterEncoding = "UTF-8";

		log.debug("[AbstractService::doPost] - targetUrl: " + targetUrl);

		if (parameters != null) {
			for (String key : parameters.keySet()) {
				// targetUrl += key + "=" + parameters.get(key) + "&";
				// post.addParameter(key, parameters.get(key));
				targetUrl += key
						+ "="
						+ parameters.get(key).replaceAll("  ", " ").replaceAll(" ", "%20").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D").replaceAll(">", "%3E")
								.replaceAll("<", "%3C") + "&";

			}
		}

		HttpRequestBase httpRequestBase = new HttpGet(targetUrl);

		// HttpMethod httpMethod = new GetMethod(targetUrl);
		if (method.equals("POST"))
			httpRequestBase = new HttpPost(targetUrl);
		// httpMethod = new PostMethod(targetUrl);

		httpRequestBase.setHeader("Content-Type", contentType);

		return httpRequestBase;
	}
}
