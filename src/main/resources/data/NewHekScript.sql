DROP TABLE IF EXISTS is_followed_by;
DROP TABLE IF EXISTS forms;
DROP TABLE IF EXISTS ar;
DROP TABLE IF EXISTS ch;
DROP TABLE IF EXISTS sg;
DROP TABLE IF EXISTS fl;
DROP TABLE IF EXISTS ge;
DROP TABLE IF EXISTS trajectory;
DROP TABLE IF EXISTS FUTURE_EVENT;


CREATE TABLE ge ( 
 kb_archivid text,
  comment_count NUMERIC,
  gs_thumburl text,
  frm_humanflag boolean, 
  event_coordsys text, 
  obs_levelnum text, 
  event_npixels text, 
  gs_imageurl text, 
  ar_polarity text, 
  frm_paramset text,
  ar_mtwilsoncls text, 
  event_starttime TIMESTAMP,
  event_type text, 
  intensmin text,
  sol_standard text, 
  obs_meanwavel text,
  frm_url text,
  noposition boolean, 
  active boolean,
  intensmax text, 
  frm_versionnumber float,
  area_uncert text, 
  hpc_geom text, 
  obs_dataprepurl text,
  chaincodetype text, 
  intensmedian text, 
  obs_channelid text,
  ar_noaaclass text,
  event_clippedspatial text,
  event_avg_rating text, 
  eventtype numeric, 
  intensunit text,
  ref_type_0 text,
  event_mapurl text,
  frm_contact text, 
  ar_penumbracls text, 
  intensmean text, 
  bound_ccstartc1 text,
  area_atdiskcenter text,
  frm_name text, 
  frm_identifier text,
  obs_observatory text,
  event_description text,
  boundbox_c2ur float,
  obs_firstprocessingdate text,
  boundbox_c2ll float, 
  frm_institute text, 
  refs_orig text, 
  ar_mcintoshcls text,
  bound_ccstartc2 text,
  event_maskurl text,
  gs_movieurl text,
  event_score text, 
  event_expires text,
  event_probability text,
  intensvar text, 
  frm_daterun TIMESTAMP,
  event_coordunit text,
  hpc_y float,
  hpc_x float,
  ref_url_0 text,
  ar_numspots text,
  kb_archivdate TIMESTAMP, 
  kb_archivist text,
  intenstotal text,
  intensskew text, 
  obs_includesnrt text,
  rasterscan text, 
  obs_wavelunit text, 
  ar_noaanum text, 
  area_atdiskcenteruncert text,
  boundbox_c1ur text,
  boundbox_c1ll text,
  event_importance_num_ratings text, 
  ar_compactnesscls text,
  event_testflag boolean,
  event_c2error float,
  hrc_r float, 
  hgs_y float,
  obs_title text,
  hgs_x float, 
  hcr_checked boolean,
  frm_specificid text, 
  event_title text, 
  obs_instrument text,
  event_c1error numeric, 
  revision numeric,
  event_endtime TIMESTAMP,
  ref_name_0 text, 
  event_importance text,
  event_coord2 float,
  event_coord3 float,
  event_coord1 float,
  area_raw text, 
  concept text, 
  event_pixelunit text,
  hgc_x float,
  hrc_a float,
  hgc_y float,
  gs_galleryid text,
  ar_zurichcls text,
  bound_ccnsteps text,
  intenskurt text, 
  event_clippedtemporal text,
  rasterscantype text,
  area_unit text, 
  obs_lastprocessingdate text,
  PRIMARY KEY (kb_archivid)
  );


SELECT AddGeometryColumn('ge', 'hgc_bbox',4326,'Polygon',2);
SELECT AddGeometryColumn('ge', 'hgc_coord',4326,'Point',2);

SELECT AddGeometryColumn('ge', 'hgs_bbox',4326,'Polygon',2);
SELECT AddGeometryColumn('ge', 'hgs_coord',4326,'Point',2);

SELECT AddGeometryColumn('ge', 'hpc_bbox',4326,'Polygon',2);
SELECT AddGeometryColumn('ge', 'hpc_coord',4326,'Point',2);

SELECT AddGeometryColumn('ge', 'hrc_bbox',4326,'Polygon',2);
SELECT AddGeometryColumn('ge', 'hrc_coord',4326,'Point',2);


SELECT AddGeometryColumn('ge', 'hgc_boundcc',4326,'Polygon',2);
SELECT AddGeometryColumn('ge', 'hgs_boundcc',4326,'Polygon',2);
SELECT AddGeometryColumn('ge', 'hpc_boundcc',4326,'Polygon',2);
SELECT AddGeometryColumn('ge', 'hrc_boundcc',4326,'Polygon',2);

SELECT AddGeometryColumn('ge', 'bound_chaincode',4326,'Polygon',2);

CREATE INDEX event_type_timestamp_index ON ge (event_starttime, event_type);
CREATE INDEX hpc_coord_index ON ge USING GIST (hpc_coord);

ALTER TABLE ge ADD CONSTRAINT check_time CHECK (event_starttime < event_endtime);
ALTER TABLE ge ADD CONSTRAINT check_duration CHECK (EXTRACT(day FROM (event_endtime - event_starttime))<=6);

CREATE TABLE ar(
  kb_archivid text REFERENCES ge (kb_archivid),
  meanphotoenergydensity text,
  ar_spotarearepruncert text,
  meaninclinationgamma text,
  currenthelicityunit text,
  totalenergydensityunit text,
  meantwistalpha text,
  absnetcurrenthelicity text,
  meancurrenthelicity text,
  highshearareaunit text,
  currentunit text,
  maxmagfieldstrength text,
  totalphotoenergy text,
  ar_spotarearepr text,
  totalphotoenergydensity text,
  ar_sumnegsignedflux text,
  gwillunit text,
  ar_neutrallength text,
  ar_pilcurvature text,
  ar_spotareareprunit text,
  meanshearangle text,
  ref_name_1 text,
  ar_spotarearawuncert text,
  meanenergydensityunit text,
  ref_type_1 text,
  meangradienttotal text,
  ar_sumpossignedflux text,
  meanvertcurrentdensity text,
  meangradientvert text,
  ar_axislength text,
  ar_spotarearaw text,
  unsignedflux text,
  highshearareapercent text,
  magfluxunit text,
  maxmagfieldstrengthunit text,
  sharp_noaa_ars text,
  log_r_value text,
  totalphotoenergyunit text,
  ar_lengthunit text,
  meangradienthorz text,
  twistunit text,
  gwill text,
  ar_spotarearawunit text,
  gradientunit text,
  currentdensityunit text,
  highsheararea text,
  unsignedcurrenthelicity text,
  savncpp text,
  ref_url_1 text,
  unsignedvertcurrent text,
  CONSTRAINT ar_pkey PRIMARY KEY (kb_archivid)
);
CREATE TABLE ch
(
 kb_archivid text REFERENCES ge (kb_archivid),
  skel_chaincode text,
  skel_nsteps text,
  hgs_skeletoncc text,
  skel_startc2 text,
  hrc_skeletoncc text,
  skel_startc1 text,
  hgc_skeletoncc text,
  hpc_skeletoncc text,
  CONSTRAINT ch_pkey PRIMARY KEY (kb_archivid)
);
CREATE TABLE sg
(
 kb_archivid text REFERENCES ge (kb_archivid),
  skel_chaincode text,
  skel_nsteps text,
  skel_startc2 text,
  skel_startc1 text,
  sg_meancontrast text,
  sg_aspectratio text,
  sg_chirality text,
  sg_peakcontrast text,
  ref_name_1 text,
  ref_type_1 text,
  event_peaktime text,
  skel_curvature text,
  ref_url_1 text,
  sg_shape text,
  sg_orientation text,
  CONSTRAINT sg_pkey PRIMARY KEY (kb_archivid)
);
CREATE TABLE fl(
 kb_archivid text REFERENCES ge (kb_archivid),
  fl_peakflux text,
  event_peaktime text,
  skel_curvature text,
  fl_fluence text,
  fl_goescls text,
  skel_chaincode text,
  skel_nsteps text,
  skel_startc2 text,
  fl_peakemunit text,
  skel_startc1 text,
  fl_efoldtime text,
  fl_fluenceunit text,
  fl_efoldtimeunit text,
  fl_peakfluxunit text,
  fl_halphaclass text,
  fl_peaktempunit text,
  fl_peakem text,
  ref_name_3 text,
  ref_name_2 text,
  ref_type_3 text,
  ref_name_1 text,
  ref_type_2 text,
  ref_type_1 text,
  fl_peaktemp text,
  ref_url_2 text,
  ref_url_3 text,
  ref_url_1 text,
  CONSTRAINT fl_pkey PRIMARY KEY (kb_archivid)
);

CREATE TABLE trajectory(
trajectory_id text PRIMARY KEY
);

CREATE TABLE forms(
trajectory_id text REFERENCES trajectory (trajectory_id),
kb_archivid text REFERENCES ge (kb_archivid),
CONSTRAINT forms_pkey PRIMARY KEY (kb_archivid,trajectory_id)
);

Create TABLE future_event(
  fe_id text PRIMARY KEY,
  event_type text not null,
  probability numeric,
  time_of_gen TIMESTAMP,
  gs_thumburl text,
  event_coordsys text, 
  event_npixels text,
  gs_imageurl text, 
  sol_standard text,
  noposition text,
  active text,
  intensmax text, 
  area_uncert text, 
  hpc_geom text,
  chaincodetype text, 
  intensmedian text, 
  ar_noaaclass text,
  event_clippedspatial text,
  eventtype text, 
  intensunit text, 
  hpc_boundcc text, 
  ar_penumbracls text, 
  intensmean text, 
  bound_ccstartc1 text,
  area_atdiskcenter text,
  event_description text, 
  boundbox_c2ur text,
  boundbox_c2ll text, 
  ar_mcintoshcls text,
  bound_ccstartc2 text,
  event_maskurl text,
  event_expires text,
  event_probability text,
  intensvar text, 
  event_coordunit text,
  hpc_y text, hpc_x text,
  ar_numspots text,
  rasterscan text, 
  ar_noaanum text, 
  area_atdiskcenteruncert text,
  boundbox_c1ur text,
  boundbox_c1ll text,
  event_testflag text
);

Create table is_followed_by(
  fe_id text REFERENCES future_event (fe_id),
  trajectory_id text REFERENCES trajectory (trajectory_id),
  CONSTRAINT is_followed_by_pkey PRIMARY KEY (fe_id,trajectory_id)  
);