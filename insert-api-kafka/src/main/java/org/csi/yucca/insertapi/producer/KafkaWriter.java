/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.insertapi.producer;

import org.csi.yucca.insertapi.config.DarwinConfig;
import org.csi.yucca.insertapi.config.KafkaConfig;

import java.util.List;

interface KafkaWriter {

    /**
     * Initialize the kafka writer. There are two type of configurations:
     *
     * @param kafkaConfig  : the configuration to write on kafka
     * @param darwinConfig : the configuration to register the avro schema on darwin
     */
    void initialize(KafkaConfig kafkaConfig, DarwinConfig darwinConfig);

    /**
     * Generate the kafka message and write it on the topic sdp_{@tenantCode}.
     * The kafka message is on avro format.
     *
     * @param tenantCode     : define the topic to write on the kafka topic
     * @param datasetCode    : it will be used to define a partition on delta
     * @param datasetVersion : it will be used to define a partition on delta
     * @param solrCollection ; define the collection to store on solr
     * @param docJson        : define the data to store on solr
     * @throws Exception if the message isn't written.
     */
    void send(String tenantCode, String datasetCode, int datasetVersion, String solrCollection, String docJson) throws Exception;



    /**
     * Generate the kafka message and write it on the topic sdp_{@tenantCode}.
     * The kafka message is on avro format.
     *
     * @param tenantCode     : define the topic to write on the kafka topic
     * @param datasetCode    : it will be used to define a partition on delta
     * @param datasetVersion : it will be used to define a partition on delta
     * @param solrCollection ; define the collection to store on solr
     * @param docJson        : define the data to store on solr. It's a list of json.
     * @throws Exception if the message isn't written.
     */
    void send(String tenantCode, String datasetCode, int datasetVersion, String solrCollection, List<String> docJson) throws Exception;
}



