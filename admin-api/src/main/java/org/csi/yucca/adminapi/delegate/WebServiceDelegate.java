/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.csi.yucca.adminapi.util.WebServiceResponse;
import org.springframework.stereotype.Service;

@Service
public class WebServiceDelegate {
	
	public static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
	public static final String SOAP_ACTION_HEADER_KEY = "SOAPAction";
	
	public static final String CONTENT_TYPE_XML = "text/xml";


	public static WebServiceResponse callWebService( String wsURL, String username, String password, 
            String xmlInput, String soapAction, String contentType)throws NoSuchAlgorithmException, KeyManagementException, IOException {
		
		HttpClientBuilder clientBuilder = HttpClients.custom();
		
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		clientBuilder.setDefaultCredentialsProvider(provider);
		CloseableHttpClient client = clientBuilder.build();
		HttpPost post = new HttpPost(wsURL);
		HttpEntity str = new StringEntity(xmlInput);
		post.setEntity(str);post.setHeader(CONTENT_TYPE_HEADER_KEY, contentType);
		post.setHeader(SOAP_ACTION_HEADER_KEY, soapAction);
		
		CloseableHttpResponse closeableHttpResponse = client.execute(post);
		
		return new WebServiceResponse(closeableHttpResponse);
	}
	
	
	

}
