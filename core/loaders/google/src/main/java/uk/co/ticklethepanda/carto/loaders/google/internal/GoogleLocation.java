package uk.co.ticklethepanda.carto.loaders.google.internal;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class GoogleLocation implements Serializable {
    private long timestampMs;
    private long latitudeE7;
    private long longitudeE7;
    private long accuracy;

    public LocalDate getDate() {
        return LocalDate.from(
                Instant.ofEpochMilli(this.getTimestampMs())
                        .atZone(ZoneId.of("UTC")));
    }
    
    public LocalDateTime getDateTime() {
        return LocalDateTime.from(
                Instant.ofEpochMilli(this.getTimestampMs())
                        .atZone(ZoneId.of("UTC")));
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public float getX() {
        return longitudeE7;
    }

    public float getY() {
        return latitudeE7;
    }

    public long getAccuracy() {
        return accuracy;
    }

}
