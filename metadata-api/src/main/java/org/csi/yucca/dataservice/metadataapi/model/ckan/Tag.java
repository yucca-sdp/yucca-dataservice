/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.model.ckan;

public class Tag {
	private String vocabulary_id;
	private String display_name;
	private String name;
	private String id;

	public String getVocabularyId() {
		return vocabulary_id;
	}

	public void setVolcabularyId(String v) {
		vocabulary_id = v;
	}

	public String getDisplayName() {
		return display_name;
	}

	public void setDisplayName(String d) {
		display_name = d;
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		name = n;
	}

	public String getId() {
		return id;
	}

	public void setId(String v) {
		id = v;
	}

	public Tag() {
	}

	public String toString() {
		return "<Tag:" + getName() + "/" + getDisplayName() + ">";
	}
}
