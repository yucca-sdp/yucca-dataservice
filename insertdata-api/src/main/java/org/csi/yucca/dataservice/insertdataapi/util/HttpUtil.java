/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.csi.yucca.dataservice.insertdataapi.model.output.ErrorOutput;

public class HttpUtil {

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	private static HttpUtil instance;

	private HttpUtil() {
	};

	public static HttpUtil getInstance() {
		if (instance == null)
			instance = new HttpUtil();
		return instance;
	}

	public String doGet(String targetUrl, String contentType, String characterEncoding,
			Map<String, String> parameters) {
		return makeCall("GET", targetUrl, contentType, characterEncoding, parameters, null);
	}

	public String doPost(String targetUrl, String contentType, String characterEncoding,
			Map<String, String> parameters) {
		return makeCall("POST", targetUrl, contentType, characterEncoding, parameters, null);
	}

	public String doPost(String targetUrl, String contentType, String characterEncoding, Map<String, String> parameters,
			String stringData) {
		return makeCall("POST", targetUrl, contentType, characterEncoding, parameters, stringData);
	}

	private String makeCall(String method, String targetUrl, String contentType, String characterEncoding,
			Map<String, String> parameters, String stringData) {
		log.debug("[AbstractService::doPost] START");
		String result = "";
		int resultCode = -1;
		try {

			HttpMethod httpMethod = prepareCall(method, targetUrl, contentType, characterEncoding, parameters, stringData);

			HttpClient httpclient = new HttpClient();
			try {
				resultCode = httpclient.executeMethod(httpMethod);
				log.debug("[AbstractService::doPost] - get result: " + resultCode);
				result = httpMethod.getResponseBodyAsString();
			} finally {
				httpMethod.releaseConnection();
			}

		} catch (IOException e) {
			log.error("[AbstractService::doPost] ERROR IOException: " + e.getMessage());
			ErrorOutput error = new ErrorOutput();
			error.setErrorCode("" + resultCode);
			error.setErrorMessage(e.getMessage());
			result = error.getErrorCode();
		} finally {
			log.debug("[AbstractService::doPost] END");
		}
		return result;
	}

	private HttpMethod prepareCall(String method, String targetUrl, String contentType, String characterEncoding,
			Map<String, String> parameters, String stringData) throws UnsupportedEncodingException {

		if (contentType == null)
			contentType = "application/json";
		if (characterEncoding == null)
			characterEncoding = "UTF-8";

		log.debug("[AbstractService::doPost] - targetUrl: " + targetUrl);

		if (parameters != null) {
			for (String key : parameters.keySet()) {
				targetUrl += key + "=" + parameters.get(key) + "&";
			}
		}

		HttpMethod httpMethod = new GetMethod(targetUrl);
		if (method.equals("POST")) {
			httpMethod = new PostMethod(targetUrl);
			if (stringData != null) {
				RequestEntity requestEntity = new StringRequestEntity(stringData, contentType, "UTF-8");
				((PostMethod)httpMethod).setRequestEntity(requestEntity);
			}

		}
		httpMethod.setRequestHeader("Content-Type", contentType);

		return httpMethod;
	}
}
