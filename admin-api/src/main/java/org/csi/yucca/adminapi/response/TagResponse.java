/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Tag;
import org.csi.yucca.adminapi.model.TagJson;
import org.csi.yucca.adminapi.util.Errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TagResponse extends Response{
	
	private Integer idTag;
	private String tagcode;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langit;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langen;
	private Integer idEcosystem;

	public TagResponse(TagJson tag) {
		super();
		this.idTag = tag.getId_tag();
		this.tagcode = tag.getTagcode();
		this.langit = tag.getLangit();
		this.langen = tag.getLangen();
		this.idEcosystem = tag.getId_ecosystem();
	}
	
	public TagResponse(Tag tag) {
		super();
		this.idTag = tag.getIdTag();
		this.tagcode = tag.getTagcode();
		this.langit = tag.getLangit();
		this.langen = tag.getLangen();
		this.idEcosystem = tag.getIdEcosystem();
	}
	
	public TagResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TagResponse(Errors errors, String arg) {
		super(errors, arg);
		// TODO Auto-generated constructor stub
	}
	public Integer getIdTag() {
		return idTag;
	}
	public void setIdTag(Integer idTag) {
		this.idTag = idTag;
	}
	public String getTagcode() {
		return tagcode;
	}
	public void setTagcode(String tagcode) {
		this.tagcode = tagcode;
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
	public Integer getIdEcosystem() {
		return idEcosystem;
	}
	public void setIdEcosystem(Integer idEcosystem) {
		this.idEcosystem = idEcosystem;
	}
}
