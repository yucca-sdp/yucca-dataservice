/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.client.db;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.util.Errors;

public class BackofficeClientDB {

	/**
	 * 
	 * @author gianfranco.stolfa
	 *
	 */
	public interface CacheDBCallable {
		Object get() throws Exception;
	}

	/**
	 * 
	 * @param logger
	 * @param callable
	 * @return
	 * @throws AdminApiClientException
	 */
	public static Object getResponse(Logger log, String logger, CacheDBCallable callable)
			throws AdminApiClientException {

		log.debug("BEGIN CLIENT ADMIN: " + logger);

		try {

			long lStartTime = System.nanoTime();

			Object response = callable.get();
			
			long lEndTime = System.nanoTime();
			Double duration = new Double(lEndTime - lStartTime)/1000000;
			
			if (response == null) {
				throw new NotFoundException(Errors.RECORD_NOT_FOUND);
			}

			log.debug("EXECUTION TIME: " + duration);

			return response;
		} 
		catch (NotFoundException nfe) {
			AdminApiClientException aae = new AdminApiClientException(HttpStatus.SC_NOT_FOUND);
			throw aae;
		}
		catch (Exception e) {
			log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}

}
