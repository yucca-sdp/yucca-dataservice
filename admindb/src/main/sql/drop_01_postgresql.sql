/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */

/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.2.1                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          Postgresql_MetadatiModel_V01_05.dez             */
/* Project name:          Yucca Metadata                                  */
/* Author:                                                                */
/* Script type:           Database drop script                            */
/* Created on:            2017-05-04 17:20                                */
/* ---------------------------------------------------------------------- */


/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */

ALTER TABLE yucca_component DROP CONSTRAINT yucca_d_phenomenon_yucca_component;

ALTER TABLE yucca_component DROP CONSTRAINT yucca_d_data_type_yucca_component;

ALTER TABLE yucca_component DROP CONSTRAINT yucca_d_measure_unit_yucca_component;

ALTER TABLE yucca_component DROP CONSTRAINT yucca_data_source_yucca_component;

ALTER TABLE yucca_d_mailtemplates DROP CONSTRAINT yucca_d_tenant_type_yucca_d_mailtemplates;

ALTER TABLE yucca_d_tag DROP CONSTRAINT yucca_ecosystem_yucca_d_tag;

ALTER TABLE yucca_d_subdomain DROP CONSTRAINT yucca_d_domain_yucca_d_subdomain;

ALTER TABLE yucca_r_stream_internal DROP CONSTRAINT yucca_stream_yucca_r_stream_internal_01;

ALTER TABLE yucca_r_stream_internal DROP CONSTRAINT yucca_stream_yucca_r_stream_internal_02;

ALTER TABLE yucca_stream DROP CONSTRAINT yucca_data_source_yucca_stream;

ALTER TABLE yucca_stream DROP CONSTRAINT yucca_smart_object_yucca_stream;

ALTER TABLE yucca_tenant DROP CONSTRAINT yucca_ecosystem_yucca_tenant;

ALTER TABLE yucca_tenant DROP CONSTRAINT yucca_organization_yucca_tenant;

ALTER TABLE yucca_tenant DROP CONSTRAINT yucca_d_tenant_type_yucca_tenant;

ALTER TABLE yucca_tenant DROP CONSTRAINT yucca_d_tenant_status_yucca_tenant;

ALTER TABLE yucca_so_position DROP CONSTRAINT yucca_smart_object_yucca_so_position;

ALTER TABLE yucca_smart_object DROP CONSTRAINT yucca_d_location_type_yucca_smart_object;

ALTER TABLE yucca_smart_object DROP CONSTRAINT yucca_d_exposure_type_yucca_smart_object;

ALTER TABLE yucca_smart_object DROP CONSTRAINT yucca_d_supply_type_yucca_smart_object;

ALTER TABLE yucca_smart_object DROP CONSTRAINT yucca_d_so_category_yucca_smart_object;

ALTER TABLE yucca_smart_object DROP CONSTRAINT yucca_d_so_type_yucca_smart_object;

ALTER TABLE yucca_smart_object DROP CONSTRAINT yucca_d_status_yucca_smart_object;

ALTER TABLE yucca_smart_object DROP CONSTRAINT yucca_organization_yucca_smart_object;

ALTER TABLE yucca_r_ecosystem_organization DROP CONSTRAINT yucca_ecosystem_yucca_r_ecosystem_organization;

ALTER TABLE yucca_r_ecosystem_organization DROP CONSTRAINT yucca_organization_yucca_r_ecosystem_organization;

ALTER TABLE yucca_r_ecosystem_domain DROP CONSTRAINT yucca_ecosystem_yucca_r_ecosystem_domain;

ALTER TABLE yucca_r_ecosystem_domain DROP CONSTRAINT yucca_d_domain_yucca_r_ecosystem_domain;

ALTER TABLE yucca_dataset DROP CONSTRAINT yucca_d_dataset_type_yucca_dataset;

ALTER TABLE yucca_dataset DROP CONSTRAINT yucca_d_dataset_subtype_yucca_dataset;

ALTER TABLE yucca_dataset DROP CONSTRAINT yucca_data_source_yucca_dataset;

ALTER TABLE yucca_dataset DROP CONSTRAINT yucca_dataset_yucca_dataset;

ALTER TABLE yucca_d_dataset_subtype DROP CONSTRAINT yucca_d_dataset_type_yucca_d_dataset_subtype;

ALTER TABLE yucca_allineamento DROP CONSTRAINT yucca_tenant_yucca_allineamento;

ALTER TABLE yucca_api DROP CONSTRAINT yucca_data_source_yucca_api;

ALTER TABLE yucca_data_source DROP CONSTRAINT yucca_organization_yucca_data_source;

ALTER TABLE yucca_data_source DROP CONSTRAINT yucca_d_subdomain_yucca_data_source;

ALTER TABLE yucca_data_source DROP CONSTRAINT yucca_dcat_yucca_data_source;

ALTER TABLE yucca_data_source DROP CONSTRAINT yucca_d_license_yucca_data_source;

ALTER TABLE yucca_data_source DROP CONSTRAINT yucca_d_status_yucca_data_source;

ALTER TABLE yucca_r_tenant_data_source DROP CONSTRAINT yucca_data_source_yucca_r_tenant_data_source;

ALTER TABLE yucca_r_tenant_data_source DROP CONSTRAINT yucca_tenant_yucca_r_tenant_data_source;

ALTER TABLE yucca_r_tag_data_source DROP CONSTRAINT yucca_data_source_yucca_r_tag_data_source;

ALTER TABLE yucca_r_tag_data_source DROP CONSTRAINT yucca_d_tag_yucca_r_tag_data_source;

ALTER TABLE yucca_r_tenant_smart_object DROP CONSTRAINT yucca_tenant_yucca_r_tenant_smart_object;

ALTER TABLE yucca_r_tenant_smart_object DROP CONSTRAINT yucca_smart_object_yucca_r_tenant_smart_object;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_dataset"                                             */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_dataset DROP CONSTRAINT PK_yucca_dataset;

ALTER TABLE yucca_dataset DROP CONSTRAINT AK_yucca_dataset_1_01;

ALTER TABLE yucca_dataset DROP CONSTRAINT AK_yucca_dataset_2_02;

/* Drop table */

DROP TABLE yucca_dataset;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_r_stream_internal"                                   */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

/* Drop table */

DROP TABLE yucca_r_stream_internal;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_api"                                                 */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_api DROP CONSTRAINT PK_yucca_api;

ALTER TABLE yucca_api DROP CONSTRAINT AK_yucca_api_1;

/* Drop table */

DROP TABLE yucca_api;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_stream"                                              */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_stream DROP CONSTRAINT PK_yucca_stream;

ALTER TABLE yucca_stream DROP CONSTRAINT AK_yucca_stream_1;

/* Drop table */

DROP TABLE yucca_stream;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_component"                                           */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_component DROP CONSTRAINT PK_yucca_component;

/* Drop table */

DROP TABLE yucca_component;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_r_tenant_smart_object"                               */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_r_tenant_smart_object DROP CONSTRAINT PK_yucca_r_tenant_smart_object;

/* Drop table */

DROP TABLE yucca_r_tenant_smart_object;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_r_tag_data_source"                                   */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_r_tag_data_source DROP CONSTRAINT PK_yucca_r_tag_data_source;

/* Drop table */

DROP TABLE yucca_r_tag_data_source;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_r_tenant_data_source"                                */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_r_tenant_data_source DROP CONSTRAINT PK_yucca_r_tenant_data_source;

/* Drop table */

DROP TABLE yucca_r_tenant_data_source;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_data_source"                                         */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_data_source DROP CONSTRAINT PK_yucca_data_source;

/* Drop table */

DROP TABLE yucca_data_source;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_allineamento"                                        */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_allineamento DROP CONSTRAINT PK_yucca_allineamento;

/* Drop table */

DROP TABLE yucca_allineamento;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_so_position"                                         */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_so_position DROP CONSTRAINT PK_yucca_so_position;

/* Drop table */

DROP TABLE yucca_so_position;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_tenant"                                              */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_tenant DROP CONSTRAINT PK_yucca_tenant;

ALTER TABLE yucca_tenant DROP CONSTRAINT AK_yucca_tenant_1;

/* Drop table */

DROP TABLE yucca_tenant;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_tag"                                               */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_tag DROP CONSTRAINT PK_yucca_d_tag;

ALTER TABLE yucca_d_tag DROP CONSTRAINT AK_yucca_d_tag_1;

/* Drop table */

DROP TABLE yucca_d_tag;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_mailtemplates"                                     */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_mailtemplates DROP CONSTRAINT PK_yucca_d_mailtemplates;

/* Drop table */

DROP TABLE yucca_d_mailtemplates;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_license"                                           */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_license DROP CONSTRAINT PK_yucca_d_license;

ALTER TABLE yucca_d_license DROP CONSTRAINT AK_yucca_d_license_1;

/* Drop table */

DROP TABLE yucca_d_license;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_dataset_subtype"                                   */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_dataset_subtype DROP CONSTRAINT PK_yucca_d_dataset_subtype;

ALTER TABLE yucca_d_dataset_subtype DROP CONSTRAINT AK_yucca_d_dataset_subtype_1;

/* Drop table */

DROP TABLE yucca_d_dataset_subtype;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_dataset_type"                                      */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_dataset_type DROP CONSTRAINT PK_yucca_d_dataset_type;

ALTER TABLE yucca_d_dataset_type DROP CONSTRAINT AK_yucca_d_dataset_type_1;

/* Drop table */

DROP TABLE yucca_d_dataset_type;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_tenant_type"                                       */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_tenant_type DROP CONSTRAINT PK_yucca_d_tenant_type;

ALTER TABLE yucca_d_tenant_type DROP CONSTRAINT AK_yucca_d_tenant_type_1;

/* Drop table */

DROP TABLE yucca_d_tenant_type;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_r_ecosystem_domain"                                  */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_r_ecosystem_domain DROP CONSTRAINT PK_yucca_r_ecosystem_domain;

/* Drop table */

DROP TABLE yucca_r_ecosystem_domain;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_r_ecosystem_organization"                            */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_r_ecosystem_organization DROP CONSTRAINT PK_yucca_r_ecosystem_organization;

/* Drop table */

DROP TABLE yucca_r_ecosystem_organization;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_tenant_status"                                     */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_tenant_status DROP CONSTRAINT PK_yucca_d_tenant_status;

ALTER TABLE yucca_d_tenant_status DROP CONSTRAINT AK_yucca_d_tenant_status_1;

/* Drop table */

DROP TABLE yucca_d_tenant_status;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_smart_object"                                        */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_smart_object DROP CONSTRAINT PK_yucca_smart_object;

ALTER TABLE yucca_smart_object DROP CONSTRAINT yucca_smart_object_slug_key;
ALTER TABLE yucca_smart_object DROP CONSTRAINT yucca_smart_object_twtusername_key;


/* Drop table */

DROP TABLE yucca_smart_object;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_organization"                                        */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_organization DROP CONSTRAINT PK_yucca_organization;

ALTER TABLE yucca_organization DROP CONSTRAINT AK_yucca_organization_1;

/* Drop table */

DROP TABLE yucca_organization;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_ecosystem"                                           */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_ecosystem DROP CONSTRAINT PK_yucca_ecosystem;

ALTER TABLE yucca_ecosystem DROP CONSTRAINT AK_yucca_ecosystem_1;

/* Drop table */

DROP TABLE yucca_ecosystem;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_dcat"                                                */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_dcat DROP CONSTRAINT PK_yucca_dcat;

/* Drop table */

DROP TABLE yucca_dcat;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_measure_unit"                                      */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_measure_unit DROP CONSTRAINT PK_yucca_d_measure_unit;

/* Drop table */

DROP TABLE yucca_d_measure_unit;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_so_type"                                           */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_so_type DROP CONSTRAINT PK_yucca_d_so_type;

/* Drop table */

DROP TABLE yucca_d_so_type;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_location_type"                                     */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_location_type DROP CONSTRAINT PK_yucca_d_location_type;

ALTER TABLE yucca_d_location_type DROP CONSTRAINT AK_yucca_d_location_type_1;

/* Drop table */

DROP TABLE yucca_d_location_type;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_exposure_type"                                     */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_exposure_type DROP CONSTRAINT PK_yucca_d_exposure_type;

ALTER TABLE yucca_d_exposure_type DROP CONSTRAINT AK_yucca_d_exposure_type_1;

/* Drop table */

DROP TABLE yucca_d_exposure_type;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_data_type"                                         */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_data_type DROP CONSTRAINT PK_yucca_d_data_type;

ALTER TABLE yucca_d_data_type DROP CONSTRAINT AK_yucca_d_data_type_1;

/* Drop table */

DROP TABLE yucca_d_data_type;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_supply_type"                                       */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_supply_type DROP CONSTRAINT PK_yucca_d_supply_type;

ALTER TABLE yucca_d_supply_type DROP CONSTRAINT AK_yucca_d_supply_type_1;

/* Drop table */

DROP TABLE yucca_d_supply_type;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_subdomain"                                         */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_subdomain DROP CONSTRAINT PK_yucca_d_subdomain;

ALTER TABLE yucca_d_subdomain DROP CONSTRAINT AK_yucca_d_subdomain_1;

/* Drop table */

DROP TABLE yucca_d_subdomain;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_phenomenon"                                        */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_phenomenon DROP CONSTRAINT PK_yucca_d_phenomenon;

/* Drop table */

DROP TABLE yucca_d_phenomenon;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_domain"                                            */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_domain DROP CONSTRAINT PK_yucca_d_domain;

ALTER TABLE yucca_d_domain DROP CONSTRAINT AK_yucca_d_domain_1;

/* Drop table */

DROP TABLE yucca_d_domain;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_status"                                            */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_status DROP CONSTRAINT PK_yucca_d_status;

ALTER TABLE yucca_d_status DROP CONSTRAINT AK_yucca_d_status_1;

/* Drop table */

DROP TABLE yucca_d_status;

/* ---------------------------------------------------------------------- */
/* Drop table "yucca_d_so_category"                                       */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE yucca_d_so_category DROP CONSTRAINT PK_yucca_d_so_category;

/* Drop table */

DROP TABLE yucca_d_so_category;

/* ADDED */

ALTER TABLE yucca_r_organization_bundles DROP CONSTRAINT yucca_bundles_yucca_r_organization_bundles;
ALTER TABLE yucca_r_organization_bundles DROP CONSTRAINT yucca_organization_yucca_r_organization_bundles;
DROP TABLE yucca_r_organization_bundles;

ALTER TABLE yucca_r_tenant_bundles DROP CONSTRAINT yucca_tenant_yucca_r_tenant_bundles;
ALTER TABLE yucca_r_tenant_bundles DROP CONSTRAINT yucca_bundles_yucca_r_tenant_bundles;
DROP TABLE yucca_r_tenant_bundles;

ALTER TABLE yucca_r_tenant_users DROP CONSTRAINT  yucca_tenant_yucca_r_tenant_users;
ALTER TABLE yucca_r_tenant_users DROP CONSTRAINT  yucca_users_yucca_r_tenant_users;
DROP TABLE yucca_r_tenant_users;

ALTER TABLE yucca_users DROP CONSTRAINT  pk_yucca_users;
ALTER TABLE yucca_users DROP CONSTRAINT  yucca_organization_yucca_users;

DROP TABLE yucca_users;
------------------------------------------------------------------

DROP TABLE yucca_d_share_type;

ALTER TABLE yucca_bundles DROP CONSTRAINT pk_yucca_bundles;

DROP TABLE yucca_bundles;






