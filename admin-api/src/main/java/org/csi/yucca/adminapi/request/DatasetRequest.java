/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

import java.util.List;

public class DatasetRequest implements IVisibility, IDataSourceRequest {

	private Integer iddataset;
	private Integer idTenant;
	private String datasetname;
	private boolean unpublished;
	private String importfiletype;
	private LicenseRequest license;
	private String visibility;
	private List<SharingTenantRequest> sharingTenants;
	private String copyright;
	private String requestername;
	private String requestersurname;
	private String requestermail;
	private Boolean privacyacceptance;
	private String icon;
	private String jdbcdburl;
	private String jdbcdbname;
	private String jdbcdbschema;
	private String jdbcdbtype;
	private String jdbctablename;
	private OpenDataRequest opendata;
	private Integer idSubdomain;
	private DcatRequest dcat;
	private List<ComponentRequest> components;
	private List<Integer> tags;
	private String disclaimer;
	private String multiSubdomain;
	private String description;
	private Integer newDataSourceVersion;
	private Integer currentDataSourceVersion;
	private Integer idDataSource;
	private String datasetcode;
	private String externalreference;

	private Boolean availablehive;
	private Boolean availablespeed;
	private Boolean istransformed;
	private String dbhiveschema;
	private String dbhivetable;
	
	private String solrcollectionname; 
	private String phoenixschemaname;
	private List<PostDataSourceGroupRequest> groups;

	public List<PostDataSourceGroupRequest> getGroups() {
		return groups;
	}

	public void setGroups(List<PostDataSourceGroupRequest> groups) {
		this.groups = groups;
	}

	public Integer getIddataset() {
		return iddataset;
	}

	public void setIddataset(Integer iddataset) {
		this.iddataset = iddataset;
	}

	public String getExternalreference() {
		return externalreference;
	}

	public void setExternalreference(String externalreference) {
		this.externalreference = externalreference;
	}

	public String getDatasetcode() {
		return datasetcode;
	}

	public void setDatasetcode(String datasetcode) {
		this.datasetcode = datasetcode;
	}

	public Integer getNewDataSourceVersion() {
		return newDataSourceVersion;
	}

	public void setNewDataSourceVersion(Integer newDataSourceVersion) {
		this.newDataSourceVersion = newDataSourceVersion;
	}

	public Integer getCurrentDataSourceVersion() {
		return currentDataSourceVersion;
	}

	public void setCurrentDataSourceVersion(Integer currentDataSourceVersion) {
		this.currentDataSourceVersion = currentDataSourceVersion;
	}

	public Integer getIdDataSource() {
		return idDataSource;
	}

	public void setIdDataSource(Integer idDataSource) {
		this.idDataSource = idDataSource;
	}

	public DatasetRequest datasetname(String datasetname) {
		setDatasetname(datasetname);
		return this;
	}

	public DatasetRequest idSubdomain(Integer idSubdomain) {
		setIdSubdomain(idSubdomain);
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMultiSubdomain() {
		return multiSubdomain;
	}

	public void setMultiSubdomain(String multiSubdomain) {
		this.multiSubdomain = multiSubdomain;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public void setUnpublished(boolean unpublished) {
		this.unpublished = unpublished;
	}

	public String getName() {
		return getDatasetname();
	}

	public Integer getIdSubdomain() {
		return idSubdomain;
	}

	public void setIdSubdomain(Integer idSubdomain) {
		this.idSubdomain = idSubdomain;
	}

	public DcatRequest getDcat() {
		return dcat;
	}

	public void setDcat(DcatRequest dcat) {
		this.dcat = dcat;
	}

	public List<ComponentRequest> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentRequest> components) {
		this.components = components;
	}

	public List<Integer> getTags() {
		return tags;
	}

	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}

	public Integer getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}

	public String getDatasetname() {
		return datasetname;
	}

	public void setDatasetname(String datasetname) {
		this.datasetname = datasetname;
	}

	public boolean getUnpublished() {
		return unpublished;
	}

	public void setUnpublished(Boolean unpublished) {
		this.unpublished = unpublished;
	}

	public String getImportfiletype() {
		return importfiletype;
	}

	public void setImportfiletype(String importfiletype) {
		this.importfiletype = importfiletype;
	}

	public LicenseRequest getLicense() {
		return license;
	}

	public void setLicense(LicenseRequest license) {
		this.license = license;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public List<SharingTenantRequest> getSharingTenants() {
		return sharingTenants;
	}

	public void setSharingTenants(List<SharingTenantRequest> sharingTenants) {
		this.sharingTenants = sharingTenants;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getRequestername() {
		return requestername;
	}

	public void setRequestername(String requestername) {
		this.requestername = requestername;
	}

	public String getRequestersurname() {
		return requestersurname;
	}

	public void setRequestersurname(String requestersurname) {
		this.requestersurname = requestersurname;
	}

	public String getRequestermail() {
		return requestermail;
	}

	public void setRequestermail(String requestermail) {
		this.requestermail = requestermail;
	}

	public Boolean getPrivacyacceptance() {
		return privacyacceptance;
	}

	public void setPrivacyacceptance(Boolean privacyacceptance) {
		this.privacyacceptance = privacyacceptance;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getJdbcdburl() {
		return jdbcdburl;
	}

	public void setJdbcdburl(String jdbcdburl) {
		this.jdbcdburl = jdbcdburl;
	}

	public String getJdbcdbname() {
		return jdbcdbname;
	}

	public void setJdbcdbname(String jdbcdbname) {
		this.jdbcdbname = jdbcdbname;
	}

	public String getJdbcdbtype() {
		return jdbcdbtype;
	}

	public void setJdbcdbtype(String jdbcdbtype) {
		this.jdbcdbtype = jdbcdbtype;
	}

	public String getJdbctablename() {
		return jdbctablename;
	}

	public void setJdbctablename(String jdbctablename) {
		this.jdbctablename = jdbctablename;
	}

	public OpenDataRequest getOpendata() {
		return opendata;
	}

	public void setOpenData(OpenDataRequest opendata) {
		this.opendata = opendata;
	}

	public Boolean getAvailablehive() {
		return availablehive;
	}

	public void setAvailablehive(Boolean availablehive) {
		this.availablehive = availablehive;
	}

	public Boolean getAvailablespeed() {
		return availablespeed;
	}

	public void setAvailablespeed(Boolean availablespeed) {
		this.availablespeed = availablespeed;
	}

	public Boolean getIstransformed() {
		return istransformed;
	}

	public void setIstransformed(Boolean istransformed) {
		this.istransformed = istransformed;
	}

	public String getDbhiveschema() {
		return dbhiveschema;
	}

	public void setDbhiveschema(String dbhiveschema) {
		this.dbhiveschema = dbhiveschema;
	}

	public String getDbhivetable() {
		return dbhivetable;
	}

	public void setDbhivetable(String dbhivetable) {
		this.dbhivetable = dbhivetable;
	}

	public String getSolrcollectionname() {
		return solrcollectionname;
	}

	public void setSolrcollectionname(String solrcollectionname) {
		this.solrcollectionname = solrcollectionname;
	}

	public String getPhoenixschemaname() {
		return phoenixschemaname;
	}

	public void setPhoenixschemaname(String phoenixschemaname) {
		this.phoenixschemaname = phoenixschemaname;
	}

	public String getJdbcdbschema() {
		return jdbcdbschema;
	}

	public void setJdbcdbschema(String jdbcdbschema) {
		this.jdbcdbschema = jdbcdbschema;
	}

}
