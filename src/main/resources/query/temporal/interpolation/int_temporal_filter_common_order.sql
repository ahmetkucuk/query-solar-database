DROP FUNCTION IF EXISTS int_temporal_filter_common_order(character varying,character varying,timestamp without time zone,timestamp without time zone);

CREATE OR REPLACE FUNCTION int_temporal_filter_common_order(tname character varying,
				    t_pred character varying,
				    tstart timestamp,
				    tend timestamp
				    )
RETURNS TABLE(KBarchivID TEXT, startTime TIMESTAMP, endTime TIMESTAMP,
TrackID NUMERIC, interpolated BOOLEAN, geom TEXT) AS
$BODY$
DECLARE
    t_query TEXT;
    col_names TEXT = '';
    temp_valid BOOLEAN;
BEGIN
--Usage example: -- select temporal_filter_order('ar_spt', 'GreaterThan',
--		'2014-12-01 21:36:23', '2014-12-03 01:36:23', 'event_starttime');

--Allowed temporal predicates are following:
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'

col_names = 'kbarchivid, startTime, endTime, TrackID, interpolated, ST_asText(geom)';


RAISE NOTICE 'Query start was : %', tstart;
RAISE NOTICE 'Query start was : %', tend;

t_query = 'SELECT ' || col_names || ' FROM ' || tname || ' ' || ' WHERE ';
temp_valid = 1;
CASE
WHEN $2 = 'Equals' THEN
    t_query = t_query || ' tsrange(startTime, endTime) = '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'LessThan' THEN

	t_query = t_query || '  tsrange(startTime, endTime) < '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'GreaterThan' THEN

	t_query = t_query || '  tsrange(startTime, endTime) > '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Contains' THEN

	t_query = t_query || '  tsrange(startTime, endTime) @> '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'ContainedBy' THEN

	t_query = t_query || '  tsrange(startTime, endTime) <@ '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Overlaps' THEN

	t_query = t_query || '  tsrange(startTime, endTime) && '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Precedes' THEN

	t_query = t_query || '  tsrange(startTime, endTime) << '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'PrecededBy' THEN

	t_query = t_query || '  tsrange(startTime, endTime) >> '||
	       'tsrange('''||tstart||''', '''||tend||''')';
ELSE
        temp_valid = 0;
END CASE;


IF temp_valid = false THEN
    RETURN QUERY EXECUTE
        'SELECT kb_archivid from ar_spt where 1=0';
END IF;

RETURN QUERY EXECUTE t_query;

END;
$BODY$
LANGUAGE plpgsql;
