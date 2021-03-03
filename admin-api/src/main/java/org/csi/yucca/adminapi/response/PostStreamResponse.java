/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PostStreamResponse {

	private Integer idStream;
	private String streamcode;
	private String streamname;

	public static PostStreamResponse build(Integer idStream){
		PostStreamResponse response = new PostStreamResponse();
		return response.idStream(idStream);
	}
	
	public PostStreamResponse idStream (Integer idStream){
		this.idStream = idStream;
		return this;
	}
	
	public PostStreamResponse streamcode (String streamcode){
		this.streamcode = streamcode;
		return this;
	}
	
	public PostStreamResponse streamname (String streamname){
		this.streamname = streamname;
		return this;
	}
	
	public Integer getIdStream() {
		return idStream;
	}
	public void setIdStream(Integer idStream) {
		this.idStream = idStream;
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
	
	
	
	
	
}
