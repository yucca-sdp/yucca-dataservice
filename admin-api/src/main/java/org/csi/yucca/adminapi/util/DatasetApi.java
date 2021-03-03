/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.util;

public enum DatasetApi {

	odata           ("GET","api"),
	odatarupar		("GET","apirupar"),
	search  		("POST","searchapi"),
	searchrupar     ("POST","searchapirupar");
	
	private String method;
	private String apicontext;
	
	DatasetApi(String method, String apicontext ){
		this.method = method;
		this.apicontext = apicontext;
	}

	public String method() {
		return method;
	}
	
	public String apicontext() {
		return apicontext;
	}


	
	
	

}
