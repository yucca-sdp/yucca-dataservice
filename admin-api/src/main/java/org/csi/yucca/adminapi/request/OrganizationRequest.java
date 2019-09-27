/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

import java.util.List;

public class OrganizationRequest {
	
	private List<Integer> ecosystemCodeList;
	private Integer idOrganization;
	private String organizationcode;
	private String description;
	private String datasolrcollectionname;
	private String measuresolrcollectionname;
	private String mediasolrcollectionname;
	private String socialsolrcollectionname;
	private String dataphoenixtablename;
	private String dataphoenixschemaname;
	private String measuresphoenixtablename;
	private String measuresphoenixschemaname;
	private String mediaphoenixtablename;
	private String mediaphoenixschemaname;
	private String socialphoenixtablename;
	private String socialphoenixschemaname;	
	
	public List<Integer> getEcosystemCodeList() {
		return ecosystemCodeList;
	}
	public void setEcosystemCodeList(List<Integer> ecosystemCodeList) {
		this.ecosystemCodeList = ecosystemCodeList;
	}
	public Integer getIdOrganization() {
		return idOrganization;
	}
	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	}
	public String getOrganizationcode() {
		return organizationcode;
	}
	public void setOrganizationcode(String organizationcode) {
		this.organizationcode = organizationcode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDatasolrcollectionname() {
		return datasolrcollectionname;
	}
	public void setDatasolrcollectionname(String datasolrcollectionname) {
		this.datasolrcollectionname = datasolrcollectionname;
	}
	public String getMeasuresolrcollectionname() {
		return measuresolrcollectionname;
	}
	public void setMeasuresolrcollectionname(String measuresolrcollectionname) {
		this.measuresolrcollectionname = measuresolrcollectionname;
	}
	public String getMediasolrcollectionname() {
		return mediasolrcollectionname;
	}
	public void setMediasolrcollectionname(String mediasolrcollectionname) {
		this.mediasolrcollectionname = mediasolrcollectionname;
	}
	public String getSocialsolrcollectionname() {
		return socialsolrcollectionname;
	}
	public void setSocialsolrcollectionname(String socialsolrcollectionname) {
		this.socialsolrcollectionname = socialsolrcollectionname;
	}
	public String getDataphoenixtablename() {
		return dataphoenixtablename;
	}
	public void setDataphoenixtablename(String dataphoenixtablename) {
		this.dataphoenixtablename = dataphoenixtablename;
	}
	public String getDataphoenixschemaname() {
		return dataphoenixschemaname;
	}
	public void setDataphoenixschemaname(String dataphoenixschemaname) {
		this.dataphoenixschemaname = dataphoenixschemaname;
	}
	public String getMeasuresphoenixtablename() {
		return measuresphoenixtablename;
	}
	public void setMeasuresphoenixtablename(String measuresphoenixtablename) {
		this.measuresphoenixtablename = measuresphoenixtablename;
	}
	public String getMeasuresphoenixschemaname() {
		return measuresphoenixschemaname;
	}
	public void setMeasuresphoenixschemaname(String measuresphoenixschemaname) {
		this.measuresphoenixschemaname = measuresphoenixschemaname;
	}
	public String getMediaphoenixtablename() {
		return mediaphoenixtablename;
	}
	public void setMediaphoenixtablename(String mediaphoenixtablename) {
		this.mediaphoenixtablename = mediaphoenixtablename;
	}
	public String getMediaphoenixschemaname() {
		return mediaphoenixschemaname;
	}
	public void setMediaphoenixschemaname(String mediaphoenixschemaname) {
		this.mediaphoenixschemaname = mediaphoenixschemaname;
	}
	public String getSocialphoenixtablename() {
		return socialphoenixtablename;
	}
	public void setSocialphoenixtablename(String socialphoenixtablename) {
		this.socialphoenixtablename = socialphoenixtablename;
	}
	public String getSocialphoenixschemaname() {
		return socialphoenixschemaname;
	}
	public void setSocialphoenixschemaname(String socialphoenixschemaname) {
		this.socialphoenixschemaname = socialphoenixschemaname;
	}
	
}
