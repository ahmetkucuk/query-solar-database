DROP FUNCTION IF EXISTS spatial_filter_common_order(character varying,character varying,
				real, real, real, real);
--Returns the kb_archivid's of the solar events with another attribute that can be
-- used as an ordering attribute. The spatial filter for the solar events are generic
-- spatial predicates. The bounding box (envelope) coordinates are given as parameters.

CREATE OR REPLACE FUNCTION spatial_filter_common_order(tname character varying, 
				    s_pred character varying,	
				    xmin real,
				    ymin real,
				    xmax real,
				    ymax real,
				    order_by_col text
				    )
RETURNS TABLE(kb_archivid text, col timestamp without time zone) AS
$BODY$
BEGIN
--Usage example: -- select spatial_filter_common_order('ar', 'Covers', 
--                                                     10, 10, 200, 200, 
--                                                     'event_starttime');

--Allowed spatial predicates are following: 
--'Intersects', 'Disjoint', 'Within', 'CoveredBy', 'Covers', 'Overlaps'
--'Contains', 'Disjoint', 'Equals', 'Touches' and some more.


--See the generic spatial boolean functions of PostGIS that starts with "ST_"
    RETURN QUERY EXECUTE 
	'SELECT kb_archivid, '|| order_by_col ||
	' FROM ' || tname || ' ' ||
	'WHERE ST_'||s_pred||'( hpc_bbox, ST_MakeEnvelope(' 
		||xmin||', '||ymin||', '||xmax||', '||ymax||',4326) )'; 
		-- bounding box limits
END;
$BODY$
LANGUAGE plpgsql;
