package uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Objects;

public class DayOfWeekBucket implements DateBucket, Comparable<DayOfWeekBucket> {

    private final DayOfWeek weekday;

    public static DayOfWeekBucket from(LocalDateTime time) {
        return new DayOfWeekBucket(DayOfWeek.from(time));
    }

    public DayOfWeekBucket(DayOfWeek weekday) {
        this.weekday = weekday;
    }

    @Override
    public String getName() {
        return weekday.toString();
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
        DayOfWeekBucket that = (DayOfWeekBucket) o;
        return weekday == that.weekday;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekday);
    }

    @Override
    public int compareTo(DayOfWeekBucket that) {
        return this.weekday.compareTo(that.weekday);
    }
}
