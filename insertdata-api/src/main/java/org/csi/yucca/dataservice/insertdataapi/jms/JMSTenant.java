/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class JMSTenant {

	private Session sessionConsumer;
	private MessageConsumer consumer;

	public JMSTenant() {
		super();
	}

	public JMSTenant(Session sessionConsumer, MessageConsumer consumer) {
		super();
		this.setSessionConsumer(sessionConsumer);
		this.consumer = consumer;
	}

	public void closeAll() throws JMSException {
		if (getSessionConsumer() != null)
			getSessionConsumer().close();
		if (getConsumer() != null)
			getConsumer().close();

	}

	public MessageConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(MessageConsumer consumer) {
		this.consumer = consumer;
	}

	public Session getSessionConsumer() {
		return sessionConsumer;
	}

	public void setSessionConsumer(Session sessionConsumer) {
		this.sessionConsumer = sessionConsumer;
	}

}
