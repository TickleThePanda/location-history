package uk.co.ticklethepanda.carto.loaders.google.internal;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class GoogleLocation implements Serializable {
    private LocalDateTime timestamp;
    private long latitudeE7;
    private long longitudeE7;
    private long accuracy;

    public LocalDate getDate() {
        return LocalDate.from(timestamp);
    }
    
    public LocalDateTime getDateTime() {
        return timestamp;
    }

    public long getTimestampMs() {
        return timestamp.toEpochSecond(ZoneOffset.UTC);
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
