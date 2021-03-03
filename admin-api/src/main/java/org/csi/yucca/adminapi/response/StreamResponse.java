/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.TagJson;
import org.csi.yucca.adminapi.util.Util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown=true)
public class StreamResponse extends Response {

	private TenantResponse tenantManager;
	private OrganizationResponse organization;
	private Integer idstream;
	private Integer version;
	private String streamcode;
	private String streamname;
	private String name;
	private Boolean unpublished;
	private String visibility;
	private String disclaimer;
	private String registrationdate;
	private StatusResponse status;
	private List<TagResponse> tags = new ArrayList<TagResponse>();
	private DomainResponse domain;
	private SubdomainResponse subdomain;
	private Boolean savedata;
	private Boolean hasIcon;
	private List<DataSourceGroupResponse> groups;

	public StreamResponse(DettaglioStream dettaglioStream) throws Exception {
		super();
		this.tenantManager = new TenantResponse(dettaglioStream);
		this.organization = new OrganizationResponse(dettaglioStream);
		this.idstream = dettaglioStream.getIdstream();
		this.version = dettaglioStream.getDatasourceversion();
		this.streamcode = dettaglioStream.getStreamcode();
		this.streamname = dettaglioStream.getStreamname();
		this.name = dettaglioStream.getDataSourceName();
		this.unpublished = Util.intToBoolean(dettaglioStream.getDataSourceUnpublished());
		this.visibility = dettaglioStream.getDataSourceVisibility();
		this.disclaimer = dettaglioStream.getDataSourceDisclaimer();
		this.registrationdate = Util.dateString(dettaglioStream.getDataSourceRegistrationDate());
		this.hasIcon = dettaglioStream.getDataSourceHasIcon();
		this.status = new StatusResponse(dettaglioStream);
		this.domain = new DomainResponse(dettaglioStream);
		this.subdomain = new SubdomainResponse(dettaglioStream);
		this.addTags(dettaglioStream.getTags());
		setSavedata(Util.intToBoolean(dettaglioStream.getSavedata()));
	}

	private void addTags(String tags) throws Exception {

		if (tags != null) {
			
			ObjectMapper mapper = new ObjectMapper();
			List<TagJson> listTagJson = mapper.readValue(tags, new TypeReference<List<TagJson>>() {});

			for (TagJson tagJson : listTagJson) {
				this.tags.add(new TagResponse(tagJson));
			}
		}

	}

	public StreamResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TenantResponse getTenantManager() {
		return tenantManager;
	}

	public void setTenantManager(TenantResponse tenantManager) {
		this.tenantManager = tenantManager;
	}

	public OrganizationResponse getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationResponse organization) {
		this.organization = organization;
	}

	public Integer getIdstream() {
		return idstream;
	}

	public void setIdstream(Integer idstream) {
		this.idstream = idstream;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getStreamcode() {
		return streamcode;
	}

	public void setStreamcode(String streamcode) {
		this.streamcode = streamcode;
	}

	public String getStreamname() {
		return streamname;
	}

	public void setStreamname(String streamname) {
		this.streamname = streamname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getUnpublished() {
		return unpublished;
	}

	public void setUnpublished(Boolean unpublished) {
		this.unpublished = unpublished;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getRegistrationdate() {
		return registrationdate;
	}

	public void setRegistrationdate(String registrationdate) {
		this.registrationdate = registrationdate;
	}

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}

	public List<TagResponse> getTags() {
		return tags;
	}

	public void setTags(List<TagResponse> tags) {
		this.tags = tags;
	}

	public DomainResponse getDomain() {
		return domain;
	}

	public void setDomain(DomainResponse domain) {
		this.domain = domain;
	}

	public SubdomainResponse getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(SubdomainResponse subdomain) {
		this.subdomain = subdomain;
	}

	public Boolean getSavedata() {
		return savedata;
	}

	public void setSavedata(Boolean savedata) {
		this.savedata = savedata;
	}

	public Boolean getHasIcon() {
		return hasIcon;
	}

	public void setHasIcon(Boolean hasIcon) {
		this.hasIcon = hasIcon;
	}

	public List<DataSourceGroupResponse> getGroups() {
		return groups;
	}

	public void setGroups(List<DataSourceGroupResponse> groups) {
		this.groups = groups;
	}

}
