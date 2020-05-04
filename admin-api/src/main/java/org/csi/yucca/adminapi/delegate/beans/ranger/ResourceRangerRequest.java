/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate.beans.ranger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceRangerRequest {
	
	PathRangerRequest path;

	public PathRangerRequest getPath() {
		return path;
	}

	public void setPath(PathRangerRequest path) {
		this.path = path;
	}
	
	
	  
}
