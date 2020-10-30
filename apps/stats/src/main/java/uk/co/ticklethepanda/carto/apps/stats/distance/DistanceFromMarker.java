package uk.co.ticklethepanda.carto.apps.stats.distance;

import uk.co.ticklethepanda.carto.apps.stats.markers.loader.Marker;

public record DistanceFromMarker(float sum, float numberOfEntries, Marker marker) {

    public DistanceFromMarker() {
        this(0f, 0f, null);
    }

    public DistanceFromMarker add(DistanceFromMarker that) {
        return new DistanceFromMarker(
                this.sum + that.sum,
                this.numberOfEntries + that.numberOfEntries,
                Marker.getLatest(this.marker, that.marker)
        );
    }

    @Override
    public String toString() {
        return "DistanceAggregate{" +
                "sum=" + sum +
                ", numberOfEntries=" + numberOfEntries +
                ", info=" + marker.info() +
                '}';
    }
    
}
