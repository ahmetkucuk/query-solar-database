DROP FUNCTION IF EXISTS temporal_filter_order(character varying, character varying,
				timestamp, timestamp, text);
CREATE OR REPLACE FUNCTION temporal_filter_order(tname character varying, 
				    t_pred character varying,
				    tstart timestamp,
				    tend timestamp,
				    order_by_col text
				    )
RETURNS TABLE(kb_archivid text, col timestamp without time zone) AS
$BODY$
DECLARE 
    t_query TEXT;
    temp_valid boolean;
BEGIN
--Usage example: -- select temporal_filter_order('ar_spt', 'GreaterThan', 
--		'2014-12-01 21:36:23', '2014-12-03 01:36:23', 'event_starttime');

--Allowed temporal predicates are following: 
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'
t_query = 'SELECT kb_archivid, ' || order_by_col || ' FROM ' ||tname || ' ' || ' WHERE ';
temp_valid = 1;
CASE
WHEN $2 = 'Equals' THEN
    t_query = t_query || ' tsrange(event_starttime, event_endtime) = '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'LessThan' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) < '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'GreaterThan' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) > '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Contains' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) @> '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'ContainedBy' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) <@ '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Overlaps' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) && '||
	       'tsrange('''||tstart||''', '''||tend||''')';	      
WHEN $2 = 'Precedes' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) << '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'PrecededBy' THEN
     
	t_query = t_query || '  tsrange(event_starttime, event_endtime) >> '||
	       'tsrange('''||tstart||''', '''||tend||''')';
ELSE
        temp_valid = 0;
END CASE;


IF temp_valid = false THEN
    RETURN QUERY EXECUTE
        'SELECT kb_archivid from ar_spt where 1=0';
END IF;

RETURN QUERY EXECUTE t_query;

RAISE NOTICE 'Query was : %', t_query;
	    
END;
$BODY$
LANGUAGE plpgsql;
