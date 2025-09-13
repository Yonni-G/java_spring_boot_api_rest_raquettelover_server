
-- TOC entry 4957 (class 0 OID 18886)
-- Dependencies: 225
-- Data for Name: places; Type: TABLE DATA; Schema: public; Owner: raquettelover
--



--
-- TOC entry 4955 (class 0 OID 18873)
-- Dependencies: 223
-- Data for Name: courts; Type: TABLE DATA; Schema: public; Owner: raquettelover
--



--
-- TOC entry 4960 (class 0 OID 18907)
-- Dependencies: 228
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: raquettelover
--

INSERT INTO public.users VALUES ('2025-09-11 10:33:51.948147', 1, '2025-09-11 10:33:51.948147', 'admin', 'nom', '$2a$10$INlHBMMzt2Cj/fIEJssTOeRZUKKBMcB.HannyvGW4YujFIFQvdvR2', 'admin@gmail.com');
INSERT INTO public.users VALUES ('2025-09-11 10:34:26.396594', 2, '2025-09-11 10:34:26.396594', 'manager', 'nom', '$2a$10$0iRAeJbxvZfVMczjPBjTLOUDcKfQ3grrii4HBOIrDuf9nGQ5c33By', 'manager@gmail.com');
INSERT INTO public.users VALUES ('2025-09-11 10:34:50.522709', 3, '2025-09-11 10:34:50.522709', 'player', 'nom', '$2a$10$LGHSBfHicjdUTmrkgYpUFO8EBaGFy8v65xB5JlhOag9IZSwmHaSNC', 'player@gmail.com');


--
-- TOC entry 4958 (class 0 OID 18893)
-- Dependencies: 226
-- Data for Name: reservations; Type: TABLE DATA; Schema: public; Owner: raquettelover
--



--
-- TOC entry 4956 (class 0 OID 18881)
-- Dependencies: 224
-- Data for Name: participations; Type: TABLE DATA; Schema: public; Owner: raquettelover
--



--
-- TOC entry 4959 (class 0 OID 18900)
-- Dependencies: 227
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: raquettelover
--

INSERT INTO public.roles VALUES (1, 'ROLE_USER');
INSERT INTO public.roles VALUES (2, 'ROLE_MANAGER');
INSERT INTO public.roles VALUES (3, 'ROLE_ADMIN');


--
-- TOC entry 4961 (class 0 OID 18916)
-- Dependencies: 229
-- Data for Name: users_places; Type: TABLE DATA; Schema: public; Owner: raquettelover
--



--
-- TOC entry 4962 (class 0 OID 18921)
-- Dependencies: 230
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: raquettelover
--

INSERT INTO public.users_roles VALUES (1, 1);
INSERT INTO public.users_roles VALUES (3, 1);
INSERT INTO public.users_roles VALUES (2, 2);
INSERT INTO public.users_roles VALUES (1, 2);
INSERT INTO public.users_roles VALUES (1, 3);


--
-- TOC entry 4968 (class 0 OID 0)
-- Dependencies: 217
-- Name: courts_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.courts_seq', 1, false);


--
-- TOC entry 4969 (class 0 OID 0)
-- Dependencies: 218
-- Name: participations_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.participations_seq', 1, false);


--
-- TOC entry 4970 (class 0 OID 0)
-- Dependencies: 219
-- Name: places_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.places_seq', 1, false);


--
-- TOC entry 4971 (class 0 OID 0)
-- Dependencies: 220
-- Name: reservations_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.reservations_seq', 1, false);


--
-- TOC entry 4972 (class 0 OID 0)
-- Dependencies: 221
-- Name: roles_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.roles_seq', 1, false);


--
-- TOC entry 4973 (class 0 OID 0)
-- Dependencies: 222
-- Name: users_seq; Type: SEQUENCE SET; Schema: public; Owner: raquettelover
--

SELECT pg_catalog.setval('public.users_seq', 51, true);


-- Completed on 2025-09-11 10:36:00

--
-- PostgreSQL database dump complete
--



