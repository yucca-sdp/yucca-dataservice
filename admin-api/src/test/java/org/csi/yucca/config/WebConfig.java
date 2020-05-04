/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.config;

import org.csi.yucca.adminapi.controller.v1.PublicController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

	@Bean
	public PublicController publicController() {
		return new PublicController();
	}

}
