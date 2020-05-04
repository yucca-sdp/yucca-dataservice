/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */

--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.7
-- Dumped by pg_dump version 9.5.5

-- Started on 2019-05-15 09:52:05

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = yucca, pg_catalog;



--
-- TOC entry 3512 (class 0 OID 78689)
-- Dependencies: 262
-- Data for Name: yucca_ecosystem; Type: TABLE DATA; Schema: yucca; Owner: yucca
--

INSERT INTO yucca_ecosystem VALUES (1, 'SDNET', 'SDNET');


--
-- TOC entry 3526 (class 0 OID 0)
-- Dependencies: 261
-- Name: yucca_ecosystem_id_ecosystem_seq; Type: SEQUENCE SET; Schema: yucca; Owner: yucca
--

SELECT pg_catalog.setval('yucca_ecosystem_id_ecosystem_seq', 2, false);


--
-- TOC entry 3513 (class 0 OID 78783)
-- Dependencies: 274
-- Data for Name: yucca_r_ecosystem_domain; Type: TABLE DATA; Schema: yucca; Owner: yucca
--

INSERT INTO yucca_r_ecosystem_domain VALUES (1, 1);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 2);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 3);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 4);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 5);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 6);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 7);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 8);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 9);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 10);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 11);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 12);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 13);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 14);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 15);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 16);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 17);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, 18);
INSERT INTO yucca_r_ecosystem_domain VALUES (1, -1);


--
-- TOC entry 3517 (class 0 OID 92760)
-- Dependencies: 308
-- Data for Name: yucca_tool; Type: TABLE DATA; Schema: yucca; Owner: yucca
--

INSERT INTO yucca_tool VALUES (1, '0.7.3', 'zeppelin');
INSERT INTO yucca_tool VALUES (2, '0.28.1', 'superset');
INSERT INTO yucca_tool VALUES (3, '0.8.1', 'zeppelin');


--
-- TOC entry 3527 (class 0 OID 0)
-- Dependencies: 307
-- Name: yucca_tool_id_tool_seq; Type: SEQUENCE SET; Schema: yucca; Owner: yucca
--

SELECT pg_catalog.setval('yucca_tool_id_tool_seq', 3, true);


-- Completed on 2019-05-15 09:52:06

--
-- PostgreSQL database dump complete
--

