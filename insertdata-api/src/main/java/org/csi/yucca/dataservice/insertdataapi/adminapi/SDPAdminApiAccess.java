/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.adminapi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.client.db.BackofficeDettaglioClientDB;
import org.csi.yucca.adminapi.client.db.BackofficeListaClientDB;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.TenantManagementResponse;
import org.csi.yucca.adminapi.response.TenantResponse;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.TenantType;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.metadata.SDPInsertMetadataApiAccess;
import org.csi.yucca.dataservice.insertdataapi.model.output.CollectionConfDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetInfo;
import org.csi.yucca.dataservice.insertdataapi.model.output.FieldsDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.StreamInfo;

public class SDPAdminApiAccess implements SDPInsertMetadataApiAccess {
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.csi.yucca.datainsert");

	@Override
	public DatasetInfo getInfoDataset(String datasetCode, long datasetVersion,
			String codiceTenant) throws Exception {
		BackofficeDettaglioStreamDatasetResponse dettaglio = getBackofficeDettaglioForDatasetCodeDatasetVersion(
				datasetCode, datasetVersion, true);
		dettaglio = checkTenantCanSendData(dettaglio, codiceTenant);
		dettaglio = checkIsInstalled(dettaglio);
		return SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToDatasetInfo(dettaglio);
	}

	@Override
	public DatasetInfo getInfoDatasetUnchecked(String datasetCode, long datasetVersion,
			String codiceTenant) throws Exception {
		BackofficeDettaglioStreamDatasetResponse dettaglio = getBackofficeDettaglioForDatasetCodeDatasetVersion(
				datasetCode, datasetVersion, true);
//		dettaglio = checkTenantCanSendData(dettaglio, codiceTenant);
//		dettaglio = checkIsInstalled(dettaglio);
		return SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToDatasetInfo(dettaglio);
	}
	
	@Override
	public DatasetInfo getInfoDataset(Long idDataset, Long datasetVersion,
			String codiceTenant) throws Exception {
		BackofficeDettaglioStreamDatasetResponse dettaglio = getBackofficeDettaglioForIdDatasetDatasetVersion(
				idDataset, datasetVersion, true);
		
		dettaglio = checkTenantCanSendData(dettaglio, codiceTenant);
		dettaglio = checkIsInstalled(dettaglio);
		return SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToDatasetInfo(dettaglio);
	}

	private BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioForIdDatasetDatasetVersion(
			Long idDataset, Long datasetVersion, boolean onlyInstalled) throws AdminApiClientException {
		BackofficeDettaglioStreamDatasetResponse dettaglio =  null;
		if (datasetVersion == null || datasetVersion == -1) {
			dettaglio = BackofficeDettaglioClientDB.getBackofficeDettaglioStreamDatasetByIdDataset(
					idDataset.intValue(),onlyInstalled, log.getName());
		}
		else {
			dettaglio = BackofficeDettaglioClientDB.getBackofficeDettaglioStreamDatasetByIdDatasetDatasetVersion(
					idDataset.intValue(), datasetVersion.intValue(), log.getName());
		}
		return dettaglio;
	}
	private BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioForDatasetCodeDatasetVersion(
			String datasetCode, Long datasetVersion, boolean onlyInstalled) throws AdminApiClientException {
		log.debug("[BackofficeDettaglioStreamDatasetResponse::getBackofficeDettaglioForDatasetCodeDatasetVersion]  datasetCode: " + datasetCode +
				" datasetVersion: " + datasetVersion + "onlyInstalled: " + onlyInstalled);

		BackofficeDettaglioStreamDatasetResponse dettaglio =  null;
		if (datasetVersion == null || datasetVersion == -1) {
			dettaglio = BackofficeDettaglioClientDB.getBackofficeDettaglioStreamDatasetByDatasetCode(
					datasetCode, onlyInstalled, log.getName());
		}
		else {
			try {
				dettaglio = BackofficeDettaglioClientDB.getBackofficeDettaglioStreamDatasetByDatasetCodeDatasetVersion(
						datasetCode, datasetVersion.intValue(), log.getName());
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return dettaglio;
	}
	
	@Override
	public ArrayList<FieldsDto> getCampiDataSet(Long idDataset,
			long datasetVersion) throws Exception {
		BackofficeDettaglioStreamDatasetResponse dettaglio = getBackofficeDettaglioForIdDatasetDatasetVersion(
				idDataset, datasetVersion, true);
		return SDPAdminApiConverter.convertComponentToFields(dettaglio.getComponents(), 
				dettaglio.getDataset().getIddataset(), dettaglio.getVersion());
	}

	@Override
	public Set<String> getTenantList() throws Exception { 
		List<TenantManagementResponse> tenants = BackofficeListaClientDB.getTenants(log.getName());
		
		Set<String> tenantsCode = new HashSet<String>();
		
		for (TenantManagementResponse tenant : tenants) {
			/* add tenant code only if it is not uninstalled */
			if (!tenant.getTenantStatus().getIdTenantStatus().equals(Status.UNINSTALLATION.id()) 
					&& 
				!tenant.getTenantType().getIdTenantType().equals(TenantType.ZERO.id())) {
				tenantsCode.add(tenant.getTenantcode());
			}
		}
		return tenantsCode;
	}

	public ArrayList<StreamInfo> getStreamInfo(String tenant, String streamApplication, String sensor) {
		 ArrayList<StreamInfo> infos = new ArrayList<>();
		
		try {
			log.debug("[SDPAdminApiAccess::getStreamInfo]  sensor: " + sensor + " streamApplication: " + streamApplication + 
					"tenant: " + tenant);
			BackofficeDettaglioStreamDatasetResponse dettaglio = BackofficeDettaglioClientDB
					.getBackofficeDettaglioStreamDatasetBySoCodeStreamCode(//SDPInsertApiConfig.getInstance().getAdminApiUrl(),
							sensor, streamApplication,true,  log.getName());

			log.debug("[SDPAdminApiAccess::getStreamInfo]  dettaglio prima: " + dettaglio.getDataset().getDatasetcode());

			dettaglio = checkTenantCanSendData(dettaglio, tenant);
			dettaglio = checkIsInstalled(dettaglio);
			
			StreamInfo streamInfo = SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToStreamInfo(dettaglio);
			log.debug("[SDPAdminApiAccess::getStreamInfo]  dettaglio prima: " + streamInfo.getDatasetCode());

			if (streamInfo!=null)
				infos.add(streamInfo);
			
		} 
		catch (AdminApiClientException nfe) {
			if(nfe.getHttpStatusCode() == HttpStatus.SC_NOT_FOUND){
				log.info("RECORD NOT FOUND", nfe);
				return null;				
			}
		}
		catch (Exception e) {
			log.error("Error", e);
			throw new InsertApiRuntimeException(e);
		}
		
		return infos;
	}

	public StreamInfo getStreamInfoForDataset(String tenant, long idDataset, long datasetVersion) {
		StreamInfo streamInfo = null;
		try {
			BackofficeDettaglioStreamDatasetResponse dettaglio = getBackofficeDettaglioForIdDatasetDatasetVersion(
					idDataset, datasetVersion, true);
			
			dettaglio = checkTenantCanSendData(dettaglio, tenant);
			
			streamInfo = SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToStreamInfo(dettaglio);
			
		} catch (Exception e) {
			log.error("Error", e);
			throw new InsertApiRuntimeException(e);
		}
		
		return streamInfo;
	}


	@Override
	public CollectionConfDto getCollectionInfo(String tenant,
			long idDataset, long datasetVersion,
			String datasetType) {
		BackofficeDettaglioStreamDatasetResponse dettaglio;
		try {
			dettaglio = getBackofficeDettaglioForIdDatasetDatasetVersion(
					idDataset, datasetVersion, true);
		} catch (AdminApiClientException e) {
			log.error("Error", e);
			throw new InsertApiRuntimeException(e);
		}  catch (Exception e) {
			log.error("Error", e);
			throw new InsertApiRuntimeException(e);
		}  catch (Throwable e) {
			log.error("Error", e);
			throw new InsertApiRuntimeException(e);
		}

		
//		dettaglio = checkTenantCanSendData(dettaglio, tenant);
		return SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToCollectionConfDto(dettaglio);
	}

	
	private BackofficeDettaglioStreamDatasetResponse checkTenantCanSendData(
			BackofficeDettaglioStreamDatasetResponse dettaglio, String tenantCode) {
		if (dettaglio!=null)
		{
			if (dettaglio.getTenantManager()==null || dettaglio.getTenantManager().getTenantcode() == null ||  !dettaglio.getTenantManager().getTenantcode().equals(tenantCode)) 
			{
				List<TenantResponse> tenants = dettaglio.getSharingTenants();
				boolean senderFound = false;
				if (tenants!=null)
				{
					for (TenantResponse tenantResponse : tenants) {
						if (tenantResponse.getTenantcode().equals(tenantCode))
						{
							if (tenantResponse.getDataoptions()>=3)
							{
								senderFound = true;
								break;
							}
						}
					}
				}
				if (!senderFound) {
					if (dettaglio.getDataset()!=null)
						log.warn("[SDPAdminApiAccess::checkTenantCanSendData] tenant=["+tenantCode+"] cannot write on dataset=["+dettaglio.getDataset().getDatasetcode()+" ver "+dettaglio.getVersion()+"]");
					else if (dettaglio.getStream()!=null)
						log.warn("[SDPAdminApiAccess::checkTenantCanSendData] tenant=["+tenantCode+"] cannot write on stream=["+dettaglio.getStream().getStreamcode()+" <--> "+dettaglio.getStream().getSmartobject().getSocode()+"]");
					else 
						log.warn("[SDPAdminApiAccess::checkTenantCanSendData] tenant=["+tenantCode+"] cannot write on datasource without dataset or stream!!!!");
					return null;
				}
			}
		}
		return dettaglio;
	}
	public static BackofficeDettaglioStreamDatasetResponse checkIsInstalled(
			BackofficeDettaglioStreamDatasetResponse dettaglio) {
		if (dettaglio!=null)
		{
			if (dettaglio.getStatus().getIdStatus() != 2) {
				if (dettaglio.getDataset()!=null)
					log.warn("[SDPAdminApiAccess::checkIsInstalled] not installed dataset=["+dettaglio.getDataset().getDatasetcode()+" ver "+dettaglio.getVersion()+"]");
				else if (dettaglio.getStream()!=null)
					log.warn("[SDPAdminApiAccess::checkIsInstalled] not installed stream=["+dettaglio.getStream().getStreamcode()+" <--> "+dettaglio.getStream().getSmartobject().getSocode()+"]");
				else 
					log.warn("[SDPAdminApiAccess::checkIsInstalled] not installed datasource without dataset or stream!!!!");

				return null;
			}
		}
		return dettaglio;
	}
}
