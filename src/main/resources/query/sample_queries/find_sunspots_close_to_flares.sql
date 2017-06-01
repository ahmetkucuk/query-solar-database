SELECT fl.kb_archivid as fl_id, count(ss.kb_archivid) as ss_id_count, ST_Perimeter(ST_Union(ss.hpc_bbox)) as perimeter

from (

SELECT * from fl WHERE tsrange(fl.event_starttime, fl.event_endtime) && tsrange('01-01-2011','01-01-2017') AND fl.FL_GOESCls ILIKE any (array['%X%','%M%'])
) as fl INNER JOIN ss ON
tsrange(ss.event_starttime, ss.event_endtime) && tsrange(fl.event_starttime - interval '2 day', fl.event_starttime - interval '1 day') AND tsrange(fl.event_starttime, fl.event_endtime) > tsrange('01-01-2017','01-01-2017')

AND ST_Overlaps(ST_Buffer(fl.hpc_bbox, 100), ss.hpc_bbox) group by fl.kb_archivid order by fl_id;