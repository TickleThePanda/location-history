package uk.co.ticklethepanda.location.history.cartograph.projection;


public class LongLat implements Point {
    private final float longitude;
    private final float latitude;

    public LongLat(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
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
    public float getHorizontalComponent() {
        return longitude;
    }

    @Override
    public float getVerticalComponent() {
        return latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongLat longLat = (LongLat) o;

        if (Float.compare(longLat.longitude, longitude) != 0) return false;
        return Float.compare(longLat.latitude, latitude) == 0;
    }

    @Override
    public int hashCode() {
        int result = (longitude != +0.0f ? Float.floatToIntBits(longitude) : 0);
        result = 31 * result + (latitude != +0.0f ? Float.floatToIntBits(latitude) : 0);
        return result;
    }
}
