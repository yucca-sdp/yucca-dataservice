/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.LinkedList;
import java.util.List;

import org.csi.yucca.dataservice.metadataapi.util.DCatSdpHelper;

import com.google.gson.annotations.SerializedName;

public class DCatVCard extends DCatObject{

	@SerializedName("vcard:fn")
	private String name;

	@SerializedName("vcard:hasEmail")
	private IdString hasEmail;

	// optional
	@SerializedName("vcard:hasTelephone")
	private IdString hasTelephone;

	@SerializedName("vcard:hasURL")
	private IdString hasURL;

	private String description; // dct:description

	// private String Kind; // vcard:Kind
	// private String organizationName; // vcard:organizationName

	public DCatVCard() {
		types = new LinkedList<String>();
		types.add("vcard:Organization"); 
		types.add("vcard:Kind");

	}
	
	public void setId(String id) {
		this.id = BASE_ID + "vcard/" + DCatSdpHelper.cleanForId(id);
	}


	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IdString getHasEmail() {
		return hasEmail;
	}

	public void setHasEmail(IdString hasEmail) {
		this.hasEmail = hasEmail;
	}

	public IdString getHasTelephone() {
		return hasTelephone;
	}

	public void setHasTelephone(IdString hasTelephone) {
		this.hasTelephone = hasTelephone;
	}

	public IdString getHasURL() {
		return hasURL;
	}

	public void setHasURL(IdString hasURL) {
		this.hasURL = hasURL;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
