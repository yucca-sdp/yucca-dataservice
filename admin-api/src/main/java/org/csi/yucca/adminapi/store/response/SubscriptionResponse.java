/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.store.response;

public class SubscriptionResponse {
	private boolean error;
	private Subscriptions[] subscriptions;

	public SubscriptionResponse() {

	}

	public boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Subscriptions[] getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Subscriptions[] subscriptions) {
		this.subscriptions = subscriptions;
	}
}
