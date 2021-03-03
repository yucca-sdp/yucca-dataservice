/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.insertapi.producer;

import it.agilelab.darwin.manager.AvroSchemaManager;
import it.agilelab.darwin.manager.AvroSchemaManagerFactory;
import org.csi.yucca.insertapi.config.DarwinConfig;
import org.csi.yucca.insertapi.config.KafkaConfig;
import org.csi.yucca.insertapi.models.YuccaMessage;
import org.apache.avro.Schema;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import scala.collection.immutable.$colon$colon;
import scala.collection.immutable.List;
import scala.collection.immutable.List$;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Future;

public class KafkaWriterImpl implements KafkaWriter {

    private AvroSchemaManager darwin;
    private Producer<byte[], byte[]> producer;


    public void initialize(KafkaConfig kafkaConfig, DarwinConfig darwinConfig) {
        this.producer = new KafkaProducer<>(kafkaConfig.getProperties());
        this.darwin = AvroSchemaManagerFactory.initialize(darwinConfig.getConfig());
        List<Schema> listScala = List$.MODULE$.empty();
        listScala = new $colon$colon<>(YuccaMessage.SCHEMA$, listScala);
        darwin.registerAll(listScala);
    }

    protected String getTopicNameByTenant(String tenantCode) throws Exception {
        String possibleCharactersForTenantCode = "[-_a-z0-9]+";

        if (!tenantCode.matches(possibleCharactersForTenantCode))
            throw new Exception("The tenant code '" + tenantCode + "' contains characters not supported.");
        else
            return "sdp_" + tenantCode;
    }

    private byte[] toAvro(YuccaMessage message) throws IOException {
        SpecificDatumWriter<YuccaMessage> writer = new SpecificDatumWriter<>(YuccaMessage.SCHEMA$);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        writer.write(message, encoder);
        encoder.flush();
        outputStream.flush();
        return outputStream.toByteArray();
    }


    public void send(String tenant, String datasetCode, int datasetVersion, String solrCollection, String docJson) throws Exception {
        String topic = getTopicNameByTenant(tenant);
        YuccaMessage message = new YuccaMessage(datasetCode, datasetVersion, solrCollection, 0, ByteBuffer.wrap(docJson.getBytes(StandardCharsets.UTF_8)), "", "");
        byte[] avro = toAvro(message);
        byte[] avroManagedByDarwin = darwin.generateAvroSingleObjectEncoded(avro, YuccaMessage.SCHEMA$);
        Future<RecordMetadata> future = producer.send(new ProducerRecord<byte[], byte[]>(topic, avroManagedByDarwin));
        producer.flush();
        future.get();
    }

    public void send(String tenant, String datasetCode, int datasetVersion, String solrCollection, java.util.List<String> docJsons) throws Exception {
        String topic = getTopicNameByTenant(tenant);
        java.util.List<Future<RecordMetadata>> futures = new ArrayList<>();
        for (String docJson : docJsons) {
            YuccaMessage message = new YuccaMessage(datasetCode, datasetVersion, solrCollection, 0, ByteBuffer.wrap(docJson.getBytes(StandardCharsets.UTF_8)), "", "");
            byte[] avro = toAvro(message);
            byte[] avroManagedByDarwin = darwin.generateAvroSingleObjectEncoded(avro, YuccaMessage.SCHEMA$);
            futures.add(producer.send(new ProducerRecord<byte[], byte[]>(topic, avroManagedByDarwin)));
        }
        producer.flush();
        for (Future<RecordMetadata> future : futures) {
            future.get();
        }
    }

}



