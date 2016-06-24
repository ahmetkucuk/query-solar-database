DROP FUNCTION IF EXISTS temporal_col_filter_page_all(text[],character varying,
				timestamp, timestamp, character VARYING, real, real, text[]);
CREATE OR REPLACE FUNCTION temporal_col_filter_page_all(tnames text[],
				    t_pred character varying,
				    tstart timestamp,
				    tend timestamp,
				    order_by_col character VARYING,
				    limit_count real,
				    offset_count real,
				    cols_out text[]
				    )
RETURNS TABLE(col1 text, col2 text, col3 text, col4 text, col5 text, col6 text, col7 text, col8 text, col9 text, col10 text) AS
$BODY$
DECLARE 
    t_query TEXT;
    tname TEXT;
    array_text TEXT;
    col_array_text TEXT;
    col TEXT;
BEGIN
--Usage example: 
--select * from temporal_col_filter_page_all( 
--	ARRAY['ar_spt', 'ch_spt']::TEXT[], 'GreaterThan', 
--	'2014-12-01 21:36:23', '2014-12-02 01:36:23', 
--	'event_starttime', 100, 0,
--	ARRAY['kb_archivid', 'event_starttime', 'hpc_boundcc', 'hpc_coord', 'event_type']::TEXT[]);
--


--Allowed temporal predicates are following: 
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'
array_text = 'ARRAY['; 

FOREACH tname in ARRAY tnames
LOOP
    array_text = array_text || quote_literal(tname) || ',';
END LOOP;
array_text = substring(array_text from 0 for char_length(array_text)) || ']::TEXT[]';
--RAISE NOTICE 'Array text was : %', array_text;


col_array_text = 'ARRAY[';
FOREACH col in ARRAY cols_out
LOOP
    col_array_text = col_array_text || quote_literal(col) || ',';
END LOOP;
col_array_text = substring(col_array_text from 0 for char_length(col_array_text)) || ']::TEXT[]';



RAISE NOTICE '%', tnames;
t_query = 'select col1, col2, col3, col4, col5, col6, col7, col8, col9, col10 from temporal_col_filter_all( ' ;
t_query = t_query || array_text ||', ' || quote_literal(t_pred) ||', ';
t_query = t_query || quote_literal(tstart) || ', ' || quote_literal(tend);
t_query = t_query || ', ' ||quote_literal(order_by_col)||  ',' || col_array_text ||') ORDER BY order_by_c';
t_query = t_query || ' LIMIT ' || limit_count || ' OFFSET ' || offset_count;
RAISE NOTICE '%', t_query;
RETURN QUERY EXECUTE t_query;
	    
END;
$BODY$
LANGUAGE plpgsql;
