/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.client.db;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.MeasureUnitResponse;

public class BackofficeDettaglioClientDB {

	static Logger LOG = Logger.getLogger(BackofficeDettaglioClientDB.class);
	
//	public static void main(String[] args) {
//		String stop = "";
//		try {
			
//			BackofficeDettaglioApiResponse mresponse = getBackofficeDettaglioApi("ds_Stresstwt_11_241", "ds_TrFl_3");
			
//			for (int i = 0; i < 7; i++) {
//				
//				stop = "";
//				
//				BackofficeDettaglioStreamDatasetResponse response = getBackofficeDettaglioStreamDatasetByIdStream(124,
//						false, "ciccio");
//				
//				stop = "";
//				
//				if(i>3){
//					stop = "";
//					BackofficeDettaglioStreamDatasetResponse response3 = getBackofficeDettaglioStreamDatasetByIdStream(125,
//					false, "ciccio");
//					stop = "";
//				}
//				
//			}
			
//			stop = "";
//			BackofficeDettaglioStreamDatasetResponse response = getBackofficeDettaglioStreamDatasetByIdStream(124,
//					false, "ciccio");
//			stop = "";

//			BackofficeDettaglioStreamDatasetResponse response2 = getBackofficeDettaglioStreamDatasetBySoCodeStreamCode(
//					"35fcb755-d518-41cf-8938-5f11f85db425", "ProvaDavideOData4", false, "ciccio");

//			stop = "";
//
//			BackofficeDettaglioStreamDatasetResponse response3 = getBackofficeDettaglioStreamDatasetByIdStream(124,
//					false, "ciccio");
//
//			stop = "";

//		} catch (Exception e) {
//			stop = "";
//		}
//
//	}

//	public static void main(String[] args) {
//
//		String stop = "";
//		
//		try {
//			
//			for (int i = 0; i < 1000; i++) {
//				
//				try {
//					
//					MeasureUnitResponse measureUnitResponse = getMeasureUnit(57, "my_log");
//
//					if (measureUnitResponse != null) {
//						System.out.println("ID: " + measureUnitResponse.getIdMeasureUnit());
//						System.out.println("Measureunit: " + measureUnitResponse.getMeasureunit());
//						System.out.println("Measureunit Category: " + measureUnitResponse.getMeasureunitcategory());
//					}
//					else{
//						System.out.println("E' NULLO");
//					}
//					
//					stop = "";
//					
//				} 
//				catch (Exception e) {
//					System.out.println("ECCEZIONE NOT FOUND");
//				}
//				
//				TimeUnit.SECONDS.sleep(2);
//			}
//		} 
//		catch (Exception e) {
//			System.out.println(e.toString());
//		}
//
//		stop = "";
//
//		
//
//		
//	}
	
	
	/**
	 * 
	 * @param idMeasureUnit
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static MeasureUnitResponse getMeasureUnit(final Integer idMeasureUnit, String logger) throws AdminApiClientException {
		
		return (MeasureUnitResponse)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getMeasureUnit(new KeyCacheDB().idMeasureUnit(idMeasureUnit));
			}
		});

	}

	
	/**
	 * 
	 * @param idDataset
	 * @param datasetVersion
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdDatasetDatasetVersion(
			final Integer idDataset, final Integer datasetVersion, String logger)
			throws AdminApiClientException {

		return (BackofficeDettaglioStreamDatasetResponse)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getDettaglioStreamDatasetByIdDatasetDatasetVersion(new KeyCacheDB().idDataset(idDataset).datasetVersion(datasetVersion));

			}
		});

	}
	
	
	/**
	 * 
	 * @param datasetCode
	 * @param datasetVersion
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByDatasetCodeDatasetVersion(
			final String datasetCode, final Integer datasetVersion, String logger) throws AdminApiClientException {
		
		return (BackofficeDettaglioStreamDatasetResponse)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getDettaglioStreamDatasetByDatasetCodeDatasetVersion(new KeyCacheDB().datasetCode(datasetCode).datasetVersion(datasetVersion));
			}
		});
		
	}
	
	
	/**
	 * 
	 * @param idDataset
	 * @param onlyInstalled
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdDataset(
			final Integer idDataset, final Boolean onlyInstalled, String logger)
			throws AdminApiClientException {
		
		return (BackofficeDettaglioStreamDatasetResponse)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getDettaglioStreamDatasetByIdDataset(new KeyCacheDB().idDataset(idDataset).onlyInstalled(onlyInstalled));
			}
		});
		
	}
	
	/**
	 * 
	 * @param datasetCode
	 * @param onlyInstalled
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByDatasetCode(
			final String datasetCode, final Boolean onlyInstalled, String logger)
			throws AdminApiClientException {

		return (BackofficeDettaglioStreamDatasetResponse)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getDettaglioStreamDatasetByDatasetCode(new KeyCacheDB().datasetCode(datasetCode).onlyInstalled(onlyInstalled));
			}
		});

	}
	
	/**
	 * OK
	 * @param idStream
	 * @param onlyInstalled
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdStream(
			final Integer idStream, final Boolean onlyInstalled, String logger) throws AdminApiClientException {
		
		return (BackofficeDettaglioStreamDatasetResponse)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getDettaglioStreamDataset(new KeyCacheDB().idStream(idStream).onlyInstalled(onlyInstalled));
			}
		});

	}
	
	/**
	 * 
	 * @param soCode
	 * @param streamCode
	 * @param onlyInstalled
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetBySoCodeStreamCode(
			final String soCode, final String streamCode, final Boolean onlyInstalled, String logger) throws AdminApiClientException {

		return (BackofficeDettaglioStreamDatasetResponse)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getDettaglioStreamDatasetBySO(new KeyCacheDB()
						.smartObjectCode(soCode).streamCode(streamCode).onlyInstalled(onlyInstalled));
			}
		});

	}
	
	/**
	 * 
	 * @param codApi
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioApiResponse getBackofficeDettaglioApi(
			final String codApi, String logger) throws AdminApiClientException {
		
		return (BackofficeDettaglioApiResponse)BackofficeClientDB.getResponse(LOG, logger, new BackofficeClientDB.CacheDBCallable() {
			@Override
			public Object get() throws Exception {
				return CacheUtilDB.getDettaglioApi(new KeyCacheDB().apiCode(codApi));
			}
		});
		
		

	}

}
