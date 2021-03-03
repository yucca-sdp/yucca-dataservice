/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.delegate.beans.ranger.PostRangerRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@PropertySource(value = { "classpath:adminapi.properties" })
public class RangerDelegate {

	private static final Logger logger = Logger.getLogger(RangerDelegate.class);

	private static RangerDelegate rangerDelegate;

	@Value("${ranger.url}")
	private String rangerUrl;

	@Value("${ranger.hdp3.url}")
	private String rangerHdp3Url;

	@Value("${rangerCreate.url}")
	private String rangerCreateUrl;

	@Value("${rangerCreate.hdp3.url}")
	private String rangerCreateHdp3Url;

	@Value("${ranger.password}")
	private String rangerPassword;

	@Value("${ranger.hdp3.password}")
	private String rangerHdp3Password;

	@Value("${ranger.username}")
	private String rangerUsername;

	@Value("${ranger.hdp3.username}")
	private String rangerHdp3Username;

	@Value("${ranger.hdp3.basicauth.user}")
	private String rangerHdp3BasicauthUser;

	@Value("${ranger.hdp3.basicauth.password}")
	private String rangerHdp3BasicauthPassword;

	public RangerDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		logger.info("[RangerDelegate::RangerDelegate] rangerUrl" + rangerUrl);
	}

	public static RangerDelegate build() {
		if (rangerDelegate == null)
			rangerDelegate = new RangerDelegate();
		return rangerDelegate;
	}

	static {
		System.setProperty("jsse.enableSNIExtension", "false");
	}

	/**
	 * <b>LISTPOLICIES</b>
	 * 
	 * curl -i -X GET
	 * http://<HOST>:<PORT>/service/public/v2/api/service/<SERVICE-NAME>/policy/<POLICY-NAME>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listPolicies(String policyName, String hdpVersion) throws Exception {


		CloseableHttpClient httpClient = getHttpClientRanger();
		String rangerUrlVersion = getRangerUrl(hdpVersion);
		logger.info("[RangerDelegate::listPolicies] URI:" + rangerUrlVersion + "/" + policyName);
		String rangerUsernameVersion = getUsername(hdpVersion);
		String rangerPasswordVersion = getRangerPassword(hdpVersion);
		logger.info("[RangerDelegate::listPolicies] URI:" + rangerUrlVersion + "/" + policyName);
		String response = HttpDelegate.makeHttpGet(httpClient, rangerUrlVersion + "/" + policyName, null,
				rangerUsernameVersion, rangerPasswordVersion, null);
		logger.info("[RangerDelegate::listPolicies] responseString:" + response);

		return response;
	}

	/**
	 * <b>CREATEPOLICY</b>
	 * 
	 * curl -i -X POST -H "Content-Type: application/json"
	 * http://<HOST>:<PORT>/service/public/v2/api/policy -d @<JSON_FILE>
	 * 
	 * @param JSON_FILE
	 * @return
	 * @throws Exception
	 */
	public String createPolicy(PostRangerRequest request, String hdpVersion) throws Exception {

		logger.info("[RangerDelegate::createPolicy] URI:" + rangerCreateUrl);

		CloseableHttpClient httpClient = getHttpClientRanger();
		String rangerCreateUrlVersion = getRangerCreateUrl(hdpVersion);
		String rangerUsernameVersion = getUsername(hdpVersion);
		String rangerPasswordVersion = getRangerPassword(hdpVersion);
		logger.info("[RangerDelegate::createPolicy] URI:" + rangerCreateUrlVersion);
		logger.info("[RangerDelegate::createPolicy] username:" + rangerUsernameVersion);
		logger.info("[RangerDelegate::createPolicy] password:" + rangerPasswordVersion);
		String response = HttpDelegate.makeHttpPost(httpClient, rangerCreateUrlVersion, null, rangerUsernameVersion,
				rangerPasswordVersion, ObjectToJson(request), null);
		logger.info("[RangerDelegate::listPolicies] responseString:" + response);

		return response;
	}

	/**
	 * <b>CREATEPOLICY</b>
	 * 
	 * curl -i -X POUT -H "Content-Type: application/json"
	 * http://<HOST>:<PORT>/service/public/v2/api/policy -d @<JSON_FILE>
	 * 
	 * @param JSON_FILE
	 * @return
	 * @throws Exception
	 */
	public String updatePolicy(PostRangerRequest request, String policyName, String hdpVersion) throws Exception {

		logger.info("[RangerDelegate::updatePolicies] URI:" + rangerUrl + "/" + policyName);

		CloseableHttpClient httpClient = getHttpClientRanger();
		String rangerUrlVersion = getRangerUrl(hdpVersion);
		String rangerUsernameVersion = getUsername(hdpVersion);
		String rangerPasswordVersion = getRangerPassword(hdpVersion);
		logger.info("[RangerDelegate::updatePolicies] URI:" + rangerUrlVersion + "/" + policyName);
		String response = HttpDelegate.makeHttpPut(httpClient, rangerUrlVersion + "/" + policyName, null,
				rangerUsernameVersion, rangerPasswordVersion, ObjectToJson(request), null);
		logger.info("[RangerDelegate::updatePolicies] responseString:" + response);

		return response;
	}

	public String getPolicyName(String organizationCode, String datasetCode) throws Exception {

		String policyName = "SDP_" + organizationCode + "_" + datasetCode;

		return policyName;
	}

	private static CloseableHttpClient getHttpClientRanger()
			throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

		// CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		return client;
	}

	public String ObjectToJson(PostRangerRequest request) {

		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(request);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	public static PostRangerRequest JsonToPostRangerRequest(String json) {

		ObjectMapper mapper = new ObjectMapper();
		PostRangerRequest request = new PostRangerRequest();
		try {
			request = mapper.readValue(json, PostRangerRequest.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return request;
	}

	private String getRangerUrl(String hdpVersion) {
		if (hdpVersion != null && !hdpVersion.equals(""))
			return rangerHdp3Url;
		else
			return rangerUrl;
	}

	private String getRangerCreateUrl(String hdpVersion) {
		if (hdpVersion != null && !hdpVersion.equals(""))
			return rangerCreateHdp3Url;
		else
			return rangerCreateUrl;
	}

	private String getRangerPassword(String hdpVersion) {
		if (hdpVersion != null && !hdpVersion.equals(""))
			return rangerHdp3Password;
		else
			return rangerPassword;
	}

	private String getUsername(String hdpVersion) {
		if (hdpVersion != null && !hdpVersion.equals(""))
			return rangerHdp3Username;
		else
			return rangerUsername;
	}
}
