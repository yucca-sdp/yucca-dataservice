/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.request.ActionOozieRequest;
import org.csi.yucca.adminapi.request.ActionRequest;
import org.csi.yucca.adminapi.request.AllineamentoScaricoDatasetRequest;
import org.csi.yucca.adminapi.request.DatasetRequest;
import org.csi.yucca.adminapi.request.DatasourcegroupDatasourceRequest;
import org.csi.yucca.adminapi.request.ImportMetadataDatasetRequest;
import org.csi.yucca.adminapi.request.PostDataSourceGroupRequest;
import org.csi.yucca.adminapi.util.ApiUserType;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DatasetService {

	ServiceResponse selectDataSets(Integer groupId, Integer groupVersion) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse updateDatasetHiveParamsFromBackoffice( Integer idDataset, Integer version, DatasetRequest datasetRequest) throws BadRequestException, NotFoundException, Exception; 
	
	ServiceResponse updateDatasetsHiveParamsFromBackoffice( DatasetRequest[] datasetRequestList) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectDataSourceGroupType(String sort) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse insertDatasourcesToDatasourcegroupByIdStream( DatasourcegroupDatasourceRequest postRequest, JwtUser authorizedUser, String organizationCode, String tenantCodeManager) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse actionOnDatasourceGroup(String organizationCode, Long idDatasourcegroup, ActionRequest actionRequest,
			String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deleteDatasourcesgroup( JwtUser authorizedUser,  Long idDatasourcegroup, String organizationCode,
			String tenantCodeManager) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse deleteDatasourcesToDatasourcegroupByIdStream(DatasourcegroupDatasourceRequest postRequest, 
			JwtUser authorizedUser, String organizationCode, String tenantCodeManager) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deleteDatasourcesToDatasourcegroupByIdDataset(DatasourcegroupDatasourceRequest postRequest, 
			JwtUser authorizedUser, String organizationCode, String tenantCodeManager) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertDatasourcesToDatasourcegroupByIdDataset(DatasourcegroupDatasourceRequest postRequest,
			JwtUser authorizedUser,  String organizationCode, String tenantCodeManager) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse updateDatasourceGroup(Long idDatasourcegroup, PostDataSourceGroupRequest dataSourceGroupRequest,
			String organizationCode, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectDataSourceGroupByTenant(String tenantCodeManager, String organizationCode,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDataSourceGroupByTenant(String tenantCodeManager, String organizationCode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDataSourceGroupById(String organizationCode, Long idDatasourcegroup, String tenantCodeManager,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertDataSourceGroup(PostDataSourceGroupRequest postDataSourceGroupRequest, 
			String organizationCode, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectIngestionConfiguration(Integer idDatasourcegroup, Integer datasourcegroupversion, String tenantCode, String dbname, String dateformat, String separator, Boolean onlyImported, Boolean help,
			HttpServletResponse httpServletResponse) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectAllineamentoScaricoDataset(Integer idOrganization) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse insertLastMongoObjectId(AllineamentoScaricoDatasetRequest request, Integer idOrganization) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectAllineamentoScaricoDataset(Integer idOrganization, Integer idDataset, Integer datasetVersion) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse deleteDatasetData(String organizationCode, Integer idDataset, String tenantCodeManager, Integer version, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception;

	ServiceResponse deleteDatasetData(Integer idDataset, Integer version)
			throws BadRequestException, NotFoundException, Exception;

	ServiceResponse uninstallingDatasets(String organizationCode, Integer idDataset, Boolean publish, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByOrganizationCode(String organizationCode, Boolean iconRequested) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByTenantCode(String tenanntCode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasets(Integer groupId, String organizationCode, String tenantCodeManager, String sort, JwtUser authorizedUser, Boolean includeShared) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectDatasetsSlim(Boolean isSlim, String tenantCode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDataset(String organizationCode, Integer idDataset, String tenantCodeManager, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByIdDataset(Integer idDataset, boolean onlyInstalled) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByIdDatasetDatasetVersion(Integer idDataset, Integer datasetVersion) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByDatasetCodeDatasetVersion(String datasetCode, Integer datasetVersion) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByDatasetCode(String datasetCode, boolean onlyInstalled) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse insertDataset(String organizationCode, Boolean publish, DatasetRequest postDatasetRequest, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertDataset(String organizationCode, Boolean publish, DatasetRequest postDatasetRequest)
			throws BadRequestException, NotFoundException, Exception;

	ServiceResponse updateDataset(String organizationCode, Integer idDataset, DatasetRequest datasetRequest, String tenantCodeManager, JwtUser authorizedUser, Boolean publish)
			throws BadRequestException, NotFoundException, Exception;

	ServiceResponse insertCSVData(MultipartFile file, Boolean skipFirstRow, String encoding, String csvSeparator, String componentInfoRequests, String organizationCode,
			Integer idDataset, String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse importMetadata(ServletContext servletContext, String organizationCode, ImportMetadataDatasetRequest importMetadataRequest, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse importMetadata(ServletContext servletContext, String organizationCode, ImportMetadataDatasetRequest importMetadataRequest)
			throws BadRequestException, NotFoundException, Exception;

	byte[] selectDatasetIcon(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	byte[] selectDatasetIcon(String datasetCode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse updateHiveExternalTable(String tableName, String organizationCode, Integer idDataset, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception;

	ServiceResponse hdfsFiles(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse rangerPolicies(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse createRangerPolicy(String organizationCode, Integer idDataset, JwtUser authorizedUser ) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse updateRangerPolicy(String organizationCode, Integer idDataset, JwtUser authorizedUser ) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse forceDownloadCsv(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByDatasetGroup(String organizationCode, Long idDatasourcegroup,
			Integer datasetGroupVersion, String tenantCodeManager,  String sort, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse updateDatasetFromBackoffice(String organizationCode, Integer idDataset, DatasetRequest datasetRequest, String tenantCodeManager, Boolean publish) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse actionOnOozie(ActionOozieRequest actionOozieRequest) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse infoOnOozie(String oozieProcessId) throws BadRequestException, NotFoundException, Exception;

}
