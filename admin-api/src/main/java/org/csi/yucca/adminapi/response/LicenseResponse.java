/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.License;
import org.csi.yucca.adminapi.model.LicenseJson;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.Util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown=true)
public class LicenseResponse extends Response{
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer idLicense;
	
	private String licensecode;
	private String description;


	public LicenseResponse() {
		super();
	}

	public LicenseResponse(String licenseJsonString) {
		super(); 
		
		LicenseJson licenseJson =  null;
		try {
			licenseJson = Util.getFromJsonString(licenseJsonString, LicenseJson.class);	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(licenseJson != null){
			this.idLicense = licenseJson.getId_license();
			this.licensecode = licenseJson.getLicensecode();
			this.description = licenseJson.getDescription();			
		}
	}

	public LicenseResponse(License license) {
		super();
		this.idLicense = license.getIdLicense();
		this.licensecode = license.getLicensecode();
		this.description = license.getDescription();
	}
	
	public LicenseResponse(Errors errors, String arg) {
		super(errors, arg);
	}
	
	public String getLicensecode() {
		return licensecode;
	}
	public void setLicensecode(String licensecode) {
		this.licensecode = licensecode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIdLicense() {
		return idLicense;
	}

	public void setIdLicense(Integer idLicense) {
		this.idLicense = idLicense;
	}

	
}
