/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class HttpDelegate {

	private static final Logger logger = Logger.getLogger(HttpDelegate.class);

	public static String makeHttpDelete(String url, String basicAuthUsername, String basicAuthPassword)
			throws HttpException, IOException {
		return makeHttpDelete(null, url, basicAuthUsername, basicAuthPassword);
	}

	public static String makeHttpDelete(CloseableHttpClient httpclient, String url, String basicAuthUsername,
			String basicAuthPassword) throws HttpException, IOException {

		logger.debug("[HttpDelegate::makeHttpDelete] url " + url);

		HttpDelete httpDelete = new HttpDelete(url);

		if (basicAuthUsername != null && basicAuthPassword != null) {
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(basicAuthUsername, basicAuthPassword);
			httpDelete.addHeader(new BasicScheme().authenticate(creds, httpDelete, null));
		}

		if (httpclient == null) {
			httpclient = HttpClients.createDefault();
		}

		CloseableHttpResponse response = httpclient.execute(httpDelete);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode >= HttpStatus.SC_OK && statusCode < 300) {
			try {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity);
			} finally {
				response.close();
			}
		} else {
			logger.error("[HttpDelegate::makeHttpDelete] ERROR Status code " + statusCode);
			throw new HttpException("ERROR: Status code " + statusCode);
		}
	}

	public static String makeHttpPost(CloseableHttpClient httpclient, String url, List<NameValuePair> params)
			throws HttpException, IOException {
		return makeHttpPost(httpclient, url, params, null, null, null, null);
	}

	public static String makeHttpPost(CloseableHttpClient httpclient, String url, List<NameValuePair> params,
			String basicAuthUsername, String basicAuthPassword, String stringData) throws HttpException, IOException {
		return makeHttpPost(httpclient, url, params, basicAuthUsername, basicAuthPassword, stringData, null);
	}

	public static String makeHttpPost(

			CloseableHttpClient httpclient, String url, List<NameValuePair> params, String basicAuthUsername,
			String basicAuthPassword, String stringData, ContentType contentType) throws HttpException, IOException {

		logger.info("[HttpDelegate::makeHttpPost] url " + url + " params " + explainParams(params));
		//logger.info("[HttpDelegate::makeHttpPost] basicAuthUsername " + basicAuthUsername + " basicAuthPassword " + basicAuthPassword);

		HttpPost postMethod = new HttpPost(url);
		if (params != null)
			postMethod.setEntity(new UrlEncodedFormEntity(params));

		if (basicAuthUsername != null && basicAuthPassword != null) {
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(basicAuthUsername, basicAuthPassword);
			postMethod.addHeader(new BasicScheme().authenticate(creds, postMethod, null));
		}
		if (stringData != null) {
			StringEntity requestEntity = new StringEntity(stringData,
					contentType == null ? ContentType.APPLICATION_JSON : contentType);
			postMethod.setEntity(requestEntity);
		}

		if (httpclient == null) {
			httpclient = HttpClients.createDefault();
		}

		CloseableHttpResponse response = httpclient.execute(postMethod);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode >= HttpStatus.SC_OK && statusCode < 300) {
			try {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity);
			} finally {
				response.close();
			}
		} else {

			String errorName = "";
			JSONObject jsonObject = null;

			try {

				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);

				// result = {"output":null,"error_name":"Json validation
				// failed","error_code":"E012","error_message":null}

				JSONParser jsonParser = new JSONParser();

				jsonObject = (JSONObject) jsonParser.parse(result);

				if (jsonObject != null) {
					errorName = (String) jsonObject.get("error_name");
				}

			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				response.close();
			}

			logger.error("[HttpDelegate::makeHttpPost] ERROR Status code " + statusCode);
			throw new HttpException(
					"ERROR: Status code " + statusCode + (errorName.isEmpty() ? "" : " - Error Name: " + errorName));
		}
	}

	public static String makeHttpPut(

			CloseableHttpClient httpclient, String url, List<NameValuePair> params, String basicAuthUsername,
			String basicAuthPassword, String stringData, ContentType contentType) throws HttpException, IOException {

		logger.debug("[HttpDelegate::makeHttpPost] url " + url + " params " + explainParams(params));

		HttpPut putMethod = new HttpPut(url);
		if (params != null)
			putMethod.setEntity(new UrlEncodedFormEntity(params));

		if (basicAuthUsername != null && basicAuthPassword != null) {
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(basicAuthUsername, basicAuthPassword);
			putMethod.addHeader(new BasicScheme().authenticate(creds, putMethod, null));
		}
		if (stringData != null) {
			StringEntity requestEntity = new StringEntity(stringData,
					contentType == null ? ContentType.APPLICATION_JSON : contentType);
			putMethod.setEntity(requestEntity);
		}

		if (httpclient == null) {
			httpclient = HttpClients.createDefault();
		}

		CloseableHttpResponse response = httpclient.execute(putMethod);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode >= HttpStatus.SC_OK && statusCode < 300) {
			try {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity);
			} finally {
				response.close();
			}
		} else {

			String errorName = "";
			JSONObject jsonObject = null;

			try {

				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);

				// result = {"output":null,"error_name":"Json validation
				// failed","error_code":"E012","error_message":null}

				JSONParser jsonParser = new JSONParser();

				jsonObject = (JSONObject) jsonParser.parse(result);

				if (jsonObject != null) {
					errorName = (String) jsonObject.get("error_name");
				}

			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				response.close();
			}

			logger.error("[HttpDelegate::makeHttpPost] ERROR Status code " + statusCode);
			throw new HttpException(
					"ERROR: Status code " + statusCode + (errorName.isEmpty() ? "" : " - Error Name: " + errorName));
		}
	}

	public static String explainParams(List<NameValuePair> params) {
		String result = "";
		if (params != null)
			for (NameValuePair nameValuePair : params) {
				result += nameValuePair.getName() + "=" + nameValuePair.getValue() + "&";
			}
		return result;
	}

	// get

	public static String makeHttpGet(CloseableHttpClient httpclient, String url, List<NameValuePair> params)
			throws HttpException, IOException, Exception {
		return makeHttpGet(httpclient, url, params, null, null, null, null);
	}

	public static String makeHttpGet(CloseableHttpClient httpclient, String url, List<NameValuePair> params,
			// String basicAuthUsername,
			String bearerAuthToken, String stringData) throws HttpException, IOException, Exception {
		return makeHttpGet(httpclient, url, params, null, null, stringData, null, bearerAuthToken, false);
	}

	public static String makeHttpGet(CloseableHttpClient httpclient, String url, List<NameValuePair> params,
			String basicAuthUsername, String basicAuthPassword, String stringData)
			throws HttpException, IOException, Exception {
		return makeHttpGet(httpclient, url, params, basicAuthUsername, basicAuthPassword, stringData, null);
	}

	public static String makeHttpGet(

			CloseableHttpClient httpclient, String url, List<NameValuePair> params, String basicAuthUsername,
			String basicAuthPassword, String stringData, ContentType contentType)
			throws HttpException, IOException, Exception {

		return makeHttpGet(httpclient, url, params, basicAuthUsername, basicAuthPassword, stringData, contentType,
				null,false);

	}

	public static String makeHttpGet(
				CloseableHttpClient httpclient, 
				String url, 
				List<NameValuePair> params, 
				String basicAuthUsername,
				String basicAuthPassword, 
				String stringData, 
				ContentType contentType, 
				String bearerAuthToken,
				Boolean isOdata
			) throws HttpException, IOException, Exception {

		logger.info("[HttpDelegate::makeHttpGet] url " + url + " params " + explainParams(params));

		URI uri = null;
		if (params != null) {
			String formattedParams = URLEncodedUtils.format(params, "utf-8");
			if(isOdata)
				formattedParams = formattedParams.replaceAll("%24", "\\$").replaceAll("\\+", "%20").replaceAll("%27", "'");
			uri = new URI(url + "?" + formattedParams);
		}
		else
			uri = new URI(url);

		HttpGet getMethod = new HttpGet(uri);

		if (bearerAuthToken != null) {
			getMethod.addHeader("Authorization", "Bearer " + bearerAuthToken);
		}

		if (basicAuthUsername != null && basicAuthPassword != null) {
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(basicAuthUsername, basicAuthPassword);
			getMethod.addHeader(new BasicScheme().authenticate(creds, getMethod, null));
		}

		CloseableHttpResponse response = httpclient.execute(getMethod);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode >= HttpStatus.SC_OK && statusCode < 300) {
			try {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity);
			} finally {
				response.close();
			}
		} else {
			logger.error("[HttpDelegate::makeHttpGet] ERROR Status code " + statusCode);
			throw new HttpException("ERROR: Status code " + statusCode);
		}
	}
	
	public static void main(String[] args) {
		//String pa = "$format=json&$filter='PMVCS' eq Codice_prodotto&$top=15";
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("$format", "json"));
		params.add(new BasicNameValuePair("$filter","'CIAO' eq Codice_prodotto"));
		params.add(new BasicNameValuePair("$top", "1000"));
		System.out.println( URLEncodedUtils.format(params, "utf-8").replaceAll("%24", "\\$").replaceAll("\\+", "%20").replaceAll("%27", "'"));
		
		
	}

}
