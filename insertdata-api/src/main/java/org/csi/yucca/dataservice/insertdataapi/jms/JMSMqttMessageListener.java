/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.BytesMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.service.StreamValidationService;

public class JMSMqttMessageListener implements MessageListener {
	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	private static StreamValidationService streamValidationService = new StreamValidationService();
	private final String codTenant;

	public JMSMqttMessageListener(String codTenant) {
		this.codTenant = codTenant;
	}

	public void onMessage(Message message) {

		try {
			if (message instanceof BytesMessage) {
				BytesMessage byteMessage = (BytesMessage) message;
				log.debug("[JMSMqttMessageListener::onMessage]  JMSListener=[" + codTenant + "] -> msg" + byteMessage);
				try {
					byte[] byteData = null;
					byteData = new byte[(int) byteMessage.getBodyLength()];
					byteMessage.readBytes(byteData);
					byteMessage.reset();
					String stringMessage = new String(byteData);
					log.debug("[JMSMqttMessageListener::onMessage]  stringMessage " + stringMessage);
					String forwardFor = "";
					if (message.getJMSReplyTo() != null)
						forwardFor = "" + message.getJMSReplyTo();
					JMSMqttMessageListener.streamValidationService.dataInsertMQTT(stringMessage, codTenant, message.getJMSMessageID(), forwardFor, "");
				} catch (InsertApiBaseException e) {
					e.printStackTrace();
					log.warn("[JMSMqttMessageListener::onMessage]  Invalid message for JMS [" + e.getErrorCode() + "]: " + e.getErrorName());
				} catch (InsertApiRuntimeException e) {
					log.error("[JMSMqttMessageListener::onMessage]  System error for JMS", e);
					throw e;
				}
			} else {
				log.warn("[JMSMqttMessageListener::onMessage]  No BytesMessage " + message);
			}
		} catch (JMSException e) {
			log.error("[JMSMqttMessageListener::onMessage]  textMessage problem", e);
		}

	}

}
