DROP FUNCTION IF EXISTS rotate_hgs_polygon(geometry, real);
CREATE OR REPLACE FUNCTION rotate_hgs_polygon(input_polygon geometry, days real)
RETURNS geometry AS
$BODY$
DECLARE
    x real;
    y real;
BEGIN
		--EXAMPLE QUERY select rotate_polygon_in_hgs(hgs_bbox, 2) from ar WHERE kb_archivid = 'ivo://helio-informatics.org/AR_SPoCA_20160710_030259_20160710T024223_0';
    RETURN st_makepolygon(ST_MakeLine(array_agg(rot_points.rotated))) from (select rotate_hgs_point((ST_DumpPoints(input_polygon)).geom, days) as rotated) as rot_points;
END;
$BODY$
LANGUAGE plpgsql;