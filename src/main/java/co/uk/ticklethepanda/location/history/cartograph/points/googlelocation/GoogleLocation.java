package co.uk.ticklethepanda.location.history.cartograph.points.googlelocation;

import co.uk.ticklethepanda.location.history.cartograph.Point;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;


public class GoogleLocation implements Point, Serializable {
    private long timestampMs;
    private long latitudeE7;
    private long longitudeE7;
    private long accuracy;

    public LocalDate getDate() {
        return LocalDate.from(
                Instant.ofEpochMilli(this.getTimestampMs())
                        .atZone(ZoneId.of("UTC")));
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public double getY() {
        return latitudeE7;
    }

    public double getX() {
        return longitudeE7;
    }

    public long getAccuracy() {
        return accuracy;
    }

    public boolean isNullIsland() {
        return latitudeE7 == 0 && longitudeE7 == 0;
    }
}
