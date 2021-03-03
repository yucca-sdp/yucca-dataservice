/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.delegate;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebServiceDelegate {

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	public static String callWebService(String wsURL, String username, String password, String xmlInput, String SOAPAction, String contentType)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} }; // evita l'utilizzo di un certificato se usi SSL
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		String responseString = "";
		String outputString = "";
		log.debug("Going to call " + wsURL);
		URL url = new URL(wsURL);
		URLConnection uc = null;
		if (username != null) {
			String userPassword = username + ":" + password;
			String encoding = Base64.encodeBase64String(userPassword.getBytes());
			uc = url.openConnection();
			uc.setRequestProperty("Authorization", "Basic " + encoding);
		} else
			uc = url.openConnection();
		HttpURLConnection httpConn;
		if (url.getProtocol().equals("https"))
			httpConn = (HttpsURLConnection) uc;
		else
			httpConn = (HttpURLConnection) uc;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", contentType + "; charset=utf-8"); // application/json
																						// o
																						// text/xml
		httpConn.setRequestProperty("SOAPAction", SOAPAction);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		// Write the content of the request to the outputstream of the HTTP
		// Connection.
		out.write(b);
		out.close();
		// Ready with sending the request.

		// Read the response.
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);

		// Write the SOAP message response to a String.
		while ((responseString = in.readLine()) != null) {
			outputString = outputString + responseString;
		}
		// Parse the String output to a org.w3c.dom.Document and be able to
		// reach every node with the org.w3c.dom API.
		// log.info("Result: " + outputString.substring(0, 200)+"...");
		return outputString;
	}

}
