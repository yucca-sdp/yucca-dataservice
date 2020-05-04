/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

public class DcatRequest {
	
	private Integer idDcat; 
//	private Timestamp dcatdataupdate;
	private String dcatdataupdate;
	private String dcatnomeorg;
	private String dcatemailorg;	  
	private String dcatcreatorname;	
	private String dcatcreatortype;	
	private String dcatcreatorid;	
	private String dcatrightsholdername;		  	
	private String dcatrightsholdertype;			
	private String dcatrightsholderid;
	
	public Integer getIdDcat() {
		return idDcat;
	}
	public String getDcatdataupdate() {
		if(idDcat != null)return null;
		return dcatdataupdate;
	}
	public String getDcatnomeorg() {
		if(idDcat != null)return null;
		return dcatnomeorg;
	}
	public String getDcatemailorg() {
		if(idDcat != null)return null;
		return dcatemailorg;
	}
	public String getDcatcreatorname() {
		if(idDcat != null)return null;
		return dcatcreatorname;
	}
	public String getDcatcreatortype() {
		if(idDcat != null)return null;
		return dcatcreatortype;
	}
	public String getDcatcreatorid() {
		if(idDcat != null)return null;
		return dcatcreatorid;
	}
	public String getDcatrightsholdername() {
		if(idDcat != null)return null;
		return dcatrightsholdername;
	}
	public String getDcatrightsholdertype() {
		if(idDcat != null)return null;
		return dcatrightsholdertype;
	}
	public String getDcatrightsholderid() {
		if(idDcat != null)return null;
		return dcatrightsholderid;
	}
	public void setIdDcat(Integer idDcat) {
		this.idDcat = idDcat;
	}
	public void setDcatdataupdate(String dcatdataupdate) {
		this.dcatdataupdate = dcatdataupdate;
	}
	public void setDcatnomeorg(String dcatnomeorg) {
		this.dcatnomeorg = dcatnomeorg;
	}
	public void setDcatemailorg(String dcatemailorg) {
		this.dcatemailorg = dcatemailorg;
	}
	public void setDcatcreatorname(String dcatcreatorname) {
		this.dcatcreatorname = dcatcreatorname;
	}
	public void setDcatcreatortype(String dcatcreatortype) {
		this.dcatcreatortype = dcatcreatortype;
	}
	public void setDcatcreatorid(String dcatcreatorid) {
		this.dcatcreatorid = dcatcreatorid;
	}
	public void setDcatrightsholdername(String dcatrightsholdername) {
		this.dcatrightsholdername = dcatrightsholdername;
	}
	public void setDcatrightsholdertype(String dcatrightsholdertype) {
		this.dcatrightsholdertype = dcatrightsholdertype;
	}
	public void setDcatrightsholderid(String dcatrightsholderid) {
		this.dcatrightsholderid = dcatrightsholderid;
	}
}
