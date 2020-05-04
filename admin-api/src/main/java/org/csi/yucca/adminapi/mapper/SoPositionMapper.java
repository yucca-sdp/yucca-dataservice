/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.csi.yucca.adminapi.model.SoPosition;
import org.csi.yucca.adminapi.util.Constants;

public interface SoPositionMapper {

	String SO_POSITION_TABLE = Constants.SCHEMA_DB + "yucca_so_position";

	/*************************************************************************
	 * 
	 * 					INSERT SO_POSITION
	 * 
	 * ***********************************************************************/
	public static final String INSERT_SO_POSITION = 
			" INSERT INTO " + SO_POSITION_TABLE + " (id_smart_object, lat, lon, elevation, room, building, floor, address, city, country, placegeometry) " +
			" VALUES (#{idSmartObject}, #{lat}, #{lon}, #{elevation}, #{room}, #{building}, #{floor}, #{address}, #{city}, #{country}, #{placegeometry})" ;
	@Insert(INSERT_SO_POSITION)
	int insertSoPosition(SoPosition soPosition);

	/*************************************************************************
	 * 
	 * 					DELETE SO POSITION
	 * 
	 * ***********************************************************************/
	public static final String DELETE_SO_POSITION = "DELETE FROM " + SO_POSITION_TABLE + " WHERE id_smart_object=#{idSmartObject}";
	@Delete(DELETE_SO_POSITION)
	int deleteSoPosition(int idSmartObject);	
	
	
	
}