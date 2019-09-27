/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.util;

import java.util.Arrays;
import java.util.List;

import org.csi.yucca.adminapi.model.Component;

public class Constants {

	public static final String SCHEMA_DB = "";
	
	public static final String CLIENT_FORMAT_DATE = "yyyyMMdd";
	
	public static final String MAIL_FORMAT_DATE = "dd/MM/yyyy";
	
	public static final int PASSWORD_LENGTH = 12;

	public static int INSTALLATION_TENANT_MAX_DATASET_NUM = 20;

	public static int INSTALLATION_TENANT_MAX_STREAMS_NUM = 20;
	
	public static String TWEET_ALIAS_COMPONENT = "-";
	
	public static Integer OTHER_MEASURE_UNIT_ID = 0;
	
	public static List<Component> TWEET_COMPONENTS = Arrays.asList(
			new Component().name("contributors").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(1).isgroupable(0),	
			new Component().name("createdAt").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.DATE_TIME.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(2).isgroupable(0),	
			new Component().name("currentUserRetweetId").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.LONG.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(3).isgroupable(0),	
			new Component().name("favoriteCount").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.INT.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(4).isgroupable(0),	
			new Component().name("lon").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(5).isgroupable(0),	
			new Component().name("lat").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(6).isgroupable(0),
			new Component().name("tweetid").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.LONG.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(7).isgroupable(0),
			new Component().name("lang").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(8).isgroupable(0),
			new Component().name("placeName").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(9).isgroupable(0),	
			new Component().name("retweetCount").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.INT.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(10).isgroupable(0),
			new Component().name("source").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(11).isgroupable(0),	
			new Component().name("getText").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(12).isgroupable(0),
			new Component().name("favorited").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.BOOLEAN.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(13).isgroupable(0),
			new Component().name("possiblySensitive").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.BOOLEAN.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(14).isgroupable(0),
			new Component().name("retweet").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.BOOLEAN.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(15).isgroupable(0),
			new Component().name("retweetedByMe").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.BOOLEAN.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(16).isgroupable(0),	
			new Component().name("truncated").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.BOOLEAN.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(17).isgroupable(0),
			new Component().name("hashTags").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(18).isgroupable(0),
			new Component().name("url").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(19).isgroupable(0),
			new Component().name("media").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(20).isgroupable(0),	
			new Component().name("mediaUrl").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(21).isgroupable(0),
			new Component().name("mediaCnt").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(22).isgroupable(0),	
			new Component().name("userId").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.LONG.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(23).isgroupable(0),	
			new Component().name("userName").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(24).isgroupable(0),
			new Component().name("userScreenName").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(25).isgroupable(0),	
			new Component().name("userMentions").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(26).isgroupable(0),	
			new Component().name("retweetParentId").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.LONG.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(27).isgroupable(1));
	
	public static Integer ADMINAPI_DATA_TYPE_INT = 1;
	public static Integer ADMINAPI_DATA_TYPE_LONG = 2;
	public static Integer ADMINAPI_DATA_TYPE_DOUBLE = 3;
	public static Integer ADMINAPI_DATA_TYPE_FLOAT = 4;
	public static Integer ADMINAPI_DATA_TYPE_STRING = 5;
	public static Integer ADMINAPI_DATA_TYPE_BOOLEAN = 6;
	public static Integer ADMINAPI_DATA_TYPE_DATETIME = 7;
	public static Integer ADMINAPI_DATA_TYPE_LONGITUDE = 8;
	public static Integer ADMINAPI_DATA_TYPE_LATITUDE = 9;
	public static Integer ADMINAPI_DATA_TYPE_BINARY = 10;
	
	public static Integer ADMINAPI_TENANT_ID_TYPE_DEVELOP = 6;
	
	public static final String API_NAMESPACE_BASE = "it.csi.smartdata.odata";

	public static Integer MAX_ODATA_RESULT_PER_PAGE = 1000;
	
	public static String ADMINAPI_DECIMAL_SEPARATOR_COMMA = "COMMA";
	public static String ADMINAPI_DECIMAL_SEPARATOR_DOT = "DOT";

}
