/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.store.response;

public class Subs {

	private String application;
	private int applicationId;
	private String prodKey;
	private String sandboxKey;
	
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public int getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	public String getProdKey() {
		return prodKey;
	}
	public void setProdKey(String prodKey) {
		this.prodKey = prodKey;
	}
	public String getSandboxKey() {
		return sandboxKey;
	}
	public void setSandboxKey(String sandboxKey) {
		this.sandboxKey = sandboxKey;
	}
}
