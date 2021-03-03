/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.client;

import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;

public class BackOfficeCreateClient {
	
	public static AllineamentoScaricoDatasetResponse createOrUpdateAllineamento(String adminApiBaseUrl, String json, Integer idOrganization, String logger) throws AdminApiClientException {
		return AdminApiClientDelegate.postFromAdminApi(AllineamentoScaricoDatasetResponse.class, adminApiBaseUrl + "/1/backoffice/allineamento/idOrganization="+idOrganization, logger, json);
	}
	
//	TEST
//	public static void main(String [] args){
//		try {
//			
//			
//			String json = "{\"idDataset\":41521,\"datasetVersion\":7,\"lastMongoObjectId\":\"ciccio\"}";
//			AllineamentoScaricoDatasetResponse allineamentoResponse = createOrUpdateAllineamento(
//					"@@ws.url@@", json, 33, "");
//			String stop = "";
//			stop = "";
//			
//		} 
//		catch (Exception e) {
//			String stop = "";
//			stop = "";
//		}
//	}
	
}