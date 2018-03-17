package co.uk.ticklethepanda.location.history.cartograph.projection;

public class EuclidPoint {

    private final float x;
    private final float y;

    public EuclidPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
