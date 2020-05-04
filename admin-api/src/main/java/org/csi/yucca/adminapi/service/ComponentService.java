/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.DataTypeRequest;
import org.csi.yucca.adminapi.request.MeasureUnitRequest;
import org.csi.yucca.adminapi.request.PhenomenonRequest;
import org.csi.yucca.adminapi.request.ToolRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface ComponentService {
	
	ServiceResponse selectDataType(Integer idDataType) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectMeasureUnit(Integer idMeasureUnit) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectPhenomenon(Integer idPhenomenon) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deletePhenomenon(Integer idPhenomenon) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse updatePhenomenon(PhenomenonRequest phenomenonRequest, Integer idPhenomenon) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertPhenomenon(PhenomenonRequest phenomenonRequest) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deleteMeasureUnit(Integer idMeasureUnit) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse updateMeasureUnit(MeasureUnitRequest measureUnitRequest, Integer idMeasureUnit) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertMeasureUnit(MeasureUnitRequest measureUnitRequest) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deleteDataType(Integer idDataType) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse updateDataType(DataTypeRequest dataTypeRequest, Integer idDataType) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertDataType(DataTypeRequest dataTypeRequest) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectDataType(String sort) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectMeasureUnit(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectPhenomenon(String sort) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectAllTools(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertTool(ToolRequest toolRequest) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deleteTool(Integer idTool) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse updateTool(ToolRequest toolRequest, Integer idTool) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectTool(Integer idTool) throws BadRequestException, NotFoundException, Exception;
}