/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.kafka;

import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;
import org.csi.yucca.insertapi.config.DarwinConfig;
import org.csi.yucca.insertapi.config.KafkaConfig;
import org.csi.yucca.insertapi.producer.KafkaWriterImpl;

public class KafkaWriterImplSingleton {
	
	 private static KafkaWriterImpl instance = null;
	 
	 private KafkaWriterImplSingleton() {};
	 
	  public static KafkaWriterImpl getInstance() {
	    if(instance==null) {
	    	instance = new KafkaWriterImpl();
	    	KafkaConfig kafkaConfig = new KafkaConfig(SDPInsertApiConfig.getInstance().getKafkaBrokers());
			DarwinConfig darwinConfig = new DarwinConfig(
				SDPInsertApiConfig.getInstance().getDarwinConfigHost(), 
				SDPInsertApiConfig.getInstance().getDarwinConfigDatabase(), 
				SDPInsertApiConfig.getInstance().getDarwinConfigUser(), 
				SDPInsertApiConfig.getInstance().getDarwinConfigPassword()
			);
			instance.initialize(kafkaConfig, darwinConfig);
	    }
	    return instance;
	  }
}
