package co.uk.ticklethepanda.location.history.cartograph.points.euclid;


import co.uk.ticklethepanda.location.history.cartograph.Point;

public class EuclidPoint implements Point {

    private final float x;
    private final float y;

    public EuclidPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
