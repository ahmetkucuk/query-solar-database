DROP FUNCTION IF EXISTS temporal_col_filter_order(character varying, character varying,
				timestamp, timestamp, text, text[]);
CREATE OR REPLACE FUNCTION temporal_col_filter_order(tname character varying,
				    t_pred character varying,
				    tstart timestamp,
				    tend timestamp,
				    order_by_col text,
				    cols_out text[]
				    )
RETURNS TABLE(col1 text, col2 text, col3 text, col4 text, col5 text, col6 text, col7 text, col8 text, col9 text, col10 text, order_by_c timestamp without time zone ) AS
$BODY$
DECLARE 
    t_query TEXT;
    col_name TEXT;
    col_name_valid BOOLEAN;
    col_names_str TEXT;
    temp_valid boolean;
    
BEGIN
--Usage example: -- select temporal_col_filter_order('ar_spt', 
--		ARRAY['kb_archivid', 'hpc_bbox']::TEXT[], 'GreaterThan', 
--		'2014-12-01 21:36:23', '2014-12-03 01:36:23', 'event_starttime');

--Allowed temporal predicates are following: 
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'

col_name_valid = 1;
FOREACH col_name IN ARRAY cols_out
LOOP
    IF col_name NOT IN ('kb_archivid', 'event_starttime', 'event_endtime', 'hpc_bbox', 'hpc_boundcc'
			, 'trajectory_id', 'interpolated', 'intrajectory', 'event_type', 'hpc_coord') THEN
        col_name_valid = 0;
        EXIT;
    END IF;
END LOOP;

IF col_name_valid THEN 
    col_names_str = ' ';
    FOREACH col_name in ARRAY cols_out
    LOOP
        IF col_name IN ('hpc_bbox', 'hpc_boundcc') THEN
            col_names_str = col_names_str || 'ST_AsText(' || col_name || ') as ' || col_name || ',';
	ELSIF col_name = 'hpc_coord' THEN 
            col_names_str = col_names_str || 'ST_AsText( ST_Centroid( hpc_bbox )) as ' || col_name || ',';
        ELSIF col_name = 'event_type' THEN
            col_names_str = col_names_str || quote_literal(substring(tname from 0 for 3)) || '::TEXT as ' || col_name || ',' ;
        ELSE
            col_names_str = col_names_str || col_name || '::TEXT,';
        END IF;
    END LOOP;
    
    --col_names_str = substring(col_names_str from 0 for char_length(col_names_str));

    FOR i IN array_length(cols_out, 1)..9 LOOP
        --RAISE NOTICE 'Query columns are invalid'; 
        col_names_str = col_names_str || ' ''''::TEXT,';
    END LOOP;

    col_names_str = col_names_str || ' ' ||order_by_col ;
    RAISE NOTICE 'Column names string %', col_names_str;
ELSE 
    RAISE NOTICE 'Query columns are invalid'; 
    col_names_str = '';
    FOR i IN 1..10 LOOP
        col_names_str = col_names_str || ' ''''::TEXT,';
    END LOOP;
    col_names_str = substring(col_names_str from 0 for char_length(col_names_str));
    RETURN QUERY EXECUTE 'SELECT ' || col_names_str || ', ''2000-01-01 00:00:00''::TIMESTAMP ';
END IF;

t_query = 'SELECT ' || col_names_str || ' FROM ' || tname || ' WHERE ';

--Allowed temporal predicates are following: 
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'
temp_valid = 1;
CASE
WHEN $2 = 'Equals' THEN
    t_query = t_query || ' tsrange(event_starttime, event_endtime) = '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'LessThan' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) < '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'GreaterThan' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) > '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Contains' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) @> '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'ContainedBy' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) <@ '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Overlaps' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) && '||
	       'tsrange('''||tstart||''', '''||tend||''')';	      
WHEN $2 = 'Precedes' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) << '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'PrecededBy' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) >> '||
	       'tsrange('''||tstart||''', '''||tend||''')';
ELSE
        temp_valid = 0;
END CASE;

IF temp_valid and col_name_valid THEN
    RAISE NOTICE 'Temporal query %', t_query;
    RETURN QUERY EXECUTE t_query;
END IF;
END;
$BODY$
LANGUAGE plpgsql;
