/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate.beans.ranger;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class PostRangerRequest {
	private String id;
	private Boolean isEnabled;
	private Integer version;
	private String service;
	private String name;
	private Integer policyType;
	private String description;
	private Boolean isAuditEnabled;
	private ResourceRangerRequest resources;
	private List <PolicyItemRequest> policyItems;
	private List<String> denyPolicyItems;
	private List<String> allowExceptions;
	private List<String> denyExceptions;
	private List<String> dataMaskPolicyItems;
	private List<String> rowFilterPolicyItems;
	
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsAuditEnabled() {
		return isAuditEnabled;
	}
	public void setIsAuditEnabled(Boolean isAuditEnabled) {
		this.isAuditEnabled = isAuditEnabled;
	}
	public ResourceRangerRequest getResources() {
		return resources;
	}
	public void setResources(ResourceRangerRequest resources) {
		this.resources = resources;
	}
	public List<PolicyItemRequest> getPolicyItems() {
		return policyItems;
	}
	public void setPolicyItems(List<PolicyItemRequest> policyItems) {
		this.policyItems = policyItems;
	}
	public List<String> getDenyPolicyItems() {
		return denyPolicyItems;
	}
	public void setDenyPolicyItems(List<String> denyPolicyItems) {
		this.denyPolicyItems = denyPolicyItems;
	}
	public List<String> getAllowExceptions() {
		return allowExceptions;
	}
	public void setAllowExceptions(List<String> allowExceptions) {
		this.allowExceptions = allowExceptions;
	}
	public List<String> getDenyExceptions() {
		return denyExceptions;
	}
	public void setDenyExceptions(List<String> denyExceptions) {
		this.denyExceptions = denyExceptions;
	}
	public List<String> getDataMaskPolicyItems() {
		return dataMaskPolicyItems;
	}
	public void setDataMaskPolicyItems(List<String> dataMaskPolicyItems) {
		this.dataMaskPolicyItems = dataMaskPolicyItems;
	}
	public List<String> getRowFilterPolicyItems() {
		return rowFilterPolicyItems;
	}
	public void setRowFilterPolicyItems(List<String> rowFilterPolicyItems) {
		this.rowFilterPolicyItems = rowFilterPolicyItems;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getPolicyType() {
		return policyType;
	}
	public void setPolicyType(Integer policyType) {
		this.policyType = policyType;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String ObjectToJSON(PostRangerRequest request) {
		
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(request);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
		}
	
	
	  
	  
}
