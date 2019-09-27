/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */

/* ---------------------------------------------------------------------- */
/* Yucca tools                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE yucca_tools_id_tool_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 12
  CACHE 1;
ALTER TABLE yucca_tools_id_tool_seq
  OWNER TO yucca;

CREATE TABLE yucca_tool
(
  id_tool integer NOT NULL DEFAULT nextval('yucca_tools_id_tool_seq'::regclass),
  toolversion character varying NOT NULL,
  name character varying NOT NULL,
  CONSTRAINT pk_yucca_tool PRIMARY KEY (id_tool)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE yucca_r_bundles_tool
(
  id_tool bigint NOT NULL,
  id_bundles bigint NOT NULL,
  enabled boolean DEFAULT false,
  CONSTRAINT yucca_bundles_yucca_r_bundles_tool FOREIGN KEY (id_bundles)
      REFERENCES yucca_bundles (id_bundles) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yucca_tool_yucca_r_bundles_tool FOREIGN KEY (id_tool)
      REFERENCES yucca_tool (id_tool) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE yucca_r_bundles_tool
  ADD CONSTRAINT yucca_bundles_yucca_r_bundles_tool FOREIGN KEY (id_bundles)
      REFERENCES yucca_bundles (id_bundles) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yucca_r_bundles_tool
  ADD CONSTRAINT yucca_tool_yucca_r_bundles_tool FOREIGN KEY (id_tool)
      REFERENCES yucca_tool (id_tool) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
  



/* ---------------------------------------------------------------------- */
/* Yucca datasource group                                        */
/* ---------------------------------------------------------------------- */
CREATE TABLE yucca_d_datasourcegroup_type
(
  id_datasourcegroup_type integer NOT NULL,
  name character varying(250),
  description character varying(250),
  CONSTRAINT pk_yucca_d_datasourcegroup_type PRIMARY KEY (id_datasourcegroup_type)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE yucca_datasourcegroup
(
  id_datasourcegroup serial NOT NULL,
  datasourcegroupversion integer NOT NULL,
  name character varying(250) NOT NULL,
  id_datasourcegroup_type integer NOT NULL,
  id_tenant bigint NOT NULL,
  color character varying(50) NOT NULL,
  status character varying(50) NOT NULL,
  CONSTRAINT pk_datasourcegroup PRIMARY KEY (id_datasourcegroup, datasourcegroupversion),
  CONSTRAINT yucca_d_datasourcegroup_type_yucca_datasourcegroup FOREIGN KEY (id_datasourcegroup_type)
      REFERENCES yucca_d_datasourcegroup_type (id_datasourcegroup_type) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yucca_tenant_yucca_datasourcegroup FOREIGN KEY (id_tenant)
      REFERENCES yucca_tenant (id_tenant) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE yucca_r_datasource_datasourcegroup
(
  id_datasourcegroup integer NOT NULL,
  datasourcegroupversion integer NOT NULL,
  id_data_source integer NOT NULL,
  datasourceversion integer NOT NULL,
  CONSTRAINT pk_datasource_datasourcegroup PRIMARY KEY (id_datasourcegroup, datasourcegroupversion, datasourceversion, id_data_source),
  CONSTRAINT fk_datasource_r_datasource_datasourcegroup FOREIGN KEY (id_data_source, datasourceversion)
      REFERENCES yucca_data_source (id_data_source, datasourceversion) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_datasourcegroup_r_datasource_datasourcegroup FOREIGN KEY (id_datasourcegroup, datasourcegroupversion)
      REFERENCES yucca_datasourcegroup (id_datasourcegroup, datasourcegroupversion) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


/* ---------------------------------------------------------------------- */
/* Yucca riferimenti tenant                    */
/* ---------------------------------------------------------------------- */

CREATE TABLE yucca_r_tenant_product
(
  id_tenant bigint NOT NULL,
  productcode character varying(50) NOT NULL,
  CONSTRAINT yucca_r_tenant_product_yucca_tenant FOREIGN KEY (id_tenant)
      REFERENCES yucca_tenant (id_tenant) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


CREATE TABLE yucca_r_tenant_contact
(
  id_tenant bigint NOT NULL,
  name character varying(100) NOT NULL,
  email character varying(100),
  contactrole character varying(100),
  productcode character varying(50),
  CONSTRAINT yucca_r_tenant_contact_yucca_tenant FOREIGN KEY (id_tenant)
      REFERENCES yucca_tenant (id_tenant) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
