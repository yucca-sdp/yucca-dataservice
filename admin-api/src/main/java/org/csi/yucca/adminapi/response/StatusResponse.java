/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.IStatus;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class StatusResponse extends Response{
	
	private Integer idStatus;
	private String statuscode;
	private String descriptionStatus;
	
	public StatusResponse() {
		super();
	}
	
	public StatusResponse(IStatus iStatusImpl) {
		super();
		if (iStatusImpl != null) {
			this.idStatus = iStatusImpl.getIdStatus();
			this.statuscode = iStatusImpl.getStatusCode();				
			this.descriptionStatus = iStatusImpl.getStatusDescription();			
		}
	}
	
	public StatusResponse(DettaglioSmartobject smartobject) {
		super();
		this.idStatus = smartobject.getIdStatus();
		this.statuscode = smartobject.getStatuscode();
		this.descriptionStatus = smartobject.getDescriptionStatus();
	}
	
	public Integer getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}
	public String getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}
	public String getDescriptionStatus() {
		return descriptionStatus;
	}
	public void setDescriptionStatus(String descriptionStatus) {
		this.descriptionStatus = descriptionStatus;
	}
	
}
