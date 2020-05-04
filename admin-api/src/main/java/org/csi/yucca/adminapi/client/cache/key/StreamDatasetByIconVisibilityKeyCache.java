/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.client.cache.key;

public class StreamDatasetByIconVisibilityKeyCache extends KeyCache{

	private static final long serialVersionUID = -4105435957077841078L;

	private Boolean iconRequested;


	public StreamDatasetByIconVisibilityKeyCache iconRequested(Boolean iconRequested){
		this.iconRequested = iconRequested;
		return this;
	}

	
	public StreamDatasetByIconVisibilityKeyCache(String adminBaseUrl, String logger) {
		super(adminBaseUrl, logger);
	}

	@Override
	public StreamDatasetByIconVisibilityKeyCache addParams(String key, String value) {
		return (StreamDatasetByIconVisibilityKeyCache)super.addParams(key, value);
	}
	
	@Override
	public StreamDatasetByIconVisibilityKeyCache addParams(String key, Boolean value) {
		return (StreamDatasetByIconVisibilityKeyCache)super.addParams(key, value);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iconRequested == null) ? 0 : iconRequested.hashCode());
		return result + super.hashCode();

	}

	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		StreamDatasetByIconVisibilityKeyCache other = (StreamDatasetByIconVisibilityKeyCache) obj;

		if (iconRequested == null) {
			if (other.iconRequested != null) return false;
		} 
		else if (!iconRequested.equals(other.iconRequested))
			return false;

		return super.equals(obj);
	}
	
}
