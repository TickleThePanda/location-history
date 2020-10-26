package uk.co.ticklethepanda.carto.core.projection;

public record EuclidPoint(float x, float y) implements Point {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EuclidPoint that = (EuclidPoint) o;

        if (Float.compare(that.x, x) != 0) return false;
        return Float.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}
