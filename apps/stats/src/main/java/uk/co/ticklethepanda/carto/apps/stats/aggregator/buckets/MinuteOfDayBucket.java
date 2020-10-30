package uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets;

import java.time.LocalDateTime;
import java.util.Objects;

public class MinuteOfDayBucket implements DateBucket, Comparable<MinuteOfDayBucket> {

    private final int minute;

    public MinuteOfDayBucket(int minute) {
        this.minute = minute;
    }

    public static MinuteOfDayBucket from(LocalDateTime time) {
        return new MinuteOfDayBucket(time.getMinute() + time.getHour() * 60);
    }

    @Override
    public String getName() {
        return Integer.toString(minute);
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
    public int compareTo(MinuteOfDayBucket that) {
        return Integer.compare(this.minute, that.minute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinuteOfDayBucket that = (MinuteOfDayBucket) o;
        return minute == that.minute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minute);
    }
}
