package co.uk.ticklethepanda.location.history.cartograph.points.latlong;

import co.uk.ticklethepanda.location.history.cartograph.Point;

public class LongLat implements Point {
    private final float x;
    private final float y;

    public LongLat(float x, float y) {
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

    @Override
    public String toString() {
        return "LatLongDate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongLat longLatDate = (LongLat) o;

        if (Float.compare(longLatDate.x, x) != 0) return false;
        return Float.compare(longLatDate.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Float.floatToIntBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Float.floatToIntBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

}
