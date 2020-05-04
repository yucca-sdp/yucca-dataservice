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
import org.csi.yucca.adminapi.model.DataSource;
import org.csi.yucca.adminapi.model.DataSourceGroup;
import org.csi.yucca.adminapi.model.DataSourceGroupType;
import org.csi.yucca.adminapi.util.Constants;
import org.csi.yucca.adminapi.util.DatasourceGroupStatus;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface DataSourceGroupMapper {
	
	public static final String DATA_SOURCE_GROUP_TABLE = Constants.SCHEMA_DB + "yucca_datasourcegroup";
	public static final String DATA_SOURCE_GROUP_TYPE_TABLE = Constants.SCHEMA_DB + "yucca_d_datasourcegroup_type";
	public static final String R_DATA_SOURCE_DATASOURCEGROUP_TABLE = Constants.SCHEMA_DB + "yucca_r_datasource_datasourcegroup";
	
	/*************************************************************************
	 * 	     select all DATA_SOURCE_GROUP_TYPE con gestione order by
	 * ***********************************************************************/
	public static final String SELECT_DATA_SOURCE_GROUP_TYPE = 
			
			" SELECT id_datasourcegroup_type, name, description FROM " + DATA_SOURCE_GROUP_TYPE_TABLE + " " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +

				/* id_datasourcegroup_type */
				"<if test=\"propName == 'idDatasourcegroupType-'\">" +
			        " id_datasourcegroup_type desc" +
		        "</if>" +
		        "<if test=\"propName == 'idDatasourcegroupType'\">" +
		            " id_datasourcegroup_type" +
	            "</if>" +			
				
	            /* name */
				"<if test=\"propName == 'name-'\">" +
		           " name desc" +
	            "</if>" +
	            "<if test=\"propName == 'name'\">" +
	               " name" +
	            "</if>" +			

	            /* description */
				"<if test=\"propName == 'description-'\">" +
		           " description desc" +
	            "</if>" +
	            "<if test=\"propName == 'description'\">" +
	               " description" +
	            "</if>" +			
	            "</foreach>" +
            "</if>";
	@Results({
        @Result(property = "idDatasourcegroupType", column = "id_datasourcegroup_type")
      })
	@Select({"<script>",
				SELECT_DATA_SOURCE_GROUP_TYPE,
             "</script>"}) 
	List<DataSourceGroupType> selectDatasourcegroupType(@Param("sortList") List<String> sortList);	
	
	
	/*************************************************************************
	 * 			INSERT R_DATASOURCE_DATASOURCEGROUP_BY_ID_STREAM
	 * ***********************************************************************/
	public static final String INSERT_R_DATASOURCE_DATASOURCEGROUP_BY_ID_STREAM =
			" INSERT INTO " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE +
			"  ( id_datasourcegroup, datasourcegroupversion, datasourceversion, id_data_source) " + 
			"  (SELECT #{idDatasourcegroup}, #{datasourcegroupversion}, #{datasourceversion},   " +
			"  id_data_source from " + StreamMapper.STREAM_TABLE + " where idstream=#{idStream} and " +
			"  datasourceversion=#{datasourceversion})  ";
	@Insert(INSERT_R_DATASOURCE_DATASOURCEGROUP_BY_ID_STREAM)
	int insertDatasourceDatasourcegroupByIdStream(
			@Param("idDatasourcegroup") Long idDatasourcegroup ,
			@Param("datasourcegroupversion") Integer datasourcegroupversion ,
			@Param("datasourceversion") Integer datasourceversion ,
			@Param("idStream") Integer idStream );	
	
	/*************************************************************************
	 * 					SELECT DATASOURCES BY DATASOURCEGROUP
	 * ***********************************************************************/
	public static final String SELECT_DATASOURCES_BY_DATASOURCEGROUP =
	" SELECT datasourceversion, id_data_source " +
	" FROM " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE +
	" WHERE id_datasourcegroup = #{idDatasourcegroup} AND " +
	" datasourcegroupversion = #{datasourcegroupversion} ";
	@Results({
        @Result(property = "idDataSource", column = "id_data_source")
      })
	@Select(SELECT_DATASOURCES_BY_DATASOURCEGROUP)
	List<DataSource> seletcDatasourcesByDatasourcegroup(
			@Param("idDatasourcegroup") Long idDatasourcegroup ,
			@Param("datasourcegroupversion") Integer datasourcegroupversion );	
	
	/*************************************************************************
	 * 					DELETE DATASOURCEGROUP
	 * ***********************************************************************/
	public static final String DELETE_DATASOURCEGROUP =
		" DELETE " + 
		"	  FROM " + DATA_SOURCE_GROUP_TABLE +
		"	 WHERE id_datasourcegroup     = #{idDatasourcegroup} AND " +
		"	       datasourcegroupversion = #{datasourcegroupversion} ";
	@Delete(DELETE_DATASOURCEGROUP)
	void deleteDatasourceGroup(
			@Param("idDatasourcegroup") Long idDatasourcegroup ,
			@Param("datasourcegroupversion") Integer datasourcegroupversion );	
	
	/*************************************************************************
	 * 					DELETE R_DATASOURCE_DATASOURCEGROUP_BY_ID_GROUP_AND_VERSION
	 * ***********************************************************************/
	public static final String DELETE_R_DATASOURCE_DATASOURCEGROUP_BY_ID_GROUP_AND_VERSION =
		" DELETE " + 
		"	  FROM " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE +
		"	 WHERE id_datasourcegroup     = #{idDatasourcegroup} AND " +
		"	       datasourcegroupversion = #{datasourcegroupversion} ";
	@Delete(DELETE_R_DATASOURCE_DATASOURCEGROUP_BY_ID_GROUP_AND_VERSION)
	void deleteDatasourceDatasourcegroupByIdGroupAndVersion(
			@Param("idDatasourcegroup") Long idDatasourcegroup ,
			@Param("datasourcegroupversion") Integer datasourcegroupversion );	
	
	/*************************************************************************
	 * 					UPDATE DATA SOURCE GROUP
	 * ***********************************************************************/
	public static final String UPDATE_DATA_SOURCE_GROUP =
	" UPDATE " + DATA_SOURCE_GROUP_TABLE + " datasourcegroup" +
	"   SET id_tenant = #{idTenant}, " + 
	"       datasourcegroupversion = #{datasourcegroupversion}, " + 
	"       name = #{name}, " + 
	"       id_datasourcegroup_type = #{idDatasourcegroupType}, " + 
	"       color = #{color}, " + 
	"       status = #{status} " +
	" WHERE datasourcegroup.id_datasourcegroup = #{idDatasourcegroup} and " + 
    " (datasourcegroup.id_datasourcegroup, datasourcegroup.datasourcegroupversion) in " + 
	" (select id_datasourcegroup , max(datasourcegroupversion) " + 
	"   from " + DATA_SOURCE_GROUP_TABLE + 
	"  where id_datasourcegroup = datasourcegroup.id_datasourcegroup " +
    " group by id_datasourcegroup) ";
	@Update(UPDATE_DATA_SOURCE_GROUP)
	int updateDataSourceGroup(DataSourceGroup dataSourceGroup);
	
	
	/*************************************************************************
	 * 					CONSOLIDATE DATA SOURCE GROUP
	 * ***********************************************************************/
	public static final String DISMISS_OLD_DATA_SOURCE_GROUP =
	" UPDATE " + DATA_SOURCE_GROUP_TABLE + " ds" +
	"   SET status = 'DISMISSED'" + 
	" WHERE id_datasourcegroup = #{idDatasourcegroup} and " + 
    " datasourcegroupversion < " + 
	" (select max(datasourcegroupversion) " + 
	"   from " + DATA_SOURCE_GROUP_TABLE + 
	"  where id_datasourcegroup = ds.id_datasourcegroup " +
    " group by id_datasourcegroup) ";
	@Update(DISMISS_OLD_DATA_SOURCE_GROUP)
	int dismissOldVersionOfDataSourceGroup(DataSourceGroup dataSourceGroup);
	
	/*************************************************************************
	 * 					DELETE R_DATASOURCE_DATASOURCEGROUP
	 * ***********************************************************************/
	public static final String DELETE_R_DATASOURCE_DATASOURCEGROUP =
		" DELETE " + 
		"	  FROM " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE +
		"	 WHERE id_datasourcegroup     = #{idDatasourcegroup} AND " +
		"	       datasourcegroupversion = #{datasourcegroupversion} AND " +
		"	       id_data_source         = #{idDataSource} AND " +
		"	       datasourceversion      = #{datasourceversion} ";
	@Delete(DELETE_R_DATASOURCE_DATASOURCEGROUP)
	void deleteDatasourceDatasourcegroup(
			@Param("idDatasourcegroup") Long idDatasourcegroup ,
			@Param("datasourcegroupversion") Integer datasourcegroupversion ,
			@Param("datasourceversion") Integer datasourceversion ,
			@Param("idDataSource") Integer idDataSource );	

	/*************************************************************************
	 * 					DELETE R_DATASOURCE_DATASOURCEGROUP BY ID DATASET
	 * ***********************************************************************/	
	public static final String DELETE_R_DATASOURCE_DATASOURCEGROUP_BY_ID_DATASET =
	" DELETE FROM " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE +
	" WHERE id_datasourcegroup     = #{idDatasourcegroup} AND " + 
	"       datasourcegroupversion = #{datasourcegroupversion} AND " + 
	"       datasourceversion = #{datasourceversion} AND " +
	" id_data_source in ( " +
	" select id_data_source from " + DatasetMapper.DATASET_TABLE  + 
	" where iddataset = #{idDataset} AND " +
	" datasourceversion = #{datasourceversion} " +
	" ) ";
	@Delete(DELETE_R_DATASOURCE_DATASOURCEGROUP_BY_ID_DATASET)
	int deleteDatasourceDatasourcegroupByIdDataset(
			@Param("idDatasourcegroup") Long idDatasourcegroup ,
			@Param("datasourcegroupversion") Integer datasourcegroupversion ,
			@Param("datasourceversion") Integer datasourceversion ,
			@Param("idDataset") Integer idDataset );	

	/*************************************************************************
	 * 					DELETE R_DATASOURCE_DATASOURCEGROUP BY ID STREAM
	 * ***********************************************************************/	
	public static final String DELETE_R_DATASOURCE_DATASOURCEGROUP_BY_ID_STREAM =
	 " DELETE FROM " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE + 
	 " WHERE id_datasourcegroup = #{idDatasourcegroup} AND " + 
	 "       datasourcegroupversion = #{datasourcegroupversion} AND " + 
     "       datasourceversion = #{datasourceversion} AND " +
     "   id_data_source in ( " +
	 "  select id_data_source from " + StreamMapper.STREAM_TABLE  + 
	 "  where idstream = #{idStream} AND " +
	 "        datasourceversion = #{datasourceversion} ) "; 
	@Delete(DELETE_R_DATASOURCE_DATASOURCEGROUP_BY_ID_STREAM)
	int deleteDatasourceDatasourcegroupByIdStream(
			@Param("idDatasourcegroup") Long idDatasourcegroup ,
			@Param("datasourcegroupversion") Integer datasourcegroupversion ,
			@Param("datasourceversion") Integer datasourceversion ,
			@Param("idStream") Integer idStream );	
	
	/*************************************************************************
	 * 					DELETE R_DATASOURCE_DATASOURCEGROUP BY ID STREAM
	 * ***********************************************************************/	
	public static final String DELETE_ALL_R_DATASOURCE_DATASOURCEGROUP_BY_ID_STREAM =
	 " DELETE FROM " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE + 
	 " WHERE datasourceversion = #{datasourceversion} AND " +
     "   id_data_source in ( " +
	 "  select id_data_source from " + StreamMapper.STREAM_TABLE  + 
	 "  where idstream = #{idStream} AND " +
	 "        datasourceversion = #{datasourceversion} ) "; 
	@Delete(DELETE_ALL_R_DATASOURCE_DATASOURCEGROUP_BY_ID_STREAM)
	int deleteAllDatasourceDatasourcegroupByIdStream(
			@Param("datasourceversion") Integer datasourceversion ,
			@Param("idStream") Integer idStream );		
	
	/*************************************************************************
	 * 			INSERT R_DATASOURCE_DATASOURCEGROUP_BY_ID_DATASET
	 * ***********************************************************************/
	public static final String INSERT_R_DATASOURCE_DATASOURCEGROUP_BY_ID_DATASET =
	" INSERT INTO " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE +
	" ( id_datasourcegroup, datasourcegroupversion, datasourceversion, id_data_source) " + 
	" (SELECT #{idDatasourcegroup}, #{datasourcegroupversion}, #{datasourceversion}, " + 
	        " id_data_source from " + DatasetMapper.DATASET_TABLE + " where iddataset=#{idDataset} and" + 
	        " datasourceversion=#{datasourceversion}) ";
	@Insert(INSERT_R_DATASOURCE_DATASOURCEGROUP_BY_ID_DATASET)
	int insertDatasourceDatasourcegroupByIdDataSet(
			@Param("idDatasourcegroup") Long idDatasourcegroup ,
			@Param("datasourcegroupversion") Integer datasourcegroupversion ,
			@Param("datasourceversion") Integer datasourceversion ,
			@Param("idDataset") Integer idDataset );	
	
	/*************************************************************************
	 * 			INSERT R_DATASOURCE_DATASOURCEGROUP
	 * ***********************************************************************/
	public static final String INSERT_R_DATASOURCE_DATASOURCEGROUP = 
	" INSERT INTO " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE
	+ "( id_datasourcegroup, datasourcegroupversion, datasourceversion, id_data_source) " +
    "  VALUES (#{idDatasourcegroup}, #{datasourcegroupversion}, #{datasourceversion}, #{idDataSource}) ";
	@Insert(INSERT_R_DATASOURCE_DATASOURCEGROUP)
	int insertDatasourceDatasourcegroup(
			@Param("idDatasourcegroup") Long idDatasourcegroup ,
			@Param("datasourcegroupversion") Integer datasourcegroupversion ,
			@Param("datasourceversion") Integer datasourceversion ,
			@Param("idDataSource") Integer idDataSource );	
	
	
	
	
	
	
	
	
	/*************************************************************************
	 * 					INSERT DATA SOURCE GROUP
	 * ***********************************************************************/
	public static final String INSERT_DATA_SOURCE_GROUP = 
	" INSERT INTO " + DATA_SOURCE_GROUP_TABLE +
			" ( " + 
			"  <if test=\"idDatasourcegroup != null\">id_datasourcegroup,</if>" +
			" id_tenant, datasourcegroupversion, name, id_datasourcegroup_type, color, status ) " +
			" VALUES " + 
			" ( <if test=\"idDatasourcegroup != null\">#{idDatasourcegroup},</if>" + 
			"  #{idTenant}, #{datasourcegroupversion}, #{name}, #{idDatasourcegroupType}, #{color} , #{status} ) ";
	@Insert({"<script>",INSERT_DATA_SOURCE_GROUP,"</script>"})
	@Options(useGeneratedKeys=true, keyProperty="idDatasourcegroup", keyColumn="id_datasourcegroup")
	int insertDataSourceGroup(DataSourceGroup dataSourceGroup);

	/*************************************************************************
	 * 					SELECT LAST VERSION DATA SOURCE GROUP
	 * ***********************************************************************/
	public static final String SELECT_LAST_VERSION_DATA_SOURCE_GROUP_BY_ID =
		"	SELECT status, id_tenant, datasourcegroupversion, datasourcegroup.name, datasourcegroup.id_datasourcegroup_type, " + 
		"          id_datasourcegroup, color , group_type.name name_datasourcegroup_type, group_type.description description_datasourcegroup_type " + 
		"     FROM " + DATA_SOURCE_GROUP_TABLE + " datasourcegroup, " + DATA_SOURCE_GROUP_TYPE_TABLE + " group_type " + 
		"    WHERE group_type.id_datasourcegroup_type = datasourcegroup.id_datasourcegroup_type and " + 
		"          id_datasourcegroup = #{idDatasourcegroup} order by datasourcegroupversion DESC limit 1 ";
	@Results({
        @Result(property = "idTenant",  column = "id_tenant"),
        @Result(property = "idDatasourcegroupType",  column = "id_datasourcegroup_type"),
        @Result(property = "idDatasourcegroup",  column = "id_datasourcegroup"),
        @Result(property = "nameDatasourcegroupType",  column = "name_datasourcegroup_type"),
        @Result(property = "descriptionDatasourcegroupType",  column = "description_datasourcegroup_type")
      })
	@Select({"<script>",SELECT_LAST_VERSION_DATA_SOURCE_GROUP_BY_ID,"</script>"})
	DataSourceGroup selectLastVersionDataSourceGroupById(@Param("idDatasourcegroup") Long idDatasourcegroup );	
	
	/*************************************************************************
	 * 					SELECT DATA SOURCE GROUP
	 * ***********************************************************************/
	public static final String SELECT_DATA_SOURCE_GROUP_BY_ID = 
		"	 SELECT status, id_tenant, datasourcegroupversion, datasourcegroup.name, datasourcegroup.id_datasourcegroup_type, " + 
		"        id_datasourcegroup, color , group_type.name name_datasourcegroup_type, group_type.description description_datasourcegroup_type " +
		"   FROM " + DATA_SOURCE_GROUP_TABLE + " datasourcegroup, " + DATA_SOURCE_GROUP_TYPE_TABLE + " group_type " +
		"  WHERE id_tenant = #{idTenant} and   " +

	    "         <if test=\"version != null\">" +
		"          datasourcegroupversion = #{version} and " +
		"         </if>" +
		
        "         <if test=\"version == null\">" +
		"	       (datasourcegroup.id_datasourcegroup, datasourcegroup.datasourcegroupversion) in " +
		"	         (select id_datasourcegroup , max(datasourcegroupversion) " +
		"	            from yucca_datasourcegroup " +
		"	           where id_datasourcegroup = datasourcegroup.id_datasourcegroup" + 
		"	        group by id_datasourcegroup) and"		+
		"         </if>" +
		
		"        group_type.id_datasourcegroup_type = datasourcegroup.id_datasourcegroup_type and " +
		"        id_datasourcegroup = #{idDatasourcegroup} ";
	@Results({
        @Result(property = "idTenant",  column = "id_tenant"),
        @Result(property = "idDatasourcegroupType",  column = "id_datasourcegroup_type"),
        @Result(property = "idDatasourcegroup",  column = "id_datasourcegroup"),
        @Result(property = "nameDatasourcegroupType",  column = "name_datasourcegroup_type"),
        @Result(property = "descriptionDatasourcegroupType",  column = "description_datasourcegroup_type")
      })
	@Select({"<script>",SELECT_DATA_SOURCE_GROUP_BY_ID,"</script>"})
	DataSourceGroup selectDataSourceGroupById(
			@Param("idTenant") Integer idTenant,
			@Param("version") Integer version,
			@Param("idDatasourcegroup") Long idDatasourcegroup );	

	/*************************************************************************
	 * 					SELECT DATA SOURCE GROUP by tenant
	 * ***********************************************************************/
	public static final String SELECT_DATA_SOURCE_GROUP_BY_TENANT =
		"SELECT status, datasourcegroup.id_tenant, datasourcegroupversion, datasourcegroup.name, datasourcegroup.id_datasourcegroup_type, id_datasourcegroup, color ," +
		"	group_type.name name_datasourcegroup_type, group_type.description description_datasourcegroup_type" +
		"	  FROM " + DATA_SOURCE_GROUP_TABLE + " datasourcegroup, " + TenantMapper.TENANT_TABLE + " tenant, " + DATA_SOURCE_GROUP_TYPE_TABLE + " group_type" +
		"	 WHERE datasourcegroup.id_tenant = tenant.id_tenant and " +
		"	       tenant.tenantcode = #{tenantCodeManager} and " +
		"	       group_type.id_datasourcegroup_type = datasourcegroup.id_datasourcegroup_type and" +
		
		"	       (datasourcegroup.id_datasourcegroup, datasourcegroup.datasourcegroupversion) in " +
		"	         (select id_datasourcegroup , max(datasourcegroupversion) " +
		"	            from "+ DATA_SOURCE_GROUP_TABLE +
		"	           where id_datasourcegroup = datasourcegroup.id_datasourcegroup" + 
		"	        group by id_datasourcegroup) ";
	@Results({
        @Result(property = "idTenant",  column = "id_tenant"),
        @Result(property = "idDatasourcegroupType",  column = "id_datasourcegroup_type"),
        @Result(property = "idDatasourcegroup",  column = "id_datasourcegroup"),
        @Result(property = "nameDatasourcegroupType",  column = "name_datasourcegroup_type"),
        @Result(property = "descriptionDatasourcegroupType",  column = "description_datasourcegroup_type")
      })
	@Select({"<script>",SELECT_DATA_SOURCE_GROUP_BY_TENANT,"</script>"})
	List<DataSourceGroup> selectDataSourceGroupByTenant( @Param("tenantCodeManager") String tenantCodeManager );	

	
	/*************************************************************************
	 * 		SELECT DATA SOURCE GROUPS BY ID_DATASET AND DATASETVERSION
	 * ***********************************************************************/
	public static final String SELECT_DATA_SOURCE_GROUPS_BY_ID_DATASET_AND_DATASETVERSION =
	" SELECT GRP.id_tenant, GRP.datasourcegroupversion, GRP.name, GRP.id_datasourcegroup_type, " + 
	" GRP.id_datasourcegroup, GRP.color, GRP.status " +
	" FROM " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE + " DS_GROUP , " + DatasetMapper.DATASET_TABLE + " DS, " + DATA_SOURCE_GROUP_TABLE + " GRP " +
	" where  " +
	" DS.iddataset = #{idDataset} AND " +
	" DS.datasourceversion = #{datasourceversion} AND " +
	 
	" DS_GROUP.id_data_source = DS.id_data_source AND " + 
	" DS_GROUP.datasourceversion = DS.datasourceversion and " +
	" (DS_GROUP.id_datasourcegroup , DS_GROUP.datasourcegroupversion) in " +
	 
	" ( select id_datasourcegroup, " + 
	" max(datasourcegroupversion) " +
	" from " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE +  
	" where id_datasourcegroup = DS_GROUP.id_datasourcegroup " +
	"and id_data_source=DS.id_data_source " +
	"and datasourceversion=DS.datasourceversion " +	
	" group by id_datasourcegroup) AND " +
	
	" GRP.id_datasourcegroup = DS_GROUP.id_datasourcegroup AND " +
	" GRP.datasourcegroupversion = DS_GROUP.datasourcegroupversion ";
	@Results({
        @Result(property = "idTenant",  column = "id_tenant"),
        @Result(property = "idDatasourcegroupType",  column = "id_datasourcegroup_type"),
        @Result(property = "idDatasourcegroup",  column = "id_datasourcegroup"),
        @Result(property = "nameDatasourcegroupType",  column = "name_datasourcegroup_type"),
        @Result(property = "descriptionDatasourcegroupType",  column = "description_datasourcegroup_type")
      })
	@Select(SELECT_DATA_SOURCE_GROUPS_BY_ID_DATASET_AND_DATASETVERSION)
	List<DataSourceGroup> selectDataSourceGroupByIdDatasetAndDatasetVersion( 
			@Param("idDataset") Integer idDataset, 
			@Param("datasourceversion") Integer datasourceversion );	
	
	/*************************************************************************
	 * 	 SELECT DATA SOURCE GROUPS BY ID_DATASOURCE AND DATASOURCEVERSION
	 * ***********************************************************************/
	public static final String SELECT_DATA_SOURCE_GROUPS_BY_ID_DATASOURCE_AND_DATASOURCEVERSION =
			" SELECT GRP.id_tenant, GRP.datasourcegroupversion, GRP.name, GRP.id_datasourcegroup_type, " + 
			" GRP.id_datasourcegroup, GRP.color, GRP.status " +
			" FROM " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE + " DS_GROUP ,  " + DATA_SOURCE_GROUP_TABLE + " GRP " + 
			" where " +

			" DS_GROUP.id_data_source = #{idDataSource} AND " + 
			" DS_GROUP.datasourceversion = #{datasourceversion} AND " + 
			" (DS_GROUP.id_datasourcegroup , DS_GROUP.datasourcegroupversion) in " + 
			 
			" ( select id_datasourcegroup, max(datasourcegroupversion) " +
			" from " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE +
			" where id_datasourcegroup = DS_GROUP.id_datasourcegroup " + 
			" group by id_datasourcegroup) AND " +
			
			" GRP.id_datasourcegroup = DS_GROUP.id_datasourcegroup AND " + 
			" GRP.datasourcegroupversion = DS_GROUP.datasourcegroupversion "; 
	@Results({
        @Result(property = "idTenant",  column = "id_tenant"),
        @Result(property = "idDatasourcegroupType",  column = "id_datasourcegroup_type"),
        @Result(property = "idDatasourcegroup",  column = "id_datasourcegroup"),
        @Result(property = "nameDatasourcegroupType",  column = "name_datasourcegroup_type"),
        @Result(property = "descriptionDatasourcegroupType",  column = "description_datasourcegroup_type")
      })
	@Select(SELECT_DATA_SOURCE_GROUPS_BY_ID_DATASOURCE_AND_DATASOURCEVERSION)
	List<DataSourceGroup> selectDataSourceGroupByIdDatasourceAndVersion( 
			@Param("idDataSource") Integer idDataSource, 
			@Param("datasourceversion") Integer datasourceversion );	
	
	/*************************************************************************
	 * 			INSERT R_DATASOURCE_DATASOURCEGROUP_BY_ID_DATASET
	 * ***********************************************************************/
	public static final String SELECT_COUNT_R_DATASOURCE_DATASOURCEGROUP_BY_ID_DATASET = 
			"SELECT count(*) from " + R_DATA_SOURCE_DATASOURCEGROUP_TABLE+ " where id_datasourcegroup=#{idDatasourcegroup} and datasourcegroupversion =#{datasourcegroupversion}"
					+ " and  datasourceversion=#{datasourceversion} and id_data_source=(select id_data_source from yucca_dataset where datasourceversion=#{datasourceversion} and iddataset=#{idDataset})";
	@Select(SELECT_COUNT_R_DATASOURCE_DATASOURCEGROUP_BY_ID_DATASET)
	Integer selectCountDatasourceDatasourcegroupByIdDataSet(
			@Param("idDatasourcegroup") Long idDatasourcegroup ,
			@Param("datasourcegroupversion") Integer datasourcegroupversion ,
			@Param("datasourceversion") Integer datasourceversion ,
			@Param("idDataset") Integer idDataset );
	
}