/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate.beans.httpresponse;

import java.util.List;

public class TenantProductContactDHttpResponse {

	private Integer __count;

	private List<TenantProductContactResultHttpResponse> results;

	private String __next;

	public String get__next() {
		return __next;
	}

	public void set__next(String __next) {
		this.__next = __next;
	}

	public Integer get__count() {
		return __count;
	}

	public void set__count(Integer __count) {
		this.__count = __count;
	}

	public List<TenantProductContactResultHttpResponse> getResults() {
		return results;
	}

	public void setResults(List<TenantProductContactResultHttpResponse> results) {
		this.results = results;
	}

}
