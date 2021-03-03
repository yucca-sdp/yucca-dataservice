/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface TechnicalService {
	
	ServiceResponse selectDatasetSubtype(String datasetTypeCode, String sort) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetType(String sort) throws BadRequestException, NotFoundException, Exception;
}
