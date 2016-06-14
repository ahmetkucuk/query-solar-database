DROP FUNCTION IF EXISTS spatiotemporal_filter_page_all(text[],character varying, character varying,
				timestamp, timestamp, real, real, real, real, character VARYING, real, real);
CREATE OR REPLACE FUNCTION spatiotemporal_filter_page_all(tnames text[],
				    t_pred character varying,
				    s_pred character varying,	
				    tstart timestamp,
				    tend timestamp,
				    xmin real,
				    ymin real,
				    xmax real,
				    ymax real,
				    order_by_col character VARYING,
				    limit_count real,
				    offset_count real
				    )
RETURNS TABLE(kb_archivid text) AS
$BODY$
DECLARE 
    st_query TEXT;
    tname TEXT;
    array_text TEXT;
    spat_conditions TEXT;
    st_query_tail TEXT;
    query_page TEXT;
    temp_valid boolean;
    spat_valid boolean;
BEGIN
--Usage example: 
--select * from spatiotemporal_filter_page_all( 
--	ARRAY['ar_spt', 'ch_spt']::TEXT[], 'GreaterThan', 'Intersects', 
--	'2014-12-01 21:36:23', '2014-12-02 01:36:23', 10, 10, 300, 300, 
--	'event_starttime', 10, 20);
--


--Allowed temporal predicates are following: 
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'
array_text = 'ARRAY['; --ar_spt', 'ch_spt']'

FOREACH tname in ARRAY tnames
LOOP
    array_text = array_text || quote_literal(tname) || ',';
END LOOP;
array_text = substring(array_text from 0 for char_length(array_text)) || ']::TEXT[]';
--RAISE NOTICE 'Array text was : %', array_text;



RAISE NOTICE '%', tnames;
st_query = 'select kb_archivid from spatiotemporal_filter_all( ' ;
st_query = st_query || array_text ||', ''' || t_pred ||''', ''' || s_pred || ''', '''|| tstart;
st_query = st_query ||''', '''|| tend|| ''', ' || xmin|| ', '|| ymin ||', ' || xmax || ', ' ;
st_query = st_query ||ymax || ', ''' ||order_by_col ||''') ORDER BY col';
st_query = st_query || ' LIMIT ' || limit_count || ' OFFSET ' || offset_count;
RAISE NOTICE 'Query: %', st_query;
RETURN QUERY EXECUTE st_query;
	    
END;
$BODY$
LANGUAGE plpgsql;
