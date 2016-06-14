DROP FUNCTION IF EXISTS spatial_filter_all(text[], character varying,
				real, real, real, real, text);
CREATE OR REPLACE FUNCTION spatial_filter_all(tnames text[], 
				    s_pred character varying,	
				    xmin real,
				    ymin real,
				    xmax real,
				    ymax real,
				    order_by_col text
				    )
RETURNS TABLE(kb_archivid text, col timestamp without time zone) AS
$BODY$
DECLARE 
    tname TEXT;
    s_query TEXT;

BEGIN
--Usage example: -- select spatial_filter_all(ARRAY['ar_spt', 'ch_spt']::TEXT[], 
--			'Intersects', 10, 10, 300, 300, 'event_starttime');

--Allowed spatial predicates are following: 
--'Intersects', 'Disjoint', 'Within', 'CoveredBy', 'Covers', 
--'Overlaps', 'Contains', 'Disjoint', 'Equals', 'Touches'

FOREACH tname IN ARRAY tnames
LOOP 
   s_query = 'select * from spatial_filter_order( ''';
   s_query = s_query || tname ||''', ''' || s_pred || ''', ' ;
   s_query = s_query || xmin|| ', '|| ymin ||', ' || xmax || ', ';
   s_query = s_query || ymax || ', ''' ||order_by_col ||''')';
   RAISE NOTICE '%', tname;
   RAISE NOTICE '%', s_query;
   RETURN QUERY EXECUTE s_query; --returns multiple times from different tables   
END LOOP;
	    
END;
$BODY$
LANGUAGE plpgsql;
