/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.DettaglioStream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ListStreamResponse extends StreamResponse{
	
	private ListStreamSmartobjectResponse smartobject;
	
	public ListStreamResponse(DettaglioStream dettaglioStream) throws Exception {
		super(dettaglioStream);
		this.smartobject = new ListStreamSmartobjectResponse(dettaglioStream);
	}

	public ListStreamResponse() {
		super();
	}

	public ListStreamSmartobjectResponse getSmartobject() {
		return smartobject;
	}

	public void setSmartobject(ListStreamSmartobjectResponse smartobject) {
		this.smartobject = smartobject;
	}
	
}
