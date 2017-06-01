DROP FUNCTION IF EXISTS rotate_hgs_point(geometry, real);
CREATE OR REPLACE FUNCTION rotate_hgs_point(input_point geometry, days real)
RETURNS geometry AS
$BODY$
DECLARE
    x real;
    y real;
BEGIN
	x := st_x(input_point);
	y := st_y(input_point);

	x = x + days * (14.44 - 3.0 * pow(sin(radians(y)), 2.0));

    RETURN ST_GeomFromText('POINT('||x||' '||y||')');
END;
$BODY$
LANGUAGE plpgsql;