/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model.builder;

import org.csi.yucca.adminapi.delegate.beans.httpresponse.TenantProductContactResultHttpResponse;
import org.csi.yucca.adminapi.model.TenantProductContact;

public class TenantProductContactBuilder {

	private Integer idTenant;
	private String name;
	private String email;
	private String contactrole;
	private String productcode;

	public TenantProductContactBuilder idTenant(Integer idTenant) {
		this.idTenant = idTenant;
		return this;
	}

	public TenantProductContactBuilder name(String name) {
		this.name = name;
		return this;
	}

	public TenantProductContactBuilder email(String email) {
		this.email = email;
		return this;
	}

	public TenantProductContactBuilder contactrole(String contactrole) {
		this.contactrole = contactrole;
		return this;
	}

	public TenantProductContactBuilder productcode(String productcode) {
		this.productcode = productcode;
		return this;
	}

	public TenantProductContact build() {

		TenantProductContact newInstance = new TenantProductContact();

		newInstance.setIdTenant(this.idTenant);
		newInstance.setName(this.name);
		newInstance.setEmail(this.email);
		newInstance.setContactrole(this.contactrole);
		newInstance.setProductcode(this.productcode);
		return newInstance;
	}

	public TenantProductContact buildPm(TenantProductContactResultHttpResponse resultContact, String emailDomain) {
		this.name = resultContact.getPm();
		this.email = emailFromFullName(resultContact.getPm(), emailDomain);
		this.contactrole = "Pm";
		return build();
	}

	public TenantProductContact buildBim(TenantProductContactResultHttpResponse resultContact, String emailDomain) {
		this.name = resultContact.getBim();
		this.email = emailFromFullName(resultContact.getBim(), emailDomain);
		this.contactrole = "Bim";
		return build();
	}

	public TenantProductContact buildRefServizio(TenantProductContactResultHttpResponse resultContact,
			String emailDomain) {
		this.name = resultContact.getRef_servizio();
		this.email = emailFromFullName(resultContact.getRef_servizio(), emailDomain);
		this.contactrole = "Ref_servizio";
		return build();
	}

	private static String emailFromFullName(String fullName, String domain) {

		if (fullName == null) {
			return "";
		}
		fullName = fullName.replace("'", "");
		
		String[] words = fullName.split("\\ ");
		String out  ="";	
			switch (words.length) {
			case 1:
				out = words[0];
				break;
			case 3:
				out = words[0].length()>3?words[1]+words[2]+"."+words[0]:words[2]+"."+words[0]+words[1];
				break;
			default:
				out = words[1]+"."+words[0];
				break;
			}
			
			return out + "@" + domain;
	}

	public static void main(String[] args) {
		String fullName = "bosco maria luigia";
		fullName = "di tommaso elisabetta";
		fullName = "franceschetti alessandro ";

		System.out.println(emailFromFullName(fullName, "csi.it"));
	}
}
