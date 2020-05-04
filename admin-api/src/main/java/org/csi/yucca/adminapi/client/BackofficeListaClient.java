/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.client;

import java.util.List;

import org.csi.yucca.adminapi.client.cache.CacheUtilNone;
import org.csi.yucca.adminapi.client.cache.key.KeyCache;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.DataSourceGroupResponse;
import org.csi.yucca.adminapi.response.OrganizationResponse;
import org.csi.yucca.adminapi.response.TenantManagementResponse;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

public class BackofficeListaClient {

	public static List<TenantManagementResponse> getTenants(String adminApiBaseUrl, String logger)
			throws AdminApiClientException {
		try {
//			return CacheUtil2k.getTenants(new KeyCache(adminApiBaseUrl, logger));	
			return CacheUtilNone.getTenants(new KeyCache(adminApiBaseUrl, logger));	
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
		
	}

	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDataset(String adminApiBaseUrl,
			String organizationCode, String logger) throws AdminApiClientException {
		try {
			return CacheUtilNone.getListStreamDataset(new KeyCache(adminApiBaseUrl, logger).code(organizationCode));	
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
		
	}
	
	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDatasetByIconVisibility(String adminApiBaseUrl,
			String organizationCode, String logger, Boolean iconRequested) throws AdminApiClientException {
		try {
			KeyCache keycache = new KeyCache(adminApiBaseUrl, logger);
			keycache.code(organizationCode);
			keycache.iconRequested(iconRequested);
			//return CacheUtilNone.getListStreamDatasetByIconVisibility(new KeyCache(adminApiBaseUrl, logger).code(organizationCode));	
			return CacheUtilNone.getListStreamDatasetByIconVisibility(keycache);
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
		
	}

	public static List<OrganizationResponse> getOrganizations(String adminApiBaseUrl, String logger)
			throws AdminApiClientException {
		
		try {
			return CacheUtilNone.getOrganizations(new KeyCache(adminApiBaseUrl, logger));	
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}		
		
	}

	public static List<AllineamentoScaricoDatasetResponse> getAllineamentoByIdOrganization(String adminApiBaseUrl,
			Integer idOrganization, String logger) throws AdminApiClientException {
		try {
			return CacheUtilNone.getAllineamento(new KeyCache(adminApiBaseUrl, logger).id(idOrganization));
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDatasetByTenantCode(String adminApiBaseUrl,
			String tenantCode, String logger) throws AdminApiClientException {
		try {
			return CacheUtilNone.getListStreamDatasetByTenantCode(new KeyCache(adminApiBaseUrl, logger).code(tenantCode));	
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
		
	}
	
	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDatasetByGroupIdVersion(String adminApiBaseUrl,
			Integer groupId, Integer version, String logger) throws AdminApiClientException {
		try {
			return CacheUtilNone.getListStreamDatasetByGroupIdVersion(new KeyCache(adminApiBaseUrl, logger), groupId, version);	
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
		
	}
	
	public static List<DataSourceGroupResponse> getListDatasourcegropsByTenant(String adminApiBaseUrl, String organizationCode,
			String tenantCode, String logger) throws AdminApiClientException {
		try {
			KeyCache keyCache = new KeyCache(adminApiBaseUrl, logger);
			keyCache.addParams("tenantCodeManager", tenantCode);
			return CacheUtilNone.getListDatasourcegropsByTenant(keyCache,organizationCode );	
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
		
	}


}