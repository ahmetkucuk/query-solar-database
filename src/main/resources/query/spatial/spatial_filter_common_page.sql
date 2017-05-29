DROP FUNCTION IF EXISTS spatial_filter_common_page(text[], character varying,
				real, real, real, real, character varying, real, real);
--Returns the spatiotemporal attributes of solar events from multiple tables 
-- that are filtered using a predicate and a spatial window (bounding box, 
-- given with four coordinates [xmin, xmax, ymin, ymax]). 
-- The last two parameters are for paging and limiting. See the usage example
CREATE OR REPLACE FUNCTION spatial_filter_common_page(tnames text[],
				    s_pred character varying,	
				    xmin real,
				    ymin real,
				    xmax real,
				    ymax real,
				    order_by_col character varying,
				    limit_count real,
				    offset_count real
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
    s_query TEXT;
    tname TEXT;
    array_text TEXT;
BEGIN
--Usage example: 
--select * from spatial_filter_common_page( 
--	ARRAY['ar', 'ch']::TEXT[], 'Intersects', 10, 10, 300, 300, 
--	'event_starttime', 30, 40);

--The above example will return 30 records from ar and ch, 
-- starting from the offset 40.

--Allowed spatial predicates are following: 
--'Intersects', 'Disjoint', 'Within', 'CoveredBy', 'Covers', 
--'Overlaps', 'Contains', 'Disjoint', 'Equals', 'Touches'

array_text = 'ARRAY[';
FOREACH tname in ARRAY tnames
LOOP
    array_text = array_text || quote_literal(tname) || ',';
END LOOP;
array_text = substring(array_text from 0 for char_length(array_text)) || ']::TEXT[]';
--RAISE NOTICE 'Array text was : %', array_text;



RAISE NOTICE '%', tnames;
s_query = 'select * from spatial_filter_common( ' ;
s_query = s_query || array_text || ', ' || quote_literal(s_pred) || ', ' || xmin|| ', '|| ymin ||', ' || xmax || ', ' ;
s_query = s_query || ymax || ', ' || quote_literal(order_by_col) ||') ';
s_query = s_query || ' ORDER BY ' || order_by_col;
s_query = s_query || ' LIMIT ' || limit_count || ' OFFSET ' || offset_count;
RAISE NOTICE 'Query: %', s_query;
RETURN QUERY EXECUTE s_query;
	    
END;
$BODY$
LANGUAGE plpgsql;
