/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.util.NamedThreadFactory;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

@WebListener
public class JMSContextListener implements ServletContextListener {
	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");
	private ExecutorService exServiceMain = null;
	private ExecutorService exServiceMqtt = null;

	public void contextInitialized(ServletContextEvent arg0) {
		log.info("[JMSContextListener::contextInitialized]");
		if (SDPInsertApiConfig.getInstance().isServiceEnable(SDPInsertApiConfig.ENABLE_STREAM_REALTIME_SERVICE_JMS)) {
			log.info("[II::II] ENABLE_STREAM_REALTIME_SERVICE_JMS ENABLED");
			exServiceMain = Executors.newSingleThreadExecutor(new NamedThreadFactory("JMS-Context-Listener"));
			exServiceMain.execute(new JMSConsumerMainThread());
		} 
		if (SDPInsertApiConfig.getInstance().isServiceEnable(SDPInsertApiConfig.ENABLE_STREAM_VALIDATION_SERVICE_JMS)) {
			log.info("[II::II] ENABLE_STREAM_VALIDATION_SERVICE_JMS ENABLED");
			exServiceMqtt = Executors.newSingleThreadExecutor(new NamedThreadFactory("JMS-Context-Listener"));
			exServiceMqtt.execute(new JMSConsumerMqttThread());

		}
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("[JMSContextListener::contextDestroyed]");
		if (exServiceMain != null)
			exServiceMain.shutdownNow();
		if (exServiceMqtt != null)
			exServiceMqtt.shutdownNow();
	}

}
