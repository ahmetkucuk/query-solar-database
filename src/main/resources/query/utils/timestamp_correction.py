
input = "UPDATE %s SET event_endtime = (%s.event_endtime + interval '1 sec') where event_starttime = event_endtime;DROP INDEX public.%s_tsrange_idx;CREATE INDEX %s_tsrange_idx ON public.%s USING gist (tsrange(event_starttime, event_endtime, '[]'::text));"
#input = "select count(*) from %s where event_starttime = event_endtime;"

events = ["fi", "ar","ce","cd","ch","cw","fe","fa","fl","lp","os","ss","ef","cj","pg","ot","nr","sg","sp","cr","cc","er","tob","hy","bu","ee","pb","pt"]

for e in events:
	print(input % (e, e, e, e, e))