/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

public class OpenDataRequest {

	private String opendataauthor;
    private String opendataupdatedate;
    private String opendatalanguage;
    private String lastupdate;
    private String fabriccontrolleroutcome;
    private String opendataupdatefrequency;
    
	public String getOpendataupdatedate() {
		return opendataupdatedate;
	}
	public void setOpendataupdatedate(String opendataupdatedate) {
		this.opendataupdatedate = opendataupdatedate;
	}
	public String getOpendataauthor() {
		return opendataauthor;
	}
	public void setOpendataauthor(String opendataauthor) {
		this.opendataauthor = opendataauthor;
	}
	public String getOpendatalanguage() {
		return opendatalanguage;
	}
	public void setOpendatalanguage(String opendatalanguage) {
		this.opendatalanguage = opendatalanguage;
	}
	public String getLastupdate() {
		return lastupdate;
	}
	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}
	public String getFabriccontrolleroutcome() {
		return fabriccontrolleroutcome;
	}
	public void setFabriccontrolleroutcome(String fabriccontrolleroutcome) {
		this.fabriccontrolleroutcome = fabriccontrolleroutcome;
	}
	public String getOpendataupdatefrequency() {
		return opendataupdatefrequency;
	}
	public void setOpendataupdatefrequency(String opendataupdatefrequency) {
		this.opendataupdatefrequency = opendataupdatefrequency;
	}
	
    

}
