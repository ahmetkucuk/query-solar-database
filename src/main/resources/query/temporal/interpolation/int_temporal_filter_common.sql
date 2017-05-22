DROP FUNCTION IF EXISTS int_temporal_filter_common(text[],character varying,timestamp without time zone,timestamp without time zone);
CREATE OR REPLACE FUNCTION int_temporal_filter_common(tnames text[],
				    t_pred character varying,
				    tstart timestamp,
				    tend timestamp
				    )
RETURNS TABLE(KBarchivID TEXT, startTime TIMESTAMP, endTime TIMESTAMP,
TrackID NUMERIC, interpolated BOOLEAN, geom TEXT, event_type TEXT) AS
$BODY$
DECLARE
    tname TEXT;
    t_query TEXT;

BEGIN
--Usage example:
--select temporal_filter_all(ARRAY['ar_spt', 'ch_spt']::TEXT[],
--	'GreaterThan', '2014-12-01 21:36:23', '2014-12-03 01:36:23', 'event_starttime');

--Allowed temporal predicates are following:
--'Equals', 'LessThan', 'GreaterThan', 'Contains', 'ContainedBy',
--'Overlaps', 'Precedes', 'PrecededBy'

FOREACH tname IN ARRAY tnames
LOOP
   t_query = 'select * from int_temporal_filter_common_order( ' || quote_literal(tname);
   t_query = t_query ||', ' || quote_literal(t_pred) ||', ';
   t_query = t_query || quote_literal(tstart)||' , '|| quote_literal(tend) ;
   t_query = t_query || ')';
   RETURN QUERY EXECUTE t_query; --returns multiple times from different tables
END LOOP;

END;
$BODY$
LANGUAGE plpgsql;
