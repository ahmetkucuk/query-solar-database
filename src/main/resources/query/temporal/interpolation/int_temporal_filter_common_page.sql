DROP FUNCTION IF EXISTS int_temporal_filter_common_page(text[],character varying,
				timestamp, timestamp, TEXT, real, real);
CREATE OR REPLACE FUNCTION int_temporal_filter_common_page(tnames text[],
				    t_pred character varying,
				    tstart timestamp,
				    tend timestamp,
				    order_by_col TEXT,
				    limit_count real,
				    offset_count real
				    )
RETURNS TABLE(KBarchivID TEXT, startTime TIMESTAMP, endTime TIMESTAMP,
TrackID NUMERIC, interpolated BOOLEAN, geom TEXT, eventType TEXT) AS
$BODY$
DECLARE
    t_query TEXT;
    tname TEXT;
    array_text TEXT;
BEGIN

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
t_query = 'select KBarchivID, startTime, endTime, TrackID, interpolated, ST_asText(geom), eventType from int_temporal_filter_common( ' ;
t_query = t_query || array_text ||', ' || quote_literal(t_pred) ||', ';
t_query = t_query || quote_literal(tstart) || ', ' || quote_literal(tend);
t_query = t_query ||') ORDER BY ' || order_by_col;
t_query = t_query || ' LIMIT ' || limit_count || ' OFFSET ' || offset_count;
RAISE NOTICE '%', t_query;
RETURN QUERY EXECUTE t_query;

END;
$BODY$
LANGUAGE plpgsql;
