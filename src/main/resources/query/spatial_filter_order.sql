DROP FUNCTION IF EXISTS spatial_filter_order(character varying,character varying,
				real, real, real, real);
CREATE OR REPLACE FUNCTION spatial_filter_order(tname character varying, 
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
--Usage example: -- select test_spt('ar_spt', 'Covers', 10, 10, 200, 200);
--Allowed spatial predicates are following: 
--'Intersects', 'Disjoint', 'Within', 'CoveredBy', 'Covers', 'Overlaps'
--'Contains', 'Disjoint', 'Equals', 'Touches' and some more.
--See the functions of PostGIS that starts with "ST_"
    RETURN QUERY EXECUTE 
	'SELECT kb_archivid '|| 
	'FROM ' ||tname || ' ' ||
	'WHERE ST_'||s_pred||'( hpc_bbox, ST_MakeEnvelope(' 
		||xmin||', '||ymin||', '||xmax||', '||ymax||',4326) )'; 
		-- bounding box limits
END;
$BODY$
LANGUAGE plpgsql;
