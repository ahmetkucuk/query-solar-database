DROP EXTENSION IF EXISTS PostGIS;
CREATE SCHEMA postgis;
CREATE EXTENSION PostGIS WITH SCHEMA postgis;
GRANT ALL ON postgis.geometry_columns TO PUBLIC;
GRANT ALL ON postgis.spatial_ref_sys TO PUBLIC;

VACUUM ANALYZE ge(hpc_bbox);

CREATE TABLE public.spatial_ref_sys
(
  srid integer NOT NULL,
  auth_name character varying(256),
  auth_srid integer,
  srtext character varying(2048),
  proj4text character varying(2048),
  CONSTRAINT spatial_ref_sys_pkey PRIMARY KEY (srid),
  CONSTRAINT spatial_ref_sys_srid_check CHECK (srid > 0 AND srid <= 998999)
);