/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.csi.yucca.dataservice.metadataapi.delegate.security.SecurityDelegate;


@Provider
@PreMatching
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;
	
	@Context
	private HttpServletRequest httpRequest;
	
	//private ConfigurationContext configContext;
	//private String proxyHostname;
	//private int proxyPort;
	//private OAuth2TokenValidationServiceStub oAuth2TokenValidationServiceStub;


	@Override
	public void filter(ContainerRequestContext contReqCtx) throws IOException {

		try {
			

			if (httpRequest.getSession().getAttribute("userAuth") != null)
				httpRequest.getSession().removeAttribute("userAuth");
			
			String authorizedUser = SecurityDelegate.getInstance().validateTokenBearer(httpRequest);
			httpRequest.getSession().setAttribute("userAuth", authorizedUser);

//			String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
//
//			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//
//				String token = authorizationHeader.substring("Bearer".length()).trim();
//				if (token != "") {
//					//configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
//
//					//OAuth2TokenValidationRequestDTO dto = new OAuth2TokenValidationRequestDTO();
//					//OAuth2TokenValidationRequestDTO_OAuth2AccessToken tokenDto = new OAuth2TokenValidationRequestDTO_OAuth2AccessToken();
//
//					//tokenDto.setIdentifier(token);
//					//tokenDto.setTokenType("bearer");
//					//dto.setAccessToken(tokenDto);
//					//OAuth2TokenValidationRequestDTO_TokenValidationContextParam[] arrayCt = new OAuth2TokenValidationRequestDTO_TokenValidationContextParam[1];
//					//arrayCt[0] = new OAuth2TokenValidationRequestDTO_TokenValidationContextParam();
//					//dto.setContext(arrayCt);
//					//OAuth2TokenValidationResponseDTO response = getOAuth2TokenValidationServiceStub().validate(dto);
//					
//					
//					String authorizedUser = response.getAuthorizedUser();
//					boolean isValidUser = response.getValid();
//
//					if (isValidUser) {
//						httpRequest.getSession().setAttribute("userAuth", authorizedUser);
//					}
//				}
//			}

		} catch (Exception e) {
			contReqCtx.abortWith(Response.status(Status.UNAUTHORIZED).build());
		}

	}

//	public void init(FilterConfig filterConfig) throws ServletException {
//		try {
//			getOAuth2TokenValidationServiceStub();
//		} catch (AxisFault e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	private OAuth2TokenValidationServiceStub getOAuth2TokenValidationServiceStub() throws AxisFault {
//
//		if (oAuth2TokenValidationServiceStub == null) {
//			String oauthServerUrl = Config.getInstance().getOauthBaseUrl();
//			String oauthUsername = Config.getInstance().getOauthUsername();
//			String oauthPassword = Config.getInstance().getOauthPassword();
//
//			String oAuth2TokenValidationServiceEndPoint = oauthServerUrl + "/services/OAuth2TokenValidationService";
//
//			oAuth2TokenValidationServiceStub = new OAuth2TokenValidationServiceStub(configContext, oAuth2TokenValidationServiceEndPoint);
//			ServiceClient oauth2TokenValidationService = oAuth2TokenValidationServiceStub._getServiceClient();
//			Options optionOauth2Validation = oauth2TokenValidationService.getOptions();
//			setProxyToOptions(optionOauth2Validation, oauthUsername, oauthPassword);
//		}
//
//		return oAuth2TokenValidationServiceStub;
//	}
//
//	private void setProxyToOptions(Options option, String username, String password) {
//		/**
//		 * Setting a authenticated cookie that is received from Carbon server.
//		 * If you have authenticated with Carbon server earlier, you can use
//		 * that cookie, if it has not been expired
//		 */
//		option.setProperty(HTTPConstants.COOKIE_STRING, null);
//		/**
//		 * Setting proxy property if exists
//		 */
//		if (proxyHostname != null && !proxyHostname.trim().isEmpty()) {
//			HttpTransportProperties.ProxyProperties proxyProperties = new HttpTransportProperties.ProxyProperties();
//			proxyProperties.setProxyName(proxyHostname);
//			proxyProperties.setProxyPort(proxyPort);
//			option.setProperty(HTTPConstants.PROXY, proxyProperties);
//		}
//		/**
//		 * Setting basic auth headers for authentication for carbon server
//		 */
//		HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
//		auth.setUsername(username);
//		auth.setPassword(password);
//		auth.setPreemptiveAuthentication(true);
//		option.setProperty(HTTPConstants.AUTHENTICATE, auth);
//		option.setManageSession(true);
//	}


}
