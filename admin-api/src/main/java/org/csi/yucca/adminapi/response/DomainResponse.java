/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Domain;
import org.csi.yucca.adminapi.model.IDomain;
import org.csi.yucca.adminapi.util.Errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown=true)
public class DomainResponse extends Response{
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langit;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langen;
	private Integer idDomain;
	private String domaincode;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer deprecated;
	
	public DomainResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DomainResponse(IDomain iDomainImpl) {
		super();
		if (iDomainImpl != null) {
			this.langit = iDomainImpl.getDomLangIt();
			this.langen = iDomainImpl.getDomLangEn();
			this.idDomain = iDomainImpl.getDomIdDomain();
			this.domaincode = iDomainImpl.getDomDomainCode();			
		}
	}

	public DomainResponse(Domain domain) {
		super();
		this.langit = domain.getLangit();
		this.langen = domain.getLangen();
		this.idDomain = domain.getIdDomain();
		this.domaincode = domain.getDomaincode();
		this.deprecated = domain.getDeprecated();
	}
	
	public DomainResponse(Errors errors, String arg) {
		super(errors, arg);
	}
	
	public Integer getIdDomain() {
		return idDomain;
	}
	public void setIdDomain(Integer idDomain) {
		this.idDomain = idDomain;
	}
	public String getDomaincode() {
		return domaincode;
	}
	public void setDomaincode(String domaincode) {
		this.domaincode = domaincode;
	}
	public Integer getDeprecated() {
		return deprecated;
	}
	public void setDeprecated(Integer deprecated) {
		this.deprecated = deprecated;
	}
	public String getLangit() {
		return langit;
	}
	public void setLangit(String langit) {
		this.langit = langit;
	}
	public String getLangen() {
		return langen;
	}
	public void setLangen(String langen) {
		this.langen = langen;
	}

	
}
