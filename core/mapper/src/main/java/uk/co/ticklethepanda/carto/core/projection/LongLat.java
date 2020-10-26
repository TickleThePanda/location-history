package uk.co.ticklethepanda.carto.core.projection;

public record LongLat(float longitude, float latitude) implements Point {

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
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
