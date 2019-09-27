/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */

----------------------------------------------------------------------
-- DATA TYPE
----------------------------------------------------------------------
CREATE SEQUENCE yucca_d_data_type_id_data_type_seq;
ALTER TABLE yucca_d_data_type ALTER COLUMN id_data_type SET DEFAULT nextval('yucca_d_data_type_id_data_type_seq');
ALTER TABLE yucca_d_data_type ALTER COLUMN id_data_type SET NOT NULL;

ALTER SEQUENCE yucca_d_data_type_id_data_type_seq OWNED BY yucca_d_data_type.id_data_type;
ALTER SEQUENCE yucca_d_data_type_id_data_type_seq RESTART WITH 9806;


----------------------------------------------------------------------
-- PHENOMENON
----------------------------------------------------------------------
CREATE SEQUENCE yucca_d_phenomenon_id_phenomenon_seq;
ALTER TABLE yucca_d_phenomenon ALTER COLUMN id_phenomenon SET DEFAULT nextval('yucca_d_phenomenon_id_phenomenon_seq');
ALTER TABLE yucca_d_phenomenon ALTER COLUMN id_phenomenon SET NOT NULL;

ALTER SEQUENCE yucca_d_phenomenon_id_phenomenon_seq OWNED BY yucca_d_phenomenon.id_phenomenon;
ALTER SEQUENCE yucca_d_phenomenon_id_phenomenon_seq RESTART WITH 9806;


----------------------------------------------------------------------
-- MEASURE UNIT
----------------------------------------------------------------------
CREATE SEQUENCE yucca_d_measure_unit_id_measure_unit_seq;
ALTER TABLE yucca_d_measure_unit ALTER COLUMN id_measure_unit SET DEFAULT nextval('yucca_d_measure_unit_id_measure_unit_seq');
ALTER TABLE yucca_d_measure_unit ALTER COLUMN id_measure_unit SET NOT NULL;

ALTER SEQUENCE yucca_d_measure_unit_id_measure_unit_seq OWNED BY yucca_d_measure_unit.id_measure_unit;
ALTER SEQUENCE yucca_d_measure_unit_id_measure_unit_seq RESTART WITH 9806;


----------------------------------------------------------------------
-- SUBDOMAIN
----------------------------------------------------------------------
CREATE SEQUENCE yucca_d_subdomain_id_subdomain_seq;
ALTER TABLE yucca_d_subdomain ALTER COLUMN id_subdomain SET DEFAULT nextval('yucca_d_subdomain_id_subdomain_seq');
ALTER TABLE yucca_d_subdomain ALTER COLUMN id_subdomain SET NOT NULL;

ALTER SEQUENCE yucca_d_subdomain_id_subdomain_seq OWNED BY yucca_d_subdomain.id_subdomain;
ALTER SEQUENCE yucca_d_subdomain_id_subdomain_seq RESTART WITH 9806;

---------------------------------------------------------------------- 
-- AGGIUNGERE CHIAVE UNIVOCA PER subdomaincode NELLA TABELLA SUBDOMAIN.
----------------------------------------------------------------------
ALTER TABLE yucca_d_subdomain
ADD CONSTRAINT yucca_d_subdomain_subdomaincode_key UNIQUE(subdomaincode);


----------------------------------------------------------------------
-- LICENSE
----------------------------------------------------------------------
CREATE SEQUENCE yucca_d_license_id_license_seq;
ALTER TABLE yucca_d_license ALTER COLUMN id_license SET DEFAULT nextval('yucca_d_license_id_license_seq');
ALTER TABLE yucca_d_license ALTER COLUMN id_license SET NOT NULL;

ALTER SEQUENCE yucca_d_license_id_license_seq OWNED BY yucca_d_license.id_license;
ALTER SEQUENCE yucca_d_license_id_license_seq RESTART WITH 9806;


----------------------------------------------------------------------
-- DOMAIN
----------------------------------------------------------------------
CREATE SEQUENCE yucca_d_domain_id_domain_seq;
ALTER TABLE yucca_d_domain ALTER COLUMN id_domain SET DEFAULT nextval('yucca_d_domain_id_domain_seq');
ALTER TABLE yucca_d_domain ALTER COLUMN id_domain SET NOT NULL;

ALTER SEQUENCE yucca_d_domain_id_domain_seq OWNED BY yucca_d_domain.id_domain;
ALTER SEQUENCE yucca_d_domain_id_domain_seq RESTART WITH 9806;

----------------------------------------------------------------------
-- SMART OBJECT
----------------------------------------------------------------------
CREATE SEQUENCE yucca_smart_object_id_smart_object_seq;
ALTER TABLE yucca_smart_object ALTER COLUMN id_smart_object SET DEFAULT nextval('yucca_smart_object_id_smart_object_seq');
ALTER TABLE yucca_smart_object ALTER COLUMN id_smart_object SET NOT NULL;

ALTER SEQUENCE yucca_smart_object_id_smart_object_seq OWNED BY yucca_smart_object.id_smart_object; -- 8.2 or later
ALTER SEQUENCE yucca_smart_object_id_smart_object_seq RESTART WITH 9806;


 

CREATE SEQUENCE yucca_seq_personaltenants_progressivo;
CREATE SEQUENCE yucca_seq_trialtenants_progressivo;


----------------------------------------------------------------------
-- API
----------------------------------------------------------------------
CREATE SEQUENCE yucca_api_id_api_seq;
ALTER TABLE yucca_api ALTER COLUMN idapi SET DEFAULT nextval('yucca_api_id_api_seq');



CREATE SEQUENCE dcat_id_dcat_seq; 
ALTER TABLE yucca_dcat ALTER COLUMN id_dcat SET DEFAULT nextval('dcat_id_dcat_seq'); 
ALTER TABLE yucca_dcat ALTER COLUMN id_dcat SET NOT NULL; 
ALTER SEQUENCE dcat_id_dcat_seq RESTART WITH 2500;


CREATE SEQUENCE dataset_iddataset_seq; 
ALTER TABLE yucca_dataset ALTER COLUMN iddataset SET DEFAULT nextval('dataset_iddataset_seq'); 
ALTER TABLE yucca_dataset ALTER COLUMN iddataset SET NOT NULL; 
ALTER SEQUENCE dataset_iddataset_seq OWNED BY yucca_dataset.iddataset; 
ALTER SEQUENCE dataset_iddataset_seq RESTART WITH 3000;


CREATE SEQUENCE stream_idstream_seq;
ALTER TABLE yucca_stream ALTER COLUMN idstream SET DEFAULT nextval('stream_idstream_seq');
ALTER TABLE yucca_stream ALTER COLUMN idstream SET NOT NULL;
ALTER SEQUENCE stream_idstream_seq OWNED BY yucca_stream.idstream; 
ALTER SEQUENCE stream_idstream_seq RESTART WITH 3000;
