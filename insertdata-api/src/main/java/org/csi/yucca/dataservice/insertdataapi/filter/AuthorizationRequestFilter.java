/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.filter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.integration.CacheLoader;
import org.csi.yucca.dataservice.insertdataapi.delegate.WebServiceDelegate;
import org.csi.yucca.dataservice.insertdataapi.model.output.ErrorOutput;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = { "/*" })
public class AuthorizationRequestFilter implements Filter {

	// private ConfigurationContext configContext;
	// private String proxyHostname;
	// private int proxyPort;
	// private OAuth2TokenValidationServiceStub
	// oAuth2TokenValidationServiceStub;

	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";

	private static final long RETRY_INTERVAL_DURATION = 10;
	private static final long EXPIRE_AFTER_WRITE_DURATION = 1;
	private static final long RESILIENCE_DURATION = 24;
	private static final boolean REFRESH_AHEAD = true;
	private static final TimeUnit EXPIRE_TIME_UNIT = TimeUnit.HOURS;
	private static final TimeUnit RESILIENCE_TIME_UNIT = TimeUnit.HOURS;

	private static Cache<TenantCredentialCacheKey, Boolean> tenantCredentialCache;

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	static {
		tenantCredentialCache = new Cache2kBuilder<TenantCredentialCacheKey, Boolean>() {
		}.resilienceDuration(RESILIENCE_DURATION, RESILIENCE_TIME_UNIT).expireAfterWrite(EXPIRE_AFTER_WRITE_DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.retryInterval(RETRY_INTERVAL_DURATION, TimeUnit.SECONDS).permitNullValues(false).loader(new CacheLoader<TenantCredentialCacheKey, Boolean>() {

					@Override
					public Boolean load(TenantCredentialCacheKey key) throws Exception {
						log.info(" [tenantCredentialCache] LOADER CALLED ");
						return verifyBasicAuth(key.getTenantCode(), key.getEncodedUserPassword());

					};
				}).build();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	//
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		log.debug(
				"[AuthorizationRequestFilter::verifyBasicAuth] - START " + SDPInsertApiConfig.getInstance().isServiceEnable(SDPInsertApiConfig.ENABLE_AUTHENTICATION_FILTER_HTTP));

		if (SDPInsertApiConfig.getInstance().isServiceEnable(SDPInsertApiConfig.ENABLE_AUTHENTICATION_FILTER_HTTP)) {
			String authHeader = httpRequest.getHeader(AUTHORIZATION_PROPERTY);
			String tenantCode = null;

			// log.info("[AuthorizationRequestFilter::verifyBasicAuth] -
			// pathInfo " + httpRequest.getPathInfo());
			String[] path = httpRequest.getPathInfo().split("/");
			if (path.length > 1 && path[path.length - 2].equals("input")) {
				tenantCode = path[path.length - 1];
			}
			// log.info("[AuthorizationRequestFilter::verifyBasicAuth] -
			// tenantCode " + tenantCode);
			if (authHeader != null) {
				final String encodedUserPassword = authHeader.replaceFirst(AUTHENTICATION_SCHEME + " ", "");
				// log.info("[AuthorizationRequestFilter::verifyBasicAuth] -
				// encodedUserPassword " + encodedUserPassword);

				try {
					// boolean isValidUser = verifyBasicAuth(tenantCode,
					// encodedUserPassword);
					long start = System.currentTimeMillis();
					boolean isValidUser = tenantCredentialCache.get(new TenantCredentialCacheKey(tenantCode, encodedUserPassword));
					log.info("[AuthorizationRequestFilter::verifyBasicAuth] - tenantCode " + tenantCode + " - isValidUser " + isValidUser + " elapsed ["
							+ (System.currentTimeMillis() - start) + "]");

					if (isValidUser)
						filterChain.doFilter(httpRequest, servletResponse);
					else {
						ErrorOutput authError = new ErrorOutput("Authentication Failed", "E002", "NONE", null);
						((HttpServletResponse) servletResponse).setHeader("Content-Type", "application/json");
						((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
						servletResponse.getOutputStream().write(authError.toJson().getBytes());
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.error("[AuthorizationRequestFilter::doFilter] error unexpected--> ", e);
					ErrorOutput authError = new ErrorOutput("unknown error", "", "NONE", null);
					((HttpServletResponse) servletResponse).setHeader("Content-Type", "application/json");
					((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					servletResponse.getOutputStream().write(authError.toJson().getBytes());

					//
					// ((HttpServletResponse)
					// servletResponse).setContentType("application/json");
					//
					// ((HttpServletResponse)
					// servletResponse).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					// "{\"error_name\": \"Internal Server Error\",
					// \"error_code\": \"unkown error\", \"output\":
					// \"NONE\"}");
					// ((HttpServletResponse)
					// servletResponse).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}

			} else {
				ErrorOutput authError = new ErrorOutput("Authentication Failed", "E002", "NONE", null);
				((HttpServletResponse) servletResponse).setHeader("Content-Type", "application/json");
				((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
				servletResponse.getOutputStream().write(authError.toJson().getBytes());
			}
		} else
			filterChain.doFilter(httpRequest, servletResponse);
	}

	private static boolean verifyBasicAuth(String tenantCode, String credentials)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, ParserConfigurationException, SAXException {

		log.debug("[AuthorizationRequestFilter::verifyBasicAuth] - START");

		boolean isValidUser = false;
		try {
			String decodedCredentials = new String(Base64.decodeBase64(credentials.getBytes()));
			String userName = decodedCredentials.split(":")[0];
			String password = decodedCredentials.split(":")[1];

			if (("" + tenantCode).equals(userName) || tenantCode == null) { // tenatCode
																			// dalla
																			// url
																			// e
				// verificare se servirà

				String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.ws.um.carbon.wso2.org\">";
				xmlInput += "   <soapenv:Header/>";
				xmlInput += "   <soapenv:Body>";
				xmlInput += "      <ser:authenticate>";
				xmlInput += "		<ser:userName>" + userName + "</ser:userName>";
				xmlInput += "         <ser:credential>" + password + "</ser:credential>";
				xmlInput += "      </ser:authenticate>";
				xmlInput += "   </soapenv:Body>";
				xmlInput += "</soapenv:Envelope>";

				String SOAPAction = "authenticate";
				// log.info("[AuthorizationRequestFilter::verifyBasicAuth] -
				// webserviceUrl: " +
				// SDPInsertApiConfig.getInstance().getRbacUserStoreWebserviceUrl());

				String webserviceUrl = SDPInsertApiConfig.getInstance().getRbacUserStoreWebserviceUrl();
				String rbacUser = SDPInsertApiConfig.getInstance().getRbacWebserviceUser();
				String rbacPassword = SDPInsertApiConfig.getInstance().getRbacWebservicePassword();
				String webServiceResponse = WebServiceDelegate.callWebService(webserviceUrl, rbacUser, rbacPassword, xmlInput, SOAPAction, "text/xml");

				// log.info("[AuthorizationRequestFilter::verifyBasicAuth] -
				// webServiceResponse: " + webServiceResponse);

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();

				InputSource is = new InputSource(new StringReader(webServiceResponse));
				Document doc = db.parse(is);

				Node resultNode = doc.getFirstChild().getFirstChild().getFirstChild().getFirstChild();

				String nodeValue = resultNode.getTextContent();
				isValidUser = "true".equals(nodeValue);
			} else {
				log.warn("Richiamata API per tenant:[" + tenantCode + "] con utente:[" + userName + "]");
				isValidUser = false;
			}

		} finally {
			log.debug("[AuthorizationRequestFilter::verifyBasicAuth] - END");
		}
		return isValidUser;
	}

	class TenantCredentialCacheKey implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private String tenantCode, encodedUserPassword;

		
		public TenantCredentialCacheKey() {
			super();
		}

		public TenantCredentialCacheKey(String tenantCode, String encodedUserPassword) {
			super();
			this.tenantCode = tenantCode;
			this.encodedUserPassword = encodedUserPassword;
		}

		public String getTenantCode() {
			return tenantCode;
		}

		public void setTenantCode(String tenantCode) {
			this.tenantCode = tenantCode;
		}

		public String getEncodedUserPassword() {
			return encodedUserPassword;
		}

		public void setEncodedUserPassword(String encodedUserPassword) {
			this.encodedUserPassword = encodedUserPassword;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((encodedUserPassword == null) ? 0 : encodedUserPassword.hashCode());
			result = prime * result + ((tenantCode == null) ? 0 : tenantCode.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TenantCredentialCacheKey other = (TenantCredentialCacheKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (encodedUserPassword == null) {
				if (other.encodedUserPassword != null)
					return false;
			} else if (!encodedUserPassword.equals(other.encodedUserPassword))
				return false;
			if (tenantCode == null) {
				if (other.tenantCode != null)
					return false;
			} else if (!tenantCode.equals(other.tenantCode))
				return false;
			return true;
		}

		private AuthorizationRequestFilter getOuterType() {
			return AuthorizationRequestFilter.this;
		};
		
		

	}

}