/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.csi.yucca.adminapi.model.TenantTool;
import org.csi.yucca.adminapi.model.Tool;
import org.csi.yucca.adminapi.response.ToolResponse;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author Pietro Cannalire
 *
 */
public interface ToolMapper {

	String TOOL_TABLE = Constants.SCHEMA_DB + "yucca_tool";
	String TENANT_TABLE = Constants.SCHEMA_DB + "yucca_tenant";
	String BUNDLES_TABLE = Constants.SCHEMA_DB + "yucca_bundles";
	String R_BUNDLES_TOOL_TABLE = Constants.SCHEMA_DB + "yucca_r_bundles_tool";
	String R_TENANT_BUNDLES_TABLE = Constants.SCHEMA_DB + "yucca_r_tenant_bundles";
	
	public static final String SELECT_TOOLS_ORDER_BY =  
			
			"<if test=\"sortList != null\">" +
		      " ORDER BY " +
			
			" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
		    
			  "<if test=\"propName == 'idTool-'\">" +
			    " id_tool desc" +
			  "</if>" +
			  "<if test=\"propName == 'idTool'\">" +
			    " id_tool" +
			  "</if>" +
   			
		      "<if test=\"propName == 'name-'\">" +
		        " name desc" +
	          "</if>" +
	          "<if test=\"propName == 'name'\">" +
	            " name" +
              "</if>" +

			  "<if test=\"propName == 'toolversion-'\">" +
		        " toolversion desc" +
	          "</if>" +
	          "<if test=\"propName == 'toolversion'\">" +
	            " toolversion" +
              "</if>" +
		      
            "</foreach>" +
            "</if>";
	
	/*************************************************************************
	 * 					
	 * 					INSERT R_BUNDLES_TOOL
	 * 
	 * ***********************************************************************/
	public static final String INSERT_BUNDLES_TOOLS =			
			"INSERT INTO " + R_BUNDLES_TOOL_TABLE + " " + 
			"(id_tool, id_bundles, enabled) " + 
			"VALUES (#{idTool}, " +
			" 			(SELECT DISTINCT yucca_r_tenant_bundles.id_bundles " + 
			"			FROM yucca_tenant " +
			"			LEFT JOIN yucca_r_tenant_bundles ON yucca_tenant.id_tenant = yucca_r_tenant_bundles.id_tenant " + 
			"			WHERE tenantcode=#{tenantcode}), " +			
			"		#{enabled})";
	@Results({
        @Result(property = "idTool", column = "id_tool"),
        @Result(property = "idBundles", column = "id_bundles")
      })
	@Insert(INSERT_BUNDLES_TOOLS)
	int insertBundlesTool(@Param("tenantcode") String tenantcode, @Param("idTool") Integer idTool, @Param("enabled") Boolean enabled);
	
	/*************************************************************************
	 * 					
	 * 					DELETE R_BUNDLES_TOOL
	 * 
	 * ***********************************************************************/
	public static final String DELETE_BUNDLES_TOOLS =			
			"DELETE FROM " + R_BUNDLES_TOOL_TABLE + " BUNDLES_TOOLS " + 
			"WHERE BUNDLES_TOOLS.id_bundles=" + 
			"			 		(SELECT DISTINCT yucca_r_tenant_bundles.id_bundles " + 
			"					FROM yucca_tenant " + 
			"					LEFT JOIN yucca_r_tenant_bundles ON yucca_tenant.id_tenant = yucca_r_tenant_bundles.id_tenant " + 
			"					WHERE tenantcode=#{tenantcode}) " +
			"AND BUNDLES_TOOLS.id_tool=#{idTool}";
	@Results({
        @Result(property = "idTool", column = "id_tool"),
        @Result(property = "idBundles", column = "id_bundles")
      })
	@Delete(DELETE_BUNDLES_TOOLS)
	int deleteBundlesTool(@Param("tenantcode") String tenantcode, @Param("idTool") int idTool);
	
	/*************************************************************************
	 * 
	 * 					SELECT TOOL BY IDTOOL
	 * 
	 * ***********************************************************************/
	public static final String SELECT_TOOL_BY_ID_TOOL = 
			"SELECT id_tool, name, toolversion FROM " + TOOL_TABLE + " WHERE id_tool=#{idTool}";
	@Results({
        @Result(property = "idTool", column = "id_tool")
      })
	@Select(SELECT_TOOL_BY_ID_TOOL)
	Tool selectTool(@Param("idTool") Integer idTool);

	/*************************************************************************
	 * 
	 * 					SELECT TENANT TOOL BY IDTOOL
	 * 
	 * ***********************************************************************/
	public static final String SELECT_TENANT_TOOL_BY_ID_TOOL = 
			"SELECT TOOLS.id_tool, TOOLS.name, TOOLS.toolversion , BUNDLES_TOOLS.enabled " + 
			"FROM " + TENANT_TABLE + " TENANTS, " + R_TENANT_BUNDLES_TABLE + " TENANT_BUNDLES, " + R_BUNDLES_TOOL_TABLE + " BUNDLES_TOOLS, " + TOOL_TABLE + " TOOLS " + 
			"WHERE TENANTS.id_tenant = TENANT_BUNDLES.id_tenant " + 
			"AND TENANT_BUNDLES.id_bundles = BUNDLES_TOOLS.id_bundles " + 
			"AND TOOLS.id_tool = BUNDLES_TOOLS.id_tool " + 
			"AND tenantcode=#{tenantcode}" +
			"AND TOOLS.id_tool=#{idTool}";
	@Results({
        @Result(property = "idTool", column = "id_tool")
      })
	@Select(SELECT_TENANT_TOOL_BY_ID_TOOL)
	TenantTool selectTenantToolByIdTool(@Param("tenantcode") String tenantcode, @Param("idTool") Integer idTool);
	
	/*************************************************************************
	 * 
	 * 					SELECT ALL TENANT TOOLS BY TENANTCODE
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ALL_TOOLS_BY_TENANTCODE = 
			"SELECT TOOLS.id_tool, TOOLS.name, TOOLS.toolversion , BUNDLES_TOOLS.enabled " + 
			"FROM " + TENANT_TABLE + " TENANTS, " + R_TENANT_BUNDLES_TABLE + " TENANT_BUNDLES, " + R_BUNDLES_TOOL_TABLE + " BUNDLES_TOOLS, " + TOOL_TABLE + " TOOLS " + 
			"WHERE TENANTS.id_tenant = TENANT_BUNDLES.id_tenant " + 
			"AND TENANT_BUNDLES.id_bundles = BUNDLES_TOOLS.id_bundles " + 
			"AND TOOLS.id_tool = BUNDLES_TOOLS.id_tool " + 
			"AND tenantcode=#{tenantcode} " + " " +
			SELECT_TOOLS_ORDER_BY;
	@Select({"<script>",SELECT_ALL_TOOLS_BY_TENANTCODE,"</script>"})
	@Results({
        @Result(property = "idTool", column = "id_tool")
      })
	List<TenantTool> selectAllToolsByTenantcode(@Param("tenantcode") String tenantcode, @Param("sortList") List<String> sortList);

	/*************************************************************************
	 * 
	 * 					UPDATE TENANT TOOL BY IDTOOL
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_TENANT_TOOL_BY_ID_TOOL = 
			"UPDATE " + R_BUNDLES_TOOL_TABLE + " " + 
			"SET enabled=#{enabled}" + 
			"WHERE id_bundles=(SELECT DISTINCT TENANT_BUNDLES.id_bundles " + 
			"	FROM "+TENANT_TABLE+" TENANTS, "+R_TENANT_BUNDLES_TABLE+" TENANT_BUNDLES, "+R_BUNDLES_TOOL_TABLE+" BUNDLES_TOOLS, "+TOOL_TABLE+ " TOOLS "+ 
			"	WHERE TENANTS.id_tenant = TENANT_BUNDLES.id_tenant " + 
			"	AND TENANT_BUNDLES.id_bundles = BUNDLES_TOOLS.id_bundles " + 
			"	AND TOOLS.id_tool = BUNDLES_TOOLS.id_tool " + 
			"	AND tenantcode=#{tenantcode}) " + 
			"   AND id_tool=#{idTool}" + 
			"";
	@Results({
        @Result(property = "idTool", column = "id_tool")
      })
	@Update(UPDATE_TENANT_TOOL_BY_ID_TOOL)
	int updateToolAccessibility(@Param("tenantcode") String tenantcode, @Param("idTool") Integer idTool, @Param("enabled") Boolean enabled);

	/*************************************************************************
	 * 
	 * 					SELECT TOOLS
	 * 
	 * ***********************************************************************/
	public static final String SELECT_TOOLS = 
			"SELECT id_tool, toolversion, name " + 
			"FROM "+ TOOL_TABLE + " " +  
			SELECT_TOOLS_ORDER_BY;
	@Results({
        @Result(property = "idTool", column = "id_tool")
      })
	@Select({"<script>",SELECT_TOOLS,"</script>"})
	List<Tool> selectAllTools(@Param("sortList") List<String> sortList);
	
	/*************************************************************************
	 * 
	 * 					SELECT TOOL BY NAME AND VERSION
	 * 
	 * ***********************************************************************/
	public static final String SELECT_TOOL_BY_NAME_AND_VERSION = 
			"SELECT id_tool, toolversion, name " + 
			"FROM "+ TOOL_TABLE + " " +
			"WHERE name=#{name}" +
			"AND toolversion=#{toolversion}";
	@Results({
        @Result(property = "idTool", column = "id_tool")
      })
	@Select(SELECT_TOOL_BY_NAME_AND_VERSION)
	ToolResponse selectToolByNameAndVersion(@Param("name") String name, @Param("toolversion") String toolversion);
	/*************************************************************************
	 * 
	 * 					ADD TOOL
	 * 
	 * ***********************************************************************/
	public static final String INSERT_TOOL = 
			"INSERT INTO " + TOOL_TABLE + " " +
			"(name, toolversion) " + 
			"VALUES(#{name}, #{toolversion}) ";
	@Results({
        @Result(property = "idTool", column = "id_tool")
      })
	@Insert(INSERT_TOOL)
	int insertTool(@Param("name") String name, @Param("toolversion") String toolversion);

	/*************************************************************************
	 * 
	 * 					DELETE TOOL
	 * 
	 * ***********************************************************************/
	public static final String DELETE_TOOL = 
			"DELETE FROM " + TOOL_TABLE + " " + 
			"WHERE id_tool=#{idTool}";
	@Results({
        @Result(property = "idTool", column = "id_tool")
      })
	@Delete(DELETE_TOOL)
	int deleteTool(@Param("idTool") Integer idTool);

	/*************************************************************************
	 * 
	 * 					UPDATE TOOL
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_TOOL = 
			"UPDATE " + TOOL_TABLE + " " + 
			"SET toolversion=#{toolversion}, name=#{name} " + 
			"WHERE id_tool=#{idTool} ";
	@Results({
        @Result(property = "idTool", column = "id_tool")
      })
	@Update(UPDATE_TOOL)
	int updateTool(@Param("idTool") Integer idTool, @Param("name") String name, @Param("toolversion") String toolversion);
	

	/*************************************************************************
	 * 
	 * 					UPDATE TOOLS ENVIRONMENT PREPARED
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_TOOLS_ENVIRONMENT_PREPARED = 
			"UPDATE " + BUNDLES_TABLE +" BUNDLES " + 
			"SET readytools=#{readytools} " + 
			"WHERE id_bundles=(SELECT DISTINCT TENANT_BUNDLES.id_bundles " + 
			"	FROM "+TENANT_TABLE+" TENANTS, "+R_TENANT_BUNDLES_TABLE+" TENANT_BUNDLES "+ 
			"	WHERE TENANTS.id_tenant = TENANT_BUNDLES.id_tenant " + 
			"	AND tenantcode=#{tenantcode})";
	@Results({
        @Result(property = "idTool", column = "id_tool")
      })
	@Update(UPDATE_TOOLS_ENVIRONMENT_PREPARED)
	int updateToolsEnvironmentPrepared(@Param("tenantcode") String tenantcode, @Param("readytools") Boolean readytools);
	
	
}
