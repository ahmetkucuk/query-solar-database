DROP FUNCTION IF EXISTS temporal_col_filter_all(text[], character varying,
				timestamp, timestamp, text, text[]);
CREATE OR REPLACE FUNCTION temporal_col_filter_all(tnames text[], 
				    t_pred character varying,
				    tstart timestamp,
				    tend timestamp,
				    order_by_col text,
				    cols_out text[]
				    )
RETURNS TABLE(col1 text, col2 text, col3 text, col4 text, col5 text, col6 text, col7 text, col8 text, col9 text, col10 text, order_by_c timestamp without time zone ) AS
$BODY$
DECLARE 
    tname TEXT;
    t_query TEXT;
    col_array_text TEXT;
    col TEXT;
BEGIN
--Usage example: 
--select * from temporal_col_filter_all(ARRAY['ar_spt', 'ch_spt']::TEXT[], 
--	'Overlaps', '2014-12-01 21:36:23', '2014-12-03 01:36:23', 'event_starttime',
--	ARRAY['kb_archivid', 'hpc_boundcc', 'hpc_coord', 'event_type']::TEXT[]);

--Allowed temporal predicates are following: 
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'

col_array_text = 'ARRAY[';
FOREACH col in ARRAY cols_out
LOOP
    col_array_text = col_array_text || quote_literal(col) || ',';
END LOOP;
col_array_text = substring(col_array_text from 0 for char_length(col_array_text)) || ']::TEXT[]';

FOREACH tname IN ARRAY tnames
LOOP 
   t_query = 'select * from temporal_col_filter_order( ' || quote_literal(tname);
   t_query = t_query ||', ' || quote_literal(t_pred) ||', ';
   t_query = t_query || quote_literal(tstart)||' , '|| quote_literal(tend) ;
   t_query = t_query || ', ' ||quote_literal(order_by_col) || ', '|| col_array_text || ')';
   RAISE NOTICE '%', tname;
   RAISE NOTICE '%', t_query;
   RETURN QUERY EXECUTE t_query; --returns multiple times from different tables   
END LOOP;
	    
END;
$BODY$
LANGUAGE plpgsql;
