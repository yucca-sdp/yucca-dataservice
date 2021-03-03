/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.client.db;

import java.util.List;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.OrganizationResponse;
import org.csi.yucca.adminapi.response.TenantManagementResponse;

public class BackofficeListaClientDB {
	
	static Logger LOG = Logger.getLogger(BackofficeListaClientDB.class);
	
	/**
	 * 
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	@SuppressWarnings("unchecked")
	public static List<TenantManagementResponse> getTenants(String logger) throws AdminApiClientException {
		
		return (List<TenantManagementResponse>)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getTenants(new KeyCacheDB());
			}
		});

	}

	/**
	 * 
	 * @param organizationCode
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	@SuppressWarnings("unchecked")
	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDataset(final String organizationCode, String logger) throws AdminApiClientException {

		return (List<BackofficeDettaglioStreamDatasetResponse>)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getListStreamDataset(new KeyCacheDB().organizationCode(organizationCode));
			}
		});

	}
	
	/**
	 * 
	 * @param organizationCode
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	@SuppressWarnings("unchecked")
	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDatasetByIconVisibility(final String organizationCode, String logger, final Boolean iconRequested) throws AdminApiClientException {

		return (List<BackofficeDettaglioStreamDatasetResponse>)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				KeyCacheDB keycacheDb = new KeyCacheDB();
				keycacheDb.organizationCode(organizationCode);
				keycacheDb.iconRequested(iconRequested);
				//return CacheUtilDB.getListStreamDatasetIconVisibility(new KeyCacheDB().organizationCode(organizationCode),new KeyCacheDB().iconRequested(iconRequested));
				return CacheUtilDB.getListStreamDatasetIconVisibility(keycacheDb);
			}
		});

	}
	
	/**
	 * 
	 * @param idOrganization
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	@SuppressWarnings("unchecked")
	public static List<AllineamentoScaricoDatasetResponse> getAllineamentoByIdOrganization(final Integer idOrganization, String logger) throws AdminApiClientException {
	
		return (List<AllineamentoScaricoDatasetResponse>)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getAllineamento(new KeyCacheDB().idOrganization(idOrganization));
			}
		});

	}
	
	/**
	 * 
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	@SuppressWarnings("unchecked")
	public static List<OrganizationResponse> getOrganizations(String logger)
			throws AdminApiClientException {
		
		return (List<OrganizationResponse>)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getOrganizations(new KeyCacheDB());
			}
		});
		
	}
	

}