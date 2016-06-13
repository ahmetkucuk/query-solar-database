DROP FUNCTION IF EXISTS spatiotemporal_filter_all(text[],character varying, character varying,
				timestamp, timestamp, real, real, real, real);
CREATE OR REPLACE FUNCTION spatiotemporal_filter_all(tnames text[], 
				    t_pred character varying,
				    s_pred character varying,	
				    tstart timestamp,
				    tend timestamp,
				    xmin real,
				    ymin real,
				    xmax real,
				    ymax real
				    )
RETURNS TABLE(kb_archivid text) AS
$BODY$
DECLARE 
    tname TEXT;
    st_query TEXT;

BEGIN
--Usage example: -- select spatiotemporal_filter_all(ARRAY['ar_spt', 'ch_spt']::TEXT[], 'GreaterThan', 'Intersects', '2015-12-01 21:36:23', '2015-12-03 01:36:23', 10, 10, 300, 300);

--Allowed temporal predicates are following: 
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'

--Allowed spatial predicates are following: 
--'Intersects', 'Disjoint', 'Within', 'CoveredBy', 'Covers', 
--'Overlaps', 'Contains', 'Disjoint', 'Equals', 'Touches'

FOREACH tname IN ARRAY tnames
LOOP 
   st_query = 'select spatiotemporal_filter(' || tname ||', ' || t_pred ||', ' || s_pred || ', '|| tstart||', '|| tend|| ', ' || xmin|| ', '|| ymin ||', ' || xmax || ', ' ||ymax ||')';
   RAISE NOTICE '%', tname;
   RAISE NOTICE '%', st_query;
   RETURN QUERY EXECUTE st_query; --returns multiple times from different tables   
END LOOP;
	    
END;
$BODY$
LANGUAGE plpgsql;
