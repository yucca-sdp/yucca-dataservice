/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.insertapi.config;

import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class KafkaConfig {

    private final Properties properties;

    /**
     * Create the kafka config
     *
     * @param kafkaBrokers    : the kafka brokers are mandatory. See {@link org.apache.kafka.clients.CommonClientConfigs#BOOTSTRAP_SERVERS_DOC BOOTSTRAP_SERVERS_DOC}
     * @param otherProperties : otherProperties is optional an allow to overwrite the default configuration except
     *                          key.serializer and value.serializer
     */
    public KafkaConfig(String kafkaBrokers, Properties otherProperties) {
        String clientIdDefault = "insert_api_"+System.currentTimeMillis();
        properties = new Properties();
        properties.put(ACKS_CONFIG, "-1");  // all: the leader will wait for the full set of in-sync replicas (ISRs) to acknowledge the record
        properties.put(CLIENT_ID_CONFIG,clientIdDefault);
        properties.putAll(otherProperties);
        properties.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        properties.put(BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
    }

    public KafkaConfig(String kafkaBrokers) {
        this(kafkaBrokers, new Properties());
    }

    public Properties getProperties() {
        return properties;
    }

}
