/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.model.TenantDataSource;
import org.csi.yucca.adminapi.model.TenantProductContact;
import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;
import org.csi.yucca.adminapi.model.join.TenantManagement;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface TenantMapper {
	
	String TENANT_TABLE =           Constants.SCHEMA_DB + "yucca_tenant";
	String R_TENANT_BUNDLES_TABLE = Constants.SCHEMA_DB + "yucca_r_tenant_bundles";
	String R_TENANT_DATA_SOURCE_TABLE = Constants.SCHEMA_DB + "yucca_r_tenant_data_source";
	String TENANT_STATUS_TABLE =    Constants.SCHEMA_DB + "yucca_d_tenant_status";
	String TENANT_TYPE_TABLE =      Constants.SCHEMA_DB + "yucca_d_tenant_type";
	String SHARE_TYPE_TABLE =       Constants.SCHEMA_DB + "yucca_d_share_type";
	String TOOL_TABLE = 			Constants.SCHEMA_DB + "yucca_tool";
	String R_BUNDLES_TOOL = 		Constants.SCHEMA_DB + "yucca_r_bundles_tool";
	String R_TENANT_PRODUCT = 		Constants.SCHEMA_DB + "yucca_r_tenant_product";
	String R_TENANT_CONTACT = 		Constants.SCHEMA_DB + "yucca_r_tenant_contact";
	
	/*************************************************************************
	 * 
	 * 					INSERT TENANT CONTACT
	 * 
	 * ***********************************************************************/	
	public static final String INSERT_TENANT_CONTACT = 
			" INSERT INTO " + R_TENANT_CONTACT + "( " +
			"         id_tenant, name, email, contactrole, productcode) " +
			" VALUES (#{idTenant}, #{name}, #{email}, #{contactrole}, #{productcode}) ";
	@Insert(INSERT_TENANT_CONTACT)
	int insertTenantContact(TenantProductContact contact);
	
	/*************************************************************************
	 * 
	 * 					DELETE TENANT CONTACT
	 * 
	 * ***********************************************************************/
	public static final String DELETE_TENANT_CONTACT = 
	"DELETE FROM " + R_TENANT_CONTACT + " WHERE id_tenant = #{idTenant} and productcode = #{productcode} and contactrole=#{contactrole}";
	@Delete(DELETE_TENANT_CONTACT)
	int deleteTenantContact(TenantProductContact contact);	
	
	/*************************************************************************
	 * 
	 * 					SELECT TENANT PRODUCT
	 * 
	 * ***********************************************************************/
	public static final String SELECT_TENANT_PRODUCT = "SELECT id_tenant, productcode FROM " + R_TENANT_PRODUCT;
	@Results({
        @Result(property = "idTenant", column = "id_tenant")
      })	
	@Select(SELECT_TENANT_PRODUCT) 
	List<TenantProductContact> selectTenantProduct();
	
	public static final String SELECT_TENANT_COLUMNS = 	
			" SELECT TENANT.id_tenant, TENANT.tenantcode, TENANT.name, TENANT.description, clientkey, clientsecret, activationdate, deactivationdate, "
			+ "usagedaysnumber, TENANT.username, userfirstname, userlastname, useremail, usertypeauth, creationdate, expirationdate, id_ecosystem, "
			+ "TENANT.id_organization, id_tenant_type, id_tenant_status,  TENANT.datasolrcollectionname, TENANT.measuresolrcollectionname, "
			+ "TENANT.mediasolrcollectionname, TENANT.socialsolrcollectionname, TENANT.dataphoenixtablename, TENANT.dataphoenixschemaname, "
			+ "TENANT.measuresphoenixtablename, TENANT.measuresphoenixschemaname, TENANT.mediaphoenixtablename, TENANT.mediaphoenixschemaname, "
			+ "TENANT.socialphoenixtablename, TENANT.socialphoenixschemaname, id_share_type";
	
	public static final String SELECT_TENANT_ORDER_BY =  
			
			"<if test=\"sortList != null\">" +
		      " ORDER BY " +
			
			" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
		    
			  "<if test=\"propName == 'tenantcode-'\">" +
		        " tenantcode desc" +
	          "</if>" +
	          "<if test=\"propName == 'tenantcode'\">" +
	            " tenantcode" +
              "</if>" +
		      
		      "<if test=\"propName == 'name-'\">" +
		        " name desc" +
	          "</if>" +
	          "<if test=\"propName == 'name'\">" +
	            " name" +
              "</if>" +
			
		      "<if test=\"propName == 'idTenantStatus-'\">" +
		        " id_tenant_status desc" +
	          "</if>" +
	          "<if test=\"propName == 'idTenantStatus'\">" +
	            " id_tenant_status" +
              "</if>" +
            
            "</foreach>" +
            "</if>";	
	
	/*************************************************************************
	 * 					SELECT R_TENANT_DATA_SOURCE 
	 * ***********************************************************************/
	public static final String SELECT_R_TENANAT_DATA_SOURCE_BY_TENANT_ID =
	" SELECT id_data_source, datasourceversion, id_tenant, isactive, ismanager, " + 
    " 	dataoptions, manageoptions, activationdate, deactivationdate, " + 
    " 	managerfrom, manageruntil " +
    " FROM " + R_TENANT_DATA_SOURCE_TABLE + 
    " WHERE id_data_source = #{idDataSource} AND " +
    " 		datasourceversion = #{dataSourceVersion} AND " +
    " 		id_tenant = #{idTenant} ";
	@Results({
        @Result(property = "idDataSource", column = "id_data_source"),
        @Result(property = "idTenant", column = "id_tenant")
      })	
	@Select(SELECT_R_TENANAT_DATA_SOURCE_BY_TENANT_ID)
	TenantDataSource selectTenantDataSource(
			@Param("idDataSource") Long idDataSource, 
			@Param("dataSourceVersion") Long dataSourceVersion,
			@Param("idTenant") Integer idTenant);
	
	/*************************************************************************
	 * 					SELECT R_TENANT_DATA_SOURCE_BY_TENANT_CODE 
	 * ***********************************************************************/
//	[tenantCode, idDataSource, param3, param1, dataSourceVersion, param2]"
	public static final String SELECT_R_TENANAT_DATA_SOURCE_BY_TENANT_CODE =
	" SELECT R_TEN_DAT.id_data_source, R_TEN_DAT.datasourceversion, R_TEN_DAT.id_tenant, R_TEN_DAT.isactive, R_TEN_DAT.ismanager, " + 
	" R_TEN_DAT.dataoptions, R_TEN_DAT.manageoptions, R_TEN_DAT.activationdate, R_TEN_DAT.deactivationdate, " + 
    " R_TEN_DAT.managerfrom, R_TEN_DAT.manageruntil " + 
	" FROM " + R_TENANT_DATA_SOURCE_TABLE + " R_TEN_DAT, " + TENANT_TABLE + " TENANT " +
    " WHERE R_TEN_DAT.id_data_source = #{idDataSource} AND " + 
    " R_TEN_DAT.datasourceversion = #{dataSourceVersion} AND " + 
	" R_TEN_DAT.id_tenant = TENANT.id_tenant AND " +
	" TENANT.tenantcode = #{tenantCode} ";
	@Results({
        @Result(property = "idDataSource", column = "id_data_source"),
        @Result(property = "idTenant", column = "id_tenant")
      })	
	@Select(SELECT_R_TENANAT_DATA_SOURCE_BY_TENANT_CODE)
	TenantDataSource selectTenantDataSourceByTenantCode(
			@Param("idDataSource") Integer idDataSource, 
			@Param("dataSourceVersion") Integer dataSourceVersion,
			@Param("tenantCode") String tenantCode);
	
	/*************************************************************************
	 * 					CLONE R_TENANT_DATA_SOURCE WITH NEW VERSION
	 * ***********************************************************************/
	public static final String CLONE_R_TENANT_DATA_SOURCE_NEW_VERSION = 
	" INSERT INTO " + R_TENANT_DATA_SOURCE_TABLE  + ""
			+ "( id_data_source, datasourceversion, id_tenant, isactive, ismanager, "
			+ " dataoptions, manageoptions, activationdate, deactivationdate,  managerfrom, manageruntil) "
      + " SELECT id_data_source, #{newDataSourceVersion}, id_tenant, isactive, ismanager,"
            + " dataoptions, manageoptions, activationdate, deactivationdate,  managerfrom, manageruntil"
      + " FROM " + R_TENANT_DATA_SOURCE_TABLE  
      + " WHERE id_data_source = #{idDataSource} and datasourceversion=#{currentDataSourceVersion}";
	@Insert(CLONE_R_TENANT_DATA_SOURCE_NEW_VERSION)
	int cloneTenantDataSourceNewVersion(
			@Param("newDataSourceVersion") Integer newDataSourceVersion,
			@Param("currentDataSourceVersion") Integer currentDataSourceVersion, 
			@Param("idDataSource") Integer idDataSource);
	
	
	/*************************************************************************
	 * 
	 * 					DELETE NOT MANAGER TENANT DATASOURCE
	 * 
	 * ***********************************************************************/
	public static final String DELETE_NOT_MANAGER_TENANT_DATA_SOURCE = 
	" DELETE from " + R_TENANT_DATA_SOURCE_TABLE + " WHERE  id_data_source = #{idDataSource} and datasourceversion = #{dataSourceVersion} and ismanager = 0";
	@Delete(DELETE_NOT_MANAGER_TENANT_DATA_SOURCE)
	int deleteNotManagerTenantDataSource(@Param("idDataSource") Integer idDataSource, @Param("dataSourceVersion") Integer dataSourceVersion);	
	
	
	/*************************************************************************
	 * 
	 * 					SELECT TENANT BY TENANT CODE
	 * 
	 * ***********************************************************************/
	public static final String SELECT_DETTAGLIO_TENANT =
			" SELECT USERS.password,tenantcode, TENANT.id_tenant, TENANT.description, TENANT.name, usagedaysnumber, useremail, "+ 
			" userfirstname, userlastname, usertypeauth, TENANT.username, BUNDLES.id_bundles, BUNDLES.maxdatasetnum, "+ 
			" BUNDLES.maxstreamsnum, BUNDLES.hasstage, BUNDLES.max_odata_resultperpage, BUNDLES.zeppelin, BUNDLES.readytools, "+
			
			//Aggregazione dei tool in un unico array JSON accessibile tramite la variabile 'toolsstring'
			" (SELECT array_to_json(array_agg(row_to_json(tool))) " + 
			" FROM 	( SELECT TOOL.id_tool, TOOL.name, TOOL.toolversion, BUNDLES_TOOL.enabled " + 
			"		  FROM "+TOOL_TABLE+" TOOL, "+R_BUNDLES_TOOL+" BUNDLES_TOOL " + 
			"		  WHERE TOOL.id_tool = BUNDLES_TOOL.id_tool " + 
			"		  AND BUNDLES_TOOL.id_bundles = BUNDLES.id_bundles " +
			" 		) tool) toolsstring, " +
			
			" TENANT.id_ecosystem, ECOSYSTEM.ecosystemcode, ECOSYSTEM.description ecosystemdescription, "+
			" TENANT.id_organization, ORGANIZATION.organizationcode, ORGANIZATION.description as organizationdescription, "+
			" TENANT.id_tenant_status, TENANT_STATUS.tenantstatuscode, TENANT_STATUS.description as tenantstatusdescription, "+
			" TENANT.id_tenant_type, TENANT_TYPE.tenanttypecode, TENANT_TYPE.description tenanttypedescription, "+
			" TENANT.id_share_type, TENANT.deactivationdate,SHARE_TYPE.description as sharetypedescription, "+
			" coalesce(TENANT.datasolrcollectionname, ORGANIZATION.datasolrcollectionname) AS datasolrcollectionname, "+
			" coalesce(TENANT.measuresolrcollectionname, ORGANIZATION.measuresolrcollectionname) AS measuresolrcollectionname, "+
			" coalesce(TENANT.measuresphoenixschemaname, ORGANIZATION.measuresphoenixschemaname) AS measuresphoenixschemaname, "+
			" coalesce(TENANT.measuresphoenixtablename, ORGANIZATION.measuresphoenixtablename) AS measuresphoenixtablename, "+
			" coalesce(TENANT.mediaphoenixschemaname, ORGANIZATION.mediaphoenixschemaname) AS mediaphoenixschemaname, "+
			" coalesce(TENANT.mediaphoenixtablename, ORGANIZATION.mediaphoenixtablename) AS mediaphoenixtablename, "+
			" coalesce(TENANT.mediasolrcollectionname, ORGANIZATION.mediasolrcollectionname) AS mediasolrcollectionname, "+
			" coalesce(TENANT.socialphoenixschemaname, ORGANIZATION.socialphoenixschemaname) AS socialphoenixschemaname, "+
			" coalesce(TENANT.socialphoenixtablename, ORGANIZATION.socialphoenixtablename) AS socialphoenixtablename, "+
			" coalesce(TENANT.socialsolrcollectionname, ORGANIZATION.socialsolrcollectionname) AS socialsolrcollectionname, "+	
			" coalesce(TENANT.dataphoenixtablename, ORGANIZATION.dataphoenixtablename) AS dataphoenixtablename, "+	
			" coalesce(TENANT.dataphoenixschemaname, ORGANIZATION.dataphoenixschemaname) AS dataphoenixschemaname "+	
			" FROM yucca_tenant TENANT  "+
			" LEFT JOIN " + R_TENANT_BUNDLES_TABLE + " TENANT_BUNDLES ON TENANT.id_tenant = TENANT_BUNDLES.id_tenant "+
			" LEFT JOIN " + BundlesMapper.BUNDLES_TABLE + " BUNDLES ON BUNDLES.id_bundles = TENANT_BUNDLES.id_bundles "+
			" LEFT JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " ORGANIZATION ON TENANT.id_organization = ORGANIZATION.id_organization "+
			" LEFT JOIN " + EcosystemMapper.ECOSYSTEM_TABLE + " ECOSYSTEM ON TENANT.id_ecosystem = ECOSYSTEM.id_ecosystem "+
			" LEFT JOIN " + TENANT_STATUS_TABLE + " TENANT_STATUS ON TENANT.id_tenant_status = TENANT_STATUS.id_tenant_status "+
			" LEFT JOIN " + TENANT_TYPE_TABLE + " TENANT_TYPE ON TENANT.id_tenant_type = TENANT_TYPE.id_tenant_type "+
			" LEFT JOIN " + SHARE_TYPE_TABLE + " SHARE_TYPE ON TENANT.id_share_type = SHARE_TYPE.id_share_type "+
			" LEFT JOIN " + UserMapper.R_TENANT_USERS_TABLE + " TENANT_USERS ON TENANT.id_tenant = TENANT_USERS.id_tenant "+
			" LEFT JOIN " + UserMapper.USER_TABLE + " USERS ON USERS.id_user = TENANT_USERS.id_user and USERS.username = tenantcode "+ 
			" where tenantcode=#{tenantcode} ";
	@Results({
        @Result(property = "idBundles",             column = "id_bundles"),
        @Result(property = "maxOdataResultperpage", column = "max_odata_resultperpage"),
        @Result(property = "idEcosystem",           column = "id_ecosystem"),
        @Result(property = "idOrganization",        column = "id_organization"),
        @Result(property = "idTenantStatus",        column = "id_tenant_status"),
        @Result(property = "idTenantType",          column = "id_tenant_type"),        
        @Result(property = "idShareType",           column = "id_share_type"),
        @Result(property = "idTenant",              column = "id_tenant")
      })	
	@Select(SELECT_DETTAGLIO_TENANT) 
	DettaglioTenantBackoffice selectDettaglioTenant(@Param("tenantcode") String tenantcode);
	
	/*************************************************************************
	 * 
	 * 					SELECT ALL TENANT
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ALL_TENANTS_JOIN = 	
	" SELECT TENANT.username, TENANT.creationdate, TENANT.expirationdate, " +
	
	" TENANT.datasolrcollectionname, TENANT.measuresolrcollectionname, TENANT.mediasolrcollectionname, TENANT.socialsolrcollectionname, " + 
	
	" BUNDLES.id_bundles, BUNDLES.maxdatasetnum, BUNDLES.maxstreamsnum, BUNDLES.hasstage, BUNDLES.max_odata_resultperpage, BUNDLES.zeppelin, BUNDLES.readytools, " +
	
	// Aggregazione dei tool in un unico array JSON accessibile tramite la variabile 'toolsstring'
	" (SELECT array_to_json(array_agg(row_to_json(tool))) " + 
	" FROM 	( SELECT TOOL.id_tool, TOOL.name, TOOL.toolversion, BUNDLES_TOOL.enabled " + 
	"		  FROM "+TOOL_TABLE+" TOOL, "+R_BUNDLES_TOOL+" BUNDLES_TOOL  " + 
	"		  WHERE TOOL.id_tool = BUNDLES_TOOL.id_tool " + 
	"		  AND BUNDLES_TOOL.id_bundles = BUNDLES.id_bundles " + 
	" 		) tool) toolsstring, " +
	
	" TENANT.id_ecosystem, ECOSYSTEM.ecosystemcode, ECOSYSTEM.description ecosystemdescription, " +
	
	" TENANT.id_organization, ORGANIZATION.organizationcode, ORGANIZATION.description as organizationdescription, " +
	
	" TENANT.id_tenant_status, TENANT_STATUS.tenantstatuscode, TENANT_STATUS.description as tenantstatusdescription, " +
	
	" TENANT.id_tenant_type, TENANT_TYPE.tenanttypecode, TENANT_TYPE.description tenanttypedescription, " +
	
	" TENANT.id_share_type, SHARE_TYPE.description as sharetypedescription, " + 
	
	" TENANT.id_tenant, TENANT.description, TENANT.name, tenantcode, usagedaysnumber, useremail, userfirstname, userlastname, usertypeauth " +
	
	" FROM " + TENANT_TABLE + " TENANT " + 
	
	" LEFT JOIN " + R_TENANT_BUNDLES_TABLE + " TENANT_BUNDLES ON TENANT.id_tenant = TENANT_BUNDLES.id_tenant " + 
	" LEFT JOIN " + BundlesMapper.BUNDLES_TABLE  + " BUNDLES ON BUNDLES.id_bundles = TENANT_BUNDLES.id_bundles " +
	" LEFT JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " ORGANIZATION ON TENANT.id_organization = ORGANIZATION.id_organization " +
	" LEFT JOIN " + EcosystemMapper.ECOSYSTEM_TABLE + " ECOSYSTEM ON TENANT.id_ecosystem = ECOSYSTEM.id_ecosystem " +
	" LEFT JOIN " + TENANT_STATUS_TABLE + " TENANT_STATUS ON TENANT.id_tenant_status = TENANT_STATUS.id_tenant_status " +
	" LEFT JOIN " + TENANT_TYPE_TABLE + " TENANT_TYPE ON TENANT.id_tenant_type = TENANT_TYPE.id_tenant_type " +
	" LEFT JOIN " + SHARE_TYPE_TABLE + " SHARE_TYPE ON TENANT.id_share_type = SHARE_TYPE.id_share_type " +
	" LEFT JOIN " + UserMapper.R_TENANT_USERS_TABLE + " TENANT_USERS ON TENANT.id_tenant = TENANT_USERS.id_tenant " + 
	" LEFT JOIN " + UserMapper.USER_TABLE + " USERS ON USERS.id_user = TENANT_USERS.id_user " + SELECT_TENANT_ORDER_BY;
	@Results({
		@Result(property = "idTool",              	column = "id_tool"),
        @Result(property = "idBundles",             column = "id_bundles"),
        @Result(property = "maxOdataResultperpage", column = "max_odata_resultperpage"),
        @Result(property = "idEcosystem",           column = "id_ecosystem"),
        @Result(property = "idOrganization",        column = "id_organization"),
        @Result(property = "idTenantStatus",        column = "id_tenant_status"),
        @Result(property = "idTenantType",          column = "id_tenant_type"),        
        @Result(property = "idShareType",           column = "id_share_type"),
        @Result(property = "idTenant",              column = "id_tenant")
      })	
	@Select({"<script>",SELECT_ALL_TENANTS_JOIN,"</script>"}) 
	List<TenantManagement> selectAllTenant(@Param("sortList") List<String> sortList);	
	/*************************************************************************
	 * 
	 * 					SELECT TENANT BY TENANT CODE
	 * 
	 * ***********************************************************************/
	public static final String SELECT_TENANT_BY_TENANT_CODE =
	SELECT_TENANT_COLUMNS + " FROM " + TENANT_TABLE + " TENANT " + 
	" WHERE  tenantcode = #{tenantCode}";
	@Results({
        @Result(property = "idTenant",       column = "id_tenant"),
        @Result(property = "idEcosystem",    column = "id_ecosystem"),
        @Result(property = "idOrganization", column = "id_organization"),
        @Result(property = "idTenantType",   column = "id_tenant_type"),
        @Result(property = "idTenantStatus", column = "id_tenant_status"),
        @Result(property = "idShareType",    column = "id_share_type")
      })	
	@Select(SELECT_TENANT_BY_TENANT_CODE) 
	Tenant selectTenantByTenantCode(@Param("tenantCode") String tenantCode);
	
	
	
	/*************************************************************************
	 * 
	 * 					SELECT TENANT BY ID TENANT AND ORG COODE
	 * 
	 * ***********************************************************************/
	public static final String SELECT_TENANT_ID_AND_ORG_CODE =
	SELECT_TENANT_COLUMNS + " FROM " + TENANT_TABLE + " TENANT, " + OrganizationMapper.ORGANIZATION_TABLE + " ORG  "
			+ "WHERE id_tenant = #{idTenant} AND ORG.organizationcode = #{organizationCode} AND ORG.id_organization = TENANT.id_organization";
	@Results({
        @Result(property = "idTenant",       column = "id_tenant"),
        @Result(property = "idEcosystem",    column = "id_ecosystem"),
        @Result(property = "idOrganization", column = "id_organization"),
        @Result(property = "idTenantType",   column = "id_tenant_type"),
        @Result(property = "idTenantStatus", column = "id_tenant_status"),
        @Result(property = "idShareType",    column = "id_share_type")
      })	
	@Select(SELECT_TENANT_ID_AND_ORG_CODE) 
	Tenant selectTenantByIdAndOrgCodeCode(@Param("idTenant") Integer idTenant, 
			@Param("organizationCode") String organizationCode);
	

	/*************************************************************************
	 * 
	 * 					SELECT TENANT BY CODE TENANT AND ORG COODE
	 * 
	 * ***********************************************************************/
	public static final String SELECT_TENANT_CODE_AND_ORG_CODE =
	SELECT_TENANT_COLUMNS + " FROM " + TENANT_TABLE + " TENANT, " + OrganizationMapper.ORGANIZATION_TABLE + " ORG  "
			+ "WHERE tenantcode = #{tenantCode} AND ORG.organizationcode = #{organizationCode} AND ORG.id_organization = TENANT.id_organization";
	@Results({
        @Result(property = "idTenant",       column = "id_tenant"),
        @Result(property = "idEcosystem",    column = "id_ecosystem"),
        @Result(property = "idOrganization", column = "id_organization"),
        @Result(property = "idTenantType",   column = "id_tenant_type"),
        @Result(property = "idTenantStatus", column = "id_tenant_status"),
        @Result(property = "idShareType",    column = "id_share_type")
      })	
	@Select(SELECT_TENANT_CODE_AND_ORG_CODE) 
	Tenant selectTenantByCodeAndOrgCode(@Param("tenantCode") String tenantCode, 
			@Param("organizationCode") String organizationCode);
	
	
	/*************************************************************************
	 * 
	 * 					SELECT TENANT BY ID TENANT
	 * 
	 * ***********************************************************************/
	public static final String SELECT_TENANT_BY_ID_TENANT =
	SELECT_TENANT_COLUMNS + " FROM " + TENANT_TABLE + " TENANT " + 
	" WHERE  id_tenant = #{idTenant}";
	@Results({
        @Result(property = "idTenant",       column = "id_tenant"),
        @Result(property = "idEcosystem",    column = "id_ecosystem"),
        @Result(property = "idOrganization", column = "id_organization"),
        @Result(property = "idTenantType",   column = "id_tenant_type"),
        @Result(property = "idTenantStatus", column = "id_tenant_status"),
        @Result(property = "idShareType",    column = "id_share_type")
      })	
	@Select(SELECT_TENANT_BY_ID_TENANT) 
	Tenant selectTenantByidTenant(@Param("idTenant") Integer idTenant);
	
	/*************************************************************************
	 * 
	 * 					SELECT TENANT BY USERNAME
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ACTIVE_TENANT_BY_USERNAME_AND_ID_TENANT_TYPE =
	SELECT_TENANT_COLUMNS +
	" FROM " + TENANT_TABLE + " TENANT " + 
	" WHERE TENANT.id_tenant_type = #{idTenantType} AND " +
	" ( (TENANT.activationdate is null)  OR " +
	" (TENANT.deactivationdate > current_timestamp or TENANT.deactivationdate is null) ) AND"+
	" TENANT.username = #{username}";
	@Results({
        @Result(property = "idTenant",       column = "id_tenant"),
        @Result(property = "idEcosystem",    column = "id_ecosystem"),
        @Result(property = "idOrganization", column = "id_organization"),
        @Result(property = "idTenantType",   column = "id_tenant_type"),
        @Result(property = "idTenantStatus", column = "id_tenant_status"),
        @Result(property = "idShareType",    column = "id_share_type")
      })	
	@Select(SELECT_ACTIVE_TENANT_BY_USERNAME_AND_ID_TENANT_TYPE) 
	List<Tenant> selectActiveTenantByUserNameAndIdTenantType(@Param("username") String username, @Param("idTenantType") Integer idTenantType);

	
	/*************************************************************************
	 * 
	 * 					SELECT ORGANIZATION BY ID TENANT
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ORGANIZATION_BY_ID_TENANT = 
			"SELECT id_organization FROM " + TENANT_TABLE + " where id_tenant = #{idTenant}";
	@Select(SELECT_ORGANIZATION_BY_ID_TENANT) 
	Integer selectIdOrganizationByIdTenant(@Param("idTenant") int idTenant);

	
	/*************************************************************************
	 * 
	 * 					SELECT ID TENANT BY ID ORGANIZATION
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ID_TENANT_BY_ID_ORGANIZATION = "SELECT id_tenant FROM " + TENANT_TABLE
	+ " where id_organization = #{idOrganization}";
	@Select(SELECT_ID_TENANT_BY_ID_ORGANIZATION) 
	List<Integer> selectIdTenantByIdOrganization(@Param("idOrganization") int idOrganization);
	
	
	/*************************************************************************
	 * 
	 * 					UPDATE TENANT STATUS
	 * 
	 * ***********************************************************************/	
	public static final String UPDATE_TENANT_STATUS = 
			"UPDATE " + TENANT_TABLE + " set id_tenant_status = #{idTenantStatus} where tenantcode = #{tenantCode}";
		@Update(UPDATE_TENANT_STATUS)
		int updateTenantStatus(@Param("idTenantStatus") Integer idTenantStatus, @Param("tenantCode") String tenantCode);
	
		/*************************************************************************
		 * 
		 * 					UPDATE TENANT ACTIVATIONDATE
		 * 
		 * ***********************************************************************/	
		 public static final String UPDATE_TENANT_ACTIVATIONDATE = 
				"UPDATE " + TENANT_TABLE + " set activationdate = now() where tenantcode = #{tenantCode}";
		 @Update(UPDATE_TENANT_ACTIVATIONDATE)
		int updateTenantactivationdate( @Param("tenantCode") String tenantCode);
			
			
		/*************************************************************************
		 * 
		 * 					UPDATE TENANT ACTIVATIONDATE
		 * 
		 * ***********************************************************************/	
		 public static final String UPDATE_TENANT_DEACTIVATIONDATE = 
				"UPDATE " + TENANT_TABLE + " set deactivationdate = now() where tenantcode = #{tenantCode}";
		 @Update(UPDATE_TENANT_DEACTIVATIONDATE)
		int updateTenantdeactivationdate( @Param("tenantCode") String tenantCode);

			 /*************************************************************************
		 * 
		 * 					UPDATE TENANT DEACTIVATIONDATE
		 * 
		 * ***********************************************************************/	
		public static final String UPDATE_TENANT_TRIAL_DEACTIVATIONDATE = 
				"UPDATE " + TENANT_TABLE + " set deactivationdate = now()+interval '30d' where tenantcode = #{tenantCode}";
		@Update(UPDATE_TENANT_TRIAL_DEACTIVATIONDATE)
		int updateTenantTrialDeactivationdate( @Param("tenantCode") String tenantCode);
		
	
		/*************************************************************************
		 * 
		 * 					INSERT TENANT-DATASOURCE
		 * 
		 * ***********************************************************************/
		public static final String INSERT_TENANT_DATA_SOURCE = 
				" INSERT INTO yucca_r_tenant_data_source( id_data_source, datasourceversion, id_tenant, "
				+ "isactive, ismanager, dataoptions, manageoptions, activationdate, deactivationdate, managerfrom, manageruntil) "
				+ "VALUES (#{idDataSource}, #{datasourceversion}, #{idTenant},#{isactive}, "
				+ "#{ismanager}, #{dataoptions}, #{manageoptions}, #{activationdate}, #{deactivationdate}, "
				+ "#{managerfrom}, #{manageruntil})";
		@Insert(INSERT_TENANT_DATA_SOURCE)
		int insertTenantDataSource(TenantDataSource tenantDataSource);
		
		
	/*************************************************************************
	 * 
	 * 					INSERT TENANT
	 * 
	 * ***********************************************************************/
	public static final String INSERT_TENANT = 
		"INSERT INTO " + TENANT_TABLE +
			" ( <if test=\"idTenant != null\">id_tenant,</if> creationdate, expirationdate, activationdate, deactivationdate, id_share_type, " +
			" tenantcode, name, description, clientkey, clientsecret, username, " +
			" userfirstname, userlastname, useremail, usertypeauth, id_ecosystem, " +
			" id_organization, id_tenant_type, id_tenant_status, datasolrcollectionname, " +
			" measuresolrcollectionname, mediasolrcollectionname, socialsolrcollectionname, " +
			" dataphoenixtablename, dataphoenixschemaname, measuresphoenixtablename, " +
			" measuresphoenixschemaname, mediaphoenixtablename, mediaphoenixschemaname, " +
			" socialphoenixtablename, socialphoenixschemaname ) " +
			" VALUES (<if test=\"idTenant != null\">#{idTenant},</if>#{creationdate}, #{expirationdate}, #{activationdate}, #{deactivationdate}, #{idShareType}, " +
			" #{tenantcode}, #{name}, #{description}, #{clientkey}, #{clientsecret}, " +
			" #{username}, #{userfirstname}, #{userlastname}, #{useremail}, #{usertypeauth}, #{idEcosystem}, " +
			" #{idOrganization}, #{idTenantType}, #{idTenantStatus}, #{datasolrcollectionname}, " +
			" #{measuresolrcollectionname}, #{mediasolrcollectionname}, #{socialsolrcollectionname}, " +
			" #{dataphoenixtablename}, #{dataphoenixschemaname}, #{measuresphoenixtablename}, " +
			" #{measuresphoenixschemaname}, #{mediaphoenixtablename}, #{mediaphoenixschemaname}, " +
			" #{socialphoenixtablename}, #{socialphoenixschemaname})";
	@Insert({"<script>",INSERT_TENANT,"</script>"})
	@Options(useGeneratedKeys=true, keyProperty="idTenant")
	int insertTenant(Tenant tenant);
	
	
	/*************************************************************************
	 * 
	 * 					INSERT TENANT BUNDLES
	 * 
	 * ***********************************************************************/	
	public static final String INSERT_TENANT_BUNDLES = 
			"INSERT INTO " + R_TENANT_BUNDLES_TABLE + "(id_tenant, id_bundles)VALUES (#{idTenant}, #{idBundles})";
	@Insert(INSERT_TENANT_BUNDLES)
	int insertTenantBundles(@Param("idTenant") int idTenant, @Param("idBundles") int idBundles);
	
	/*************************************************************************
	 * 
	 * 					DELETE TENANT
	 * 
	 * ***********************************************************************/
	public static final String DELETE_TENANT = "DELETE FROM " + TENANT_TABLE + " WHERE tenantcode=#{tenantcode}";
	@Delete(DELETE_TENANT)
	int deleteTenant(String tenantcode);	

	
	/*************************************************************************
	 * 
	 * 					DELETE TENANT
	 * 
	 * ***********************************************************************/
	public static final String DELETE_TENANT_BUNDLES_BY_BUNDLES = 
			"DELETE FROM " + R_TENANT_BUNDLES_TABLE + " WHERE id_bundles=#{idBundles}";
	@Delete(DELETE_TENANT_BUNDLES_BY_BUNDLES)
	int deleteTenantBundlesByBundles(Integer idBundles);	
	
	
	/*************************************************************************
	 * 
	 * 					DELETE TENANT BY idOrganaizzation
	 * 
	 * ***********************************************************************/
	public static final String DELETE_TENANT_BY_ID_ORGANIZATION = 
			"DELETE FROM " + TENANT_TABLE + " WHERE id_organization=#{idOrganization}";
	@Delete(DELETE_TENANT_BY_ID_ORGANIZATION)
	int deleteTenantByIdOrganization(Integer idOrganization);
	
	/*************************************************************************
	 * 
	 * 					UPDATE TENANT CLIENT CREDENTIAL
	 * 
	 * ***********************************************************************/	
	public static final String UPDATE_TENANT_CREDENTIAL_ = 
			"UPDATE " + TENANT_TABLE + " set clientkey = #{clientkey},clientsecret=#{clientsecret}  where tenantcode = #{tenantCode}";
		@Update(UPDATE_TENANT_CREDENTIAL_)
		int updateTenantClientCredential(@Param("clientkey") String clientkey, @Param("clientsecret") String clientsecret, @Param("tenantCode") String tenantCode);

	
	
}