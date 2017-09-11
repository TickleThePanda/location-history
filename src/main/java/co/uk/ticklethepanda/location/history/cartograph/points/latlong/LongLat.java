package co.uk.ticklethepanda.location.history.cartograph.points.latlong;

import co.uk.ticklethepanda.location.history.cartograph.Rectangle;

import java.util.List;


public class LongLat {
    private final float longitude;
    private final float latitude;

    public LongLat(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static LongLat getMaxBound(
            List<LongLat> locations) {
        if (locations.size() == 0) {
            return null;
        }
        float maxX = locations.get(0).getLongitude();
        float maxY = locations.get(0).getLatitude();
        for (LongLat mapPoint : locations) {
            maxX = Math.max(maxX, mapPoint.getLongitude());
            maxY = Math.max(maxY, mapPoint.getLatitude());
        }
        return new LongLat(maxX, maxY);
    }

    public static LongLat getMinBound(
            List<LongLat> locations) {
        if (locations.size() == 0) {
            return null;
        }
        float minX = locations.get(0).getLongitude();
        float minY = locations.get(0).getLatitude();
        for (LongLat mapPoint : locations) {
            minX = Math.min(minX, mapPoint.getLongitude());
            minY = Math.min(minY, mapPoint.getLatitude());
        }
        return new LongLat(minX, minY);
    }

    public static Rectangle getBoundingRectangle(
            List<LongLat> locations) {
        if (locations.size() == 0) {
            return null;
        }
        LongLat min = LongLat.getMinBound(locations);
        LongLat max = LongLat.getMaxBound(locations);

        float rectangleWidth = max.getLongitude() - min.getLongitude();
        float rectangleHeight = max.getLatitude() - min.getLatitude();

        Rectangle rect = new Rectangle(min.getLongitude(), min.getLatitude(),
                rectangleWidth, rectangleHeight);

        return rect;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "LatLongDate{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongLat longLatDate = (LongLat) o;

        if (Float.compare(longLatDate.longitude, longitude) != 0) return false;
        return Float.compare(longLatDate.latitude, latitude) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Float.floatToIntBits(longitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Float.floatToIntBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

}
