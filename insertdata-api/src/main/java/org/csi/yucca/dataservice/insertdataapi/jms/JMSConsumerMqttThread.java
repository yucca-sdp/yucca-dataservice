/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.jms;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.metadata.SDPInsertMedataFactory;
import org.csi.yucca.dataservice.insertdataapi.metadata.SDPInsertMetadataApiAccess;

public class JMSConsumerMqttThread implements Runnable, ExceptionListener {
	public static final String VIRTUAL_QUEUE_CONSUMER_INSERTAPI_INPUT = "VirtualQueueConsumer.esbin.input";

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	private Connection connectionExternal;
	private Map<String, JMSTenant> jmsTenants = new ConcurrentHashMap<String, JMSTenant>();
	private PooledConnectionFactory connectionFactoryExternal;

	public void run() {

		try {
			log.info("[JMSConsumerMqttThread::run] Starting connection...");



			// external Connection
			connectionFactoryExternal = JMSConnectionUtils.createExternalActiveMQConnection();

			 //Create a Connection external
			 connectionExternal =
			 connectionFactoryExternal.createConnection();
			 connectionExternal.start();
			 connectionExternal.setExceptionListener(this);

			while (true) {
				log.info("[JMSConsumerMqttThread::run] Get tenant list and update sessions...");
				SDPInsertMetadataApiAccess metadataAccess = SDPInsertMedataFactory.getSDPInsertMetadataApiAccess();
				Set<String> tenants=null;
				try {
					tenants = metadataAccess.getTenantList();
					Iterator<String> iter = jmsTenants.keySet().iterator();
					while (iter.hasNext()) {
						String oldTenant = (String) iter.next();
						if (!tenants.contains(oldTenant)) {
							jmsTenants.get(oldTenant).closeAll();
							jmsTenants.remove(oldTenant);
							log.info("[JMSConsumerMqttThread::run] Disconnected for tenant:" + oldTenant);
						}
					}
					iter = tenants.iterator();
					while (iter.hasNext()) {
						String newTenant = (String) iter.next();
						if (!jmsTenants.containsKey(newTenant)) // new tenant!
						{

							Session sessionConsumer = connectionExternal.createSession(false, Session.AUTO_ACKNOWLEDGE);

							// Consumer
							Destination destinationConsumer = sessionConsumer.createQueue(VIRTUAL_QUEUE_CONSUMER_INSERTAPI_INPUT + "." + newTenant);
							log.info("[JMSConsumerMqttThread::run] Connected to queue:" + destinationConsumer.toString());
							MessageConsumer consumer = sessionConsumer.createConsumer(destinationConsumer);

							consumer.setMessageListener(new JMSMqttMessageListener(newTenant));

							JMSTenant jmsTenant = new JMSTenant(sessionConsumer, consumer);
							jmsTenants.put(newTenant, jmsTenant);
						}
					}
				} catch (Exception e) {
					log.error("[JMSConsumerMqttThread::run] Error reading tenant list... continue with old list", e);
				}
				if (tenants== null || tenants.isEmpty()) {
					log.info("[JMSConsumerMqttThread::run] Tenants list is empty!! retry after 10 sec");
					Thread.sleep(10 * 1000);
					}
				else
					Thread.sleep(5 * 60 * 1000);
			}

		} catch (InterruptedException ie) {
			log.warn("[JMSConsumerMqttThread::run] JMSConsumerMqttThread shutdown");
			closing();
		} catch (Exception e) {
			log.error("[JMSConsumerMqttThread::run] Error on Starting connection..." + e.getMessage(), e);

		}
	}

	private void closing() {
		log.info("[JMSConsumerMqttThread::run] Closing connection...");
		try {
			Iterator<JMSTenant> iter = jmsTenants.values().iterator();
			while (iter.hasNext()) {
				iter.next().closeAll();
			}

			connectionExternal.close();
			connectionFactoryExternal.stop();
		} catch (JMSException e) {
			log.error("[JMSConsumerMqttThread::run] Error on Closing connection..." + e.getMessage(), e);
		}
		log.info("[JMSConsumerMqttThread::run] Closed");

	}

	public synchronized void onException(JMSException ex) {
		System.out.println("JMS Exception occured. Shutting down client.");
	}

}
