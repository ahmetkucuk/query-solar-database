package temp;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public class Coordinate {

    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public int x() {
        return (int)x;
    }

    public int y() {
        return (int)y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return Math.round(x) + " " + Math.round(y);
    }
}
