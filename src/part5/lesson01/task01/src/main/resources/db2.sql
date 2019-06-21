--
-- PostgreSQL database dump
--

-- Dumped from database version 10.8
-- Dumped by pg_dump version 10.8

-- Started on 2019-06-17 14:40:40

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
-- TOC entry 599 (class 1247 OID 16416)
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
-- TOC entry 199 (class 1259 OID 16507)
-- Name: LOGS; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public."LOGS" (
    "ID" bigint NOT NULL,
    "DATE" timestamp without time zone,
    "LOG_LEVEL" character varying(5),
    "MESSAGE" text,
    "EXCEPTION" text
);


ALTER TABLE public."LOGS" OWNER TO root;

--
-- TOC entry 200 (class 1259 OID 16535)
-- Name: LOGS_ID_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public."LOGS_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."LOGS_ID_seq" OWNER TO root;

--
-- TOC entry 2823 (class 0 OID 0)
-- Dependencies: 200
-- Name: LOGS_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public."LOGS_ID_seq" OWNED BY public."LOGS"."ID";


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
-- TOC entry 2689 (class 2604 OID 16537)
-- Name: LOGS ID; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public."LOGS" ALTER COLUMN "ID" SET DEFAULT nextval('public."LOGS_ID_seq"'::regclass);


--
-- TOC entry 2814 (class 0 OID 16507)
-- Dependencies: 199
-- Data for Name: LOGS; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public."LOGS" ("ID", "DATE", "LOG_LEVEL", "MESSAGE", "EXCEPTION") FROM stdin;
\.


--
-- TOC entry 2812 (class 0 OID 16403)
-- Dependencies: 197
-- Data for Name: ROLE; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public."ROLE" (id, name, description) FROM stdin;
\.


--
-- TOC entry 2811 (class 0 OID 16395)
-- Dependencies: 196
-- Data for Name: USER; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public."USER" (id, name, birthday, "login_ID", city, email, description) FROM stdin;
\.


--
-- TOC entry 2813 (class 0 OID 16406)
-- Dependencies: 198
-- Data for Name: USER_ROLE; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public."USER_ROLE" (id, user_id, role_id) FROM stdin;
\.


--
-- TOC entry 2824 (class 0 OID 0)
-- Dependencies: 200
-- Name: LOGS_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public."LOGS_ID_seq"', 83, true);


--
-- TOC entry 2822 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO root;


-- Completed on 2019-06-17 14:40:40

--
-- PostgreSQL database dump complete
--

