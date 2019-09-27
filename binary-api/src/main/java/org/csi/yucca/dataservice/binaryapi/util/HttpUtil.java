/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.binaryapi.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.binaryapi.model.output.ErrorOutput;

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

			HttpMethod httpMethod = prepareCall(method, targetUrl, contentType, characterEncoding, parameters);

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

	private HttpMethod prepareCall(String method, String targetUrl, String contentType, String characterEncoding, Map<String, String> parameters) {

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
		if (method.equals("POST"))
			httpMethod = new PostMethod(targetUrl);

		httpMethod.setRequestHeader("Content-Type", contentType);

		return httpMethod;
	}
}
