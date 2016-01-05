DROP FUNCTION IF EXISTS temporal_filter(character varying,character varying,
				timestamp, timestamp);
CREATE OR REPLACE FUNCTION temporal_filter(tname character varying, 
				    t_pred character varying,	
				    tstart timestamp,
				    tend timestamp
				    )
RETURNS TABLE(kb_archivid text) AS
$BODY$
BEGIN
--Usage example: -- select temporal_filter('ar_spt', 'Equals', 10, 10, 200, 200);
--Allowed temporal predicates are following: 
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'
CASE
WHEN $2 = 'Equals' THEN
    RETURN QUERY EXECUTE 
	'SELECT kb_archivid '|| 
	'FROM ' ||tname || ' ' ||
	'WHERE tsrange(event_starttime, event_endtime) = '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'LessThan' THEN
    RETURN QUERY EXECUTE 
	'SELECT kb_archivid '|| 
	'FROM ' ||tname || ' ' ||
	'WHERE tsrange(event_starttime, event_endtime) < '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'GreaterThan' THEN
    RETURN QUERY EXECUTE 
	'SELECT kb_archivid '|| 
	'FROM ' ||tname || ' ' ||
	'WHERE tsrange(event_starttime, event_endtime) > '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Contains' THEN
    RETURN QUERY EXECUTE 
	'SELECT kb_archivid '|| 
	'FROM ' ||tname || ' ' ||
	'WHERE tsrange(event_starttime, event_endtime) @> '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'ContainedBy' THEN
    RETURN QUERY EXECUTE 
	'SELECT kb_archivid '|| 
	'FROM ' ||tname || ' ' ||
	'WHERE tsrange(event_starttime, event_endtime) <@ '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'Overlaps' THEN
    RETURN QUERY EXECUTE 
	'SELECT kb_archivid '|| 
	'FROM ' ||tname || ' ' ||
	'WHERE tsrange(event_starttime, event_endtime) && '||
	       'tsrange('''||tstart||''', '''||tend||''')';	      
WHEN $2 = 'Precedes' THEN
    RETURN QUERY EXECUTE 
	'SELECT kb_archivid '|| 
	'FROM ' ||tname || ' ' ||
	'WHERE tsrange(event_starttime, event_endtime) << '||
	       'tsrange('''||tstart||''', '''||tend||''')';
WHEN $2 = 'PrecededBy' THEN
    RETURN QUERY EXECUTE 
	'SELECT kb_archivid '|| 
	'FROM ' ||tname || ' ' ||
	'WHERE tsrange(event_starttime, event_endtime) >> '||
	       'tsrange('''||tstart||''', '''||tend||''')';
ELSE
    RETURN QUERY EXECUTE
        'SELECT kb_archivid from ar_spt where 1=0';
END CASE;	    
END;
$BODY$
LANGUAGE plpgsql;
