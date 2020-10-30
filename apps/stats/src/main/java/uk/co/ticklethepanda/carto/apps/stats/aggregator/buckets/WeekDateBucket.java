package uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class WeekDateBucket implements DateBucket, Comparable<WeekDateBucket> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-ww");

    private final String weekDate;
    
    public static WeekDateBucket from(LocalDateTime time) {
        return new WeekDateBucket(FORMATTER.format(time));
    }

    private WeekDateBucket(String weekDate) {
        this.weekDate = weekDate;
    }
    
    @Override
    public String getName() {
        return weekDate;
    }

    @Override
    public float getLength() {
        return 1;
    }

    @Override
    public String getLengthUnit() {
        return "day";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeekDateBucket that = (WeekDateBucket) o;
        return Objects.equals(weekDate, that.weekDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekDate);
    }

    @Override
    public int compareTo(WeekDateBucket that) {
        return this.weekDate.compareTo(that.weekDate);
    }
}
