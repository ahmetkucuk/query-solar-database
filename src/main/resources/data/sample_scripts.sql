SELECT event_type, count(*) as event_count_by_type from ge
WHERE event_starttime < '2012-05-01 01:14:47' AND event_endtime < '2012-06-01 01:14:47' GROUP BY event_type;


SELECT event_type, count(*) as event_count_by_type from ge
WHERE event_starttime > '2012-05-01 01:14:47' AND event_endtime < '2012-06-01 01:14:47' GROUP BY event_type;


select count(*) from ge as g1, ge as g2 where ST_intersects(g1.hrc_bbox, g2.hrc_bbox) AND
g1.event_starttime < '2012-01-10 04:45:00'::timestamp;

select count(*) from ge as g1, ge as g2
where ST_intersects(g1.hpc_bbox, g2.hpc_bbox) AND g1.event_starttime < '2012-01-10 04:45:00'::timestamp;

select event_type, ST_AsText(hpc_bbox) from ge LEFT JOIN ar ON ge.kb_archivid = ar.kb_archivid WHERE ge.kb_archivid = 'AR_SPoCA_20120201_190542_20120201T184913_7';