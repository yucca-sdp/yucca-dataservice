/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.service.impl;

import static org.csi.yucca.adminapi.util.Constants.MAX_ODATA_RESULT_PER_PAGE;
import static org.csi.yucca.adminapi.util.ServiceUtil.API_SUBTYPE_ODATA;
import static org.csi.yucca.adminapi.util.ServiceUtil.DATASOURCE_VERSION;
import static org.csi.yucca.adminapi.util.ServiceUtil.MULTI_SUBDOMAIN_ID_DOMAIN;
import static org.csi.yucca.adminapi.util.ServiceUtil.MULTI_SUBDOMAIN_PATTERN;
import static org.csi.yucca.adminapi.util.ServiceUtil.buildResponse;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkAuthTenant;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkComponents;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkIfFoundRecord;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkLicense;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkList;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkMandatoryParameter;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkTenant;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkVisibility;
import static org.csi.yucca.adminapi.util.ServiceUtil.getSortList;
import static org.csi.yucca.adminapi.util.ServiceUtil.getTenantCodeListFromUser;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertBinaryComponents;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertComponents;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertDataSource;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertDcat;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertLicense;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertSharingTenants;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertTags;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertTenantDataSource;
import static org.csi.yucca.adminapi.util.ServiceUtil.maximumLimitErrorsReached;
import static org.csi.yucca.adminapi.util.ServiceUtil.updateDataSource;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.delegate.HdfsDelegate;
import org.csi.yucca.adminapi.delegate.HiveDelegate;
import org.csi.yucca.adminapi.delegate.HttpDelegate;
import org.csi.yucca.adminapi.delegate.OozieDelegate;
import org.csi.yucca.adminapi.delegate.PublisherDelegate;
import org.csi.yucca.adminapi.delegate.RangerDelegate;
import org.csi.yucca.adminapi.delegate.SolrDelegate;
import org.csi.yucca.adminapi.delegate.StoreDelegate;
import org.csi.yucca.adminapi.delegate.beans.hdfs.FileStatusesContainer;
import org.csi.yucca.adminapi.delegate.beans.ranger.PolicyItemRequest;
import org.csi.yucca.adminapi.delegate.beans.ranger.PostRangerRequest;
import org.csi.yucca.adminapi.delegate.beans.ranger.ResourceRangerRequest;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.importmetadata.DatabaseConfiguration;
import org.csi.yucca.adminapi.importmetadata.DatabaseReader;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.mapper.AllineamentoScaricoDatasetMapper;
import org.csi.yucca.adminapi.mapper.ApiMapper;
import org.csi.yucca.adminapi.mapper.BundlesMapper;
import org.csi.yucca.adminapi.mapper.ComponentMapper;
import org.csi.yucca.adminapi.mapper.DataSourceGroupMapper;
import org.csi.yucca.adminapi.mapper.DataSourceMapper;
import org.csi.yucca.adminapi.mapper.DatasetMapper;
import org.csi.yucca.adminapi.mapper.DcatMapper;
import org.csi.yucca.adminapi.mapper.LicenseMapper;
import org.csi.yucca.adminapi.mapper.OrganizationMapper;
import org.csi.yucca.adminapi.mapper.SequenceMapper;
import org.csi.yucca.adminapi.mapper.SmartobjectMapper;
import org.csi.yucca.adminapi.mapper.StreamMapper;
import org.csi.yucca.adminapi.mapper.SubdomainMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.mapper.UserMapper;
import org.csi.yucca.adminapi.model.AllineamentoScaricoDataset;
import org.csi.yucca.adminapi.model.Api;
import org.csi.yucca.adminapi.model.Bundles;
import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.model.DataSource;
import org.csi.yucca.adminapi.model.DataSourceGroup;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.Dettaglio;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.InternalDettaglioStream;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.SharingTenantsJson;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.model.User;
import org.csi.yucca.adminapi.model.builder.AllineamentoScaricoDatasetBuilder;
import org.csi.yucca.adminapi.model.builder.DataSourceGroupBuilder;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.model.join.IngestionConfiguration;
import org.csi.yucca.adminapi.request.ActionOozieRequest;
import org.csi.yucca.adminapi.request.ActionRequest;
import org.csi.yucca.adminapi.request.AllineamentoScaricoDatasetRequest;
import org.csi.yucca.adminapi.request.ComponentInfoRequest;
import org.csi.yucca.adminapi.request.ComponentRequest;
import org.csi.yucca.adminapi.request.DataSourceRequest;
import org.csi.yucca.adminapi.request.DatasetRequest;
import org.csi.yucca.adminapi.request.DatasourcegroupDatasourceRequest;
import org.csi.yucca.adminapi.request.ImportMetadataDatasetRequest;
import org.csi.yucca.adminapi.request.InvioCsvRequest;
import org.csi.yucca.adminapi.request.PostDataSourceGroupRequest;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.DataSourceGroupResponse;
import org.csi.yucca.adminapi.response.DataSourceGroupTypeResponse;
import org.csi.yucca.adminapi.response.DatasetResponse;
import org.csi.yucca.adminapi.response.DettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.IngestionConfigurationResponse;
import org.csi.yucca.adminapi.response.PostDatasetResponse;
import org.csi.yucca.adminapi.response.TenantResponse;
import org.csi.yucca.adminapi.response.builder.AllineamentoScaricoDatasetResponseBuilder;
import org.csi.yucca.adminapi.response.builder.DataSourceGroupResponseBuilder;
import org.csi.yucca.adminapi.response.builder.IngestionConfigurationResponseBuilder;
import org.csi.yucca.adminapi.service.DatasetService;
import org.csi.yucca.adminapi.service.MailService;
import org.csi.yucca.adminapi.store.response.Subs;
import org.csi.yucca.adminapi.store.response.SubscriptionByUsernameResponse;
import org.csi.yucca.adminapi.util.Action;
import org.csi.yucca.adminapi.util.DataOption;
import org.csi.yucca.adminapi.util.DataSourceGroupType;
import org.csi.yucca.adminapi.util.DataType;
import org.csi.yucca.adminapi.util.DatasetSubtype;
import org.csi.yucca.adminapi.util.DatasourceGroupStatus;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ManageOption;
import org.csi.yucca.adminapi.util.ObjectId;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.Util;
import org.csi.yucca.adminapi.util.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONValue;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Configuration
@PropertySources({ @PropertySource("classpath:adminapi.properties"), @PropertySource("classpath:adminapiSecret.properties") })
public class DatasetServiceImpl implements DatasetService {

	private static final Logger logger = Logger.getLogger(DatasetServiceImpl.class);
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private DataSourceGroupMapper dataSourceGroupMapper;
	
	@Autowired
	private DatasetMapper datasetMapper;

	@Autowired
	private TenantMapper tenantMapper;

	@Autowired
	private OrganizationMapper organizationMapper;

	@Autowired
	private DataSourceMapper dataSourceMapper;

	@Autowired
	private LicenseMapper licenseMapper;

	@Autowired
	private DcatMapper dcatMapper;

	@Autowired
	private SequenceMapper sequenceMapper;

	@Autowired
	private ComponentMapper componentMapper;

	@Autowired
	private ApiMapper apiMapper;

	@Autowired
	private SubdomainMapper subdomainMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private StreamMapper streamMapper;

	@Autowired
	private SmartobjectMapper smartobjectMapper;

	@Autowired
	private BundlesMapper bundlesMapper;

	@Autowired
	private AllineamentoScaricoDatasetMapper allineamentoScaricoDatasetMapper;

	@Autowired
	private MailService mailService;
	
	@Value("${hive.jdbc.user}")
	private String hiveUser;

	@Value("${hive.jdbc.password}")
	private String hivePassword;

	@Value("${hive.jdbc.url}")
	private String hiveUrl;

	@Value("${datainsert.base.url}")
	private String datainsertBaseUrl;

	@Value("${datainsert.delete.url}")
	private String datainsertDeleteUrl;

	@Value("${oozie.url}")
	private String oozieUrl;

	@Value("${adminapi.url}")
	private String adminapiUrl;

	@Value("${knox.user.batch}")
	private String knoxUserBatch;
	
	@Value("${ranger.service}")
	private String service;
	
	@Value("${userportal.url}")
	private String userportalUrl;
	
	
	
	
	public interface DatasourceGroupRemover{
		int perform(DatasourcegroupDatasourceRequest postRequest);
	}
	public interface DatasourceGroupAdder{
		int perform(DatasourcegroupDatasourceRequest postRequest, DataSourceGroup dataSourceGroup);
	}
	
	@Override
	public ServiceResponse selectDataSets(Integer groupId, Integer groupVersion)
			throws BadRequestException, NotFoundException, Exception {
		
		List<BackofficeDettaglioStreamDatasetResponse> response = new ArrayList<>();
		
		List<Dettaglio> datasets = datasetMapper.selectDatasetsByGroup(groupId, groupVersion);

		List<DettaglioDataset> dettaglioDatasets = new ArrayList<>();
		
		for (Dettaglio dataset : Util.nullGuard(datasets)) {
			DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasource(dataset.getIdDataSource(), dataset.getDatasourceversion());
			/* recupera i gruppi a cui sono associati i dataset */
			List<DataSourceGroup> modelGroups = dataSourceGroupMapper.selectDataSourceGroupByIdDatasetAndDatasetVersion(dettaglioDataset.getIddataset(), 
					dettaglioDataset.getDatasourceversion());
			List<DataSourceGroupResponse> responceGroups = new ArrayList<>();  
			if (modelGroups != null) {
				for (DataSourceGroup dataSourceGroup : modelGroups) {
					responceGroups.add(new DataSourceGroupResponseBuilder(dataSourceGroup).build());
				}
			}
			BackofficeDettaglioStreamDatasetResponse dettaglioStreamDataset = ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper);
			dettaglioStreamDataset.setGroups(responceGroups);
			response.add(dettaglioStreamDataset);
			//dettaglioDatasets.add(dettaglioDataset);
		}
		
		/*for (DettaglioDataset dettaglioDataset : dettaglioDatasets) {
			response.add(ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper));
		}*/
		
		return buildResponse(response);
	}
	
	@Override
	public ServiceResponse updateDatasetHiveParamsFromBackoffice( Integer idDataset, Integer version, DatasetRequest datasetRequest) throws BadRequestException, NotFoundException, Exception {
		
		datasetMapper.updateDatasetHive(
				Util.booleanToInt(datasetRequest.getAvailablehive()), 
				Util.booleanToInt(datasetRequest.getAvailablespeed()), 
				Util.booleanToInt(datasetRequest.getIstransformed()), 
				datasetRequest.getDbhiveschema(), 
				datasetRequest.getDbhivetable(), 
				idDataset, 
				version);

		return ServiceResponse.build().NO_CONTENT();
	}
	
	@Override
	public ServiceResponse updateDatasetsHiveParamsFromBackoffice( DatasetRequest[] datasetRequestList) throws BadRequestException, NotFoundException, Exception {
		
	for (DatasetRequest datasetRequest:datasetRequestList)	{
		datasetMapper.updateDatasetHive(
				Util.booleanToInt(datasetRequest.getAvailablehive()), 
				Util.booleanToInt(datasetRequest.getAvailablespeed()), 
				Util.booleanToInt(datasetRequest.getIstransformed()), 
				datasetRequest.getDbhiveschema(), 
				datasetRequest.getDbhivetable(),
				datasetRequest.getIddataset(),
				datasetRequest.getCurrentDataSourceVersion());
	}	
		

		return ServiceResponse.build().NO_CONTENT();
	}
	
	@Override
	public ServiceResponse selectDataSourceGroupType(String sort)throws BadRequestException, NotFoundException, Exception {

		List<String> sortList = ServiceUtil.getSortList(sort, org.csi.yucca.adminapi.model.DataSourceGroupType.class);
		
		List<org.csi.yucca.adminapi.model.DataSourceGroupType> modelList = dataSourceGroupMapper.selectDatasourcegroupType(sortList);
		
		ServiceUtil.checkList(modelList);
		
		return ServiceUtil.buildResponse(ServiceUtil.getResponseList(modelList, DataSourceGroupTypeResponse.class));
	}
	
	@Override
	public ServiceResponse insertDatasourcesToDatasourcegroupByIdStream( DatasourcegroupDatasourceRequest postRequest, JwtUser authorizedUser, String organizationCode, String tenantCodeManager) throws BadRequestException, NotFoundException, Exception {
		
		DatasourceGroupAdder adder = new DatasourceGroupAdder() {
			@Override
			public int perform(DatasourcegroupDatasourceRequest postRequest, DataSourceGroup dataSourceGroup) {
				int count = 0;
				for (DataSourceRequest dataSourceRequest : postRequest.getDatasources()) {
					
					count += dataSourceGroupMapper.insertDatasourceDatasourcegroupByIdStream(
							postRequest.getIdDatasourceGroup(), 
							dataSourceGroup.getDatasourcegroupversion(), 
							dataSourceRequest.getDatasourceversion(), 
							dataSourceRequest.getIdStream());
				}

				return count;
			}
		};
		
		return insertDatasourcesToDatasourcegroup( postRequest, authorizedUser, organizationCode, tenantCodeManager, adder);
	}

	
	/**
	 * 
	 * @param postRequest
	 * @param authorizedUser
	 * @param organizationCode
	 * @param tenantCodeManager
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	@Override
	public ServiceResponse insertDatasourcesToDatasourcegroupByIdDataset( DatasourcegroupDatasourceRequest postRequest, JwtUser authorizedUser, String organizationCode, String tenantCodeManager) throws BadRequestException, NotFoundException, Exception {
		
		DatasourceGroupAdder adder = new DatasourceGroupAdder() {
			@Override
			public int perform(DatasourcegroupDatasourceRequest postRequest, DataSourceGroup dataSourceGroup) {
				int count = 0;
				for (DataSourceRequest dataSourceRequest : postRequest.getDatasources()) {
					Integer existing = dataSourceGroupMapper.selectCountDatasourceDatasourcegroupByIdDataSet(postRequest.getIdDatasourceGroup(), 
								dataSourceGroup.getDatasourcegroupversion(), 
								dataSourceRequest.getDatasourceversion(), 
								dataSourceRequest.getIdDataset());
					if(existing==0) {
						try {
							count += dataSourceGroupMapper.insertDatasourceDatasourcegroupByIdDataSet(
									postRequest.getIdDatasourceGroup(), 
									dataSourceGroup.getDatasourcegroupversion(), 
									dataSourceRequest.getDatasourceversion(), 
									dataSourceRequest.getIdDataset());
						} catch (DuplicateKeyException  e) {
							logger.warn("[DatasetServiceImpl::insertDatasourcesToDatasourcegroupByIdDataset] dataset already in group - idDataset " + dataSourceRequest.getIdDataset() + " version: " + 
						dataSourceRequest.getDatasourceversion() + " datasourcegroup: " + dataSourceGroup.getDatasourcegroupversion());
							
						}
					}
				}

				return count;
			}
		};
		
		return insertDatasourcesToDatasourcegroup( postRequest, authorizedUser, organizationCode, tenantCodeManager, adder);
	}
	
	/**
	 * 
	 * @param postRequest
	 * @param authorizedUser
	 * @param organizationCode
	 * @param tenantCodeManager
	 * @param adder
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private ServiceResponse insertDatasourcesToDatasourcegroup(
			DatasourcegroupDatasourceRequest postRequest,
			JwtUser authorizedUser, 
			String organizationCode,
			String tenantCodeManager,
			DatasourceGroupAdder adder) throws BadRequestException, NotFoundException, Exception {
		
		// da scommentare quando terminato begin:
//		Tenant tenant = checkTenant(tenantCodeManager, organizationCode, tenantMapper);
//		checkAuthTenant(authorizedUser, tenant.getIdTenant(), tenantMapper);
		// da scommentare quando terminato end:
		
		DataSourceGroup dataSourceGroup = checkDataSourceGroup(postRequest.getIdDatasourceGroup());
		
		int count = 0;
		try {
			count = adder.perform(postRequest, dataSourceGroup);
		} catch (Exception e) {
			logger.error("insertDatasourcesToDatasourcegroup: " + e.toString());
		}
		
		return ServiceResponse.build().object(count);
	}
	
	@Override
	public ServiceResponse actionOnDatasourceGroup(String organizationCode, Long idDatasourcegroup,
			ActionRequest actionRequest, String tenantCodeManager, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {

		// da scommentare quando terminato begin:
//		Tenant tenant = checkTenant(tenantCodeManager, organizationCode, tenantMapper);
//		checkAuthTenant(authorizedUser, tenant.getIdTenant(), tenantMapper);
		// da scommentare quando terminato end:

		checkMandatoryParameter(actionRequest.getAction(), "action");

		if (Action.NEW_VERSION.code().equals(actionRequest.getAction())) {
			newDatasourceGroupVersion(idDatasourcegroup);
		}
		else if (Action.CONSOLIDATE.code().equals(actionRequest.getAction())) {
			consolidateDatasourceGroupVersion(idDatasourcegroup);
		}
		else if (Action.DISMISS.code().equals(actionRequest.getAction())) {
			dismissDatasourceGroupVersion(idDatasourcegroup);
		}
		else if (Action.RESTORE.code().equals(actionRequest.getAction())) {
			restoreDatasourceGroupVersion(idDatasourcegroup);
		}
		
		return ServiceResponse.build().NO_CONTENT();
	}

	
	/**
	 * 
	 * @param authorizedUser
	 * @param idDatasourcegroup
	 * @param organizationCode
	 * @param tenantCodeManager
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse deleteDatasourcesgroup(
			JwtUser authorizedUser, 
			Long idDatasourcegroup,
			String organizationCode,
			String tenantCodeManager) throws BadRequestException, NotFoundException, Exception {
		
		// da scommentare quando terminato begin:
//		Tenant tenant = checkTenant(tenantCodeManager, organizationCode, tenantMapper);
//		checkAuthTenant(authorizedUser, tenant.getIdTenant(), tenantMapper);
		// da scommentare quando terminato end:
		
		// verifica l'esistenza del group, se esiste controlla lo stato, se non è in bozza solleva eccezione:
		DataSourceGroup dataSourceGroup = checkDataSourceGroup(idDatasourcegroup);

		dataSourceGroupMapper.deleteDatasourceDatasourcegroupByIdGroupAndVersion(idDatasourcegroup, 
				dataSourceGroup.getDatasourcegroupversion());
		dataSourceGroupMapper.deleteDatasourceGroup(idDatasourcegroup, dataSourceGroup.getDatasourcegroupversion());
		
		return ServiceResponse.build().NO_CONTENT();
	}
	
	/**
	 * 
	 * @param postRequest
	 * @param authorizedUser
	 * @param organizationCode
	 * @param tenantCodeManager
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	@Override
	public ServiceResponse deleteDatasourcesToDatasourcegroupByIdDataset(
			DatasourcegroupDatasourceRequest postRequest, JwtUser authorizedUser, 
			String organizationCode, String tenantCodeManager) throws BadRequestException, NotFoundException, Exception{

		DatasourceGroupRemover remover = new DatasourceGroupRemover() {
			@Override
			public int perform(DatasourcegroupDatasourceRequest postRequest) {
				int deletedRecordsCount = 0;
				for (DataSourceRequest dataSourceRequest : postRequest.getDatasources()) {
					deletedRecordsCount += dataSourceGroupMapper.deleteDatasourceDatasourcegroupByIdDataset( postRequest.getIdDatasourceGroup(), postRequest.getDatasourcegroupversion(), dataSourceRequest.getDatasourceversion(), dataSourceRequest.getIdDataset());
				}
				return deletedRecordsCount;
			}
		};
		
		return deleteDatasourcesToDatasourcegroup(postRequest, authorizedUser, organizationCode, tenantCodeManager, remover);
	}

	@Override
	public ServiceResponse deleteDatasourcesToDatasourcegroupByIdStream(
			DatasourcegroupDatasourceRequest postRequest, JwtUser authorizedUser, 
			String organizationCode, String tenantCodeManager) throws BadRequestException, NotFoundException, Exception{

		DatasourceGroupRemover remover = new DatasourceGroupRemover() {
			@Override
			public int perform(DatasourcegroupDatasourceRequest postRequest) {
				int deletedRecordsCount = 0;
				for (DataSourceRequest dataSourceRequest : postRequest.getDatasources()) {
					deletedRecordsCount += dataSourceGroupMapper.deleteDatasourceDatasourcegroupByIdStream( 
							postRequest.getIdDatasourceGroup(), 
							postRequest.getDatasourcegroupversion(), 
							dataSourceRequest.getDatasourceversion(), 
							dataSourceRequest.getIdStream());
				}
				return deletedRecordsCount;
			}
		};
		
		return deleteDatasourcesToDatasourcegroup(postRequest, authorizedUser, organizationCode, tenantCodeManager, remover);
	}
	
	/**
	 * 
	 * @param postRequest
	 * @param authorizedUser
	 * @param organizationCode
	 * @param tenantCodeManager
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse deleteDatasourcesToDatasourcegroup(
			DatasourcegroupDatasourceRequest postRequest,
			JwtUser authorizedUser, String organizationCode,
			String tenantCodeManager, DatasourceGroupRemover remover) throws BadRequestException, NotFoundException, Exception {
		
		// da scommentare quando terminato begin:
//		Tenant tenant = checkTenant(tenantCodeManager, organizationCode, tenantMapper);
//		checkAuthTenant(authorizedUser, tenant.getIdTenant(), tenantMapper);
		// da scommentare quando terminato end:
		// provv da togliere begin:
		Tenant tenant = tenantMapper.selectTenantByTenantCode(tenantCodeManager);
		//  provv da togliere end
		
		/* verifica l'esistenza del group, se esiste controlla lo stato, se non è in bozza solleva eccezione */
		checkDataSourceGroup(postRequest.getIdDatasourceGroup(), postRequest.getDatasourcegroupversion(), tenant.getIdTenant());

		/* delete dei datasource */
		int deletedRecordsCount = remover.perform(postRequest);
		
		return ServiceResponse.build().object(deletedRecordsCount);
	}



	/**
	 * Verifica l'esistenza del datasource group:
	 *  
	 *  - se non esiste solleva NotfoundException.
	 *  - se esiste ed è in stato "FROZEN" solleva BadRequestException.
	 * 
	 * @param idDatasourcegroup
	 * @return
	 * @throws Exception
	 */
	private DataSourceGroup checkDataSourceGroup(Long idDatasourcegroup)throws Exception{
		DataSourceGroup currentDataSourceGroup = dataSourceGroupMapper.selectLastVersionDataSourceGroupById(idDatasourcegroup);
		
		checkIfFoundRecord(currentDataSourceGroup, "Datasource Group [ id: " + idDatasourcegroup + " ]");

		if (DatasourceGroupStatus.FROZEN.name().equals(currentDataSourceGroup.getStatus())) {
			throw new BadRequestException(Errors.NOT_ACCEPTABLE, "dataSource status: [ " + DatasourceGroupStatus.FROZEN + " ]");
		}			

		return currentDataSourceGroup;
	}
	
	/**
	 * 
	 * @param idDatasourcegroup
	 * @param version
	 * @param idTenant
	 * @return
	 * @throws Exception
	 */
	private DataSourceGroup checkDataSourceGroup(Long idDatasourcegroup, Integer version, Integer idTenant)throws Exception{
		DataSourceGroup currentDataSourceGroup = dataSourceGroupMapper.selectDataSourceGroupById(idTenant, version, idDatasourcegroup);
		
		checkIfFoundRecord(currentDataSourceGroup, "Datasource Group [ id: " + idDatasourcegroup + " ]");

		if (DatasourceGroupStatus.FROZEN.name().equals(currentDataSourceGroup.getStatus())) {
			throw new BadRequestException(Errors.NOT_ACCEPTABLE, "dataSource status: [ " + DatasourceGroupStatus.FROZEN + " ]");
		}			

		return currentDataSourceGroup;
	}
	
	@Override
	public ServiceResponse updateDatasourceGroup(
			Long idDatasourcegroup, 
			PostDataSourceGroupRequest requestDataSourceGroup,
			String organizationCode,
			JwtUser authorizedUser ) throws BadRequestException, NotFoundException, Exception {
		
// da scommentare quando terminato begin:
//		checkAuthTenant(authorizedUser, requestDataSourceGroup.getIdTenant(), tenantMapper);
//		checkTenant(requestDataSourceGroup.getIdTenant(), organizationCode, tenantMapper);
// da scommentare quando terminato end:		

		DataSourceGroup currentDataSourceGroup = checkDataSourceGroup(idDatasourcegroup);
		
		DataSourceGroup modifiedDataSourceGroup = new DataSourceGroupBuilder()
				    .name(requestDataSourceGroup.getName() != null ? requestDataSourceGroup.getName() : currentDataSourceGroup.getName())
				    .color(requestDataSourceGroup.getColor() != null ? requestDataSourceGroup.getColor() : currentDataSourceGroup.getColor())
				    .idDatasourcegroupType(currentDataSourceGroup.getIdDatasourcegroupType())
					.idDatasourcegroup(idDatasourcegroup)
					.idTenant(currentDataSourceGroup.getIdTenant())
					.datasourcegroupversion(currentDataSourceGroup.getDatasourcegroupversion())
					.status(currentDataSourceGroup.getStatus())
					.build();

		dataSourceGroupMapper.updateDataSourceGroup(modifiedDataSourceGroup);

		DataSourceGroupResponse response = new DataSourceGroupResponseBuilder(modifiedDataSourceGroup)
				.descriptionDatasourcegroupType(currentDataSourceGroup.getDescriptionDatasourcegroupType())
				.nameDatasourcegroupType(currentDataSourceGroup.getNameDatasourcegroupType())
				.build(); 
		
		return ServiceResponse.build().object(response);
	}

	@Override
	public ServiceResponse selectDataSourceGroupByTenant(String tenantCodeManager, String organizationCode, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {
		
		// da scommentare quando terminato begin:
//		Tenant tenant = checkTenant(tenantCodeManager, organizationCode, tenantMapper);
//		checkAuthTenant(authorizedUser, tenant.getIdTenant(), tenantMapper);
		// da scommentare quando terminato end:

		List<DataSourceGroupResponse> response = new ArrayList<>();
		
		List<DataSourceGroup> modelList = dataSourceGroupMapper.selectDataSourceGroupByTenant(tenantCodeManager);
		
		checkList(modelList);
		
		for (DataSourceGroup dataSourceGroup : modelList) {
			response.add(new DataSourceGroupResponseBuilder(dataSourceGroup).build());
		}
		
		return buildResponse(response);
	}
	
	@Override
	public ServiceResponse selectDataSourceGroupByTenant(String tenantCodeManager, String organizationCode)
			throws BadRequestException, NotFoundException, Exception {
		
		List<DataSourceGroupResponse> response = new ArrayList<>();
		
		List<DataSourceGroup> modelList = dataSourceGroupMapper.selectDataSourceGroupByTenant(tenantCodeManager);
		
		checkList(modelList);
		
		for (DataSourceGroup dataSourceGroup : modelList) {
			response.add(new DataSourceGroupResponseBuilder(dataSourceGroup).build());
		}
		
		return buildResponse(response);
	}

	
	@Override
	public ServiceResponse selectDataSourceGroupById(
			String organizationCode, Long idDatasourcegroup, String tenantCodeManager,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception{
		
		// da scommentare quando terminato begin:
//		Tenant tenant = checkTenant(tenantCodeManager, organizationCode, tenantMapper);
//		
//		checkAuthTenant(authorizedUser, tenant.getIdTenant(), tenantMapper);
		// da scommentare quando terminato end:
		Tenant tenant = tenantMapper.selectTenantByTenantCode(tenantCodeManager); // <<< da togliere quando si scommenta l'auth
		
		checkIfFoundRecord(tenant, "Tenant [ " + tenantCodeManager + " ]");
		
		DataSourceGroup dataSourceGroup = dataSourceGroupMapper.selectDataSourceGroupById(tenant.getIdTenant(), null, idDatasourcegroup);
		
		checkIfFoundRecord(dataSourceGroup, "Data Source Group [ id: " + idDatasourcegroup + " ]");
		
		DataSourceGroupResponse response = new DataSourceGroupResponseBuilder(dataSourceGroup).build(); 
		
		return ServiceResponse.build().object(response);
	}
	
	@Override
	public ServiceResponse insertDataSourceGroup(
			PostDataSourceGroupRequest request, String organizationCode,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

// da scommentare quando terminato begin:
//		checkAuthTenant(authorizedUser, postDataSourceGroupRequest.getIdTenant(), tenantMapper);
//		
//		checkTenant(postDataSourceGroupRequest.getIdTenant(), 
//				organizationCode, tenantMapper);
// da scommentare quando terminato end:

		checkMandatoryParameter(request.getIdTenant(), "idTenant");
		checkMandatoryParameter(request.getName(), "name");
		checkMandatoryParameter(request.getIdDatasourcegroupType(), "idDatasourcegroupType");
		checkMandatoryParameter(request.getColor(), "color");
		
		DataSourceGroup dataSourceGroup = new DataSourceGroupBuilder()
											.idTenant(request.getIdTenant())
											.name(request.getName())
											.idDatasourcegroupType(request.getIdDatasourcegroupType())
											.color(request.getColor())
											.datasourcegroupversion(DATASOURCE_VERSION)
											.status(DatasourceGroupStatus.DRAFT.name())
											.build();
		
		dataSourceGroupMapper.insertDataSourceGroup(dataSourceGroup);

		dataSourceGroup = dataSourceGroupMapper.selectDataSourceGroupById(dataSourceGroup.getIdTenant(), DATASOURCE_VERSION, dataSourceGroup.getIdDatasourcegroup());
		
		DataSourceGroupResponse response = new DataSourceGroupResponseBuilder(dataSourceGroup).build(); 
		
		return ServiceResponse.build().object(response);
	}
	
	@Override
	public ServiceResponse selectIngestionConfiguration(Integer idDatasourcegroup, Integer datasourcegroupversion, String tenantCode, String dbname, String dateformat, 
			String separator, Boolean onlyImported, Boolean help, HttpServletResponse httpServletResponse) throws BadRequestException, NotFoundException, Exception {

		if (help) {
			return buildResponse("CSV da usare per caricare un csv contentente la configuarazione da usare per l'ingestion\n\n" + "PARAMETRI\n"
					+ " - dbName: nome database da includere, se omesso vengono presi tutti\n" + " - sep: separatore di colonna, se omesso viene usata la tabulazione\n"
					+ " - onlyImported: flag indicante se includere solo i dataset importati da db: default true\n"
					+ " - dateformat: formato data, se omesso viene usato dd/MM/yyyy\n");
		}
		
		List<IngestionConfiguration> list = datasetMapper.selectIngestionConfiguration(dbname, tenantCode, onlyImported, idDatasourcegroup, datasourcegroupversion);
		checkList(list);

		List<IngestionConfigurationResponse> listCsvRow = new ArrayList<>();
		for (IngestionConfiguration model : list) {
			listCsvRow.add(new IngestionConfigurationResponseBuilder(model).build(dateformat));
		}

		ServiceUtil.downloadCsv(listCsvRow, "ingestionConf.csv", separator.charAt(0), httpServletResponse, 
				"table", "column", "comments", "datasetCode", "domain", "subdomain",
				"visibility", "opendata", "registrationDate", "dbName", "dbSchema", 
				"dbUrl", "columnIndex", "jdbcNativeType", "hiveType", "dbhiveschema", "dbhivetable");

		return buildResponse("Downloaded CSV file with " + list.size() + "records.");
	}

	@Override
	public ServiceResponse insertLastMongoObjectId(AllineamentoScaricoDatasetRequest request, Integer idOrganization) throws BadRequestException, NotFoundException, Exception {

		Organization organization = organizationMapper.selectOrganizationById(idOrganization);

		checkIfFoundRecord(organization, "Not found organization: " + idOrganization);

		checkMandatoryParameter(request.getLastMongoObjectId(), "LastMongoObjectId");
		checkMandatoryParameter(request.getDatasetVersion(), "DatasetVersion");
		checkMandatoryParameter(request.getIdDataset(), "IdDataset");

		String lastMongoObjectId = allineamentoScaricoDatasetMapper.selectLastMongoObjectId(idOrganization, request.getIdDataset(), request.getDatasetVersion());

		if (lastMongoObjectId != null) {
			allineamentoScaricoDatasetMapper.updateLastMongoObjectId(new AllineamentoScaricoDatasetBuilder(request, idOrganization).build());
		} else {
			allineamentoScaricoDatasetMapper.insertAllineamentoScaricoDataset(new AllineamentoScaricoDatasetBuilder(request, idOrganization).build());
		}

		return buildResponse(new AllineamentoScaricoDatasetResponseBuilder().idOrganization(idOrganization).idDataset(request.getIdDataset())
				.datasetVersion(request.getDatasetVersion()).lastMongoObjectId(request.getLastMongoObjectId()).build());

	}

	@Override
	public ServiceResponse selectAllineamentoScaricoDataset(Integer idOrganization, Integer idDataset, Integer datasetVersion)
			throws BadRequestException, NotFoundException, Exception {

		String lastMongoObjectId = allineamentoScaricoDatasetMapper.selectLastMongoObjectId(idOrganization, idDataset, datasetVersion);

		checkIfFoundRecord(lastMongoObjectId, "lastMongoObjectId");

		return buildResponse(new AllineamentoScaricoDatasetResponseBuilder().idOrganization(idOrganization).idDataset(idDataset).datasetVersion(datasetVersion)
				.lastMongoObjectId(lastMongoObjectId).build());
	}

	@Override
	public ServiceResponse selectAllineamentoScaricoDataset(Integer idOrganization) throws BadRequestException, NotFoundException, Exception {

		List<AllineamentoScaricoDataset> listModel = allineamentoScaricoDatasetMapper.selectAllineamentoScaricoDatasetByOrganization(idOrganization);

		checkList(listModel);

		List<AllineamentoScaricoDatasetResponse> listResponse = new ArrayList<>();

		for (AllineamentoScaricoDataset model : listModel) {
			listResponse.add(new AllineamentoScaricoDatasetResponseBuilder(model).build());
		}

		return buildResponse(listResponse);
	}

	@Override
	public ServiceResponse deleteDatasetData(String organizationCode, Integer idDataset, String tenantCodeManager, Integer version, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {

		// Verifica che l'utente loggato nel jwt possa utilizzare il tenant
		// passato, se non puo' rilancia UNAUTHORIZED:
		ServiceUtil.checkAuthTenant(authorizedUser, tenantCodeManager);

		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		checkIfFoundRecord(dataset);

		User user = userMapper.selectUserByIdDataSourceAndVersion(dataset.getIdDataSource(), dataset.getDatasourceversion(), tenantCodeManager, DataOption.WRITE.id());

		String url = datainsertDeleteUrl + tenantCodeManager + "/" + idDataset + (version != null ? "/" + version : "");

		HttpDelegate.makeHttpDelete(url, user.getUsername(), user.getPassword());

		return ServiceResponse.build().NO_CONTENT();
	}

	@Override
	public ServiceResponse uninstallingDatasets(String organizationCode, Integer idDataset, Boolean publish, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {

		
		DettaglioDataset dataset = null;
		if(organizationCode == null && publish == null && authorizedUser == null)  // from backoffice
			dataset = datasetMapper.selectDettaglioDatasetByIdDataset(idDataset, true);
		else
			dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		
		ServiceUtil.checkIfFoundRecord(dataset);

		// Aggiorna lo stato di tutte le versioni del datasource mettendolo a
		// 'uninst':
		ServiceUtil.updateDataSourceStatusAllVersion(Status.UNINSTALLATION.id(), dataset.getIdDataSource(), dataSourceMapper);

		// spubblicazione delle api odata e la cancellazione del documento Solr
		if (publish == null || publish)
			removeOdataApiAndSolrDocument(dataset.getDatasetcode());

		return ServiceResponse.build().NO_CONTENT();
	}

	@Override
	public ServiceResponse selectDatasetByOrganizationCode(String organizationCode) throws BadRequestException, NotFoundException, Exception {

		List<BackofficeDettaglioStreamDatasetResponse> response = new ArrayList<>();

		List<DettaglioDataset> listDettaglioDataset = datasetMapper.selectListaDettaglioDatasetByOrganizationCode(organizationCode);

		checkList(listDettaglioDataset);

		for (DettaglioDataset dettaglioDataset : listDettaglioDataset) {
			// response.add(getBackofficeDettaglioStreamDatasetResponse(dettaglioDataset));
			response.add(ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper));
		}
		return buildResponse(response);
	}

	@Override
	public ServiceResponse selectDatasetByTenantCode(String tenantCode) throws BadRequestException, NotFoundException, Exception {

		List<DettaglioDataset> listDettaglioDataset = datasetMapper.selectListaDettaglioDatasetByTenantCode(tenantCode);

		checkList(listDettaglioDataset);

		List<BackofficeDettaglioStreamDatasetResponse> response = new ArrayList<>();
		
		for (DettaglioDataset dettaglioDataset : listDettaglioDataset) {
			// response.add(getBackofficeDettaglioStreamDatasetResponse(dettaglioDataset));
			
			
			/* recupera i gruppi a cui sono associati i dataset */
			List<DataSourceGroup> modelGroups = dataSourceGroupMapper.selectDataSourceGroupByIdDatasetAndDatasetVersion(dettaglioDataset.getIddataset(), 
					dettaglioDataset.getDatasourceversion());
			List<DataSourceGroupResponse> responceGroups = new ArrayList<>();  
			if (modelGroups != null) {
				for (DataSourceGroup dataSourceGroup : modelGroups) {
					responceGroups.add(new DataSourceGroupResponseBuilder(dataSourceGroup).build());
				}
			}
			BackofficeDettaglioStreamDatasetResponse dettaglioStreamDataset = ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper);
			//response.add(ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper));
			dettaglioStreamDataset.setGroups(responceGroups);
			response.add(dettaglioStreamDataset);
		
		}
		return buildResponse(response);
	}

	@Override
	public ServiceResponse insertCSVData(MultipartFile file, Boolean skipFirstRow, String encoding, String csvSeparator, String componentInfoRequestsJson, String organizationCode,
			Integer idDataset, String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		logger.info("[DatasetServiceImpl::insertCSVData] Begin idDataset:[" + idDataset + "], componentInfoRequestsJson:[" + componentInfoRequestsJson + "]");

		List<ComponentInfoRequest> componentInfoRequests = Util.getComponentInfoRequests(componentInfoRequestsJson);

		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));
		checkIfFoundRecord(dataset);

		List<String[]> records = Util.getRecords(file, skipFirstRow, csvSeparator);

		checkComponentsSize(dataset.getComponents(), componentInfoRequests);

		// could throw bad request.
		checkCsvFile(records, dataset.getComponents(), componentInfoRequests);

		List<String> csvRows = getJsonCsvRows(records, dataset.getComponents(), componentInfoRequests);

		InvioCsvRequest invioCsvRequest = new InvioCsvRequest().datasetCode(dataset.getDatasetcode()).datasetVersion(dataset.getDatasourceversion()).values(csvRows);

		logger.debug("tenantCodeManager: " + tenantCodeManager);

		User user = userMapper.selectUserByIdDataSourceAndVersion(dataset.getIdDataSource(), dataset.getDatasourceversion(), tenantCodeManager, DataOption.WRITE.id());

		logger.debug(user != null ? "user: " + user.getUsername() : "user è nullo!");

		try {
			HttpDelegate.makeHttpPost(null, datainsertBaseUrl + user.getUsername(), null, user.getUsername(), user.getPassword(), invioCsvRequest.toString());
		} 
		catch (Exception e) {
			throw new BadRequestException(null, e.getMessage());
		}

		logger.info("[DatasetServiceImpl::insertCSVData] END");

		int number = csvRows == null ? 0 : csvRows.size();

		String message = "Row's number: " + number;

		String importedfiles = dataset.getImportedfiles();
		if (importedfiles == null || importedfiles.equals("")){
			importedfiles = file.getOriginalFilename();
		}
		else{
			importedfiles += "," + file.getOriginalFilename();
		}

		datasetMapper.updateImportedFiles(dataset.getIdDataSource(), dataset.getDatasourceversion(), importedfiles);

		return ServiceResponse.build().object(message);
	}

	/**
	 * 
	 * @param file
	 * @param skipFirstRow
	 * @param components
	 * @param componentInfoRequests
	 * @param csvSeparator
	 * @return
	 * @throws Exception
	 */
	private List<String> getJsonCsvRows(List<String[]> records, 
			ComponentJson[] components, 
			List<ComponentInfoRequest> componentInfoRequests) throws Exception {

		List<String> result = new ArrayList<String>();

		for (String[] columns : records) {

			StringBuilder resultRow = new StringBuilder();

			for (int c = 0; c < columns.length; c++) {

				ComponentInfoRequest info = getInfoByNumColumn(c, componentInfoRequests);
				
				if (info == null || info.isSkipColumn()) {
					continue;
				}
				
				ComponentJson component = getComponentResponseById(info.getIdComponent(), components);

				String doubleQuote = "";

				if (DataType.STRING.id().equals(component.getIdDataType()) || DataType.DATE_TIME.id().equals(component.getIdDataType())) {

					doubleQuote = "\"";

				}

				if (DataType.DATE_TIME.id().equals(component.getIdDataType())) {

					columns[c] = Util.isThisDateValid(columns[c], info.getDateFormat());

				}

				resultRow.append("\"").append(component.getName()).append("\"").append(":").append(doubleQuote).append(JSONValue.escape(columns[c])).append(doubleQuote)
						.append(",");
			}

			resultRow.setLength(resultRow.length() - 1);

			result.add("{" + resultRow.toString() + "}");

		}

		return result;

	}

	/**
	 * 
	 * @param columnValues
	 * @param component
	 * @param value
	 * @param columnNumber
	 * @param rowNumber
	 * @param errors
	 */
	private void checkKeyComponent(
			Map<Integer, Set<String>> columnValues, 
			ComponentJson component, 
			String value, 
			Integer columnNumber, 
			Integer rowNumber, 
			List<String> errors) {

		if (component != null && component.getIskey().equals(1)) {

			Set<String> values = columnValues.get(columnNumber);

			if (values == null) {
				values = new HashSet<>();
			}

			if (values.contains(value)) {
				errors.add("Errore alla riga " + rowNumber + ", clonna " + columnNumber + ", valore di chiave duplicato: " + value);
			} 
			else {

				values.add(value);

				columnValues.put(columnNumber, values);
			}

		}
	}

	/**
	 * 
	 * @param recordsFromCSV
	 * @param columnsDescriptor
	 * @param componentInfoRequests
	 * @throws Exception
	 */
	private void checkCsvFile(
			List<String[]> recordsFromCSV, 
			ComponentJson[] columnsDescriptor, 
			List<ComponentInfoRequest> componentInfoRequests ) throws Exception {

		checkComponentsSize(columnsDescriptor, componentInfoRequests);

		List<String> errors = new ArrayList<String>();
		
		Map<Integer, Set<String>> columnValues = new HashMap<>();

		int rowNumber = 0;

		for (String[] recordCsv : recordsFromCSV) {

			if (recordCsv.length < columnsDescriptor.length) {
				errors.add("Errore alla riga " + rowNumber + ", il numero di colonne deve essere: " + columnsDescriptor.length);
				rowNumber++;
				continue;
			}

			for (int c = 0; c < recordCsv.length; c++) {

				if (maximumLimitErrorsReached(errors)){
					break;
				}

				ComponentInfoRequest info = getInfoByNumColumn(c, componentInfoRequests);
				
				if (info == null || info.isSkipColumn()){
					continue;
				}
				
				ComponentJson component = getComponentResponseById(info.getIdComponent(), columnsDescriptor);
				
				String column = recordCsv[c];

				checkKeyComponent(columnValues, component, column, c, rowNumber, errors);

				for (DataType dateType : DataType.values()) {
					if (dateType.id().equals(component.getIdDataType())) {
						try {

							if (DataType.DATE_TIME == dateType) {
							
								String formattedDate = Util.isThisDateValid(column, info.getDateFormat());
								
								if (formattedDate == null) {
									throw new Exception();
								}
								
								continue;
							} 
							else if ((DataType.DOUBLE == dateType || DataType.FLOAT == dateType || DataType.LATITUDE == dateType || DataType.LONGITUDE == dateType) ) {
								String formattedColumn = Util.formatDecimalValue(column, info.getDecimalSeparator());
								column = formattedColumn;
								recordsFromCSV.get(rowNumber)[c] = formattedColumn;
							}
							
							dateType.checkValue(column);
						} 
						catch (Exception e) {
							errors.add("Errore alla riga " + rowNumber + ", il dato della colonna " + component.getName() + " non e' di tipo " + dateType.description());
							
						}
						break;
					}
				}
				
			}

			rowNumber++;
		}

		if (!errors.isEmpty()) {
			throw new BadRequestException(Errors.INCORRECT_VALUE).args(errors);
		}
		
	}

	/**
	 * 
	 * @param datasetRequest
	 * @throws Exception
	 */
	private void updateDatasetComponent(DatasetRequest datasetRequest) throws Exception {

		// COMPONENT already present
		List<Integer> listIdComponent = new ArrayList<Integer>();
		for (ComponentRequest component : datasetRequest.getComponents()) {
			if (component.getIdComponent() != null) {
				listIdComponent.add(component.getIdComponent());
			}
		}
		if (listIdComponent.size() > 0)
			componentMapper.cloneComponent(datasetRequest.getNewDataSourceVersion(), listIdComponent);

		for (ComponentRequest component : datasetRequest.getComponents()) {
			if (component.getIdComponent() != null) {
				componentMapper.updateClonedComponent(component.getName(), component.getAlias(), component.getInorder(), component.getIdMeasureUnit(),
						datasetRequest.getNewDataSourceVersion(), datasetRequest.getIdDataSource(), component.getForeignkey(), component.getHivetype(),
						component.getJdbcnativetype(), Util.booleanToInt(component.getIskey()), Util.booleanToInt(component.getIsgroupable()));
			}
		}

		// new component
		ServiceUtil.insertComponents(datasetRequest.getComponents(), datasetRequest.getIdDataSource(), datasetRequest.getNewDataSourceVersion(),
				datasetRequest.getNewDataSourceVersion(), componentMapper);
	}

	/**
	 * 
	 * @param datasetRequest
	 * @throws Exception
	 */
	private void updateSharingTenants(DatasetRequest datasetRequest) throws Exception {
		if (datasetRequest.getSharingTenants() != null && !datasetRequest.getSharingTenants().isEmpty()) {
			tenantMapper.deleteNotManagerTenantDataSource(datasetRequest.getIdDataSource(), datasetRequest.getCurrentDataSourceVersion());

			ServiceUtil.insertSharingTenants(datasetRequest.getSharingTenants(), datasetRequest.getIdDataSource(), Util.getNow(), DataOption.READ_AND_USE.id(),
					ManageOption.NO_RIGHT.id(), datasetRequest.getNewDataSourceVersion(), tenantMapper);
		}
	}

	/**
	 * 
	 * @param datasetRequest
	 * @throws Exception
	 */
	private void updateDatasetTransaction(DatasetRequest datasetRequest, String tenantCodeManager, Boolean publish) throws Exception {

		logger.info("BEGIN [DatasetServiceImpl::updateDatasetTransaction]");

		// dcat
		Integer idDcat = insertDcat(datasetRequest.getDcat(), dcatMapper);

		// license
		Integer idLicense = insertLicense(datasetRequest.getLicense(), licenseMapper);

		// data source
		dataSourceMapper.cloneDataSource(datasetRequest.getNewDataSourceVersion(), datasetRequest.getCurrentDataSourceVersion(), datasetRequest.getIdDataSource());
		updateDataSource(datasetRequest, idDcat, idLicense, datasetRequest.getIdDataSource(), datasetRequest.getNewDataSourceVersion(), dataSourceMapper);

		// data set
		datasetMapper.cloneDataset(datasetRequest.getNewDataSourceVersion(), datasetRequest.getCurrentDataSourceVersion(), datasetRequest.getIdDataSource());
		datasetMapper.updateDataset(datasetRequest.getDatasetname(), datasetRequest.getDescription(), datasetRequest.getIdDataSource(), datasetRequest.getNewDataSourceVersion());

		// tags
		insertTags(datasetRequest.getTags(), datasetRequest.getIdDataSource(), datasetRequest.getNewDataSourceVersion(), dataSourceMapper);

		// groups
		List<PostDataSourceGroupRequest> requestGroups = datasetRequest.getGroups();
		for ( PostDataSourceGroupRequest group : Util.nullGuard(requestGroups) ) {
			DataSourceGroup dataSourceGroup = dataSourceGroupMapper.selectDataSourceGroupById(datasetRequest.getIdTenant(), group.getDatasourcegroupversion(), group.getIdDatasourcegroup());
			
			if(dataSourceGroup !=null && dataSourceGroup.getStatus().equals(DatasourceGroupStatus.DRAFT.name())) {
				dataSourceGroupMapper.deleteDatasourceDatasourcegroup(
						group.getIdDatasourcegroup(), 
						group.getDatasourcegroupversion(), 
						datasetRequest.getCurrentDataSourceVersion(), 
						datasetRequest.getIdDataSource());
				dataSourceGroupMapper.insertDatasourceDatasourcegroup(
						group.getIdDatasourcegroup(), 
						group.getDatasourcegroupversion(), 
						datasetRequest.getNewDataSourceVersion(), 
						datasetRequest.getIdDataSource());
			}
		}
		
		// components
		updateDatasetComponent(datasetRequest);

		// sharing tenants
		updateSharingTenants(datasetRequest);

		// TENANT-DATASOURCE
		insertTenantDataSource(datasetRequest.getIdTenant(), datasetRequest.getIdDataSource(), datasetRequest.getNewDataSourceVersion(), Util.getNow(), tenantMapper);

		if (publish == null || publish) {
			try {
				CloseableHttpClient httpclient = PublisherDelegate.build().registerToStoreInit();
				logger.debug("Build publisher delegate...");

				if (!datasetRequest.getUnpublished()) {

					DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasetCode(datasetRequest.getDatasetcode(), false);
					String apiName = null;

					Bundles bundles = bundlesMapper.selectBundlesByTenantCode(tenantCodeManager);

					apiMapper.insertApi(

							Api.buildOutput(datasetRequest.getNewDataSourceVersion()).apicode(datasetRequest.getDatasetcode()).apiname(dettaglioDataset.getDatasetname())
									.entitynamespace(Api.ENTITY_NAMESPACE + datasetRequest.getDatasetcode()).apisubtype(API_SUBTYPE_ODATA)
									.idDataSource(dettaglioDataset.getIdDataSource())
									.maxOdataResultperpage(bundles != null ? bundles.getMaxOdataResultperpage() : MAX_ODATA_RESULT_PER_PAGE));

					// publisher
					logger.debug("publisher delegate add api...");
					apiName = PublisherDelegate.build().addApi(httpclient, dettaglioDataset);

					logger.debug("publisher delegate publish api...");
					PublisherDelegate.build().publishApi(httpclient, "1.0", apiName, "admin");
					updateDatasetSubscriptionIntoStore(httpclient, dettaglioDataset.getDataSourceVisibility(), dettaglioDataset, apiName);

					logger.debug("solr delegate add document...");
					SolrDelegate.build().addDocument(dettaglioDataset);

				} else {
					removeOdataApiAndSolrDocument(httpclient, datasetRequest.getDatasetcode());
				}
			} catch (Exception e) {
				logger.error("[DatasetServiceImpl::updateDatasetTransaction] Publish API - error " + e.getMessage());
				e.printStackTrace();
				throw new BadRequestException(Errors.INTERNAL_SERVER_ERROR, " An error occurred during the publication of the API, please try to save again");

			}
		} else if (!datasetRequest.getUnpublished()) {
			DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasetCode(datasetRequest.getDatasetcode(), false);

			Bundles bundles = bundlesMapper.selectBundlesByTenantCode(tenantCodeManager);

			apiMapper.insertApi(

					Api.buildOutput(datasetRequest.getNewDataSourceVersion()).apicode(datasetRequest.getDatasetcode()).apiname(dettaglioDataset.getDatasetname())
							.entitynamespace(Api.ENTITY_NAMESPACE + datasetRequest.getDatasetcode()).apisubtype(API_SUBTYPE_ODATA).idDataSource(dettaglioDataset.getIdDataSource())
							.maxOdataResultperpage(bundles != null ? bundles.getMaxOdataResultperpage() : MAX_ODATA_RESULT_PER_PAGE));

		}

		logger.info("END [DatasetServiceImpl::updateDatasetTransaction]");

	}

	public void updateDatasetSubscriptionIntoStore(CloseableHttpClient httpClient, String visibility, Dataset datasetNew, String apiName) {

		SubscriptionByUsernameResponse listOfApplication = null;
		try {
			listOfApplication = StoreDelegate.build().listSubscriptionByApiAndUserName(httpClient, apiName, "admin");
			List<SharingTenantsJson> tenants = new LinkedList<SharingTenantsJson>();
			if (datasetNew.getSharingTenant() != null)
				tenants = mapper.readValue(datasetNew.getSharingTenant(), new TypeReference<List<SharingTenantsJson>>() {
				});
			SharingTenantsJson owner = new SharingTenantsJson();
			owner.setTenantcode(datasetNew.getTenantCode());
			tenants.add(owner);
			Subs[] subs = listOfApplication.getSubscriptions();
			if (visibility.equals("public")) {
				for (Subs appNames : subs) {
					StoreDelegate.build().unSubscribeApiWithUsername(httpClient, apiName, null, appNames.getApplicationId(), "admin");
				}
			} else {
				for (SharingTenantsJson newTenantSh : tenants) {
					boolean foundInDesiderata = false;
					for (Subs appNames : subs) {
						if (appNames.getApplication().equals("userportal_" + newTenantSh.getTenantcode())) {
							foundInDesiderata = true;
						}
					}
					if (!foundInDesiderata)
						StoreDelegate.build().subscribeApiWithUsername(httpClient, apiName, "userportal_" + newTenantSh.getTenantcode(), "admin");
				}

				for (Subs appNames : subs) {
					boolean notFound = true;
					for (SharingTenantsJson newTenantSh : tenants) {
						if (appNames.getApplication().equals("userportal_" + newTenantSh.getTenantcode())) {
							notFound = false;
						}
					}
					if (notFound)
						StoreDelegate.build().unSubscribeApiWithUsername(httpClient, apiName, null, appNames.getApplicationId(), "admin");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse updateDataset(String organizationCode, Integer idDataset, DatasetRequest datasetRequest, String tenantCodeManager, JwtUser authorizedUser,
			Boolean publish) throws BadRequestException, NotFoundException, Exception {

		Tenant tenant = checkAuthTenant(authorizedUser, datasetRequest.getIdTenant(), tenantMapper);
		
		Dataset oldDataset = updateDatasetValidation(tenant, datasetRequest, authorizedUser, organizationCode, idDataset, tenantCodeManager);

		updateDatasetTransaction(datasetRequest, tenantCodeManager, publish);

		PostDatasetResponse response = PostDatasetResponse.build(idDataset).datasetcode(datasetRequest.getDatasetcode()).datasetname(datasetRequest.getDatasetname());
		
		/* Informativa per creazione dataset opendata - invio mail */ 
		logger.info("[DatasetServiceImpl::updateDataset] visibility " + datasetRequest.getVisibility());
		if (Visibility.PUBLIC.code().equals(datasetRequest.getVisibility()) && oldDataset!=null 
				&& oldDataset.getDataSourceVisibility()!=null && oldDataset.getDataSourceVisibility().equals(Visibility.PRIVATE.code())) {
			logger.info("[DatasetServiceImpl::updateDataset] send mail");
			mailService.sendOpendataCreationInformative(response, tenant, authorizedUser, userportalUrl);
		}
		
		return ServiceResponse.build().object(response);

	}
	
	/**
	 * 
	 * @param datasetRequest
	 * @param authorizedUser
	 * @param organizationCode
	 * @param organization
	 * @param idDataset
	 * @param tenantCodeManager
	 * @throws Exception
	 */
	private Dataset updateDatasetValidation(Tenant tenant, DatasetRequest datasetRequest, JwtUser authorizedUser, String organizationCode, Integer idDataset, String tenantCodeManager)
			throws Exception {
		List<String> authorizedTenants = getTenantCodeListFromUser(authorizedUser);
		return updateDatasetValidation(tenant, datasetRequest, authorizedTenants, organizationCode, idDataset, tenantCodeManager);
	}

	/**
	 * 
	 * @param datasetRequest
	 * @param authorizedUser
	 * @param organizationCode
	 * @param organization
	 * @param idDataset
	 * @param tenantCodeManager
	 * @throws Exception
	 */
	private Dataset updateDatasetValidation(Tenant tenant, DatasetRequest datasetRequest, List<String> authorizedTenants, String organizationCode, Integer idDataset, String tenantCodeManager)
			throws Exception {

		// check organization code:
		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode);
		checkIfFoundRecord(organization, "Not found organization code: " + organizationCode);

		// check tag list:
		// provvisoriamente viene commentato
		// checkList(datasetRequest.getTags(), "tags");

		// datasetname
		checkMandatoryParameter(datasetRequest.getDatasetname(), "datasetname");

		// license
		checkLicense(datasetRequest.getLicense());

		// visibility
		checkVisibility(datasetRequest, tenantMapper);

		// groups
		if(datasetRequest.getGroups()!=null)
			for (PostDataSourceGroupRequest postRequest : datasetRequest.getGroups()) {
				checkDataSourceGroup(postRequest.getIdDatasourcegroup());
			}

		// dataset
		Dataset dataset = datasetMapper.selectDatasetForUpdate(tenantCodeManager, idDataset, organizationCode, authorizedTenants);
		checkIfFoundRecord(dataset);

		if (Status.UNINSTALLATION.id().equals(dataset.getIdStatus())) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Status: " + Status.UNINSTALLATION.description());
		}

		if (dataset.getDomIdDomain().equals(-1) && datasetRequest.getUnpublished() == false) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Multi domain must be unpublished.");
		}

		checkComponents(datasetRequest.getComponents(), dataset.getIdDataSource(), dataset.getDatasourceversion(), componentMapper, tenant);

		datasetRequest.setNewDataSourceVersion(dataset.getDatasourceversion() + 1);
		datasetRequest.setCurrentDataSourceVersion(dataset.getDatasourceversion());
		datasetRequest.setIdDataSource(dataset.getIdDataSource());
		datasetRequest.setDatasetcode(dataset.getDatasetcode());
		return dataset;
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse insertDataset(String organizationCode, Boolean publish, DatasetRequest postDatasetRequest, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {

		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode);

		if (postDatasetRequest.getMultiSubdomain() != null)
			postDatasetRequest.setMultiSubdomain(postDatasetRequest.getMultiSubdomain().toUpperCase());

		Tenant tenant = checkTenant(postDatasetRequest.getIdTenant(), organizationCode, tenantMapper);

		insertDatasetValidation(postDatasetRequest, authorizedUser, organizationCode, organization, tenant);

		PostDatasetResponse response = insertDatasetTransaction(postDatasetRequest, authorizedUser, organization, tenant, publish);
		
		/* Informativa per creazione dataset opendata - invio mail */ 
		if (Visibility.PUBLIC.code().equals(postDatasetRequest.getVisibility())) {
			mailService.sendOpendataCreationInformative(response, tenant, authorizedUser, userportalUrl);
		}
		
		return ServiceResponse.build().object(response);
	}
	
	/**
	 * 
	 */
	@Override
	public ServiceResponse insertDataset(String organizationCode, Boolean publish, DatasetRequest postDatasetRequest)
			throws BadRequestException, NotFoundException, Exception {

		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode);

		if (postDatasetRequest.getMultiSubdomain() != null)
			postDatasetRequest.setMultiSubdomain(postDatasetRequest.getMultiSubdomain().toUpperCase());

		Tenant tenant = checkTenant(postDatasetRequest.getIdTenant(), organizationCode, tenantMapper);

		insertDatasetValidation(postDatasetRequest, null, organizationCode, organization, null);

		PostDatasetResponse response = insertDatasetTransaction(postDatasetRequest, null, organization, tenant, publish);
		
		/* Informativa per creazione dataset opendata - invio mail */ 
		/*if (Visibility.PUBLIC.code().equals(postDatasetRequest.getVisibility())) {
			mailService.sendOpendataCreationInformative(response, tenant, authorizedUser, userportalUrl);
		}*/
		
		return ServiceResponse.build().object(response);
	}


	/**
	 * 
	 */
	@Override
	public ServiceResponse selectDataset(String organizationCode, Integer idDataset, String tenantCodeManager, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDataset(
				tenantCodeManager, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));
		
		checkIfFoundRecord(dettaglioDataset);

		/* trova i gruppi a cui è associato il dataset */
		List<DataSourceGroup> modelGroups = dataSourceGroupMapper.selectDataSourceGroupByIdDatasetAndDatasetVersion(idDataset, dettaglioDataset.getDatasourceversion());
		List<DataSourceGroupResponse> responceGroups = new ArrayList<>();
		if (modelGroups != null) {
			for (DataSourceGroup dataSourceGroup : modelGroups) {
				responceGroups.add(new DataSourceGroupResponseBuilder(dataSourceGroup).build());
			}
		}
		
		if (DatasetSubtype.STREAM.id().equals(dettaglioDataset.getIdDatasetSubtype()) || DatasetSubtype.SOCIAL.id().equals(dettaglioDataset.getIdDatasetSubtype())) {

			DettaglioStream dettaglioStream = streamMapper.selectStreamByDatasource(dettaglioDataset.getIdDataSource(), dettaglioDataset.getDatasourceversion());
			if (dettaglioStream != null) {

				DettaglioSmartobject dettaglioSmartobject = smartobjectMapper.selectSmartobjectById(dettaglioStream.getIdSmartObject());

				List<InternalDettaglioStream> listInternalStream = streamMapper.selectInternalStream(dettaglioStream.getIdDataSource(), dettaglioStream.getDatasourceversion());

				DettaglioStreamDatasetResponse res = new DettaglioStreamDatasetResponse(dettaglioStream, dettaglioDataset, dettaglioSmartobject, listInternalStream);
				res.setGroups(responceGroups);
				return buildResponse(res);
			}
		}

		DettaglioStreamDatasetResponse res = new DettaglioStreamDatasetResponse(dettaglioDataset);
		res.setGroups(responceGroups);
		return buildResponse(res);
	}

	@Override
	public ServiceResponse selectDatasetByIdDataset(Integer idDataset, boolean onlyInstalled) throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByIdDataset(idDataset, onlyInstalled);

		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper);

		return buildResponse(dettaglio);
	}

	@Override
	public ServiceResponse selectDatasetByDatasetCodeDatasetVersion(String datasetCode, Integer datasetVersion) throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasetCodeDatasourceVersion(datasetCode, datasetVersion);

		checkIfFoundRecord(dettaglioDataset);

		// return
		// buildResponse(getBackofficeDettaglioStreamDatasetResponse(dettaglioDataset));
		return buildResponse(ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper));

	}

	@Override
	public ServiceResponse selectDatasetByDatasetCode(String datasetCode, boolean onlyInstalled) throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasetCode(datasetCode, onlyInstalled);

		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper);

		return buildResponse(dettaglio);
	}

	@Override
	public ServiceResponse selectDatasetByIdDatasetDatasetVersion(Integer idDataset, Integer datasetVersion) throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByIdDatasetDatasourceVersion(idDataset, datasetVersion);

		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper);

		return buildResponse(dettaglio);
	}

	/**
	 * 
	 */
	@Override
	public byte[] selectDatasetIcon(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		checkIfFoundRecord(dettaglioDataset);

		return Util.convertIconFromDBToByte(dettaglioDataset.getDataSourceIcon());
	}

	@Override
	public byte[] selectDatasetIcon(String datasetCode) throws BadRequestException, NotFoundException, Exception {

		String icon = datasetMapper.selectDatasetIconByStreamcodeAndSoCode(datasetCode, true);

		// checkIfFoundRecord(icon);

		return Util.convertIconFromDBToByte(icon);
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse selectDatasets(Integer groupId, String organizationCode, String tenantCodeManager, String sort, JwtUser authorizedUser, Boolean includeShared)
			throws BadRequestException, NotFoundException, UnauthorizedException, Exception {

		checkAuthTenant(authorizedUser, tenantCodeManager);

		List<String> sortList = getSortList(sort, Dataset.class);

		if(includeShared == null)
			includeShared = false;
		
		List<Dataset> dataSetList = datasetMapper.selectDataSets(tenantCodeManager, organizationCode, sortList, getTenantCodeListFromUser(authorizedUser), includeShared, groupId);

		checkList(dataSetList);

		List<DatasetResponse> listResponse = new ArrayList<DatasetResponse>();
		for (Dataset dataset : dataSetList) {
			
			/* recupera i gruppi a cui sono associati i dataset */
			List<DataSourceGroup> modelGroups = dataSourceGroupMapper.selectDataSourceGroupByIdDatasetAndDatasetVersion(dataset.getIddataset(), 
					dataset.getDatasourceversion());
			List<DataSourceGroupResponse> responceGroups = new ArrayList<>();  
			if (modelGroups != null) {
				for (DataSourceGroup dataSourceGroup : modelGroups) {
					responceGroups.add(new DataSourceGroupResponseBuilder(dataSourceGroup).build());
				}
			}
			
			DatasetResponse res = new DatasetResponse(dataset);
			res.setGroups(responceGroups);
			listResponse.add(res);
		}

		return buildResponse(listResponse);

	}

	/**
	 * 
	 * @param components
	 * @return
	 */
	private boolean isBinaryDataset(List<ComponentRequest> components) {
		for (ComponentRequest component : components) {
			if (DataType.BINARY.id() == component.getIdDataType()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param postDatasetRequest
	 * @param authorizedUser
	 * @param organizationCode
	 * @param organization
	 * @throws Exception
	 */
	private void insertDatasetValidation(DatasetRequest datasetRequest, JwtUser authorizedUser, String organizationCode, Organization organization, Tenant tenant) throws Exception {

		// component
		checkComponents(datasetRequest.getComponents(), componentMapper, tenant);

		// verifica che sia presente idSubdomain o multisubdomanin
		if (datasetRequest.getIdSubdomain() == null && datasetRequest.getMultiSubdomain() == null) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, "Mandatory idSubdomanin or multiSubdomain");
		}

		// verifica il multisubdomain
		if (datasetRequest.getMultiSubdomain() != null && !datasetRequest.getMultiSubdomain().matches(MULTI_SUBDOMAIN_PATTERN)) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Incorrect pattern for multisubdomain. Must be [ " + MULTI_SUBDOMAIN_PATTERN + " ]");
		}

		// Se id_subdomain è nullo verificare che "unpublished " sia true
		if (datasetRequest.getIdSubdomain() == null && datasetRequest.getUnpublished() == false) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "If idSubdomain is null unpublished must be true.");
		}

		checkIfFoundRecord(organization, "Not found organization code: " + organizationCode);

		// commentato provvisoriamente per migrazione
		// checkList(datasetRequest.getTags(), "tags");
		// groups
		if(datasetRequest.getGroups()!=null)
			for (PostDataSourceGroupRequest postRequest : datasetRequest.getGroups()) {
				checkDataSourceGroup(postRequest.getIdDatasourcegroup());
			}


		if(authorizedUser != null)
			checkAuthTenant(authorizedUser, datasetRequest.getIdTenant(), tenantMapper);
		checkMandatoryParameter(datasetRequest.getDatasetname(), "datasetname");
		checkLicense(datasetRequest.getLicense());
		checkVisibility(datasetRequest, tenantMapper);
	}

	/**
	 * 
	 * @param postDatasetRequest
	 * @param organization
	 * @param idSubdomain
	 * @param tenant
	 * @return
	 * @throws Exception
	 */
	private Integer insertBinary(DatasetRequest postDatasetRequest, Organization organization, Integer idSubdomain, Tenant tenant) throws Exception {
		Integer idBinaryDataSource = null;
		if (isBinaryDataset(postDatasetRequest.getComponents())) {

			// BINARY DATASOURCE
			idBinaryDataSource = insertDataSource(new DatasetRequest().datasetname(postDatasetRequest.getDatasetname()).idSubdomain(idSubdomain), organization.getIdOrganization(),
					Status.INSTALLED.id(), dataSourceMapper);

			Integer idBinary = null;
			// Quando viene passato l'idDattaset il binary viene generato con un
			// id-1
			if (postDatasetRequest.getIddataset() != null) {
				idBinary = new Integer(postDatasetRequest.getIddataset() - 1);
			}

			// INSERT DATASET
			ServiceUtil.insertDataset(idBinaryDataSource, DATASOURCE_VERSION, postDatasetRequest.getDatasetname(), postDatasetRequest.getDescription(), DatasetSubtype.BINARY.id(),
					tenant, organization, datasetMapper, sequenceMapper, idBinary, null);

			// BINARY COMPONENT
			insertBinaryComponents(idBinaryDataSource, componentMapper);
		}
		return idBinaryDataSource;
	}

	/**
	 * 
	 * @param postDatasetRequest
	 * @return
	 */
	private Integer insertSubdomain(DatasetRequest postDatasetRequest) {

		if (postDatasetRequest.getIdSubdomain() != null) {
			return postDatasetRequest.getIdSubdomain();
		}

		Subdomain subdomain = new Subdomain().idDomain(MULTI_SUBDOMAIN_ID_DOMAIN).langEn(postDatasetRequest.getMultiSubdomain()).langIt(postDatasetRequest.getMultiSubdomain())
				.subdomaincode(postDatasetRequest.getMultiSubdomain());

		subdomainMapper.insertSubdomain(subdomain);

		return subdomain.getIdSubdomain();
	}

	/**
	 * 
	 * @param postDatasetRequest
	 * @param authorizedUser
	 * @param organization
	 * @return
	 * @throws Exception
	 */
	private PostDatasetResponse insertDatasetTransaction(DatasetRequest postDatasetRequest, JwtUser authorizedUser, Organization organization, Tenant tenant, Boolean publish)
			throws Exception {

		// insert subdomain
		Integer idSubdomain = insertSubdomain(postDatasetRequest);

		// BINARY
		Integer idBinaryDataSource = insertBinary(postDatasetRequest, organization, idSubdomain, tenant);

		// INSERT LICENSE:
		Integer idLicense = insertLicense(postDatasetRequest.getLicense(), licenseMapper);

		// INSERT DCAT:
		Integer idDcat = insertDcat(postDatasetRequest.getDcat(), dcatMapper);

		// INSERT DATA SOURCE:
		Integer idDataSource = insertDataSource(postDatasetRequest.idSubdomain(idSubdomain), organization.getIdOrganization(), idDcat, idLicense, Status.INSTALLED.id(),
				dataSourceMapper);

		// INSERT DATASET
		Dataset dataset = ServiceUtil.insertDataset(idDataSource, DATASOURCE_VERSION, postDatasetRequest.getDatasetname(), postDatasetRequest.getDescription(),
				DatasetSubtype.BULK.id(), postDatasetRequest.getImportfiletype(), DATASOURCE_VERSION, idBinaryDataSource, postDatasetRequest.getJdbcdburl(),
				postDatasetRequest.getJdbcdbname(), postDatasetRequest.getJdbcdbtype(), postDatasetRequest.getJdbctablename(), postDatasetRequest.getJdbcdbschema(), tenant,
				organization, datasetMapper, sequenceMapper, postDatasetRequest.getIddataset(), postDatasetRequest.getDatasetcode(),
				Util.booleanToInt(postDatasetRequest.getAvailablehive()), Util.booleanToInt(postDatasetRequest.getAvailablespeed()),
				Util.booleanToInt(postDatasetRequest.getIstransformed()), postDatasetRequest.getDbhiveschema(), postDatasetRequest.getDbhivetable());

		// TAGS
		insertTags(postDatasetRequest.getTags(), idDataSource, DATASOURCE_VERSION, dataSourceMapper);

		// COMPONENT
		insertComponents(postDatasetRequest.getComponents(), idDataSource, ServiceUtil.DATASOURCE_VERSION, ServiceUtil.DATASOURCE_VERSION, componentMapper);

		// TENANT-DATASOURCE
		insertTenantDataSource(postDatasetRequest.getIdTenant(), idDataSource, ServiceUtil.DATASOURCE_VERSION, Util.getNow(), tenantMapper);

		// SHARING TENANT
		insertSharingTenants(postDatasetRequest.getSharingTenants(), idDataSource, Util.getNow(), DataOption.READ_AND_USE.id(), ManageOption.NO_RIGHT.id(), tenantMapper);
		
		// GROUPS
		List<PostDataSourceGroupRequest> requestGroups = postDatasetRequest.getGroups();
		for ( PostDataSourceGroupRequest group : Util.nullGuard(requestGroups) ) {
			dataSourceGroupMapper.insertDatasourceDatasourcegroup(
					group.getIdDatasourcegroup(), 
					group.getDatasourcegroupversion(), 
					DATASOURCE_VERSION, 
					idDataSource);
		}
		


		PostDatasetResponse response = PostDatasetResponse.build(dataset.getIddataset());
		// API
		if (publish == null || publish) {
			try {
				CloseableHttpClient httpclient = PublisherDelegate.build().registerToStoreInit();
				if (!postDatasetRequest.getUnpublished()) {
					DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasetCode(dataset.getDatasetcode(), false);

					String apiName = null;

					Bundles bundles = bundlesMapper.selectBundlesByTenantCode(dettaglioDataset.getTenantCode());

					apiMapper.insertApi(Api.buildOutput(DATASOURCE_VERSION).apicode(dataset.getDatasetcode()).apiname(dettaglioDataset.getDatasetname())
							.apisubtype(API_SUBTYPE_ODATA).idDataSource(dettaglioDataset.getIdDataSource()).entitynamespace(Api.ENTITY_NAMESPACE + dataset.getDatasetcode())
							.maxOdataResultperpage(bundles != null ? bundles.getMaxOdataResultperpage() : MAX_ODATA_RESULT_PER_PAGE));
					// publisher
					apiName = PublisherDelegate.build().addApi(httpclient, dettaglioDataset);
					PublisherDelegate.build().publishApi(httpclient, "1.0", apiName, "admin");
					updateDatasetSubscriptionIntoStore(httpclient, dettaglioDataset.getDataSourceVisibility(), dettaglioDataset, apiName);
					SolrDelegate.build().addDocument(dettaglioDataset);

				} else {
					logger.info("[DatasetServiceImpl::insertDatasetTransaction] - unpublish datasetcode: " + postDatasetRequest.getDatasetcode());
					try {
						String removeApiResponse = PublisherDelegate.build().removeApi(httpclient, PublisherDelegate.createApiNameOData(postDatasetRequest.getDatasetcode()));
						logger.info("[DatasetServiceImpl::insertDatasetTransaction] - unpublish removeApi: " + removeApiResponse);

					} catch (Exception ex) {
						logger.error("[DatasetServiceImpl::insertDatasetTransaction] unpublish removeApi ERROR" + postDatasetRequest.getDatasetcode() + " - " + ex.getMessage());
					}
					try {
						SolrDelegate.build().removeDocument(postDatasetRequest.getDatasetcode());
						logger.info("[DatasetServiceImpl::insertDatasetTransaction] - unpublish removeDocument: " + postDatasetRequest.getDatasetcode());
					} catch (Exception ex) {
						logger.error(
								"[DatasetServiceImpl::insertDatasetTransaction] unpublish removeDocument ERROR" + postDatasetRequest.getDatasetcode() + " - " + ex.getMessage());
					}
				}
			} catch (Exception e) {
				logger.error("[DatasetServiceImpl::insertDatasetTransaction] Publish API - error " + e.getMessage());
				e.printStackTrace();
				response.addWarning(" An error occurred during the publication of the API, please try to save again");
				throw new BadRequestException(Errors.INTERNAL_SERVER_ERROR, " An error occurred during the publication of the API, please try to save again");
			}
		} else if (!postDatasetRequest.getUnpublished()) {
			DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasetCode(dataset.getDatasetcode(), false);
			Bundles bundles = bundlesMapper.selectBundlesByTenantCode(dettaglioDataset.getTenantCode());
			apiMapper.insertApi(Api.buildOutput(DATASOURCE_VERSION).apicode(dataset.getDatasetcode()).apiname(dettaglioDataset.getDatasetname()).apisubtype(API_SUBTYPE_ODATA)
					.idDataSource(dettaglioDataset.getIdDataSource()).entitynamespace(Api.ENTITY_NAMESPACE + dataset.getDatasetcode())
					.maxOdataResultperpage(bundles != null ? bundles.getMaxOdataResultperpage() : MAX_ODATA_RESULT_PER_PAGE));

		}
		response.setDatasetcode(dataset.getDatasetcode());
		response.setDatasetname(dataset.getDatasetname());
		return response;
	}

	/**
	 * 
	 * @param numColumn
	 * @param list
	 * @return
	 */
	private ComponentInfoRequest getInfoByNumColumn(Integer numColumn, List<ComponentInfoRequest> list) {
		for (ComponentInfoRequest componentInfoRequest : list) {
			if (componentInfoRequest.getNumColumn().equals(numColumn)) {
				return componentInfoRequest;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param idComponent
	 * @param list
	 * @return
	 */
	private ComponentJson getComponentResponseById(Integer idComponent, ComponentJson[] components) {
		for (ComponentJson component : components) {

			if (component.getId_component().equals(idComponent)) {
				return component;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param components
	 * @param componentInfoRequests
	 * @throws Exception
	 */
	private void checkComponentsSize(ComponentJson[] components, List<ComponentInfoRequest> componentInfoRequests) throws Exception {
		
		int columnCount = 0;

		for (ComponentInfoRequest info : componentInfoRequests) {
			if ( info != null && !info.isSkipColumn()){
				columnCount++;
			}
		}

		logger.debug("[DatasetServiceImpl::checkComponentsSize] components.size:[" + components.length + "], componentInfoRequestsCount:[" + columnCount + "]");

		if (columnCount != components.length || columnCount == 0) {
			throw new BadRequestException(Errors.NOT_ACCEPTABLE);
		}			
		
	}

	@Override
	public ServiceResponse importMetadata(ServletContext servletContext, String organizationCode, ImportMetadataDatasetRequest importMetadataRequest, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {

		Tenant tenant = tenantMapper.selectTenantByTenantCode(importMetadataRequest.getTenantCode());

		if (DatabaseConfiguration.DB_TYPE_HIVE.equals(importMetadataRequest.getDbType())) {
			importMetadataRequest.setJdbcDbname(DatabaseReader.getHiveDbName(tenant.getIdTenantType(), importMetadataRequest.getTenantCode(), organizationCode));
			importMetadataRequest.setJdbcHostname("yucca_datalake");
		}
		List<DettaglioDataset> existingMedatataList = datasetMapper.selectDatasetFromJdbc(importMetadataRequest.getJdbcHostname(), importMetadataRequest.getJdbcDbname(),
				importMetadataRequest.getDbType(), importMetadataRequest.getTenantCode(), organizationCode, getTenantCodeListFromUser(authorizedUser));

		// DettaglioDataset dettaglioDataset =
		// datasetMapper.selectDettaglioDataset(tenantCodeManager, idDataset,
		// organizationCode, getTenantCodeListFromUser(authorizedUser));

		DatabaseReader databaseReader = new DatabaseReader(servletContext, organizationCode, importMetadataRequest.getTenantCode(), importMetadataRequest.getDbType(),
				importMetadataRequest.getJdbcHostname(), importMetadataRequest.getJdbcDbname(), importMetadataRequest.getJdbcUsername(), importMetadataRequest.getJdbcPassword(),
				existingMedatataList, hiveUser, hivePassword, hiveUrl, tenant.getIdTenantType());
		String schema = databaseReader.loadSchema();

		return buildResponse(schema);
	}
	
	@Override
	public ServiceResponse importMetadata(ServletContext servletContext, String organizationCode, ImportMetadataDatasetRequest importMetadataRequest)
			throws BadRequestException, NotFoundException, Exception {

		//Tenant tenant = tenantMapper.selectTenantByTenantCode(importMetadataRequest.getTenantCode());
		
		//List<String> tenantCodeList = new ArrayList<>();
		
		//tenantCodeList.add(tenant.getTenantcode());
		

		if (DatabaseConfiguration.DB_TYPE_HIVE.equals(importMetadataRequest.getDbType())) {
			importMetadataRequest.setJdbcDbname(DatabaseReader.getHiveDbName(importMetadataRequest.getDomain(), importMetadataRequest.getSubdomain(), organizationCode));
			importMetadataRequest.setJdbcHostname("yucca_datalake");
		}
		
		List<String> tenantCodeList = new LinkedList<String>();
		tenantCodeList.add(importMetadataRequest.getTenantCode());
		String hiveSchema = DatabaseReader.getHiveDbName(importMetadataRequest.getDomain(), importMetadataRequest.getSubdomain(), organizationCode);
		List<DettaglioDataset> existingMedatataList = datasetMapper.selectDatasetFromHiveParam(hiveSchema, importMetadataRequest.getTenantCode(), organizationCode,tenantCodeList);

		// DettaglioDataset dettaglioDataset =
		// datasetMapper.selectDettaglioDataset(tenantCodeManager, idDataset,
		// organizationCode, getTenantCodeListFromUser(authorizedUser));

		DatabaseReader databaseReader = new DatabaseReader(servletContext, organizationCode, importMetadataRequest.getDbType(),
				importMetadataRequest.getJdbcHostname(), importMetadataRequest.getJdbcDbname(), importMetadataRequest.getJdbcUsername(), importMetadataRequest.getJdbcPassword(),
				existingMedatataList, hiveUser, hivePassword, hiveUrl, importMetadataRequest.getDomain(),importMetadataRequest.getSubdomain());
		String schema = databaseReader.loadSchema();

		return buildResponse(schema);
	}

	private void removeOdataApiAndSolrDocument(String datasetCode) throws Exception {
		CloseableHttpClient httpclient = PublisherDelegate.build().registerToStoreInit();
		removeOdataApiAndSolrDocument(httpclient, datasetCode);
	}

	private void removeOdataApiAndSolrDocument(CloseableHttpClient httpclient, String datasetCode) {
		logger.info("[DatasetServiceImpl::removeOdataApiAndSolrDocument] - unpublish datasetcode: " + datasetCode);
		try {
			String removeApiResponse = PublisherDelegate.build().removeApi(httpclient, PublisherDelegate.createApiNameOData(datasetCode));
			logger.info("[DatasetServiceImpl::removeOdataApiAndSolrDocument] - unpublish removeApi: " + removeApiResponse);

		} catch (Exception ex) {
			logger.error("[DatasetServiceImpl::removeOdataApiAndSolrDocument] unpublish removeApi ERROR" + datasetCode + " - " + ex.getMessage());
		}

		try {
			SolrDelegate.build().removeDocument(datasetCode);
		} catch (Exception ex) {
			logger.error("[DatasetServiceImpl::removeOdataApiAndSolrDocument] unpublish removeDocument ERROR" + datasetCode + " - " + ex.getMessage());
		}
	}

	@Override
	public ServiceResponse updateHiveExternalTable(String tableName, String organizationCode, Integer idDataset, JwtUser authorizedUser) throws Exception {

		logger.info("[DatasetServiceImpl::updateHiveExternalTable] Begin idDataset:[" + idDataset + "]");

		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));
		checkIfFoundRecord(dataset);

		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dataset, streamMapper, smartobjectMapper, datasetMapper);

		String message = "";
		try {
			String tenantCodeManager = dataset.getTenantCode();

			logger.info("[DatasetServiceImpl::updateHiveExternalTable] createExternalTable tableName: " + tableName + ", organizationCode: " +organizationCode+ 
						", tenantCodeManager: " + tenantCodeManager + ", dettaglio: " +dettaglio);
			HiveDelegate.build().createExternalTable(tableName, organizationCode, tenantCodeManager, dettaglio);
			logger.info("[DatasetServiceImpl::updateHiveExternalTable] createExternalTable ok"); 
			message = "Ok";
		} catch (ClassNotFoundException e) {
			logger.error("[DatasetServiceImpl::updateHiveExternalTable] ClassNotFoundException  idDataset:[" + idDataset + "]" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			logger.error("[DatasetServiceImpl::updateHiveExternalTable] SQLException  idDataset:[" + idDataset + "]" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return ServiceResponse.build().object(message);
	}

	@Override
	public ServiceResponse hdfsFiles(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {
		logger.info("[DatasetServiceImpl::hdfsFiles] Begin idDataset:[" + idDataset + "]");

		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dataset, streamMapper, smartobjectMapper, datasetMapper);

		checkIfFoundRecord(dataset);

		String hdfsDir;
		try {
			hdfsDir = HdfsDelegate.build().getHdfsDir(organizationCode, dettaglio);
			FileStatusesContainer listStatus = HdfsDelegate.build().listStatus(hdfsDir);
			return ServiceResponse.build().object(listStatus);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new NotFoundException(Errors.HDFS_FOLDER_NOT_FOUND);
		}

	}
	
	@Override
	public ServiceResponse rangerPolicies(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {
		logger.info("[DatasetServiceImpl::rangerPolicies] Begin idDataset:[" + idDataset + "]");

		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dataset, streamMapper, smartobjectMapper, datasetMapper);

		checkIfFoundRecord(dataset);

		String hdfsPolicyName = "";
		
		try {
			hdfsPolicyName = RangerDelegate.build().getPolicyName(organizationCode, dettaglio.getDataset().getDatasetcode());
			String policy = RangerDelegate.build().listPolicies(hdfsPolicyName);
			return ServiceResponse.build().object(policy);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	@Override
	public ServiceResponse createRangerPolicy(String organizationCode, Integer idDataset, JwtUser authorizedUser ) throws BadRequestException, NotFoundException, Exception {	
		logger.info("[DatasetServiceImpl::createRangerPolicy] Begin idDataset:[" + idDataset + "]");
		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));
	
		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dataset, streamMapper, smartobjectMapper, datasetMapper);
	
		checkIfFoundRecord(dataset);
		String hdfsPolicyName = "";
		String hdfsDir="";
		
		try {
			PostRangerRequest rangerRequest =  new PostRangerRequest();
			hdfsPolicyName = RangerDelegate.build().getPolicyName(organizationCode, dettaglio.getDataset().getDatasetcode());
			hdfsDir = HdfsDelegate.build().getHdfsDir(organizationCode, dettaglio);
			ResourceRangerRequest resource = new ResourceRangerRequest();
			List <PolicyItemRequest> policies = new ArrayList <PolicyItemRequest>();
			
			/* Insert policyItem*/
			policies = ServiceUtil.insertPolicyItemRequest(dettaglio,policies);
			
			/*Insert ResourceRangerRequest*/			
			resource = ServiceUtil.insertResourceRangerRequest(hdfsDir,resource);
			
			/*Insert PostRangerRequest*/
			rangerRequest =  ServiceUtil.insertPostRangerRequest(policies, resource, rangerRequest,service, hdfsPolicyName);
						
			String policy = RangerDelegate.build().createPolicy(rangerRequest);
			return ServiceResponse.build().object(policy);
			
		}  catch (BadRequestException e) {
			logger.error("[DatasetServiceImpl::createRangerPolicy] BadRequestException  policyName:[" + hdfsPolicyName + "]" + e.getMessage());
			e.printStackTrace();
			throw e;
		
		 } catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	@Override
	public ServiceResponse updateRangerPolicy(String organizationCode, Integer idDataset, JwtUser authorizedUser ) throws BadRequestException, NotFoundException, Exception {	
	

		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dataset, streamMapper, smartobjectMapper, datasetMapper);

		checkIfFoundRecord(dataset);

		String hdfsPolicyName = "";
		
		try {
			hdfsPolicyName = RangerDelegate.build().getPolicyName(organizationCode, dettaglio.getDataset().getDatasetcode());
			String policy = RangerDelegate.build().listPolicies(hdfsPolicyName);
			PostRangerRequest updateRequest = RangerDelegate.JsonToPostRangerRequest(policy);
			
			updateRequest.getPolicyItems().get(0).setGroups(new ArrayList<String>());
			
			for(TenantResponse tenant:dettaglio.getSharingTenants()){
					updateRequest.getPolicyItems().get(0).getGroups().add(tenant.getTenantcode());
				
			}
			
			updateRequest.getPolicyItems().get(0).getGroups().add(dettaglio.getTenantManager().getTenantcode());
			
		
			String update = RangerDelegate.build().updatePolicy(updateRequest,hdfsPolicyName);
			return ServiceResponse.build().object(update);	
		}  catch (BadRequestException e) {
			logger.error("[DatasetServiceImpl::updateRangerPolicy] BadRequestException  policyName:[" + hdfsPolicyName + "]" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

	}

	@Override
	public ServiceResponse forceDownloadCsv(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {
		logger.info("[DatasetServiceImpl::forceDownloadCsv] Begin idDataset:[" + idDataset + "]");

		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		checkIfFoundRecord(dataset);
		ObjectId maxId = new ObjectId();

		StringBuffer xmlInput = new StringBuffer();
		xmlInput.append("<configuration>");
		xmlInput.append("  <property>");
		xmlInput.append("    <name>datasetCode</name>");
		xmlInput.append("    <value>" + dataset.getDatasetcode() + "</value>");
		xmlInput.append("  </property>");
		xmlInput.append("  <property>");
		xmlInput.append("    <name>user.name</name>");
		xmlInput.append("    <value>" + knoxUserBatch + "</value>");
		xmlInput.append("  </property>");
		xmlInput.append("   <property>");
		xmlInput.append("    <name>adminApiUri</name>");
		xmlInput.append("    <value>" + adminapiUrl + "</value>");
		xmlInput.append("  </property>");
		xmlInput.append("  <property>");
		xmlInput.append("    <name>mapreduce.job.user.name</name>");
		xmlInput.append("    <value>" + knoxUserBatch + "</value>");
		xmlInput.append("  </property>");
		xmlInput.append("  <property>");
		xmlInput.append("    <name>maxId</name>");
		xmlInput.append("    <value>" + maxId + "</value>");
		xmlInput.append("  </property>");
		xmlInput.append("  <property>");
		xmlInput.append("    <name>oozie.wf.application.path</name>");
		xmlInput.append("    <value>/csi_scripts/DOWNLOADCSV/WF_DOWNLOADCSV_SINGLEDS_MAXID.xml</value>");
		xmlInput.append("  </property>");
		xmlInput.append("  <property>");
		xmlInput.append("    <name>organizations</name>");
		xmlInput.append("    <value>" + organizationCode + "</value>");
		xmlInput.append("  </property>");
		xmlInput.append("</configuration>");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String oozieCompleteUrl = oozieUrl + "/v1/jobs?action=start";
		String response = HttpDelegate.makeHttpPost(httpclient, oozieCompleteUrl, null, null, null, xmlInput.toString(), ContentType.APPLICATION_XML);

		logger.info("[DatasetServiceImpl::forceDownloadCsv] response: " + response);

		return ServiceResponse.build().object(response);
	}
	
	@Override
	public ServiceResponse actionOnOozie(ActionOozieRequest actionOozieRequest) throws BadRequestException, NotFoundException, Exception {
		logger.info("[DatasetServiceImpl::actionOnOozie] Begin ");
				
		String response = "";
		
		if (actionOozieRequest.getAction().equals("promotion")) { 
			 response = OozieDelegate.build().makePromotion(actionOozieRequest);
		}
		
		else if (actionOozieRequest.getAction().equals("pubblication")) { 
			 response = OozieDelegate.build().makePubblication(actionOozieRequest);
		}
		
		logger.info("[DatasetServiceImpl::actionOnOozie] response: " + response);

		return ServiceResponse.build().object(response);
	}
	
	@Override
	public ServiceResponse infoOnOozie(String oozieProcessId) throws BadRequestException, NotFoundException, Exception {
		logger.info("[DatasetServiceImpl::infoOnOozie] Begin ");

		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String oozieCompleteUrl = oozieUrl + "/v1/job/"+oozieProcessId+"?show=info";
		String response = HttpDelegate.makeHttpGet(httpclient, oozieCompleteUrl,null);

		logger.info("[DatasetServiceImpl::infoOnOozie] response: " + response);

		return ServiceResponse.build().object(response);
	}

	@Override
	public ServiceResponse selectDatasetsSlim(Boolean isSlim, String tenantCode) throws BadRequestException, NotFoundException, Exception {

		if(isSlim==null || !isSlim)
			throw new BadRequestException(Errors.PROPERTY_NOT_FOUND, "slim=true required");
		List<BackofficeDettaglioStreamDatasetResponse> response = new ArrayList<>();

		List<DettaglioDataset> listDettaglioDataset = null;
		if(tenantCode!=null)
			listDettaglioDataset = datasetMapper.selectListaDettaglioDatasetByTenantCode(tenantCode);
		else
			listDettaglioDataset = datasetMapper.selectListaDettaglioDataset();

		checkList(listDettaglioDataset);

		for (DettaglioDataset dettaglioDataset : listDettaglioDataset) {
			// response.add(getBackofficeDettaglioStreamDatasetResponse(dettaglioDataset));
			//response.add(ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper));
			dettaglioDataset.setDataSourceIcon(null);
			dettaglioDataset.setComponents(null);
			response.add(new BackofficeDettaglioStreamDatasetResponse(dettaglioDataset, null));
		}
		return buildResponse(response );	}

	/*public static void main(String[] args) {
		String[] columns = new String[] {"2,43","22.22","12.22","34,43",
				"3.132,43","1,222.22","1,112.22","3.234,43",
				"4 22,43","4 552.22","1 200.22","3 400,43",
				"2","3","12","34"};
		String[] decimalSeparators = new String[] {Constants.ADMINAPI_DECIMAL_SEPARATOR_COMMA,Constants.ADMINAPI_DECIMAL_SEPARATOR_DOT,Constants.ADMINAPI_DECIMAL_SEPARATOR_DOT,Constants.ADMINAPI_DECIMAL_SEPARATOR_COMMA,
				Constants.ADMINAPI_DECIMAL_SEPARATOR_COMMA,Constants.ADMINAPI_DECIMAL_SEPARATOR_DOT,Constants.ADMINAPI_DECIMAL_SEPARATOR_DOT,Constants.ADMINAPI_DECIMAL_SEPARATOR_COMMA,
				Constants.ADMINAPI_DECIMAL_SEPARATOR_COMMA,Constants.ADMINAPI_DECIMAL_SEPARATOR_DOT,Constants.ADMINAPI_DECIMAL_SEPARATOR_DOT,Constants.ADMINAPI_DECIMAL_SEPARATOR_COMMA,
				Constants.ADMINAPI_DECIMAL_SEPARATOR_COMMA,Constants.ADMINAPI_DECIMAL_SEPARATOR_DOT,Constants.ADMINAPI_DECIMAL_SEPARATOR_DOT,Constants.ADMINAPI_DECIMAL_SEPARATOR_COMMA};
		try {
			for (int i = 0; i < columns.length; i++) {
				String num = Util.formatDecimalValue(columns[i], decimalSeparators[i]);
				Double d = Double.valueOf(num);
				System.out.println(decimalSeparators[i] + " - column " + columns[i] + " - num " + "d "+ d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 	Verifica l'esistenza dei datasource associati al tenant.
	 * 
	 * @param listDataSource
	 * @param tenantCodeManager
	 */
//	private void checkDatasourceList(List<DataSourceRequest> listDataSource, String tenantCodeManager)throws Exception{
//
//		for (DataSourceRequest dataSource: listDataSource) {
//
//			TenantDataSource tenantDataSource = tenantMapper.selectTenantDataSourceByTenantCode(
//					dataSource.getIdDatasource(), 
//					dataSource.getDatasourceversion(), 
//					tenantCodeManager);
//
//			checkIfFoundRecord(tenantDataSource, 
//					"Datasource not found: [ id: " + dataSource.getIdDatasource() + 
//					", version: " + dataSource.getDatasourceversion() + ", tenant: "+ tenantCodeManager + " ]");
//		}
//	}

	/**
	 * 
	 * @param idDatasourcegroup
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private void consolidateDatasourceGroupVersion(Long idDatasourcegroup) throws BadRequestException, NotFoundException, Exception {
		DataSourceGroup dataSourceGroup = dataSourceGroupMapper.selectLastVersionDataSourceGroupById(idDatasourcegroup);
		
		checkIfFoundRecord(dataSourceGroup, "data source group [ id: " + idDatasourcegroup + " ]");
		
		if ( DataSourceGroupType.USER_DEFINED.id().equals(dataSourceGroup.getIdDatasourcegroupType()) ||
				 DatasourceGroupStatus.FROZEN.name().equals(dataSourceGroup.getStatus())) {
				throw new BadRequestException(Errors.NOT_ACCEPTABLE,  "for this action dataSourceGroup must be " + DatasourceGroupStatus.DRAFT.name() + " and  SPECIAL DATASET type.");
		}

		List<DataSource> datasources = dataSourceGroupMapper.seletcDatasourcesByDatasourcegroup(idDatasourcegroup, dataSourceGroup.getDatasourcegroupversion());
		
		checkList(datasources, "datasources");			

		dataSourceGroup.setStatus(DatasourceGroupStatus.FROZEN.name());
		dataSourceGroupMapper.dismissOldVersionOfDataSourceGroup(dataSourceGroup);
		
		dataSourceGroupMapper.updateDataSourceGroup(dataSourceGroup);
	}
	
	/**
	 * 
	 * @param idDatasourcegroup
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private void newDatasourceGroupVersion(Long idDatasourcegroup) throws BadRequestException, NotFoundException, Exception {
		DataSourceGroup dataSourceGroup = dataSourceGroupMapper.selectLastVersionDataSourceGroupById(idDatasourcegroup);
		checkIfFoundRecord(dataSourceGroup, "data source group [ id: " + idDatasourcegroup + " ]");

		if ( DataSourceGroupType.USER_DEFINED.id().equals(dataSourceGroup.getIdDatasourcegroupType()) ||
			 DatasourceGroupStatus.DRAFT.name().equals(dataSourceGroup.getStatus())) {
			throw new BadRequestException(Errors.NOT_ACCEPTABLE, "for this action dataSourceGroup must be " + DatasourceGroupStatus.FROZEN.name() + " or " + DatasourceGroupStatus.DISMISSED.name() + " and SPECIAL DATASET type.");
		}
		
		//	crea una copia del datasetgroup con versione incrementata e stato bozza: 
		DataSourceGroup newVersionDataSourceGroup = new DataSourceGroupBuilder()
				  .idDatasourcegroup(dataSourceGroup.getIdDatasourcegroup())
				  .idTenant(dataSourceGroup.getIdTenant())
				  .datasourcegroupversion(dataSourceGroup.getDatasourcegroupversion() + 1)
				  .name(dataSourceGroup.getName())
				  .idDatasourcegroupType(dataSourceGroup.getIdDatasourcegroupType())
				  .color(dataSourceGroup.getColor())
				  .status(DatasourceGroupStatus.DRAFT.name()).build();
		dataSourceGroupMapper.insertDataSourceGroup(newVersionDataSourceGroup);

		// copia tutte le relazioni con i dataset:
		List<DataSource> datasources = dataSourceGroupMapper.seletcDatasourcesByDatasourcegroup(dataSourceGroup.getIdDatasourcegroup(), 
				dataSourceGroup.getDatasourcegroupversion());
		for (DataSource dataSource : datasources) {
			dataSourceGroupMapper.insertDatasourceDatasourcegroup(
					newVersionDataSourceGroup.getIdDatasourcegroup(), 
					newVersionDataSourceGroup.getDatasourcegroupversion(), 
					dataSource.getDatasourceversion(), dataSource.getIdDataSource());
		}
	}
		
	@Override
	public ServiceResponse deleteDatasetData(Integer idDataset, Integer version)
			throws BadRequestException, NotFoundException, Exception {

		// Verifica che l'utente loggato nel jwt possa utilizzare il tenant

		DettaglioDataset dataset = datasetMapper.selectDettaglioDatasetByIdDataset(idDataset, false);

		checkIfFoundRecord(dataset);

		User user = userMapper.selectUserByIdDataSourceAndVersion(dataset.getIdDataSource(), dataset.getDatasourceversion(), dataset.getTenantCode(), DataOption.WRITE.id());

		String url = datainsertDeleteUrl + dataset.getTenantCode() + "/" + idDataset + (version != null ? "/" + version : "");

		HttpDelegate.makeHttpDelete(url, user.getUsername(), user.getPassword());

		return ServiceResponse.build().NO_CONTENT();
	}
	
	/**
	 * 
	 * @param idDatasourcegroup
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private void dismissDatasourceGroupVersion(Long idDatasourcegroup) throws BadRequestException, NotFoundException, Exception {
		DataSourceGroup dataSourceGroup = dataSourceGroupMapper.selectLastVersionDataSourceGroupById(idDatasourcegroup);
		
		checkIfFoundRecord(dataSourceGroup, "data source group [ id: " + idDatasourcegroup + " ]");
		
		if ( DataSourceGroupType.USER_DEFINED.id().equals(dataSourceGroup.getIdDatasourcegroupType()) ||
				 !DatasourceGroupStatus.FROZEN.name().equals(dataSourceGroup.getStatus())) {
				throw new BadRequestException(Errors.NOT_ACCEPTABLE,  "for this action dataSourceGroup must be " + DatasourceGroupStatus.FROZEN.name() + " and  SPECIAL DATASET type.");
		}

		List<DataSource> datasources = dataSourceGroupMapper.seletcDatasourcesByDatasourcegroup(idDatasourcegroup, dataSourceGroup.getDatasourcegroupversion());
		
		checkList(datasources, "datasources");			

		dataSourceGroup.setStatus(DatasourceGroupStatus.DISMISSED.name());
		
		dataSourceGroupMapper.updateDataSourceGroup(dataSourceGroup);
	}
	
	/**
	 * 
	 * @param idDatasourcegroup
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private void restoreDatasourceGroupVersion(Long idDatasourcegroup) throws BadRequestException, NotFoundException, Exception {
		
		DataSourceGroup dataSourceGroup = dataSourceGroupMapper.selectLastVersionDataSourceGroupById(idDatasourcegroup);
		
		checkIfFoundRecord(dataSourceGroup, "data source group [ id: " + idDatasourcegroup + " ]");
		
		logger.info("[DatasetServiceImpl::restoreDatasourceGroupVersion] - IdDatasourcegroupType: " + dataSourceGroup.getIdDatasourcegroupType() + " | status: " + dataSourceGroup.getStatus());
		if ( DataSourceGroupType.USER_DEFINED.id().equals(dataSourceGroup.getIdDatasourcegroupType()) ||
				 !DatasourceGroupStatus.DISMISSED.name().equals(dataSourceGroup.getStatus())) {
				throw new BadRequestException(Errors.NOT_ACCEPTABLE,  "for t dataSourceGroup must be " + DatasourceGroupStatus.DISMISSED.name() + " and  SPECIAL DATASET type.");
		}

		List<DataSource> datasources = dataSourceGroupMapper.seletcDatasourcesByDatasourcegroup(idDatasourcegroup, dataSourceGroup.getDatasourcegroupversion());
		
		checkList(datasources, "datasources");			

		dataSourceGroup.setStatus(DatasourceGroupStatus.FROZEN.name());
		
		dataSourceGroupMapper.updateDataSourceGroup(dataSourceGroup);
	}

	@Override
	public ServiceResponse selectDatasetByDatasetGroup(String organizationCode, Long idDatasourcegroup,
			Integer datasetGroupVersion, String tenantCodeManager, String sort, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {
		checkAuthTenant(authorizedUser, tenantCodeManager);

		List<String> sortList = getSortList(sort, Dataset.class);

		List<Dataset> dataSetList = datasetMapper.selectDataSetsByDatasetGroup(tenantCodeManager, organizationCode, sortList, getTenantCodeListFromUser(authorizedUser), idDatasourcegroup, datasetGroupVersion);

		checkList(dataSetList);

		List<DatasetResponse> listResponse = new ArrayList<DatasetResponse>();
		for (Dataset dataset : dataSetList) {
			
			DatasetResponse res = new DatasetResponse(dataset);
			listResponse.add(res);
		}

		return buildResponse(listResponse);	
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse updateDatasetFromBackoffice(String organizationCode, Integer idDataset, DatasetRequest datasetRequest, String tenantCodeManager, 
			Boolean publish) throws BadRequestException, NotFoundException, Exception {

		Tenant tenant = checkTenant(datasetRequest.getIdTenant(), organizationCode, tenantMapper);

		List<String> authorizedTenants = new LinkedList<String>();
		authorizedTenants.add(tenant.getTenantcode());
		
		updateDatasetValidation(tenant, datasetRequest, authorizedTenants, organizationCode,  idDataset, tenantCodeManager);

		updateDatasetTransaction(datasetRequest, tenantCodeManager, publish);

		PostDatasetResponse response = PostDatasetResponse.build(idDataset).datasetcode(datasetRequest.getDatasetcode()).datasetname(datasetRequest.getDatasetname());
				
		return ServiceResponse.build().object(response);

	}

}
