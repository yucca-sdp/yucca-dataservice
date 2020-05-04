/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;



import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.delegate.beans.ranger.PostRangerRequest;
import org.csi.yucca.adminapi.model.ComponentJson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonInclude;



@Configuration
@PropertySource(value = { "classpath:adminapi.properties" })
public class RangerDelegate {

	private static final Logger logger = Logger.getLogger(RangerDelegate.class);

	private static RangerDelegate rangerDelegate;

	@Value("${ranger.url}")
	private String rangerUrl;
	
	@Value("${rangerCreate.url}")
	private String rangerCreateUrl;

	@Value("${ranger.password}")
	private String rangerPassword;
	
	@Value("${ranger.username}")
	private String rangerUsername;


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
	 * curl -i -X 
	 * GET http://<HOST>:<PORT>/service/public/v2/api/service/<SERVICE-NAME>/policy/<POLICY-NAME>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listPolicies(String policyName) throws Exception {
	
		logger.info("[RangerDelegate::listPolicies] URI:" + rangerUrl + "/" + policyName);
		
		CloseableHttpClient httpClient = getHttpClientRanger();

		String response = HttpDelegate.makeHttpGet(httpClient, rangerUrl+ "/" + policyName, null, rangerUsername, rangerPassword, null);
		
		logger.info("[RangerDelegate::listPolicies] responseString:" + response);

	    return response;
	}
	
	/**
	 * <b>CREATEPOLICY</b>
	 * 
	 * curl -i -X 
	 * POST -H "Content-Type: application/json" http://<HOST>:<PORT>/service/public/v2/api/policy -d @<JSON_FILE>
	 * 
	 * @param JSON_FILE
	 * @return
	 * @throws Exception
	 */
	public String createPolicy(PostRangerRequest request) throws Exception {
	
		logger.info("[RangerDelegate::createPolicy] URI:" + rangerCreateUrl);
		
		CloseableHttpClient httpClient = getHttpClientRanger();
		
		String response = HttpDelegate.makeHttpPost(httpClient, rangerCreateUrl, null, rangerUsername, rangerPassword, ObjectToJson(request), null);
		
		logger.info("[RangerDelegate::listPolicies] responseString:" + response);
 
	    return response;
	}
	
	/**
	 * <b>CREATEPOLICY</b>
	 * 
	 * curl -i -X 
	 * POUT -H "Content-Type: application/json" http://<HOST>:<PORT>/service/public/v2/api/policy -d @<JSON_FILE>
	 * 
	 * @param JSON_FILE
	 * @return
	 * @throws Exception
	 */
	public String updatePolicy(PostRangerRequest request, String policyName ) throws Exception {
	
		logger.info("[RangerDelegate::updatePolicies] URI:" + rangerUrl + "/" + policyName);
		
		CloseableHttpClient httpClient = getHttpClientRanger();
		
		String response = HttpDelegate.makeHttpPut(httpClient, rangerUrl + "/" + policyName, null, rangerUsername, rangerPassword, ObjectToJson(request), null);
		
		logger.info("[RangerDelegate::updatePolicies] responseString:" + response);
 
	    return response;
	}
	

	public String getPolicyName(String organizationCode, String datasetCode) throws Exception {
		
		String policyName = "SDP_" + organizationCode + "_" + datasetCode;
	       
	    return policyName;
	}
	
	
	private static CloseableHttpClient getHttpClientRanger() {
		CloseableHttpClient client = HttpClientBuilder.create().build();
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
			request = mapper.readValue(json,PostRangerRequest.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return request;
		}
	
	
}
