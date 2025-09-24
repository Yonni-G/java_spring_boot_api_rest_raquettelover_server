--
-- PostgreSQL database dump
--



-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.6

-- Started on 2025-09-24 16:07:58

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.courts DROP CONSTRAINT IF EXISTS fkst4xmub7kax4ut3uja8xor3ag;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS fko0lyr18alid21rkl0ay7y985k;
ALTER TABLE IF EXISTS ONLY public.users_places DROP CONSTRAINT IF EXISTS fkn3fuuy7uo1m8xrn9n87mohe4t;
ALTER TABLE IF EXISTS ONLY public.users_roles DROP CONSTRAINT IF EXISTS fkj6m8fwv7oqv74fcehir1a9ffy;
ALTER TABLE IF EXISTS ONLY public.reservations DROP CONSTRAINT IF EXISTS fkfhyeea492li3rmcfehysgcphf;
ALTER TABLE IF EXISTS ONLY public.users_places DROP CONSTRAINT IF EXISTS fkddoqy9fk6pjkehrvgrj823e5;
ALTER TABLE IF EXISTS ONLY public.reservations DROP CONSTRAINT IF EXISTS fkb5g9io5h54iwl2inkno50ppln;
ALTER TABLE IF EXISTS ONLY public.participations DROP CONSTRAINT IF EXISTS fk9uu5l63nqnclum0c4s10wal5j;
ALTER TABLE IF EXISTS ONLY public.users_roles DROP CONSTRAINT IF EXISTS fk2o0jvgh89lemvvo17cbqvdxaa;
ALTER TABLE IF EXISTS ONLY public.participations DROP CONSTRAINT IF EXISTS fk166yf958qjqf8uoslyuk9e19p;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_username_key;
ALTER TABLE IF EXISTS ONLY public.users_roles DROP CONSTRAINT IF EXISTS users_roles_pkey;
ALTER TABLE IF EXISTS ONLY public.users_places DROP CONSTRAINT IF EXISTS users_places_pkey;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_pkey;
ALTER TABLE IF EXISTS ONLY public.places DROP CONSTRAINT IF EXISTS unique_code_lieu;
ALTER TABLE IF EXISTS ONLY public.roles DROP CONSTRAINT IF EXISTS roles_pkey;
ALTER TABLE IF EXISTS ONLY public.roles DROP CONSTRAINT IF EXISTS roles_name_key;
ALTER TABLE IF EXISTS ONLY public.reservations DROP CONSTRAINT IF EXISTS reservations_pkey;
ALTER TABLE IF EXISTS ONLY public.places DROP CONSTRAINT IF EXISTS places_pkey;
ALTER TABLE IF EXISTS ONLY public.participations DROP CONSTRAINT IF EXISTS participations_pkey;
ALTER TABLE IF EXISTS ONLY public.courts DROP CONSTRAINT IF EXISTS courts_pkey;
DROP SEQUENCE IF EXISTS public.users_seq;
DROP TABLE IF EXISTS public.users_roles;
DROP TABLE IF EXISTS public.users_places;
DROP TABLE IF EXISTS public.users;
DROP SEQUENCE IF EXISTS public.roles_seq;
DROP TABLE IF EXISTS public.roles;
DROP SEQUENCE IF EXISTS public.reservations_seq;
DROP TABLE IF EXISTS public.reservations;
DROP SEQUENCE IF EXISTS public.places_seq;
DROP TABLE IF EXISTS public.places;
DROP SEQUENCE IF EXISTS public.participations_seq;
DROP TABLE IF EXISTS public.participations;
DROP SEQUENCE IF EXISTS public.courts_seq;
DROP TABLE IF EXISTS public.courts;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 223 (class 1259 OID 18074)
-- Name: courts; Type: TABLE; Schema: public; Owner: raquettelover
--

CREATE TABLE public.courts (
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    place_id bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    name character varying(100) NOT NULL,
    description character varying(500),
    type character varying(255) NOT NULL,
    CONSTRAINT courts_type_check CHECK (((type)::text = ANY ((ARRAY['PADEL'::character varying, 'SQUASH'::character varying, 'TENNIS'::character varying])::text[])))
);


ALTER TABLE public.courts OWNER TO raquettelover;

--
-- TOC entry 217 (class 1259 OID 18068)
-- Name: courts_seq; Type: SEQUENCE; Schema: public; Owner: raquettelover
--

CREATE SEQUENCE public.courts_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.courts_seq OWNER TO raquettelover;

--
-- TOC entry 224 (class 1259 OID 18082)
-- Name: participations; Type: TABLE; Schema: public; Owner: raquettelover
--

CREATE TABLE public.participations (
    is_guest boolean NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    reservation_id bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    user_id bigint NOT NULL,
    phone_number character varying(10),
    first_name character varying(50),
    email character varying(255)
);


ALTER TABLE public.participations OWNER TO raquettelover;

--
-- TOC entry 218 (class 1259 OID 18069)
-- Name: participations_seq; Type: SEQUENCE; Schema: public; Owner: raquettelover
--

CREATE SEQUENCE public.participations_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.participations_seq OWNER TO raquettelover;

--
-- TOC entry 225 (class 1259 OID 18087)
-- Name: places; Type: TABLE; Schema: public; Owner: raquettelover
--

CREATE TABLE public.places (
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    address character varying(255) NOT NULL,
    code_lieu character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.places OWNER TO raquettelover;

--
-- TOC entry 219 (class 1259 OID 18070)
-- Name: places_seq; Type: SEQUENCE; Schema: public; Owner: raquettelover
--

CREATE SEQUENCE public.places_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.places_seq OWNER TO raquettelover;

--
-- TOC entry 226 (class 1259 OID 18094)
-- Name: reservations; Type: TABLE; Schema: public; Owner: raquettelover
--

CREATE TABLE public.reservations (
    duration integer NOT NULL,
    reservation_at date NOT NULL,
    start_hour integer NOT NULL,
    court_id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    user_id bigint NOT NULL,
    CONSTRAINT reservations_duration_check CHECK (((duration <= 24) AND (duration >= 1))),
    CONSTRAINT reservations_start_hour_check CHECK (((start_hour >= 0) AND (start_hour <= 23)))
);


ALTER TABLE public.reservations OWNER TO raquettelover;

--
-- TOC entry 220 (class 1259 OID 18071)
-- Name: reservations_seq; Type: SEQUENCE; Schema: public; Owner: raquettelover
--

CREATE SEQUENCE public.reservations_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reservations_seq OWNER TO raquettelover;

--
-- TOC entry 227 (class 1259 OID 18101)
-- Name: roles; Type: TABLE; Schema: public; Owner: raquettelover
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.roles OWNER TO raquettelover;

--
-- TOC entry 221 (class 1259 OID 18072)
-- Name: roles_seq; Type: SEQUENCE; Schema: public; Owner: raquettelover
--

CREATE SEQUENCE public.roles_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_seq OWNER TO raquettelover;

--
-- TOC entry 228 (class 1259 OID 18108)
-- Name: users; Type: TABLE; Schema: public; Owner: raquettelover
--

CREATE TABLE public.users (
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    password character varying(255),
    username character varying(255),
    place bigint,
    place_id bigint
);


ALTER TABLE public.users OWNER TO raquettelover;

--
-- TOC entry 229 (class 1259 OID 18117)
-- Name: users_places; Type: TABLE; Schema: public; Owner: raquettelover
--

CREATE TABLE public.users_places (
    created_at timestamp(6) without time zone NOT NULL,
    place_id bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    user_id bigint NOT NULL
);


ALTER TABLE public.users_places OWNER TO raquettelover;

--
-- TOC entry 230 (class 1259 OID 18122)
-- Name: users_roles; Type: TABLE; Schema: public; Owner: raquettelover
--

CREATE TABLE public.users_roles (
    role_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.users_roles OWNER TO raquettelover;

--
-- TOC entry 222 (class 1259 OID 18073)
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: raquettelover
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_seq OWNER TO raquettelover;

--
-- TOC entry 4860 (class 0 OID 18074)
-- Dependencies: 223
-- Data for Name: courts; Type: TABLE DATA; Schema: public; Owner: raquettelover
--

INSERT INTO public.courts VALUES ('2025-09-24 13:41:56.55632', 202, 3, '2025-09-24 13:41:56.55632', 'Court de Padel 1', 'C''est le court le plus près de la porte', 'PADEL');
INSERT INTO public.courts VALUES ('2025-09-24 13:42:24.301238', 203, 3, '2025-09-24 13:42:24.301238', 'Court de Padel 2', 'C''est le court au fond de la salle', 'PADEL');
INSERT INTO public.courts VALUES ('2025-09-24 13:43:50.163163', 204, 3, '2025-09-24 13:43:50.163163', 'Court de Squash', 'Il se situe à droite de l''entrée principale', 'SQUASH');
INSERT INTO public.courts VALUES ('2025-09-24 13:44:29.96469', 205, 3, '2025-09-24 13:44:29.96469', 'Court de Tennis A', 'Court à l''extérieur', 'TENNIS');


--
-- TOC entry 4861 (class 0 OID 18082)
-- Dependencies: 224
-- Data for Name: participations; Type: TABLE DATA; Schema: public; Owner: raquettelover
--



--
-- TOC entry 4862 (class 0 OID 18087)
-- Dependencies: 225
-- Data for Name: places; Type: TABLE DATA; Schema: public; Owner: raquettelover
--

INSERT INTO public.places VALUES ('2025-09-22 15:26:26.980191', 3, '2025-09-24 13:05:39.428305', '1 Rue Suzanne Lenglen, 44470 Carquefou', 'artlignefitness', 'Artligne Fitness');


--
-- TOC entry 4863 (class 0 OID 18094)
-- Dependencies: 226
-- Data for Name: reservations; Type: TABLE DATA; Schema: public; Owner: raquettelover
--



--
-- TOC entry 4864 (class 0 OID 18101)
-- Dependencies: 227
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: raquettelover
--

INSERT INTO public.roles VALUES (1, 'ROLE_USER');
INSERT INTO public.roles VALUES (2, 'ROLE_MANAGER');
INSERT INTO public.roles VALUES (3, 'ROLE_ADMIN');


--
-- TOC entry 4865 (class 0 OID 18108)
-- Dependencies: 228
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: raquettelover
--

INSERT INTO public.users VALUES ('2025-09-11 10:33:51.948147', 1, '2025-09-11 10:33:51.948147', 'admin', 'nom', '$2a$10$INlHBMMzt2Cj/fIEJssTOeRZUKKBMcB.HannyvGW4YujFIFQvdvR2', 'admin@gmail.com', NULL, NULL);
INSERT INTO public.users VALUES ('2025-09-11 10:34:26.396594', 2, '2025-09-11 10:34:26.396594', 'manager', 'nom', '$2a$10$0iRAeJbxvZfVMczjPBjTLOUDcKfQ3grrii4HBOIrDuf9nGQ5c33By', 'manager@gmail.com', NULL, NULL);
INSERT INTO public.users VALUES ('2025-09-11 10:34:50.522709', 3, '2025-09-11 10:34:50.522709', 'player', 'nom', '$2a$10$LGHSBfHicjdUTmrkgYpUFO8EBaGFy8v65xB5JlhOag9IZSwmHaSNC', 'player@gmail.com', NULL, NULL);
INSERT INTO public.users VALUES ('2025-09-22 12:39:47.817858', 52, '2025-09-22 12:39:47.817858', 'Guerrault', 'Yonni', '$2a$10$uViG4B1bfPZZzTdyt.N8duJJ1AG0gNZMBTkM7T6tjieqoquybFWi2', 'yonni4@gmail.com', NULL, NULL);
INSERT INTO public.users VALUES ('2025-09-22 13:40:50.52526', 102, '2025-09-22 13:40:50.52526', 'Guerrault', 'Yonni', '$2a$10$BEDWrPeROFU7NH.0QVi8K.IaHfK3qI6.MgSR0TOjFoRU9BkbEIipa', 'player2@gmail.com', NULL, NULL);
INSERT INTO public.users VALUES ('2025-09-22 14:31:53.187671', 153, '2025-09-22 14:31:53.187671', 'Guerrault', 'Yonni', '$2a$10$V299AXzyhZnnlWvvDonPauLQRnzyETiTvS45.TEq3TCuMNAMikyRW', 'maneger2@gmail.com', NULL, NULL);
INSERT INTO public.users VALUES ('2025-09-22 14:47:00.821611', 203, '2025-09-22 14:47:00.821611', 'Guerrault', 'Yonni', '$2a$10$qycoqC8p64wZnIG1Xjlr9uwvqDlBBc14Xnubpix.YxvcebW.Shbwm', 'admin88@gmail.com', NULL, NULL);


--
-- TOC entry 4866 (class 0 OID 18117)
-- Dependencies: 229
-- Data for Name: users_places; Type: TABLE DATA; Schema: public; Owner: raquettelover
--

INSERT INTO public.users_places VALUES ('2025-09-22 15:26:26.988072', 3, '2025-09-22 15:26:26.988072', 2);
INSERT INTO public.users_places VALUES ('2025-09-24 13:05:39.464866', 3, '2025-09-24 13:05:39.464866', 1);


--
-- TOC entry 4867 (class 0 OID 18122)
-- Dependencies: 230
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: raquettelover
--

INSERT INTO public.users_roles VALUES (1, 1);
INSERT INTO public.users_roles VALUES (3, 1);
INSERT INTO public.users_roles VALUES (2, 2);
INSERT INTO public.users_roles VALUES (1, 2);
INSERT INTO public.users_roles VALUES (1, 3);
INSERT INTO public.users_roles VALUES (1, 52);
INSERT INTO public.users_roles VALUES (1, 102);
INSERT INTO public.users_roles VALUES (2, 153);
INSERT INTO public.users_roles VALUES (1, 153);
INSERT INTO public.users_roles VALUES (2, 203);
INSERT INTO public.users_roles VALUES (1, 203);


--
-- TOC entry 4873 (class 0 OID 0)
-- Dependencies: 217
-- Name: courts_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.courts_seq', 251, true);


--
-- TOC entry 4874 (class 0 OID 0)
-- Dependencies: 218
-- Name: participations_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.participations_seq', 1, false);


--
-- TOC entry 4875 (class 0 OID 0)
-- Dependencies: 219
-- Name: places_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.places_seq', 51, true);


--
-- TOC entry 4876 (class 0 OID 0)
-- Dependencies: 220
-- Name: reservations_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.reservations_seq', 1, false);


--
-- TOC entry 4877 (class 0 OID 0)
-- Dependencies: 221
-- Name: roles_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.roles_seq', 1, false);


--
-- TOC entry 4878 (class 0 OID 0)
-- Dependencies: 222
-- Name: users_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.users_seq', 301, true);


--
-- TOC entry 4678 (class 2606 OID 18081)
-- Name: courts courts_pkey; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.courts
    ADD CONSTRAINT courts_pkey PRIMARY KEY (id);


--
-- TOC entry 4680 (class 2606 OID 18086)
-- Name: participations participations_pkey; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.participations
    ADD CONSTRAINT participations_pkey PRIMARY KEY (id);


--
-- TOC entry 4682 (class 2606 OID 18093)
-- Name: places places_pkey; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.places
    ADD CONSTRAINT places_pkey PRIMARY KEY (id);


--
-- TOC entry 4686 (class 2606 OID 18100)
-- Name: reservations reservations_pkey; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_pkey PRIMARY KEY (id);


--
-- TOC entry 4688 (class 2606 OID 18107)
-- Name: roles roles_name_key; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_name_key UNIQUE (name);


--
-- TOC entry 4690 (class 2606 OID 18105)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 4684 (class 2606 OID 24582)
-- Name: places unique_code_lieu; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.places
    ADD CONSTRAINT unique_code_lieu UNIQUE (code_lieu);


--
-- TOC entry 4692 (class 2606 OID 18114)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4696 (class 2606 OID 18121)
-- Name: users_places users_places_pkey; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.users_places
    ADD CONSTRAINT users_places_pkey PRIMARY KEY (place_id, user_id);


--
-- TOC entry 4698 (class 2606 OID 18126)
-- Name: users_roles users_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (role_id, user_id);


--
-- TOC entry 4694 (class 2606 OID 18116)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 4700 (class 2606 OID 18137)
-- Name: participations fk166yf958qjqf8uoslyuk9e19p; Type: FK CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.participations
    ADD CONSTRAINT fk166yf958qjqf8uoslyuk9e19p FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4707 (class 2606 OID 18167)
-- Name: users_roles fk2o0jvgh89lemvvo17cbqvdxaa; Type: FK CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4701 (class 2606 OID 18132)
-- Name: participations fk9uu5l63nqnclum0c4s10wal5j; Type: FK CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.participations
    ADD CONSTRAINT fk9uu5l63nqnclum0c4s10wal5j FOREIGN KEY (reservation_id) REFERENCES public.reservations(id);


--
-- TOC entry 4702 (class 2606 OID 18147)
-- Name: reservations fkb5g9io5h54iwl2inkno50ppln; Type: FK CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT fkb5g9io5h54iwl2inkno50ppln FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4705 (class 2606 OID 18157)
-- Name: users_places fkddoqy9fk6pjkehrvgrj823e5; Type: FK CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.users_places
    ADD CONSTRAINT fkddoqy9fk6pjkehrvgrj823e5 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4703 (class 2606 OID 18142)
-- Name: reservations fkfhyeea492li3rmcfehysgcphf; Type: FK CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT fkfhyeea492li3rmcfehysgcphf FOREIGN KEY (court_id) REFERENCES public.courts(id);


--
-- TOC entry 4708 (class 2606 OID 18162)
-- Name: users_roles fkj6m8fwv7oqv74fcehir1a9ffy; Type: FK CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fkj6m8fwv7oqv74fcehir1a9ffy FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- TOC entry 4706 (class 2606 OID 18152)
-- Name: users_places fkn3fuuy7uo1m8xrn9n87mohe4t; Type: FK CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.users_places
    ADD CONSTRAINT fkn3fuuy7uo1m8xrn9n87mohe4t FOREIGN KEY (place_id) REFERENCES public.places(id);


--
-- TOC entry 4704 (class 2606 OID 24576)
-- Name: users fko0lyr18alid21rkl0ay7y985k; Type: FK CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fko0lyr18alid21rkl0ay7y985k FOREIGN KEY (place_id) REFERENCES public.places(id);


--
-- TOC entry 4699 (class 2606 OID 18127)
-- Name: courts fkst4xmub7kax4ut3uja8xor3ag; Type: FK CONSTRAINT; Schema: public; Owner: raquettelover
--

ALTER TABLE ONLY public.courts
    ADD CONSTRAINT fkst4xmub7kax4ut3uja8xor3ag FOREIGN KEY (place_id) REFERENCES public.places(id);


-- Completed on 2025-09-24 16:07:58

--
-- PostgreSQL database dump complete
--



