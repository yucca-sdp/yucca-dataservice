/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.model.output;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;

public class DatasetBulkInsertOutput {

	
	private InsertApiBaseException insertException=null;
	public InsertApiBaseException getInsertException() {
		return insertException;
	}

	public void setInsertException(InsertApiBaseException insertException) {
		this.insertException = insertException;
	}

	private List<DatasetBulkInsertIOperationReport> dataBLockreport=new ArrayList<DatasetBulkInsertIOperationReport>();
	
	private String globalRequestId=null;

	public List<DatasetBulkInsertIOperationReport> getDataBLockreport() {
		return dataBLockreport;
	}

	public void setDataBLockreport(List<DatasetBulkInsertIOperationReport> dataBLockreport) {
		this.dataBLockreport = dataBLockreport;
	}

	public String getGlobalRequestId() {
		return globalRequestId;
	}

	public void setGlobalRequestId(String globalRequestId) {
		this.globalRequestId = globalRequestId;
	}
	
}
