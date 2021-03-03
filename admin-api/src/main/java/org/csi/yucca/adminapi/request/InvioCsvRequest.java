/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class InvioCsvRequest {

	private String datasetCode;
	
	private Integer datasetVersion;
	
	private List<String> values;

	public InvioCsvRequest datasetCode(String datasetCode){
		this.datasetCode = datasetCode;
		return this;
	}
	public InvioCsvRequest datasetVersion(Integer datasetVersion){
		this.datasetVersion = datasetVersion;
		return this;
	}
	public InvioCsvRequest values(List<String> values){
		this.values = values;
		return this;
	}

	public String getDatasetCode() {
		return datasetCode;
	}

	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}

	public Integer getDatasetVersion() {
		return datasetVersion;
	}

	public void setDatasetVersion(Integer datasetVersion) {
		this.datasetVersion = datasetVersion;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	} 
	
	@Override
	public String toString() {
	
		String valueString = "";
		if (values!=null) 
			valueString = StringUtils.join(values, ",");
		return "{\"datasetCode\":\""+datasetCode+"\",\"datasetVersion\":"+datasetVersion+",\"values\":["+
				valueString	+"]}";
	}
	
}
