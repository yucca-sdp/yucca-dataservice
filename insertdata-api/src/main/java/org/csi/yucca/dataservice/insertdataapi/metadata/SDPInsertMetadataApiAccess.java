/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.metadata;

import java.util.ArrayList;
import java.util.Set;

import org.csi.yucca.dataservice.insertdataapi.model.output.CollectionConfDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetInfo;
import org.csi.yucca.dataservice.insertdataapi.model.output.FieldsDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.StreamInfo;

public interface SDPInsertMetadataApiAccess {

	public abstract DatasetInfo getInfoDataset(String datasetCode,
			long datasetVersion, String codiceTenant) throws Exception;

	public abstract DatasetInfo getInfoDataset(Long idDataset,
			Long datasetVersion, String codiceTenant) throws Exception;

	public abstract ArrayList<FieldsDto> getCampiDataSet(Long idDataset,
			long datasetVersion) throws Exception;

	public abstract Set<String> getTenantList() throws Exception;

	public abstract ArrayList<StreamInfo> getStreamInfo(String tenant, String streamApplication, String sensor);

	public abstract StreamInfo getStreamInfoForDataset(String tenant, long idDataset,
			long datasetVersion);

	public abstract CollectionConfDto getCollectionInfo(String tenant,
			long idDatasetTrovato, long datasetVersionTrovato,
			String datasetType);

	public abstract DatasetInfo getInfoDatasetUnchecked(String datasetCode, long datasetVersion, String codiceTenant) throws Exception;
}