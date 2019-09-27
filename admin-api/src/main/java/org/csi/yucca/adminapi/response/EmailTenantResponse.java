/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import java.sql.Timestamp;

import org.csi.yucca.adminapi.util.Util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class EmailTenantResponse extends Response{

	private String destinatario; 
	private String soggetto;
	private String testo;
	
	private String tenantCode;
	private String tenantPassword;
	private String deactivationdate;
	
	public static String TENANT_CODE_MARKS = "XX_tenantcode_XX";
	public static String TENANT_PASSWORD_MARKS = "XX_tenantpwd_XX";
	public static String TENANT_DATEEND_MARKS = "XX_dateend_XX";
	
	public static EmailTenantResponse build(String tenantCode, String tenantPassword, Timestamp deactivationdate){
		
		EmailTenantResponse emailTenantResponse = new EmailTenantResponse();
		
		emailTenantResponse.tenantCode = tenantCode;
		emailTenantResponse.tenantPassword = tenantPassword;
		emailTenantResponse.deactivationdate = Util.dateStringMail(deactivationdate);
		
		return emailTenantResponse;
	}
	
	private EmailTenantResponse() {
		super();
	}

	public EmailTenantResponse soggetto(String soggetto){
		setSoggetto(soggetto);
		return this;
	}

	public EmailTenantResponse testo(String testo){
		setTesto(testo);
		return this;
	}
	
	public EmailTenantResponse destinatario(String destinatario) {
		setDestinatario(destinatario);
		return this;
	}
	
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(String soggetto) {
		if (soggetto != null) {
			this.soggetto = soggetto.replaceAll(TENANT_CODE_MARKS, this.tenantCode);
			this.soggetto = this.soggetto.replaceAll(TENANT_PASSWORD_MARKS, this.tenantPassword);
		}
		else{
			this.soggetto = "";	
		}
	}
	
	public String getTesto() {
		return testo;
	}
	
	public void setTesto(String testo) {
		if (testo != null) {
			this.testo = testo.replaceAll(TENANT_CODE_MARKS, this.tenantCode);
			this.testo = this.testo.replaceAll(TENANT_PASSWORD_MARKS, this.tenantPassword);
			if(this.deactivationdate != "")
				this.testo = this.testo.replaceAll(TENANT_DATEEND_MARKS, this.deactivationdate);
		}
		else{
			this.testo = "";	
		}
	} 

	
	

}
