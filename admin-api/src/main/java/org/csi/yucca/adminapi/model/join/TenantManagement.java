/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.model.join;

import java.io.Serializable;
import java.sql.Timestamp;

import org.csi.yucca.adminapi.model.TenantTool;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TenantManagement implements Serializable {

	private static final long serialVersionUID = 6174944924329134719L;

	private Timestamp creationdate;

	private Timestamp expirationdate;

	private String dataSolrCollectionName;

	private String measureSolrCollectionName;

	private String mediaSolrCollectionName;

	private String socialSolrCollectionName;

	private String username;

	private Integer idBundles;

	private Integer maxdatasetnum;

	private Integer maxstreamsnum;

	private String hasstage;

	private Integer maxOdataResultperpage;

	private String zeppelin;

	private Boolean readytools;

	private TenantTool[] tools;

	private String toolsstring;

	private Integer idEcosystem;

	private String ecosystemcode;

	private String ecosystemdescription;

	private Integer idOrganization;

	private String organizationcode;

	private String organizationdescription;

	private Integer idTenantStatus;

	private String tenantstatuscode;

	private String tenantstatusdescription;

	private Integer idTenantType;

	private String tenanttypecode;

	private String tenanttypedescription;

	private Integer idShareType;

	private String sharetypedescription;

	private Integer idTenant;

	private String description;

	private String name;

	private String tenantcode;

	private Integer usagedaysnumber;

	private String useremail;

	private String userfirstname;

	private String userlastname;

	private String usertypeauth;
	
	private Boolean flagmigrated;
	
	private String hdpversion;

	public Timestamp getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Timestamp creationdate) {
		this.creationdate = creationdate;
	}

	public Timestamp getExpirationdate() {
		return expirationdate;
	}

	public void setExpirationdate(Timestamp expirationdate) {
		this.expirationdate = expirationdate;
	}

	public String getDataSolrCollectionName() {
		return dataSolrCollectionName;
	}

	public void setDataSolrCollectionName(String dataSolrCollectionName) {
		this.dataSolrCollectionName = dataSolrCollectionName;
	}

	public String getMeasureSolrCollectionName() {
		return measureSolrCollectionName;
	}

	public void setMeasureSolrCollectionName(String measureSolrCollectionName) {
		this.measureSolrCollectionName = measureSolrCollectionName;
	}

	public String getMediaSolrCollectionName() {
		return mediaSolrCollectionName;
	}

	public void setMediaSolrCollectionName(String mediaSolrCollectionName) {
		this.mediaSolrCollectionName = mediaSolrCollectionName;
	}

	public String getSocialSolrCollectionName() {
		return socialSolrCollectionName;
	}

	public void setSocialSolrCollectionName(String socialSolrCollectionName) {
		this.socialSolrCollectionName = socialSolrCollectionName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getIdBundles() {
		return idBundles;
	}

	public void setIdBundles(Integer idBundles) {
		this.idBundles = idBundles;
	}

	public Integer getMaxdatasetnum() {
		return maxdatasetnum;
	}

	public void setMaxdatasetnum(Integer maxdatasetnum) {
		this.maxdatasetnum = maxdatasetnum;
	}

	public Integer getMaxstreamsnum() {
		return maxstreamsnum;
	}

	public void setMaxstreamsnum(Integer maxstreamsnum) {
		this.maxstreamsnum = maxstreamsnum;
	}

	public String getHasstage() {
		return hasstage;
	}

	public void setHasstage(String hasstage) {
		this.hasstage = hasstage;
	}

	public Integer getMaxOdataResultperpage() {
		return maxOdataResultperpage;
	}

	public void setMaxOdataResultperpage(Integer maxOdataResultperpage) {
		this.maxOdataResultperpage = maxOdataResultperpage;
	}

	public String getZeppelin() {
		return zeppelin;
	}

	public void setZeppelin(String zeppelin) {
		this.zeppelin = zeppelin;
	}

	public Integer getIdEcosystem() {
		return idEcosystem;
	}

	public void setIdEcosystem(Integer idEcosystem) {
		this.idEcosystem = idEcosystem;
	}

	public String getEcosystemcode() {
		return ecosystemcode;
	}

	public void setEcosystemcode(String ecosystemcode) {
		this.ecosystemcode = ecosystemcode;
	}

	public String getEcosystemdescription() {
		return ecosystemdescription;
	}

	public void setEcosystemdescription(String ecosystemdescription) {
		this.ecosystemdescription = ecosystemdescription;
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

	public String getOrganizationdescription() {
		return organizationdescription;
	}

	public void setOrganizationdescription(String organizationdescription) {
		this.organizationdescription = organizationdescription;
	}

	public Integer getIdTenantStatus() {
		return idTenantStatus;
	}

	public void setIdTenantStatus(Integer idTenantStatus) {
		this.idTenantStatus = idTenantStatus;
	}

	public String getTenantstatuscode() {
		return tenantstatuscode;
	}

	public void setTenantstatuscode(String tenantstatuscode) {
		this.tenantstatuscode = tenantstatuscode;
	}

	public String getTenantstatusdescription() {
		return tenantstatusdescription;
	}

	public void setTenantstatusdescription(String tenantstatusdescription) {
		this.tenantstatusdescription = tenantstatusdescription;
	}

	public Integer getIdTenantType() {
		return idTenantType;
	}

	public void setIdTenantType(Integer idTenantType) {
		this.idTenantType = idTenantType;
	}

	public String getTenanttypecode() {
		return tenanttypecode;
	}

	public void setTenanttypecode(String tenanttypecode) {
		this.tenanttypecode = tenanttypecode;
	}

	public String getTenanttypedescription() {
		return tenanttypedescription;
	}

	public void setTenanttypedescription(String tenanttypedescription) {
		this.tenanttypedescription = tenanttypedescription;
	}

	public Integer getIdShareType() {
		return idShareType;
	}

	public void setIdShareType(Integer idShareType) {
		this.idShareType = idShareType;
	}

	public String getSharetypedescription() {
		return sharetypedescription;
	}

	public void setSharetypedescription(String sharetypedescription) {
		this.sharetypedescription = sharetypedescription;
	}

	public Integer getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTenantcode() {
		return tenantcode;
	}

	public void setTenantcode(String tenantcode) {
		this.tenantcode = tenantcode;
	}

	public Integer getUsagedaysnumber() {
		return usagedaysnumber;
	}

	public void setUsagedaysnumber(Integer usagedaysnumber) {
		this.usagedaysnumber = usagedaysnumber;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUserfirstname() {
		return userfirstname;
	}

	public void setUserfirstname(String userfirstname) {
		this.userfirstname = userfirstname;
	}

	public String getUserlastname() {
		return userlastname;
	}

	public void setUserlastname(String userlastname) {
		this.userlastname = userlastname;
	}

	public String getUsertypeauth() {
		return usertypeauth;
	}

	public void setUsertypeauth(String usertypeauth) {
		this.usertypeauth = usertypeauth;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	public Boolean getflagmigrated() {
		return flagmigrated;
	}

	public void setflagmigrated(Boolean flagmigrated) {
		this.flagmigrated = flagmigrated;
	}

	public String getHdpVersion() {
		return hdpversion;
	}

	public void setHdpVersion(String hdpversion) {
		this.hdpversion = hdpversion;
	}

	public Boolean getReadytools() {
		return readytools;
	}

	public void setReadytools(Boolean readytools) {
		this.readytools = readytools;
	}

	public TenantTool[] getTools() {
		return tools;
	}

	public void setTools(TenantTool[] tools) {
		this.tools = tools;
	}

	private void setArrayTools(TenantTool[] tools) {
		this.tools = tools;
	}

	public String getToolsstring() {
		return toolsstring;
	}

	public void setToolsstring(String toolsstring) {
		this.toolsstring = toolsstring;
		if (toolsstring != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				setArrayTools(mapper.readValue(toolsstring, TenantTool[].class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
