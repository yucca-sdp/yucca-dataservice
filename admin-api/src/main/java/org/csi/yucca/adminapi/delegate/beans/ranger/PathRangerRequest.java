/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate.beans.ranger;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PathRangerRequest {
	
	List<String> values;
    Boolean isExcludes;
    Boolean isRecursive;
    
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public Boolean getIsExcludes() {
		return isExcludes;
	}
	public void setIsExcludes(Boolean isExcludes) {
		this.isExcludes = isExcludes;
	}
	public Boolean getIsRecursive() {
		return isRecursive;
	}
	public void setIsRecursive(Boolean isRecursive) {
		this.isRecursive = isRecursive;
	}
    
    
	  
}
