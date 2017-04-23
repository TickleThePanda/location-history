package co.uk.ticklethepanda.location.history.cartograph.points.latlong;

import co.uk.ticklethepanda.location.history.cartograph.Point;

public class LatLong implements Point {
    private final double x;
    private final double y;

    public LatLong(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "LatLong{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
