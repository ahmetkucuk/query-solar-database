DROP FUNCTION IF EXISTS temporal_filter_all(text[], character varying,
				timestamp, timestamp, text);
CREATE OR REPLACE FUNCTION temporal_filter_all(tnames text[], 
				    t_pred character varying,
				    tstart timestamp,
				    tend timestamp,
				    order_by_col text
				    )
RETURNS TABLE(kb_archivid text, col timestamp without time zone) AS
$BODY$
DECLARE 
    tname TEXT;
    t_query TEXT;

BEGIN
--Usage example: 
--select temporal_filter_all(ARRAY['ar_spt', 'ch_spt']::TEXT[], 
--	'GreaterThan', '2014-12-01 21:36:23', '2014-12-03 01:36:23', 'event_starttime');

--Allowed temporal predicates are following: 
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'

FOREACH tname IN ARRAY tnames
LOOP 
   t_query = 'select * from temporal_filter_order( ' || quote_literal(tname);
   t_query = t_query ||', ' || quote_literal(t_pred) ||', ';
   t_query = t_query || quote_literal(tstart)||' , '|| quote_literal(tend) ;
   t_query = t_query || ', ' ||quote_literal(order_by_col) ||')';
   RAISE NOTICE '%', tname;
   RAISE NOTICE '%', t_query;
   RETURN QUERY EXECUTE t_query; --returns multiple times from different tables   
END LOOP;
	    
END;
$BODY$
LANGUAGE plpgsql;
