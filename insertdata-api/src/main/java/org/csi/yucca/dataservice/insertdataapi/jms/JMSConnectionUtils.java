/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class JMSConnectionUtils {


	public static PooledConnectionFactory createInternalActiveMQConnection() {
		return 	JMSConnectionUtils.createConnection(SDPInsertApiConfig.getInstance().getJMSMbInternalUrl(), SDPInsertApiConfig.getInstance()
				.getJMSMbExternalUsername(), SDPInsertApiConfig.getInstance().getJMSMbInternalPassword(), 2000, 1000L, 3., true, 1800L, 100);
	}

	public static PooledConnectionFactory createExternalActiveMQConnection() {
		return 	JMSConnectionUtils.createConnection(SDPInsertApiConfig.getInstance().getJMSMbExternalUrl(), SDPInsertApiConfig.getInstance()
				.getJMSMbExternalUsername(), SDPInsertApiConfig.getInstance().getJMSMbExternalPassword(), 2000);
	}

	private static PooledConnectionFactory createConnection(String url, String username, String password, Integer maxThreadPoolSize) {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

		if (maxThreadPoolSize != null)
			connectionFactory.setMaxThreadPoolSize(maxThreadPoolSize);

		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);

		return new PooledConnectionFactory(connectionFactory);
	}

	private static PooledConnectionFactory createConnection(String url, String username, String password, Integer maxThreadPoolSize, Long initialRedeliveryDelay, Double backOffMultiplier,
			Boolean useExponentialBackOff, Long maximumRedeliveryDelay, Integer maximumRedeliveries) {

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

		if (maxThreadPoolSize != null)
			connectionFactory.setMaxThreadPoolSize(maxThreadPoolSize);

		RedeliveryPolicy policy = new RedeliveryPolicy();
		if (initialRedeliveryDelay != null)
			policy.setInitialRedeliveryDelay(initialRedeliveryDelay);
		if (backOffMultiplier != null)
			policy.setBackOffMultiplier(backOffMultiplier);
		if (useExponentialBackOff != null)
			policy.setUseExponentialBackOff(useExponentialBackOff);
		if (maximumRedeliveryDelay != null)
			policy.setMaximumRedeliveryDelay(maximumRedeliveryDelay);
		if (maximumRedeliveries != null)
			policy.setMaximumRedeliveries(maximumRedeliveries);

		connectionFactory.setRedeliveryPolicy(policy);
		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);

		return new PooledConnectionFactory(connectionFactory);
	}


}
