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

    public float getDistanceInMetersTo(LongLat that) {
        
        double R = 6371e3; // metres
        double φ1 = this.latitude * Math.PI / 180.0; // φ, λ in radians
        double φ2 = that.latitude * Math.PI / 180.0;
        double Δφ = (that.latitude - this.latitude) * Math.PI / 180.0;
        double Δλ = (that.longitude - this.longitude) * Math.PI / 180.0;

        double a = Math.sin(Δφ/2.0) * Math.sin(Δφ/2.0) +
                Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ/2.0) * Math.sin(Δλ/2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0-a));

        return (float) (R * c); // in metres
    }
}
