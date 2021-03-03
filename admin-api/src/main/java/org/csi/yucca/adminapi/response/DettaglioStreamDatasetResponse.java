/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.InternalDettaglioStream;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.util.Util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown=true)
public class DettaglioStreamDatasetResponse extends DataSourceResponse {

	private List<ComponentResponse> components = new ArrayList<ComponentResponse>();
	private List<TenantResponse> sharingTenants = new ArrayList<TenantResponse>();
	private OpenDataResponse opendata;
	private LicenseResponse license;
	private DcatResponse dcat;
	private String hdpVersion;
	private String[] apiContexts;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private StreamDettaglioResponse stream;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private DatasetDettaglioResponse dataset;
	
	public DettaglioStreamDatasetResponse() {
		super();
	}

	
	@JsonIgnore
	private void setAllParameter(DettaglioStream dettaglioStream)throws Exception{
		Util.addSharingTenants(dettaglioStream.getSharingTenant(), this.sharingTenants);
		Util.addComponents(dettaglioStream.getComponents(), this.components);
		this.opendata = new OpenDataResponse(dettaglioStream);
		this.license = new LicenseResponse(dettaglioStream.getLicense());
		this.dcat = new DcatResponse(dettaglioStream.getDcat());
	}
	
	@JsonIgnore
	private void setAllParameter(DettaglioDataset dettaglioDataset,String[] apiContexts )throws Exception{
		Util.addSharingTenants(dettaglioDataset.getSharingTenant(), this.sharingTenants);
		Util.addComponents(dettaglioDataset.getComponents(), this.components);
		this.opendata = new OpenDataResponse(dettaglioDataset);
		this.license = new LicenseResponse(dettaglioDataset.getLicense());
		this.dcat = new DcatResponse(dettaglioDataset.getDcat());
		this.hdpVersion = dettaglioDataset.getHdpVersion();
		this.apiContexts = apiContexts;
	}
	
	public DettaglioStreamDatasetResponse(
			DettaglioStream dettaglioStream, 
			DettaglioDataset dettaglioDataset, 
			DettaglioSmartobject dettaglioSmartobject, 
			List<InternalDettaglioStream> listInternalStream) throws Exception {
		
		super(dettaglioStream);
		setAllParameter(dettaglioStream);
		
		this.stream = new StreamDettaglioResponse(dettaglioStream, dettaglioSmartobject, listInternalStream);
		this.dataset = new DatasetDettaglioResponse(dettaglioDataset);
	}
	
	public DettaglioStreamDatasetResponse(DettaglioStream dettaglioStream, DettaglioSmartobject dettaglioSmartobject, 
			List<InternalDettaglioStream> listInternalStream) throws Exception {
		super(dettaglioStream);
		setAllParameter(dettaglioStream);
		this.stream = new StreamDettaglioResponse(dettaglioStream, dettaglioSmartobject, listInternalStream);
	}
	
	public DettaglioStreamDatasetResponse(DettaglioDataset dettaglioDataset, String[] apiContexts ) throws Exception {
		super(dettaglioDataset);
		setAllParameter(dettaglioDataset, apiContexts);
		this.dataset = new DatasetDettaglioResponse(dettaglioDataset);
	}

	public List<ComponentResponse> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentResponse> components) {
		this.components = components;
	}

	public List<TenantResponse> getSharingTenants() {
		return sharingTenants;
	}

	public void setSharingTenants(List<TenantResponse> sharingTenants) {
		this.sharingTenants = sharingTenants;
	}

	public OpenDataResponse getOpendata() {
		return opendata;
	}

	public void setOpnedata(OpenDataResponse opendata) {
		this.opendata = opendata;
	}

	public LicenseResponse getLicense() {
		return license;
	}

	public void setLicense(LicenseResponse license) {
		this.license = license;
	}

	public DcatResponse getDcat() {
		return dcat;
	}

	public void setDcat(DcatResponse dcat) {
		this.dcat = dcat;
	}

	public StreamDettaglioResponse getStream() {
		return stream;
	}

	public void setStream(StreamDettaglioResponse stream) {
		this.stream = stream;
	}

	public DatasetDettaglioResponse getDataset() {
		return dataset;
	}

	public void setDataset(DatasetDettaglioResponse dataset) {
		this.dataset = dataset;
	}


	public String[] getApiContexts() {
		return apiContexts;
	}


	public void setApiContexts(String[] apiContexts) {
		this.apiContexts = apiContexts;
	}




	
}
