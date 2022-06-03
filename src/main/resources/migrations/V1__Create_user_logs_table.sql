
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

SET default_tablespace = '';

CREATE TABLE IF NOT EXISTS public.user_access_log (
    id bigint NOT NULL,
    date timestamp without time zone,
    ip character varying(255),
    status character varying(255),
    user_agent character varying(255)
);

ALTER TABLE public.user_access_log OWNER TO airs_user;
