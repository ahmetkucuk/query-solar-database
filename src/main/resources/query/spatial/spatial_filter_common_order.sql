DROP FUNCTION IF EXISTS spatial_filter_common_order(character varying,character varying,
				real, real, real, real);
--Returns the spatiotemporal attributes of solar events with an ordering
-- attribute. The spatial filter for the solar events are generic
-- spatial predicates. The bounding box (envelope) coordinates are 
-- given as parameters.

CREATE OR REPLACE FUNCTION spatial_filter_common_order(tname character varying, 
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
		orderby TIMESTAMP) AS
$BODY$
DECLARE
    col_names TEXT = '';
BEGIN
--Usage example: 
-- select spatial_filter_common_order('ar', 'Covers', 
--                                     10, 10, 200, 200, 
--                                    'event_starttime');

--Allowed spatial predicates are following: 
--'Intersects', 'Disjoint', 'Within', 'CoveredBy', 'Covers', 'Overlaps'
--'Contains', 'Disjoint', 'Equals', 'Touches' and some more.



col_names = 'kb_archivid, event_starttime, event_endtime, ST_AsText(hpc_boundcc), ST_AsText(hpc_coord), event_type, event_coordunit, ST_AsText(hpc_bbox), ' || order_by_col || ' as orderby';


--See the generic spatial boolean functions of PostGIS that starts with "ST_"
    RETURN QUERY EXECUTE 
	'SELECT ' || col_names ||  
	' FROM ' || tname || ' ' ||
	'WHERE ST_'||s_pred||'( hpc_bbox, ST_MakeEnvelope(' 
		||xmin||', '||ymin||', '||xmax||', '||ymax||',4326) )'; 
		-- bounding box limits
END;
$BODY$
LANGUAGE plpgsql;
