package co.uk.ticklethepanda.location.history.cartograph.projection;

public class EuclidPoint implements Point {

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


    @Override
    public float getHorizontalComponent() {
        return x;
    }

    @Override
    public float getVerticalComponent() {
        return y;
    }
}
