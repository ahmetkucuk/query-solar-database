DROP FUNCTION IF EXISTS spatial_filter_common(text[], character varying,
				real, real, real, real, text);
--Returns the spatiotemporal attributes of solar event records 
-- from multiple event tables (e.g., ar, ch, ef) using "spatial_filter_common_order"
-- function. The spatial predicates bbox coordinates are passed as parameters.
CREATE OR REPLACE FUNCTION spatial_filter_common(tnames text[], 
				    s_pred character varying,	
				    xmin real,
				    ymin real,
				    xmax real,
				    ymax real,
				    order_by_col text
				    )
RETURNS TABLE(kb_archivid TEXT, 
		event_starttime TIMESTAMP, 
		event_endtime TIMESTAMP, 
		hpc_boundcc TEXT, 
		hpc_coord TEXT, 
		event_type TEXT, 
		event_coordunit TEXT, 
		hpc_bbox TEXT, 
		orderby TIMESTAMP ) AS
$BODY$
DECLARE 
    tname TEXT;
    s_query TEXT;

BEGIN
--Usage example: 
--select spatial_filter_common(ARRAY['ar', 'ch']::TEXT[], 
--			'Intersects', 10, 10, 300, 300, 'event_starttime');
--OR
--select * from spatial_filter_common(ARRAY['ar', 'ch']::TEXT[], 'Intersects', 
--					10, 10, 300, 300, 
--					'event_starttime'); 

FOREACH tname IN ARRAY tnames
LOOP 
   s_query = 'select * from spatial_filter_common_order( ''';
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
