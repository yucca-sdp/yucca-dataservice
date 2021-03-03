/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.model.ckan;

public class Resource {
	private String id;
	private String resource_group_id;
	private String name;
	private String description;
	private String created;
	private String url;
	private String webstore_url;
	private String cache_last_updated;
	private String package_id;
	private String webstore_last_updated;
	private int size;
	private int position;
	private String resource_type;
	private String last_modified;
	private String hash;
	private String format;
	private String mimetype;
	private String mimetype_inner;
	private String cache_url;

	public Resource() {
	}

	public String createResourceV2(){
		return "format="+getFormat()+"||" + "url="+getUrl();
	}
	
	public String getResource_group_id() {
		return resource_group_id;
	}

	public void setResource_group_id(String resource_group_id) {
		this.resource_group_id = resource_group_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getCreated() {
		return created;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setWebstore_url(String webstore_url) {
		this.webstore_url = webstore_url;
	}

	public String getWebstore_url() {
		return webstore_url;
	}

	public void setCache_last_updated(String cache_last_updated) {
		this.cache_last_updated = cache_last_updated;
	}

	public String getCache_last_updated() {
		return cache_last_updated;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	public String getPackage_id() {
		return package_id;
	}

	public void setWebstore_last_updated(String webstore_last_updated) {
		this.webstore_last_updated = webstore_last_updated;
	}

	public String getWebstore_last_updated() {
		return webstore_last_updated;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setLast_modified(String last_modified) {
		this.last_modified = last_modified;
	}

	public String getLast_modified() {
		return last_modified;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getHash() {
		return hash;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype_inner(String mimetype_inner) {
		this.mimetype_inner = mimetype_inner;
	}

	public String getMimetype_inner() {
		return mimetype_inner;
	}

	public void setCache_url(String cache_url) {
		this.cache_url = cache_url;
	}

	public String getCache_url() {
		return cache_url;
	}

}
