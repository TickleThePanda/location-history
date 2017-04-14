package co.uk.ticklethepanda.location.history.points.ecp;

import co.uk.ticklethepanda.location.history.cartograph.Point;

public class EcpPoint implements Point {
    private final double x;
    private final double y;

    public EcpPoint(double x, double y) {
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
}
