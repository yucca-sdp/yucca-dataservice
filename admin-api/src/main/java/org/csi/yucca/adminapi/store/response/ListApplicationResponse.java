/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.store.response;

public class ListApplicationResponse {
	private boolean error;
	private Application[] applications;

	public ListApplicationResponse() {

	}

	public boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Application[] getApplications() {
		return applications;
	}

	public void setApplication(Application[] applications) {
		this.applications = applications;
	}
}
