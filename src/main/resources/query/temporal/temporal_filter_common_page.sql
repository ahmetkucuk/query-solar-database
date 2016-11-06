DROP FUNCTION IF EXISTS temporal_filter_common_page(text[],character varying,
				timestamp, timestamp, TEXT, real, real);
CREATE OR REPLACE FUNCTION temporal_filter_common_page(tnames text[],
				    t_pred character varying,
				    tstart timestamp,
				    tend timestamp,
				    order_by_col TEXT,
				    limit_count real,
				    offset_count real
				    )
RETURNS TABLE(ID TEXT, StartTime TIMESTAMP , EndTime TIMESTAMP , CC TEXT
, Coordinate TEXT, EventType TEXT, CoordUnit TEXT, Bbox TEXT) AS
$BODY$
DECLARE
    t_query TEXT;
    tname TEXT;
    array_text TEXT;
BEGIN
--Usage example:
--select * from temporal_filter_common_page(ARRAY['ar', 'ch']::TEXT[], 'GreaterThan','2014-12-01 21:36:23', '2014-12-02 01:36:23','event_starttime', 10, 20);
--


--Allowed temporal predicates are following:
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'
array_text = 'ARRAY['; --ar_spt', 'ch_spt']'

FOREACH tname in ARRAY tnames
LOOP
    array_text = array_text || quote_literal(tname) || ',';
END LOOP;
array_text = substring(array_text from 0 for char_length(array_text)) || ']::TEXT[]';
--RAISE NOTICE 'Array text was : %', array_text;


RAISE NOTICE '%', tnames;
t_query = 'select kb_archivid, event_starttime, event_endtime, ST_AsText(hpc_boundcc), ST_AsText(hpc_coord), event_type, event_coordunit, ST_AsText(hpc_bbox) from temporal_filter_common( ' ;
t_query = t_query || array_text ||', ' || quote_literal(t_pred) ||', ';
t_query = t_query || quote_literal(tstart) || ', ' || quote_literal(tend);
t_query = t_query || ', ' ||quote_literal(order_by_col)||') ORDER BY ' || order_by_col;
t_query = t_query || ' LIMIT ' || limit_count || ' OFFSET ' || offset_count;
RAISE NOTICE '%', t_query;
RETURN QUERY EXECUTE t_query;

END;
$BODY$
LANGUAGE plpgsql;
