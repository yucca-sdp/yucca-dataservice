/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

public class ActionOozieRequest {

	private String action;
	private String operationCode;
	private String eleIds;
	private Boolean addBdaInfo;
	private Boolean addBdaUniqueId;
	private String prjName;
	private String tenantCode;
	private String toWrite;
	private Integer makeHiveToCsv;
	
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public String getEleIds() {
		return eleIds;
	}
	public void setEleIds(String eleIds) {
		this.eleIds = eleIds;
	}
	public Boolean getAddBdaInfo() {
		return addBdaInfo;
	}
	public void setAddBdaInfo(Boolean addBdaInfo) {
		this.addBdaInfo = addBdaInfo;
	}
	public Boolean getAddBdaUniqueId() {
		return addBdaUniqueId;
	}
	public void setAddBdaUniqueId(Boolean addBdaUniqueId) {
		this.addBdaUniqueId = addBdaUniqueId;
	}
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	public String getTenantCode() {
		return tenantCode;
	}
	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}
	public String getToWrite() {
		return toWrite;
	}
	public void setToWrite(String toWrite) {
		this.toWrite = toWrite;
	}
	public Integer getMakeHiveToCsv() {
		return makeHiveToCsv;
	}
	public void setMakeHiveToCsv(Integer makeHiveTOCsv) {
		this.makeHiveToCsv = makeHiveTOCsv;
	}
	


	

	
	
}
