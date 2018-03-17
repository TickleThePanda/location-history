package co.uk.ticklethepanda.location.history.cartograph.projection;

import co.uk.ticklethepanda.location.history.cartograph.Rectangle;

import java.util.List;


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


    @Override
    public float getHorizontalComponent() {
        return longitude;
    }

    @Override
    public float getVerticalComponent() {
        return latitude;
    }
}
