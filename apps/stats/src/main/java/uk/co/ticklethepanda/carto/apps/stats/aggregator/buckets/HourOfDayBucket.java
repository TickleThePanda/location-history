package uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets;

import java.time.LocalDateTime;
import java.util.Objects;

public class HourOfDayBucket implements DateBucket, Comparable<HourOfDayBucket> {

    private final int hour;

    public HourOfDayBucket(int hour) {
        this.hour = hour;
    }

    public static HourOfDayBucket from(LocalDateTime time) {
        return new HourOfDayBucket(time.getHour());
    }

    @Override
    public String getName() {
        return Integer.toString(hour);
    }

    @Override
    public float getLength() {
        return 1;
    }

    @Override
    public String getLengthUnit() {
        return "hour";
    }

    @Override
    public int compareTo(HourOfDayBucket that) {
        return Integer.compare(this.hour, that.hour);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HourOfDayBucket that = (HourOfDayBucket) o;
        return hour == that.hour;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour);
    }
}
