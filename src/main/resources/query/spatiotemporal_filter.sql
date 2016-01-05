DROP FUNCTION IF EXISTS spatiotemporal_filter(character varying,character varying, character varying,
				timestamp, timestamp, real, real, real, real);
CREATE OR REPLACE FUNCTION spatiotemporal_filter(tname character varying, 
				    t_pred character varying,
				    s_pred character varying,	
				    tstart timestamp,
				    tend timestamp,
				    xmin real,
				    ymin real,
				    xmax real,
				    ymax real
				    )
RETURNS TABLE(kb_archivid text) AS
$BODY$
DECLARE 
    st_query TEXT;
    spat_conditions TEXT;
    temp_valid boolean;
    spat_valid boolean;
BEGIN
--Usage example: -- select temporal_filter('ar_spt', 'Equals', 10, 10, 200, 200);
--Allowed temporal predicates are following: 
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'
st_query = 'SELECT kb_archivid FROM ' ||tname || ' ' || ' WHERE ';
temp_valid = 1;
CASE
WHEN $2 = 'Equals' THEN
    st_query = st_query || ' tsrange(event_starttime, event_endtime) = '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'LessThan' THEN
     
	st_query = st_query || '  tsrange(event_starttime, event_endtime) < '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'GreaterThan' THEN
     
	st_query = st_query || '  tsrange(event_starttime, event_endtime) > '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Contains' THEN
     
	st_query = st_query || '  tsrange(event_starttime, event_endtime) @> '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'ContainedBy' THEN
     
	st_query = st_query || '  tsrange(event_starttime, event_endtime) <@ '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Overlaps' THEN
     
	st_query = st_query || '  tsrange(event_starttime, event_endtime) && '||
	       'tsrange('''||tstart||''', '''||tend||''')';	      
WHEN $2 = 'Precedes' THEN
     
	st_query = st_query || '  tsrange(event_starttime, event_endtime) << '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'PrecededBy' THEN
     
	st_query = st_query || '  tsrange(event_starttime, event_endtime) >> '||
	       'tsrange('''||tstart||''', '''||tend||''')';
ELSE
        temp_valid = 0;
END CASE;

spat_valid = 0;

IF s_pred in ('Intersects', 'Disjoint', 'Within', 'CoveredBy', 'Covers', 'Overlaps', 'Contains', 'Disjoint', 'Equals', 'Touches') THEN
spat_valid = 1;
spat_conditions = 'ST_'||s_pred||'( hpc_bbox, ST_MakeEnvelope(' ||xmin||', '||ymin||', '||xmax||', '||ymax||',4326) )';
END IF;

RAISE NOTICE 'Spat_valid : %', spat_valid;

IF (temp_valid = true and spat_valid = true) THEN
    st_query = st_query || ' and ' || spat_conditions;
END IF;

IF (temp_valid = false and spat_valid = true) THEN
    st_query = st_query || spat_conditions;
END IF;

IF temp_valid = false and spat_valid = false THEN
    RETURN QUERY EXECUTE
        'SELECT kb_archivid from ar_spt where 1=0';
END IF;

RETURN QUERY EXECUTE st_query;

RAISE NOTICE 'Query was : %', st_query;
	    
END;
$BODY$
LANGUAGE plpgsql;
