/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package it.csi.smartdata.dataapi.exception;

import java.util.Locale;

import org.apache.olingo.odata2.api.exception.ODataApplicationException;

public class SDPOrderBySizeException extends ODataApplicationException {

	  public SDPOrderBySizeException(String message, Locale locale) {
		super(message, locale);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;


}
