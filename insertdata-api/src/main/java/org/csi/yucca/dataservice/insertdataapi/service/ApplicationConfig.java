/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

@ApplicationPath("/")
public class ApplicationConfig extends Application {
	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	private Set<Object> singletons = new HashSet<Object>();

	public ApplicationConfig() {
		if (SDPInsertApiConfig.getInstance().isServiceEnable(SDPInsertApiConfig.ENABLE_STREAM_MASSIVE_SERVICE_HTTP)) {
			singletons.add(new StreamService());
			log.info("[II::II] ENABLE_STREAM_MASSIVE_SERVICE_HTTP ENABLED");
		}
		if (SDPInsertApiConfig.getInstance().isServiceEnable(SDPInsertApiConfig.ENABLE_DATA_MASSIVE_SERVICE_HTTP)) {
			singletons.add(new DatasetService());
			log.info("[II::II] ENABLE_DATA_MASSIVE_SERVICE_HTTP ENABLED");
		}
		if (SDPInsertApiConfig.getInstance().isServiceEnable(SDPInsertApiConfig.ENABLE_MEDIA_MASSIVE_SERVICE_HTTP)) {
			singletons.add(new MediaService());
			log.info("[II::II] ENABLE_MEDIA_MASSIVE_SERVICE_HTTP ENABLED");
		}

		if (SDPInsertApiConfig.getInstance().isServiceEnable(SDPInsertApiConfig.ENABLE_STREAM_VALIDATION_SERVICE_HTTP)) {
			singletons.add(new StreamValidationService());
			log.info("[II::II] ENABLE_STREAM_VALIDATION_SERVICE_HTTP ENABLED");
		}
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		// classes.add(AuthorizationInterceptor.class);
		return classes;
	}

}
