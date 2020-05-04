/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */

/* ---------------------------------------------------------------------- */
/* Tables                                                                 */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Add table "yucca_component"                                            */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_component (
    id_component SERIAL   NOT NULL,
    name TEXT   NOT NULL,
    alias TEXT ,
    inOrder SMALLINT  DEFAULT 0,
    tolerance NUMERIC(100,8) ,
    since_version SMALLINT ,
    id_phenomenon INTEGER ,
    id_data_type INTEGER   NOT NULL,
    id_measure_unit INTEGER ,
    id_data_source INTEGER   NOT NULL,
    datasourceVersion INTEGER   NOT NULL,
    jdbcNativeType character varying(50),
    hiveType character varying(50),
    isKey  SMALLINT  DEFAULT 0,
	sourceColumn INTEGER,
	sourceColumnName  TEXT ,
	required SMALLINT  DEFAULT 1,
	foreignkey character varying(400),
	isgroupable SMALLINT NULL;
	CONSTRAINT PK_yucca_component PRIMARY KEY (id_component),
    CONSTRAINT AK_yucca_component_name_1 UNIQUE (name,id_data_source,datasourceVersion)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_so_category"                                        */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_so_category (
    id_so_category INTEGER   NOT NULL,
    soCategoryCode CHARACTER VARYING(20)   NOT NULL,
    description CHARACTER VARYING(250) ,
    CONSTRAINT PK_yucca_d_so_category PRIMARY KEY (id_so_category)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_status"                                             */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_status (
    id_status INTEGER   NOT NULL,
    statusCode CHARACTER VARYING(10)   NOT NULL,
    description CHARACTER VARYING(200)   NOT NULL,
    CONSTRAINT PK_yucca_d_status PRIMARY KEY (id_status),
    CONSTRAINT AK_yucca_d_status_1 UNIQUE (statusCode)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_domain"                                             */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_domain (
    id_domain INTEGER   NOT NULL,
    domainCode CHARACTER VARYING(400)   NOT NULL,
    langIT CHARACTER VARYING(400)   NOT NULL,
    langEN CHARACTER VARYING(400)   NOT NULL,
    deprecated SMALLINT  DEFAULT 0  NOT NULL,
    CONSTRAINT PK_yucca_d_domain PRIMARY KEY (id_domain),
    CONSTRAINT AK_yucca_d_domain_1 UNIQUE (domainCode)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_phenomenon"                                         */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_phenomenon (
    id_phenomenon INTEGER   NOT NULL,
    phenomenonName CHARACTER VARYING(250)   NOT NULL,
    phenomenonCetegory CHARACTER VARYING(250)   NOT NULL,
    CONSTRAINT PK_yucca_d_phenomenon PRIMARY KEY (id_phenomenon)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_mailtemplates"                                      */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_mailtemplates (
    id_tenant_type INTEGER   NOT NULL,
    mailbody CHARACTER VARYING(20000) ,
    mailObject CHARACTER VARYING(100) ,
    CONSTRAINT PK_yucca_d_mailtemplates PRIMARY KEY (id_tenant_type)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_tag"                                                */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_tag (
    id_tag SERIAL   NOT NULL,
    tagCode CHARACTER VARYING(400)   NOT NULL,
    langIT CHARACTER VARYING(400) ,
    langEN CHARACTER VARYING(400) ,
    id_ecosystem BIGINT ,
    CONSTRAINT PK_yucca_d_tag PRIMARY KEY (id_tag),
    CONSTRAINT AK_yucca_d_tag_1 UNIQUE (tagCode, id_ecosystem)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_subdomain"                                          */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_subdomain (
    id_subdomain INTEGER   NOT NULL,
    subdomainCode CHARACTER VARYING(400)   NOT NULL,
    lang_it CHARACTER VARYING(400)   NOT NULL,
    lang_en CHARACTER VARYING(400)   NOT NULL,
    deprecated SMALLINT  DEFAULT 0  NOT NULL,
    id_domain INTEGER   NOT NULL,
    CONSTRAINT PK_yucca_d_subdomain PRIMARY KEY (id_subdomain),
    CONSTRAINT AK_yucca_d_subdomain_1 UNIQUE (id_domain, id_subdomain)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_supply_type"                                        */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_supply_type (
    id_supply_type INTEGER   NOT NULL,
    supplyType CHARACTER VARYING(40)   NOT NULL,
    description CHARACTER VARYING(100) ,
    CONSTRAINT PK_yucca_d_supply_type PRIMARY KEY (id_supply_type),
    CONSTRAINT AK_yucca_d_supply_type_1 UNIQUE (supplyType)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_data_type"                                          */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_data_type (
    id_data_type INTEGER   NOT NULL,
    dataTypeCode CHARACTER VARYING(20)   NOT NULL,
    description CHARACTER VARYING(250) ,
    CONSTRAINT PK_yucca_d_data_type PRIMARY KEY (id_data_type),
    CONSTRAINT AK_yucca_d_data_type_1 UNIQUE (dataTypeCode)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_exposure_type"                                      */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_exposure_type (
    id_exposure_type INTEGER   NOT NULL,
    exposureType CHARACTER VARYING(40)   NOT NULL,
    description CHARACTER VARYING(100) ,
    CONSTRAINT PK_yucca_d_exposure_type PRIMARY KEY (id_exposure_type),
    CONSTRAINT AK_yucca_d_exposure_type_1 UNIQUE (exposureType)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_location_type"                                      */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_location_type (
    id_location_type INTEGER   NOT NULL,
    locationType CHARACTER VARYING(40)   NOT NULL,
    description CHARACTER VARYING(100) ,
    CONSTRAINT PK_yucca_d_location_type PRIMARY KEY (id_location_type),
    CONSTRAINT AK_yucca_d_location_type_1 UNIQUE (locationType)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_so_type"                                            */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_so_type (
    id_so_type INTEGER   NOT NULL,
    soTypeCode CHARACTER VARYING(20)   NOT NULL,
    description CHARACTER VARYING(250) ,
    CONSTRAINT PK_yucca_d_so_type PRIMARY KEY (id_so_type)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_measure_unit"                                       */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_measure_unit (
    id_measure_unit INTEGER   NOT NULL,
    measureUnit CHARACTER VARYING(250)   NOT NULL,
    measureUnitCategory CHARACTER VARYING(250) ,
    CONSTRAINT PK_yucca_d_measure_unit PRIMARY KEY (id_measure_unit)
);


-- chg: yucca_d_share_type
CREATE TABLE yucca_d_share_type
(
   id_share_type serial, 
   description character varying(100) NOT NULL, 
   CONSTRAINT pk_yucca_d_share_type PRIMARY KEY (id_share_type)
);
/* ---------------------------------------------------------------------- */
/* Add table "yucca_dcat"                                                 */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_dcat (
    id_dcat BIGINT   NOT NULL,
    dcatDataUpdate TIMESTAMP WITH TIME ZONE ,
    dcatNomeOrg CHARACTER VARYING(200)   NOT NULL,
    dcatEmailOrg CHARACTER VARYING(200)   NOT NULL,
    dcatCreatorName CHARACTER VARYING(400) ,
    dcatCreatorType CHARACTER VARYING(400) ,
    dcatCreatorId CHARACTER VARYING(400) ,
    dcatRightsHolderName CHARACTER VARYING(400) ,
    dcatRightsHolderType CHARACTER VARYING(400) ,
    dcatRightsHolderId CHARACTER VARYING(400) ,
    dcatReady SMALLINT  DEFAULT 1,
    CONSTRAINT PK_yucca_dcat PRIMARY KEY (id_dcat)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_ecosystem"                                            */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_ecosystem (
    id_ecosystem SERIAL   NOT NULL,
    ecosystemCode CHARACTER VARYING(40)   NOT NULL,
    description CHARACTER VARYING(200) ,
    CONSTRAINT PK_yucca_ecosystem PRIMARY KEY (id_ecosystem),
    CONSTRAINT AK_yucca_ecosystem_1 UNIQUE (ecosystemCode)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_organization"                                         */
/* 	                                                                      */
/* CHG: ADDED INFO FOR SPEED LAYER                                        */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_organization (
    id_organization SERIAL   NOT NULL,
    organizationCode CHARACTER VARYING(50)   NOT NULL,
    description CHARACTER VARYING(400) ,
    datasolrcollectionname character varying(200),
	measuresolrcollectionname character varying(200),
	mediasolrcollectionname character varying(200),
	socialsolrcollectionname character varying(200),
	dataphoenixtablename character varying(200),
	dataphoenixschemaname character varying(200),
	measuresphoenixtablename character varying(200),
	measuresphoenixschemaname character varying(200),
	mediaphoenixtablename character varying(200),
	mediaphoenixschemaname character varying(200),
	socialphoenixtablename character varying(200),
	socialphoenixschemaname character varying(200),
    CONSTRAINT PK_yucca_organization PRIMARY KEY (id_organization),
    CONSTRAINT AK_yucca_organization_1 UNIQUE (organizationCode)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_r_stream_internal"                                    */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_r_stream_internal (
    id_data_sourceInternal INTEGER   NOT NULL,
    datasourceVersionInternal INTEGER   NOT NULL,
    idStream INTEGER   NOT NULL,
    stream_alias CHARACTER VARYING(200) ,
    PRIMARY KEY (id_data_sourceInternal, datasourceVersionInternal, idStream)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_stream"                                               */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_stream (
    id_data_source INTEGER   NOT NULL,
    datasourceVersion INTEGER   NOT NULL,
    idStream INTEGER   NOT NULL,
    streamCode CHARACTER VARYING(200)   NOT NULL,
    streamName CHARACTER VARYING(100) ,
    publishStream SMALLINT ,
    saveData SMALLINT ,
    fps DOUBLE PRECISION ,
    internalQuery TEXT ,
    twtQuery CHARACTER VARYING(1000) ,
    twtGeolocLat DOUBLE PRECISION ,
    twtGeolocLon DOUBLE PRECISION ,
    twtGeolocRadius DOUBLE PRECISION ,
    twtGeolocUnit CHARACTER VARYING(100) ,
    twtLang CHARACTER VARYING(50) ,
    twtLocale CHARACTER VARYING(50) ,
    twtCount SMALLINT ,
    twtResultType CHARACTER VARYING(50) ,
    twtUntil CHARACTER VARYING(50) ,
    twtRatePercentage SMALLINT ,
    twtLastSearchId BIGINT ,
    id_smart_object INTEGER   NOT NULL,
    CONSTRAINT PK_yucca_stream PRIMARY KEY (id_data_source, datasourceVersion),
    CONSTRAINT AK_yucca_stream_1 UNIQUE (idStream, datasourceVersion),
    CONSTRAINT ak_yucca_stream_2 UNIQUE (id_smart_object, streamcode, datasourceversion)

);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_tenant"        
 * 
 * CHG: DROPPED SOME COLUMNS AND ADDED                                    */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_tenant (
    id_tenant SERIAL   NOT NULL,
    tenantCode CHARACTER VARYING(100)   NOT NULL,
    name CHARACTER VARYING(200)   NOT NULL,
    description CHARACTER VARYING(1000) ,
    clientKey CHARACTER VARYING(500) ,
    clientSecret CHARACTER VARYING(500) ,
    activationDate DATE  DEFAULT now(),
    deactivationDate DATE ,
    usageDaysNumber SMALLINT  DEFAULT -1  NOT NULL,
    username CHARACTER VARYING(100)   NOT NULL,    
    userFirstname CHARACTER VARYING(100)   NOT NULL,
    userLastname CHARACTER VARYING(100)   NOT NULL,
    userEmail CHARACTER VARYING(100)   NOT NULL,
    userTypeAuth CHARACTER VARYING(50)   NOT NULL,
    creationDate DATE  DEFAULT now(),
    expirationDate DATE ,
    id_ecosystem BIGINT ,
    id_organization BIGINT   NOT NULL,
    id_tenant_type INTEGER  DEFAULT 1  NOT NULL,
    id_tenant_status INTEGER  DEFAULT 1  NOT NULL,
    dataSolrCollectionName CHARACTER VARYING(200) ,
    measureSolrCollectionName CHARACTER VARYING(200) ,
    mediaSolrCollectionName CHARACTER VARYING(200) ,
    socialSolrCollectionName CHARACTER VARYING(200) ,
    dataPhoenixTableName CHARACTER VARYING(200) ,
    dataPhoenixSchemaName CHARACTER VARYING(200) ,
    measuresPhoenixTableName CHARACTER VARYING(200) ,
    measuresPhoenixSchemaName CHARACTER VARYING(200) ,
    mediaPhoenixTableName CHARACTER VARYING(200) ,
    mediaPhoenixSchemaName CHARACTER VARYING(200) ,
    socialPhoenixTableName CHARACTER VARYING(200) ,
    socialPhoenixSchemaName CHARACTER VARYING(200) ,
    id_share_type integer NOT NULL DEFAULT 2,
    CONSTRAINT PK_yucca_tenant PRIMARY KEY (id_tenant),
    CONSTRAINT AK_yucca_tenant_1 UNIQUE (tenantCode)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_so_position"                                          */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_so_position (
    id_smart_object INTEGER   NOT NULL,
    lat NUMERIC(100,8) ,
    lon NUMERIC(100,8) ,
    elevation NUMERIC(100,8) ,
    room CHARACTER VARYING(100) ,
    building CHARACTER VARYING(150) ,
    floor INTEGER ,
    address CHARACTER VARYING(200) ,
    city CHARACTER VARYING(200) ,
    country CHARACTER VARYING(200) ,
    placeGeometry TEXT ,
    CONSTRAINT PK_yucca_so_position PRIMARY KEY (id_smart_object)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_smart_object"                                         */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_smart_object (
    id_smart_object SERIAL   NOT NULL,
    soCode CHARACTER VARYING(100)   NOT NULL,
    name CHARACTER VARYING(100)   NOT NULL,
    description CHARACTER VARYING(250) ,
    urlAdmin CHARACTER VARYING(2000) ,
    fbcOperationFeedback CHARACTER VARYING(2000) ,
    swClientVersion CHARACTER VARYING(10) ,
    version SMALLINT ,
    model CHARACTER VARYING(100) ,
    deploymentVersion SMALLINT ,
    soStatus CHARACTER VARYING(100) ,
    creationDate TIMESTAMP WITH TIME ZONE ,
    twtUsername CHARACTER VARYING(200) ,
    twtUserToken CHARACTER VARYING(500) ,
    twtTokenSecret CHARACTER VARYING(500) ,
    twtName CHARACTER VARYING(100) ,
    twtUserId BIGINT ,
    twtMaxStreams SMALLINT ,
    slug CHARACTER VARYING(100)   NOT NULL,
    id_location_type INTEGER ,
    id_exposure_type INTEGER ,
    id_supply_type INTEGER ,
    id_so_category INTEGER   NOT NULL,
    id_so_type INTEGER   NOT NULL,
    id_status INTEGER   NOT NULL,
    id_organization INTEGER   NOT NULL,
    CONSTRAINT PK_yucca_smart_object PRIMARY KEY (id_smart_object),
    CONSTRAINT yucca_smart_object_slug_key UNIQUE(slug),
    CONSTRAINT yucca_smart_object_twtusername_key UNIQUE(twtusername),
    CONSTRAINT ak_yucca_smart_object_socode UNIQUE (socode)
   );

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_tenant_status"                                      */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_tenant_status (
    id_tenant_status INTEGER   NOT NULL,
    tenantStatusCode CHARACTER VARYING(40)   NOT NULL,
    description CHARACTER VARYING(100) ,
    CONSTRAINT PK_yucca_d_tenant_status PRIMARY KEY (id_tenant_status),
    CONSTRAINT AK_yucca_d_tenant_status_1 UNIQUE (tenantStatusCode)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_r_ecosystem_organization"                             */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_r_ecosystem_organization (
    id_ecosystem BIGINT   NOT NULL,
    id_organization BIGINT   NOT NULL,
    CONSTRAINT PK_yucca_r_ecosystem_organization PRIMARY KEY (id_ecosystem, id_organization)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_r_ecosystem_domain"                                   */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_r_ecosystem_domain (
    id_ecosystem BIGINT   NOT NULL,
    id_domain INTEGER   NOT NULL,
    CONSTRAINT PK_yucca_r_ecosystem_domain PRIMARY KEY (id_ecosystem, id_domain)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_tenant_type"                                        */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_tenant_type (
    id_tenant_type INTEGER   NOT NULL,
    tenantTypeCode CHARACTER VARYING(10)   NOT NULL,
    description CHARACTER VARYING(100) ,
    CONSTRAINT PK_yucca_d_tenant_type PRIMARY KEY (id_tenant_type),
    CONSTRAINT AK_yucca_d_tenant_type_1 UNIQUE (tenantTypeCode)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_dataset"                                              */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_dataset (
    id_data_source INTEGER   NOT NULL,
    datasourceVersion INTEGER   NOT NULL,
    idDataset INTEGER   NOT NULL,
    datasetCode CHARACTER VARYING(100)   NOT NULL,
    datasetName CHARACTER VARYING(255) ,
    description TEXT ,
    startIngestionDate TIMESTAMP WITH TIME ZONE ,
    endIngestionDate TIMESTAMP WITH TIME ZONE ,
    importFileType CHARACTER VARYING(100) ,
    id_dataset_type INTEGER   NOT NULL,
    id_dataset_subtype INTEGER   NOT NULL,
    solrCollectionName CHARACTER VARYING(200) ,
    phoenixTableName CHARACTER VARYING(200) ,
    phoenixSchemaName CHARACTER VARYING(200) ,
    availableHive SMALLINT ,
    availableSpeed SMALLINT,
    isTransformed SMALLINT,
    dbHiveSchema CHARACTER VARYING(200) ,
    dbHiveTable CHARACTER VARYING(200) ,
    id_data_source_binary INTEGER ,
    datasourceVersion_binary INTEGER ,
    importedfiles text,
    jdbcdburl CHARACTER VARYING(400) ,
	jdbcdbname CHARACTER VARYING(400) ,
	jdbcdbtype CHARACTER VARYING(400) ,
	jdbctablename CHARACTER VARYING(400) ,
	jdbcdbschema character varying(400),
    CONSTRAINT PK_yucca_dataset PRIMARY KEY (id_data_source, datasourceVersion),
    CONSTRAINT AK_yucca_dataset_1_01 UNIQUE (datasetCode, datasourceVersion),
    CONSTRAINT AK_yucca_dataset_2_02 UNIQUE (idDataset, datasourceVersion)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_dataset_type"                                       */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_dataset_type (
    id_dataset_type INTEGER   NOT NULL,
    dataset_type CHARACTER VARYING(40)   NOT NULL,
    description CHARACTER VARYING(250) ,
    CONSTRAINT PK_yucca_d_dataset_type PRIMARY KEY (id_dataset_type),
    CONSTRAINT AK_yucca_d_dataset_type_1 UNIQUE (dataset_type)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_dataset_subtype"                                    */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_dataset_subtype (
    id_dataset_subtype INTEGER   NOT NULL,
    dataset_subtype CHARACTER VARYING(40)   NOT NULL,
    description CHARACTER VARYING(250) ,
    id_dataset_type INTEGER   NOT NULL,
    CONSTRAINT PK_yucca_d_dataset_subtype PRIMARY KEY (id_dataset_subtype),
    CONSTRAINT AK_yucca_d_dataset_subtype_1 UNIQUE (id_dataset_type, id_dataset_subtype)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_allineamento"                                         */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_allineamento (
    id_tenant BIGINT   NOT NULL,
    locked SMALLINT   NOT NULL,
    lastObjectid CHARACTER VARYING(40) ,
    CONSTRAINT PK_yucca_allineamento PRIMARY KEY (id_tenant)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_api"                                                  */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_api (
    idApi INTEGER   NOT NULL,
    apiCode CHARACTER VARYING(200)   NOT NULL,
    apiName CHARACTER VARYING(200) ,
	apitype CHARACTER VARYING(20)  NOT NULL ,
	apisubtype CHARACTER VARYING(20)  NOT NULL,
	entitynamespace CHARACTER VARYING(200) ,
	max_odata_resultperpage integer DEFAULT 1000,
	id_data_source INTEGER   NOT NULL,
    datasourceVersion INTEGER   NOT NULL,
    CONSTRAINT PK_yucca_api PRIMARY KEY (idApi)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_data_source"                                          */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_data_source (
    id_data_source SERIAL   NOT NULL,
    datasourceVersion INTEGER   NOT NULL,
    isCurrent SMALLINT ,
    name CHARACTER VARYING(255) ,
    visibility CHARACTER VARYING(100) ,
    copyright CHARACTER VARYING(2000) ,
    disclaimer CHARACTER VARYING(2000) ,
    registrationDate TIMESTAMP WITH TIME ZONE ,
    requesterName CHARACTER VARYING(500) ,
    requesterSurname CHARACTER VARYING(500) ,
    requesterMail CHARACTER VARYING(500) ,
    privacyAcceptance SMALLINT ,
    icon TEXT ,
    isOpendata SMALLINT  DEFAULT -1  NOT NULL,
    externalreference CHARACTER VARYING(400) ,
    opendataAuthor CHARACTER VARYING(400) ,
    opendataUpdateDate TIMESTAMP WITH TIME ZONE ,
    opendataLanguage CHARACTER VARYING(40) ,
    lastUpdate CHARACTER VARYING(100) ,
    unpublished SMALLINT  DEFAULT -1  NOT NULL,
    fabricControllerOutcome CHARACTER VARYING(400) ,
    fbcOperationFeedback CHARACTER VARYING(2000) ,
    id_organization INTEGER   NOT NULL,
    id_subdomain INTEGER   NOT NULL,
    id_dcat BIGINT ,
    id_license INTEGER ,
    id_status INTEGER   NOT NULL,
    opendataupdatefrequency CHARACTER VARYING(50) ,
    CONSTRAINT PK_yucca_data_source PRIMARY KEY (id_data_source, datasourceVersion)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_r_tenant_data_source"                                 */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_r_tenant_data_source (
    id_data_source INTEGER   NOT NULL,
    datasourceVersion INTEGER   NOT NULL,
    id_tenant INTEGER   NOT NULL,
    isActive SMALLINT ,
    isManager SMALLINT ,
 	dataOptions	SMALLINT DEFAULT 2,
	manageOptions	SMALLINT DEFAULT 0,
    activationDate TIMESTAMP WITH TIME ZONE   NOT NULL,
    deactivationDate TIMESTAMP WITH TIME ZONE ,
    managerFrom TIMESTAMP WITH TIME ZONE ,
    managerUntil TIMESTAMP WITH TIME ZONE ,
    CONSTRAINT PK_yucca_r_tenant_data_source PRIMARY KEY (id_data_source, datasourceVersion, id_tenant)
);

-- se fossero da aggiungere mettere anche la versione
--CREATE UNIQUE INDEX IDX_yucca_r_tenant_data_source_1 ON yucca_r_tenant_data_source (id_data_source,nullif(isActive,0));

--CREATE UNIQUE INDEX IDX_yucca_r_tenant_data_source_2 ON yucca_r_tenant_data_source (id_data_source,nullif(isManager,0));

/* ---------------------------------------------------------------------- */
/* Add table "yucca_r_tag_data_source"                                    */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_r_tag_data_source (
    id_data_source INTEGER   NOT NULL,
    datasourceVersion INTEGER   NOT NULL,
    id_tag INTEGER   NOT NULL,
    CONSTRAINT PK_yucca_r_tag_data_source PRIMARY KEY (id_data_source, datasourceVersion, id_tag)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_d_license"                                            */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_d_license (
    id_license INTEGER   NOT NULL,
    licenseCode CHARACTER VARYING(100)   NOT NULL,
    description CHARACTER VARYING(200)   NOT NULL,
    CONSTRAINT PK_yucca_d_license PRIMARY KEY (id_license),
    CONSTRAINT AK_yucca_d_license_1 UNIQUE (licenseCode)
);

/* ---------------------------------------------------------------------- */
/* Add table "yucca_r_tenant_smart_object"                                */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_r_tenant_smart_object (
    id_tenant INTEGER   NOT NULL,
    id_smart_object INTEGER   NOT NULL,
    isActive SMALLINT ,
    isManager SMALLINT ,
    activationDate TIMESTAMP WITH TIME ZONE   NOT NULL,
    deactivationDate TIMESTAMP WITH TIME ZONE ,
    managerFrom TIMESTAMP WITH TIME ZONE ,
    managerUntil TIMESTAMP WITH TIME ZONE ,
    CONSTRAINT PK_yucca_r_tenant_smart_object PRIMARY KEY (id_tenant, id_smart_object)
);


CREATE TABLE yucca_bundles
(
   id_bundles serial, 
   maxdatasetnum integer NOT NULL DEFAULT -1, 
   maxstreamsnum integer NOT NULL DEFAULT -1, 
   hasstage character varying(100), 
   max_odata_resultperpage integer DEFAULT 1000, 
   zeppelin  character varying(100), 
   readytools boolean DEFAULT false,
   CONSTRAINT pk_yucca_bundles PRIMARY KEY (id_bundles)
);

CREATE TABLE yucca_r_organization_bundles
(
   id_bundles bigint NOT NULL, 
   id_organization bigint NOT NULL, 
   CONSTRAINT yucca_bundles_yucca_r_organization_bundles 
   				FOREIGN KEY (id_bundles) REFERENCES yucca_bundles (id_bundles) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT yucca_organization_yucca_r_organization_bundles 
   				FOREIGN KEY (id_organization) REFERENCES yucca_organization (id_organization) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE yucca_r_tenant_bundles
(
   id_tenant bigint NOT NULL, 
   id_bundles bigint NOT NULL, 
   CONSTRAINT yucca_tenant_yucca_r_tenant_bundles FOREIGN KEY (id_tenant) REFERENCES yucca_tenant (id_tenant) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT yucca_bundles_yucca_r_tenant_bundles FOREIGN KEY (id_bundles) REFERENCES yucca_bundles (id_bundles) ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE TABLE yucca_users
(
   id_user serial, 
   username character varying(100) NOT NULL, 
   id_organization bigint NOT NULL, 
   password character varying(100) NOT NULL, 
   CONSTRAINT pk_yucca_users PRIMARY KEY (id_user), 
   CONSTRAINT yucca_organization_yucca_users FOREIGN KEY (id_organization) REFERENCES yucca_organization (id_organization) ON UPDATE NO ACTION ON DELETE NO ACTION
);
------------------------------------------------------------------


CREATE TABLE yucca_r_tenant_users
(
   id_tenant bigint NOT NULL, 
   id_user bigint NOT NULL, 
   CONSTRAINT PK_yucca_r_tenant_userse PRIMARY KEY (id_tenant,id_user),
   CONSTRAINT yucca_tenant_yucca_r_tenant_users FOREIGN KEY (id_tenant) REFERENCES yucca_tenant (id_tenant) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT yucca_users_yucca_r_tenant_users FOREIGN KEY (id_user) REFERENCES yucca_users (id_user) ON UPDATE NO ACTION ON DELETE NO ACTION

);

CREATE TABLE yucca_allineamento_scarico_dataset
(
  id_organization bigint NOT NULL,
  id_dataset bigint NOT NULL,
  dataset_version integer NOT NULL,
  last_mongo_object_id character varying(40),
  CONSTRAINT yucca_organization_yucca_allineamento_scarico_dataset FOREIGN KEY (id_organization)
      REFERENCES yucca_organization (id_organization) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


/* ---------------------------------------------------------------------- */
/* Foreign key constraints                                                */
/* ---------------------------------------------------------------------- */

ALTER TABLE yucca_component ADD CONSTRAINT yucca_d_phenomenon_yucca_component 
    FOREIGN KEY (id_phenomenon) REFERENCES yucca_d_phenomenon (id_phenomenon);

ALTER TABLE yucca_component ADD CONSTRAINT yucca_d_data_type_yucca_component 
    FOREIGN KEY (id_data_type) REFERENCES yucca_d_data_type (id_data_type);

ALTER TABLE yucca_component ADD CONSTRAINT yucca_d_measure_unit_yucca_component 
    FOREIGN KEY (id_measure_unit) REFERENCES yucca_d_measure_unit (id_measure_unit);

ALTER TABLE yucca_component ADD CONSTRAINT yucca_data_source_yucca_component 
    FOREIGN KEY (id_data_source, datasourceVersion) REFERENCES yucca_data_source (id_data_source,datasourceVersion);
    
CREATE INDEX fk1_yucca_data_source_yucca_component ON yucca_component USING btree (id_data_source, datasourceversion);

ALTER TABLE yucca_d_mailtemplates ADD CONSTRAINT yucca_d_tenant_type_yucca_d_mailtemplates 
    FOREIGN KEY (id_tenant_type) REFERENCES yucca_d_tenant_type (id_tenant_type);

ALTER TABLE yucca_d_tag ADD CONSTRAINT yucca_ecosystem_yucca_d_tag 
    FOREIGN KEY (id_ecosystem) REFERENCES yucca_ecosystem (id_ecosystem);

ALTER TABLE yucca_d_subdomain ADD CONSTRAINT yucca_d_domain_yucca_d_subdomain 
    FOREIGN KEY (id_domain) REFERENCES yucca_d_domain (id_domain);

--ALTER TABLE yucca_r_stream_internal ADD CONSTRAINT yucca_stream_yucca_r_stream_internal_01 
  --  FOREIGN KEY (idstream) REFERENCES yucca_stream (idstream);

ALTER TABLE yucca_r_stream_internal ADD CONSTRAINT yucca_stream_yucca_r_stream_internal_02 
    FOREIGN KEY (id_data_sourceInternal, datasourceVersionInternal) REFERENCES yucca_stream (id_data_source,datasourceVersion);

ALTER TABLE yucca_stream ADD CONSTRAINT yucca_data_source_yucca_stream 
    FOREIGN KEY (id_data_source, datasourceVersion) REFERENCES yucca_data_source (id_data_source,datasourceVersion);

ALTER TABLE yucca_stream ADD CONSTRAINT yucca_smart_object_yucca_stream 
    FOREIGN KEY (id_smart_object) REFERENCES yucca_smart_object (id_smart_object);

ALTER TABLE yucca_tenant ADD CONSTRAINT yucca_ecosystem_yucca_tenant 
    FOREIGN KEY (id_ecosystem) REFERENCES yucca_ecosystem (id_ecosystem);

ALTER TABLE yucca_tenant ADD CONSTRAINT yucca_organization_yucca_tenant 
    FOREIGN KEY (id_organization) REFERENCES yucca_organization (id_organization);

ALTER TABLE yucca_tenant ADD CONSTRAINT yucca_d_tenant_type_yucca_tenant 
    FOREIGN KEY (id_tenant_type) REFERENCES yucca_d_tenant_type (id_tenant_type);

ALTER TABLE yucca_tenant ADD CONSTRAINT yucca_d_tenant_status_yucca_tenant 
    FOREIGN KEY (id_tenant_status) REFERENCES yucca_d_tenant_status (id_tenant_status);

CREATE INDEX fki_yucca_tenant_yucca_r_tenant_data_source ON yucca_r_tenant_data_source USING btree (id_tenant)

ALTER TABLE yucca_so_position ADD CONSTRAINT yucca_smart_object_yucca_so_position 
    FOREIGN KEY (id_smart_object) REFERENCES yucca_smart_object (id_smart_object);

ALTER TABLE yucca_smart_object ADD CONSTRAINT yucca_d_location_type_yucca_smart_object 
    FOREIGN KEY (id_location_type) REFERENCES yucca_d_location_type (id_location_type);

ALTER TABLE yucca_smart_object ADD CONSTRAINT yucca_d_exposure_type_yucca_smart_object 
    FOREIGN KEY (id_exposure_type) REFERENCES yucca_d_exposure_type (id_exposure_type);

ALTER TABLE yucca_smart_object ADD CONSTRAINT yucca_d_supply_type_yucca_smart_object 
    FOREIGN KEY (id_supply_type) REFERENCES yucca_d_supply_type (id_supply_type);

ALTER TABLE yucca_smart_object ADD CONSTRAINT yucca_d_so_category_yucca_smart_object 
    FOREIGN KEY (id_so_category) REFERENCES yucca_d_so_category (id_so_category);

ALTER TABLE yucca_smart_object ADD CONSTRAINT yucca_d_so_type_yucca_smart_object 
    FOREIGN KEY (id_so_type) REFERENCES yucca_d_so_type (id_so_type);

ALTER TABLE yucca_smart_object ADD CONSTRAINT yucca_d_status_yucca_smart_object 
    FOREIGN KEY (id_status) REFERENCES yucca_d_status (id_status);

ALTER TABLE yucca_smart_object ADD CONSTRAINT yucca_organization_yucca_smart_object 
    FOREIGN KEY (id_organization) REFERENCES yucca_organization (id_organization);

ALTER TABLE yucca_r_ecosystem_organization ADD CONSTRAINT yucca_ecosystem_yucca_r_ecosystem_organization 
    FOREIGN KEY (id_ecosystem) REFERENCES yucca_ecosystem (id_ecosystem);

ALTER TABLE yucca_r_ecosystem_organization ADD CONSTRAINT yucca_organization_yucca_r_ecosystem_organization 
    FOREIGN KEY (id_organization) REFERENCES yucca_organization (id_organization);

ALTER TABLE yucca_r_ecosystem_domain ADD CONSTRAINT yucca_ecosystem_yucca_r_ecosystem_domain 
    FOREIGN KEY (id_ecosystem) REFERENCES yucca_ecosystem (id_ecosystem);

ALTER TABLE yucca_r_ecosystem_domain ADD CONSTRAINT yucca_d_domain_yucca_r_ecosystem_domain 
    FOREIGN KEY (id_domain) REFERENCES yucca_d_domain (id_domain);

ALTER TABLE yucca_dataset ADD CONSTRAINT yucca_d_dataset_type_yucca_dataset 
    FOREIGN KEY (id_dataset_type) REFERENCES yucca_d_dataset_type (id_dataset_type);

ALTER TABLE yucca_dataset ADD CONSTRAINT yucca_d_dataset_subtype_yucca_dataset 
    FOREIGN KEY (id_dataset_subtype) REFERENCES yucca_d_dataset_subtype (id_dataset_subtype);

ALTER TABLE yucca_dataset ADD CONSTRAINT yucca_data_source_yucca_dataset 
    FOREIGN KEY (id_data_source, datasourceVersion) REFERENCES yucca_data_source (id_data_source,datasourceVersion);

ALTER TABLE yucca_dataset ADD CONSTRAINT yucca_dataset_yucca_dataset 
    FOREIGN KEY (id_data_source_binary, datasourceVersion_binary) REFERENCES yucca_dataset (id_data_source,datasourceVersion);

ALTER TABLE yucca_d_dataset_subtype ADD CONSTRAINT yucca_d_dataset_type_yucca_d_dataset_subtype 
    FOREIGN KEY (id_dataset_type) REFERENCES yucca_d_dataset_type (id_dataset_type);

ALTER TABLE yucca_allineamento ADD CONSTRAINT yucca_tenant_yucca_allineamento 
    FOREIGN KEY (id_tenant) REFERENCES yucca_tenant (id_tenant);


ALTER TABLE yucca_data_source ADD CONSTRAINT yucca_organization_yucca_data_source 
    FOREIGN KEY (id_organization) REFERENCES yucca_organization (id_organization);

ALTER TABLE yucca_data_source ADD CONSTRAINT yucca_d_subdomain_yucca_data_source 
    FOREIGN KEY (id_subdomain) REFERENCES yucca_d_subdomain (id_subdomain);

ALTER TABLE yucca_data_source ADD CONSTRAINT yucca_dcat_yucca_data_source 
    FOREIGN KEY (id_dcat) REFERENCES yucca_dcat (id_dcat);

ALTER TABLE yucca_data_source ADD CONSTRAINT yucca_d_license_yucca_data_source 
    FOREIGN KEY (id_license) REFERENCES yucca_d_license (id_license);

ALTER TABLE yucca_data_source ADD CONSTRAINT yucca_d_status_yucca_data_source 
    FOREIGN KEY (id_status) REFERENCES yucca_d_status (id_status);

ALTER TABLE yucca_r_tenant_data_source ADD CONSTRAINT yucca_data_source_yucca_r_tenant_data_source 
    FOREIGN KEY (id_data_source, datasourceVersion) REFERENCES yucca_data_source (id_data_source,datasourceVersion);

ALTER TABLE yucca_r_tenant_data_source ADD CONSTRAINT yucca_tenant_yucca_r_tenant_data_source 
    FOREIGN KEY (id_tenant) REFERENCES yucca_tenant (id_tenant);

ALTER TABLE yucca_r_tag_data_source ADD CONSTRAINT yucca_data_source_yucca_r_tag_data_source 
    FOREIGN KEY (id_data_source, datasourceVersion) REFERENCES yucca_data_source (id_data_source,datasourceVersion);

ALTER TABLE yucca_r_tag_data_source ADD CONSTRAINT yucca_d_tag_yucca_r_tag_data_source 
    FOREIGN KEY (id_tag) REFERENCES yucca_d_tag (id_tag);

ALTER TABLE yucca_r_tenant_smart_object ADD CONSTRAINT yucca_tenant_yucca_r_tenant_smart_object 
    FOREIGN KEY (id_tenant) REFERENCES yucca_tenant (id_tenant);

ALTER TABLE yucca_r_tenant_smart_object ADD CONSTRAINT yucca_smart_object_yucca_r_tenant_smart_object 
    FOREIGN KEY (id_smart_object) REFERENCES yucca_smart_object (id_smart_object);
    
    
-- CHG

ALTER TABLE yucca_tenant  ADD CONSTRAINT yucca_d_share_type_yucca_tenant 
	FOREIGN KEY (id_share_type) REFERENCES yucca_d_share_type (id_share_type) 
  	ON UPDATE NO ACTION ON DELETE NO ACTION;

  	
  	
  	
  	
CREATE OR REPLACE FUNCTION fnc_shuffle_string(testo character varying)
  RETURNS text AS
$BODY$
declare
  result text := '';
begin
  
    SELECT string_agg(ch, '') into result 
    from (
        select substr(testo, i, 1) ch
        from generate_series(1, length(testo)) i
        order by random()
        ) s;
  

    return result;
    
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

--------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE FUNCTION fnc_random_string(length integer)
  RETURNS text AS
$BODY$
declare
  chars text[] := '{0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z}';
  charsNr text[] := '{0,1,2,3,4,5,6,7,8,9}';
  charsUpper text[] := '{A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z}';
  charsLower text[] := '{a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z}';
  charsSpecial text[] := '{!,#,*}';
  result text := '';
  resultsh text := '';
  i integer := 0;
begin
  if length < 4 then
    raise exception 'Given length cannot be less than 4';

  end if;
  for i in 1..(length-4) loop
    result := result || chars[1+random()*(array_length(chars, 1)-1)];
  end loop;
  
  
  result := result || charsNr[1+random()*(array_length(charsNr, 1)-1)];
  result := result || charsUpper[1+random()*(array_length(charsUpper, 1)-1)];
  result := result || charsLower[1+random()*(array_length(charsLower, 1)-1)];
  result := result || charsSpecial[1+random()*(array_length(charsSpecial, 1)-1)];
  
  select fnc_shuffle_string(result) into resultsh;
  return resultsh;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


-----------------------------------------------------------------------------------------------------------------
