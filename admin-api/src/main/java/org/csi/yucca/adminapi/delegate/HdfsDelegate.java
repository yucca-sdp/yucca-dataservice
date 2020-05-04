/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.delegate.beans.hdfs.FileStatusContainer;
import org.csi.yucca.adminapi.delegate.beans.hdfs.FileStatusesContainer;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
//

import com.fasterxml.jackson.databind.ObjectMapper;

//
public class HdfsDelegate {

	private static final Logger logger = Logger.getLogger(HdfsDelegate.class);

	private static HdfsDelegate hdfsDelegate;

	private ObjectMapper mapper = new ObjectMapper();

	@Value("${knox.url}")
	private String knoxUrl;
	@Value("${knox.user}")
	private String knoxUser;
	@Value("${knox.password}")
	private String knoxPassword;
	@Value("${hdfs.rootdir}")
	private String hdfsRootdir;

	public HdfsDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		logger.info("[HdfsDelegate::HdfsDelegate] hdfsUrl" + knoxUrl);
	}

	public static HdfsDelegate build() {
		if (hdfsDelegate == null)
			hdfsDelegate = new HdfsDelegate();
		return hdfsDelegate;
	}

	static {
		System.setProperty("jsse.enableSNIExtension", "false");
	}

	private static CloseableHttpClient getHttpClientKnox() {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		// Settata la http GET, setto le credenziali di KNOX!
		return client;
	}

	private static CloseableHttpClient getHttpClientKnox(boolean disableRedirect) {

		if (disableRedirect)
			return HttpClientBuilder.create().disableRedirectHandling().build();
		else
			return getHttpClientKnox();
	}

	private HttpClientContext getHttpContext() {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(knoxUser, knoxPassword));
		// Add AuthCache to the execution context
		final HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		return context;
	}

	protected static long copy(InputStream input, OutputStream result) throws IOException {
		byte[] buffer = new byte[12288]; // 8K=8192 12K=12288 64K=
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			result.write(buffer, 0, n);
			count += n;
			result.flush();
		}
		result.flush();
		return count;
	}

	/*
	 * ========================================================================
	 * GET
	 * ========================================================================
	 */
	/**
	 * <b>GETHOMEDIRECTORY</b>
	 * 
	 * curl -i "http://<HOST>:<PORT>/?op=GETHOMEDIRECTORY"
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getHomeDirectory() throws MalformedURLException, IOException {
		String spec = MessageFormat.format("/?op=GETHOMEDIRECTORY&user.name={0}", this.knoxUser);

		return genericGetForStringCall(spec);
	}

	/**
	 * <b>OPEN</b>
	 * 
	 * curl -i -L "http://<HOST>:<PORT>/<PATH>?op=OPEN
	 * [&offset=<LONG>][&length=<LONG>][&buffersize=<INT>]"
	 * 
	 * @param path
	 * @param os
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public InputStream open(String path) throws MalformedURLException, IOException {
		String spec = MessageFormat.format("{0}?op=OPEN&user.name={1}", URLEncoder.encode(path, "UTF-8"), this.knoxUser);

		HttpGet get;
		CloseableHttpClient client = null;
		HttpResponse response = null;

		try {
			get = new HttpGet(new URL(knoxUrl + spec).toURI());
			get.setHeader("Content-Type", "application/octet-stream");
			client = getHttpClientKnox();
			response = client.execute(get, getHttpContext());

			if (response.getStatusLine().getStatusCode() >= 400 && response.getStatusLine().getStatusCode() < 400) {
				throw new FileNotFoundException(path);
			}
			if (response.getStatusLine().getStatusCode() >= 500) {
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, "UTF-8");
				throw new IOException("Knox excpetion [" + response.getStatusLine().getStatusCode() + "] for path [" + path + "]. Message:[" + responseString + "]");
			}

			return response.getEntity().getContent();

		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		} finally {
			// client.close();
		}

	}

	/**
	 * <b>GETCONTENTSUMMARY</b>
	 * 
	 * curl -i "http://<HOST>:<PORT>/<PATH>?op=GETCONTENTSUMMARY"
	 * 
	 * @param path
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getContentSummary(String path) throws MalformedURLException, IOException {
		String spec = MessageFormat.format("{0}?op=GETCONTENTSUMMARY&user.name={1}", URLEncoder.encode(path, "UTF-8"), this.knoxUser);
		return genericGetForStringCall(spec);

	}

	/**
	 * <b>LISTSTATUS</b>
	 * 
	 * curl -i "http://<HOST>:<PORT>/<PATH>?op=LISTSTATUS"
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public FileStatusesContainer listStatus(String path) throws Exception {
		String spec = MessageFormat.format("{0}?op=LISTSTATUS&user.name={1}", path, this.knoxUser);
		logger.info("[KnoxWebHDFSConnection::listStatus] Knox hdfsUrl:" + knoxUrl);
		logger.info("[KnoxWebHDFSConnection::listStatus] Knox spec:" + spec);
		logger.info("[KnoxWebHDFSConnection::listStatus] URI:" + new URL(knoxUrl + spec).toURI());

		HttpGet get;
		try {
			get = new HttpGet(new URL(knoxUrl + spec).toURI());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		}
		HttpResponse response = getHttpClientKnox().execute(get, getHttpContext());

		if (response.getStatusLine().getStatusCode() >= 400 && response.getStatusLine().getStatusCode() < 500) {
			throw new FileNotFoundException(path);
		}
		if (response.getStatusLine().getStatusCode() >= 500) {
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			throw new Exception("Knox excpetion [" + response.getStatusLine().getStatusCode() + "] for path [" + path + "]. Message:[" + responseString + "]");
		}

		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		logger.info("[KnoxWebHDFSConnection::listStatus] responseString:" + responseString);

		FileStatusesContainer fs = mapper.readValue(responseString, FileStatusesContainer.class);

		logger.info("[KnoxWebHDFSConnection::listStatus] fs:" + fs);
		if (fs != null)
			logger.info("[KnoxWebHDFSConnection::listStatus] fs.getFileStatus():" + fs.getFileStatuses());

		return fs;
	}

	/**
	 * <b>GETFILESTATUS</b>
	 * 
	 * curl -i "http://<HOST>:<PORT>/<PATH>?op=GETFILESTATUS"
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public FileStatusContainer getFileStatus(String path) throws Exception {
		String spec = MessageFormat.format("{0}?op=GETFILESTATUS&user.name={1}", URLEncoder.encode(path, "UTF-8"), this.knoxUser);
		HttpGet get;
		try {
			get = new HttpGet(new URL(knoxUrl + spec).toURI());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		}
		HttpResponse response = getHttpClientKnox().execute(get, getHttpContext());

		if (response.getStatusLine().getStatusCode() >= 400 && response.getStatusLine().getStatusCode() < 400) {
			throw new FileNotFoundException(path);
		}
		if (response.getStatusLine().getStatusCode() >= 500) {
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			throw new Exception("Knox excpetion [" + response.getStatusLine().getStatusCode() + "] for path [" + path + "]. Message:[" + responseString + "]");
		}

		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");

		FileStatusContainer fs = mapper.readValue(responseString, FileStatusContainer.class);

		return fs;

	}

	/**
	 * <b>GETFILECHECKSUM</b>
	 * 
	 * curl -i "http://<HOST>:<PORT>/<PATH>?op=GETFILECHECKSUM"
	 * 
	 * @param path
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getFileCheckSum(String path) throws MalformedURLException, IOException {
		String spec = MessageFormat.format("{0}?op=GETFILECHECKSUM&user.name={1}", URLEncoder.encode(path, "UTF-8"), this.knoxUser);
		return genericGetForStringCall(spec);

	}

	private String genericGetForStringCall(String spec) throws MalformedURLException, IOException, ClientProtocolException {
		HttpGet get;
		CloseableHttpClient client = null;
		try {
			get = new HttpGet(new URL(knoxUrl + spec).toURI());
			client = getHttpClientKnox();
			HttpResponse response = client.execute(get, getHttpContext());
			return EntityUtils.toString(response.getEntity());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		} finally {
			if (client != null)
				client.close();
		}
	}

	private String genericPutForStringCall(String spec) throws ParseException, Exception {
		HttpPut put;
		CloseableHttpClient client = null;
		try {
			put = new HttpPut(new URL(knoxUrl + spec).toURI());
			client = getHttpClientKnox();
			HttpResponse response = client.execute(put, getHttpContext());
			logger.info("[KnoxWebHDFSConnection:genericPutForStringCall] - response = " + response);
			logger.info("[KnoxWebHDFSConnection:genericPutForStringCall] - getStatusLine = " + response.getStatusLine());
			logger.info("[KnoxWebHDFSConnection:genericPutForStringCall] - getStatusCode = " + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() > 299)
				throw new Exception(EntityUtils.toString(response.getEntity()));
			return EntityUtils.toString(response.getEntity());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		} finally {
			if (client != null)
				client.close();
		}
	}

	private String genericDeleteForStringCall(String spec) throws MalformedURLException, IOException, ClientProtocolException {
		HttpDelete delete;
		CloseableHttpClient client = null;
		try {
			delete = new HttpDelete(new URL(knoxUrl + spec).toURI());
			client = getHttpClientKnox(false);
			HttpResponse response = client.execute(delete, getHttpContext());
			return EntityUtils.toString(response.getEntity());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		} finally {
			if (client != null)
				client.close();
		}
	}

	/*
	 * ========================================================================
	 * PUT
	 * ========================================================================
	 */
	/**
	 * <b>CREATE</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=CREATE
	 * [&overwrite=<true|false>][&blocksize=<LONG>][&replication=<SHORT>]
	 * [&permission=<OCTAL>][&buffersize=<INT>]"
	 * 
	 * @param path
	 * @param is
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String create(String path, InputStream is) throws MalformedURLException, IOException {
		String resp = null;
		String spec = MessageFormat.format("{0}?op=CREATE&user.name={1}", URLEncoder.encode(path, "UTF-8"), this.knoxUser);

		logger.info("[KnoxWebHDFSConnection:create] - hdfsUrl = " + knoxUrl);
		logger.info("[KnoxWebHDFSConnection:create] - spec = " + spec);

		String redirectUrl = null;
		CloseableHttpClient client = null;
		HttpPut put;
		try {
			logger.info("[KnoxWebHDFSConnection:create] - hdfsUrl+spec = " + knoxUrl + spec);
			put = new HttpPut(new URL(knoxUrl + spec).toURI());
			client = getHttpClientKnox(true);
			HttpResponse response = client.execute(put, getHttpContext());

			if (response.getStatusLine().getStatusCode() == 307)
				redirectUrl = response.getFirstHeader("Location").getValue();

			if (redirectUrl != null) {
				HttpPut put2;
				CloseableHttpResponse response2 = null;
				try {
					put2 = new HttpPut(redirectUrl);
					InputStreamEntity entity = new InputStreamEntity(is, -1, ContentType.APPLICATION_OCTET_STREAM);

					entity.setContentType("binary/octet-stream");
					entity.setChunked(true);

					BufferedHttpEntity myEntity = null;
					try {
						myEntity = new BufferedHttpEntity(entity);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					put2.setEntity(myEntity);

					// put2.setEntity(entity);
					response2 = getHttpClientKnox().execute(put2, getHttpContext());
					resp = response2.getFirstHeader("Location").getValue();
				} finally {
					if (response2 != null)
						response2.close();
				}

			}

		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		} finally {
			client.close();
		}

		return resp;
	}

	/**
	 * <b>MKDIRS</b>
	 * 
	 * curl -i -X PUT
	 * "http://<HOST>:<PORT>/<PATH>?op=MKDIRS[&permission=<OCTAL>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 */
	public String mkdirs(String path) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=MKDIRS&user.name={1}", URLEncoder.encode(path, "UTF-8"), this.knoxUser);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>CREATESYMLINK</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=CREATESYMLINK
	 * &destination=<PATH>[&createParent=<true|false>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 */
	public String createSymLink(String srcPath, String destPath) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=CREATESYMLINK&destination={1}&user.name={2}", URLEncoder.encode(srcPath, "UTF-8"), URLEncoder.encode(destPath, "UTF-8"),
				this.knoxUser);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>RENAME</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=RENAME
	 * &destination=<PATH>[&createParent=<true|false>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 */
	public String rename(String srcPath, String destPath) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=RENAME&destination={1}&user.name={2}", URLEncoder.encode(srcPath, "UTF-8"), URLEncoder.encode(destPath, "UTF-8"), this.knoxUser);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>SETPERMISSION</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=SETPERMISSION
	 * [&permission=<OCTAL>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 */
	public String setPermission(String path, String permission) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=SETPERMISSION&permission={1}&user.name={2}", URLEncoder.encode(path, "UTF-8"), permission, this.knoxUser);
		logger.info("[KnoxWebHDFSConnection:setPermission] - spec = " + spec);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>SETOWNER</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=SETOWNER
	 * [&owner=<USER>][&group=<GROUP>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 */
	public String setOwner(String path, String owner, String group) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=SETOWNER&owner={1}&group={2}&user.name={3}", URLEncoder.encode(path, "UTF-8"), owner, group, this.knoxUser);
		logger.info("[setOwner] - spec = " + spec);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>SETREPLICATION</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=SETREPLICATION
	 * [&replication=<SHORT>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 */
	public String setReplication(String path) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=SETREPLICATION&user.name={1}", URLEncoder.encode(path, "UTF-8"), this.knoxUser);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>SETTIMES</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=SETTIMES
	 * [&modificationtime=<TIME>][&accesstime=<TIME>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 */
	public String setTimes(String path) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=SETTIMES&user.name={1}", URLEncoder.encode(path, "UTF-8"), this.knoxUser);
		return genericPutForStringCall(spec);
	}

	/*
	 * ========================================================================
	 * POST
	 * ========================================================================
	 */
	/**
	 * curl -i -X POST
	 * "http://<HOST>:<PORT>/<PATH>?op=APPEND[&buffersize=<INT>]"
	 * 
	 * @param path
	 * @param is
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String append(String path, InputStream is) throws MalformedURLException, IOException {
		String resp = null;
		// ensureValidToken();
		// String spec = MessageFormat.format("/{0}?op=APPEND&user.name={1}",
		// URLEncoder.encode(path, "UTF-8"), this.knoxUser);
		// String redirectUrl = null;
		// HttpURLConnection conn = authenticatedURL.openConnection(new URL(new
		// URL(hdfsUrl), spec), token);
		// conn.setRequestMethod("POST");
		// conn.setInstanceFollowRedirects(false);
		// conn.connect();
		// logger.info("Location:" + conn.getHeaderField("Location"));
		// resp = result(conn, true);
		// if (conn.getResponseCode() == 307)
		// redirectUrl = conn.getHeaderField("Location");
		// conn.disconnect();
		//
		// if (redirectUrl != null) {
		// conn = authenticatedURL.openConnection(new URL(redirectUrl), token);
		// conn.setRequestMethod("POST");
		// conn.setDoOutput(true);
		// conn.setDoInput(true);
		// conn.setUseCaches(false);
		// conn.setRequestProperty("Content-Type", "application/octet-stream");
		// // conn.setRequestProperty("Transfer-Encoding", "chunked");
		// final int _SIZE = is.available();
		// conn.setRequestProperty("Content-Length", "" + _SIZE);
		// conn.setFixedLengthStreamingMode(_SIZE);
		// conn.connect();
		// OutputStream os = conn.getOutputStream();
		// copy(is, os);
		// // Util.copyStream(is, os);
		// is.close();
		// os.close();
		// resp = result(conn, true);
		// conn.disconnect();
		// }

		return resp;
	}

	/*
	 * ========================================================================
	 * DELETE
	 * ========================================================================
	 */
	/**
	 * <b>DELETE</b>
	 * 
	 * curl -i -X DELETE "http://<host>:<port>/<path>?op=DELETE
	 * [&recursive=<true|false>]"
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public String delete(String path) throws MalformedURLException, IOException {
		String spec = MessageFormat.format("{0}?op=DELETE&user.name={1}", URLEncoder.encode(path, "UTF-8"), this.knoxUser);
		return genericDeleteForStringCall(spec);
	}

	private String getFinalDir(String subType, String organizationCode, String dataDomain, String codSubDomain, String vESlug) {
		String finalDir = "/" + hdfsRootdir + "/" + organizationCode.toUpperCase() + "/rawdata/" + dataDomain.toUpperCase() + "/";
		if ("bulkDataset".equals(subType))
			finalDir += "db_" + codSubDomain.toUpperCase();
		else
			finalDir += "so_" + vESlug;
		return finalDir;
	}

	private String getLastDir(String subType, String datasetCode, String streamCode) {
		if ("bulkDataset".equals(subType))
			return datasetCode;
		else
			return streamCode;
	}

	public String getHdfsDir(String organizationCode, BackofficeDettaglioStreamDatasetResponse datasource) {
		String vESlug = datasource.getStream() != null && datasource.getStream().getSmartobject() != null ? datasource.getStream().getSmartobject().getSlug() : null;
		String datasetSubtype = datasource.getDataset().getDatasetSubtype().getDatasetSubtype();
		String streamCode = datasource.getStream() != null ? datasource.getStream().getStreamcode() : null;

		logger.info("[HdfsDelegate::getHdfsDir] vESlug" + knoxUrl + " - datasetSubtype " + datasetSubtype + " - datasetcode: " + datasource.getDataset().getDatasetcode());
		return getFinalDir(datasetSubtype, organizationCode, datasource.getDomain().getDomaincode(), datasource.getSubdomain().getSubdomaincode(), vESlug) + "/"
				+ getLastDir(datasetSubtype, datasource.getDataset().getDatasetcode(), streamCode);
	}

}
