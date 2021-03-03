/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.binaryapi.model.metadata;

import java.util.Date;

import org.csi.yucca.dataservice.binaryapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Info extends AbstractEntity {

	private String datasetName;
	private String description;
	private String license;
	private String disclaimer;
	private String copyright;
	private String visibility;
	private Date registrationDate;
	private String requestorName;
	private String requestorSurname;
	private String requestornEmail;
	private String dataDomain;
	private String codSubDomain;
	private Double fps;

	private Date startIngestionDate;
	private Date endIngestionDate;
	private String importFileType;
	private String icon;

	private Long binaryIdDataset;
	private Integer binaryDatasetVersion;

	private Tag tags[];
	private Field fields[];

	private Tenantssharing tenantssharing;

	public Info() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getRequestorName() {
		return requestorName;
	}

	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}

	public String getRequestorSurname() {
		return requestorSurname;
	}

	public void setRequestorSurname(String requestorSurname) {
		this.requestorSurname = requestorSurname;
	}

	public String getRequestornEmail() {
		return requestornEmail;
	}

	public void setRequestornEmail(String requestornEmail) {
		this.requestornEmail = requestornEmail;
	}

	public String getDataDomain() {
		return dataDomain;
	}

	public void setDataDomain(String dataDomain) {
		this.dataDomain = dataDomain;
	}

	public String getCodSubDomain() {
		return codSubDomain;
	}

	public void setCodSubDomain(String codSubDomain) {
		this.codSubDomain = codSubDomain;
	}

	public Double getFps() {
		return fps;
	}

	public void setFps(Double fps) {
		this.fps = fps;
	}

	public Date getStartIngestionDate() {
		return startIngestionDate;
	}

	public void setStartIngestionDate(Date startIngestionDate) {
		this.startIngestionDate = startIngestionDate;
	}

	public Date getEndIngestionDate() {
		return endIngestionDate;
	}

	public void setEndIngestionDate(Date endIngestionDate) {
		this.endIngestionDate = endIngestionDate;
	}

	public String getImportFileType() {
		return importFileType;
	}

	public void setImportFileType(String importFileType) {
		this.importFileType = importFileType;
	}

	public Tag[] getTags() {
		return tags;
	}

	public void setTags(Tag[] tags) {
		this.tags = tags;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Tenantssharing getTenantssharing() {
		return tenantssharing;
	}

	public void setTenantssharing(Tenantssharing tenantssharing) {
		this.tenantssharing = tenantssharing;
	}

	public Long getBinaryIdDataset() {
		return binaryIdDataset;
	}

	public void setBinaryIdDataset(Long binaryIdDataset) {
		this.binaryIdDataset = binaryIdDataset;
	}

	public Integer getBinaryDatasetVersion() {
		return binaryDatasetVersion;
	}

	public void setBinaryDatasetVersion(Integer binaryDatasetVersion) {
		this.binaryDatasetVersion = binaryDatasetVersion;
	}

}
