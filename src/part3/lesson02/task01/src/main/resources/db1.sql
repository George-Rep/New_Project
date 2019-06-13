--
-- PostgreSQL database dump
--

-- Dumped from database version 10.8
-- Dumped by pg_dump version 10.8

-- Started on 2019-06-13 15:37:17

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12924)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2816 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 597 (class 1247 OID 16416)
-- Name: rolename; Type: TYPE; Schema: public; Owner: root
--

CREATE TYPE public.rolename AS ENUM (
    'Administration',
    'Clients',
    'Billing'
);


ALTER TYPE public.rolename OWNER TO root;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 197 (class 1259 OID 16403)
-- Name: ROLE; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public."ROLE" (
    id bigint,
    name public.rolename,
    description character varying(255)
);


ALTER TABLE public."ROLE" OWNER TO root;

--
-- TOC entry 196 (class 1259 OID 16395)
-- Name: USER; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public."USER" (
    id bigint NOT NULL,
    name character varying(255),
    birthday date,
    "login_ID" character varying(255),
    city character varying(255),
    email character varying(255),
    description character varying(255)
);


ALTER TABLE public."USER" OWNER TO root;

--
-- TOC entry 198 (class 1259 OID 16406)
-- Name: USER_ROLE; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public."USER_ROLE" (
    id bigint,
    user_id bigint,
    role_id bigint
);


ALTER TABLE public."USER_ROLE" OWNER TO root;

--
-- TOC entry 2806 (class 0 OID 16403)
-- Dependencies: 197
-- Data for Name: ROLE; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public."ROLE" (id, name, description) FROM stdin;
\.


--
-- TOC entry 2805 (class 0 OID 16395)
-- Dependencies: 196
-- Data for Name: USER; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public."USER" (id, name, birthday, "login_ID", city, email, description) FROM stdin;
\.


--
-- TOC entry 2807 (class 0 OID 16406)
-- Dependencies: 198
-- Data for Name: USER_ROLE; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public."USER_ROLE" (id, user_id, role_id) FROM stdin;
\.


--
-- TOC entry 2683 (class 2606 OID 16402)
-- Name: USER USER_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public."USER"
    ADD CONSTRAINT "USER_pkey" PRIMARY KEY (id);


--
-- TOC entry 2815 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO root;


-- Completed on 2019-06-13 15:37:17

--
-- PostgreSQL database dump complete
--

