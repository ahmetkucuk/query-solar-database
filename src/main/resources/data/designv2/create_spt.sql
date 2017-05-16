create table ar_spt(
kb_archivid text primary key,
event_starttime timestamp,
hpc_bbox geometry(Polygon, 4326)
);