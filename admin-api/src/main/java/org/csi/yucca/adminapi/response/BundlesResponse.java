/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.TenantTool;
import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;
import org.csi.yucca.adminapi.model.join.TenantManagement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BundlesResponse extends Response{

	private Integer idBundles;
	private Integer maxdatasetnum;
	private Integer maxstreamsnum;
	private String hasstage;
	private Integer maxOdataResultperpage;
	private String zeppelin;
	private Boolean readytools;
	private TenantTool[] tools;
	
	public BundlesResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BundlesResponse(TenantManagement tenantManagement) {
		super();
		this.idBundles = tenantManagement.getIdBundles();
		this.maxdatasetnum = tenantManagement.getMaxdatasetnum();
		this.maxstreamsnum = tenantManagement.getMaxstreamsnum();
		this.hasstage = tenantManagement.getHasstage();
		this.maxOdataResultperpage = tenantManagement.getMaxOdataResultperpage();
		this.zeppelin = tenantManagement.getZeppelin();
		this.readytools = tenantManagement.getReadytools();
		this.tools = tenantManagement.getTools();
	}

	public BundlesResponse(DettaglioTenantBackoffice dettaglioTenantBackoffice) {
		super();
		this.idBundles = dettaglioTenantBackoffice.getIdBundles();
		this.maxdatasetnum = dettaglioTenantBackoffice.getMaxdatasetnum();
		this.maxstreamsnum = dettaglioTenantBackoffice.getMaxstreamsnum();
		this.hasstage = dettaglioTenantBackoffice.getHasstage();
		this.maxOdataResultperpage = dettaglioTenantBackoffice.getMaxOdataResultperpage();
		this.zeppelin = dettaglioTenantBackoffice.getZeppelin();
		this.readytools = dettaglioTenantBackoffice.getReadytools();
		this.tools = dettaglioTenantBackoffice.getTools();
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

	public Boolean getReadyTools() {
		return readytools;
	}

	public void setReadyTools(Boolean readyTools) {
		this.readytools = readyTools;
	}

	public TenantTool[] getTools() {
		return tools;
	}

	public void setTools(TenantTool[] tools) {
		this.tools = tools;
	}
	
}
