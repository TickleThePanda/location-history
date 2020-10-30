package uk.co.ticklethepanda.carto.apps.stats.markers.loader;

import java.time.LocalDate;

public record Marker(String info, LocalDate date) {

    public static Marker getLatest(Marker a, Marker b) {
        if (a == null && b == null) {
            return null;
        } else if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            return a.date().isAfter(b.date()) ? a : b;
        }
    }
}
