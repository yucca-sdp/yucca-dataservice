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


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class PolicyItemRequest {

	private List<PolicyItemAccessRequest> accesses;
	private List<String> users;
	private List <String> groups;
	private List <String> conditions;
	private Boolean delegateAdmin;
	
	public List<PolicyItemAccessRequest> getAccesses() {
		return accesses;
	}
	public void setAccesses(List<PolicyItemAccessRequest> accesses) {
		this.accesses = accesses;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	public List<String> getGroups() {
		return groups;
	}
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	public List<String> getConditions() {
		return conditions;
	}
	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}
	public Boolean getDelegateAdmin() {
		return delegateAdmin;
	}
	public void setDelegateAdmin(Boolean delegateAdmin) {
		this.delegateAdmin = delegateAdmin;
	}
	
	
	  
	  
}
