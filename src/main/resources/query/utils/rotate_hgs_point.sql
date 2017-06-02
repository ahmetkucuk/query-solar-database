DROP FUNCTION IF EXISTS rotate_hgs_point(geometry, real);
CREATE OR REPLACE FUNCTION rotate_hgs_point(input_point geometry, hours real)
RETURNS geometry AS
$BODY$
DECLARE
    x real;
    y real;
BEGIN
	x := st_x(input_point);
	y := st_y(input_point);

	x = x + (hours/24) * (14.44 - 3.0 * pow(sin(degrees(y)), 2.0));

    RETURN ST_GeomFromEWKT('SRID=4326;POINT('||x||' '||y||')');
END;
$BODY$
LANGUAGE plpgsql;