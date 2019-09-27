/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.exception;

public class TenantListAccessException extends Exception {

	private static final long serialVersionUID = 1L;

	public TenantListAccessException(Throwable e) {
		initCause(e);
	}
}
