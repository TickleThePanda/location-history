package uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MonthBucket implements DateBucket, Comparable<MonthBucket> {

    private final Month month;

    public static MonthBucket from(LocalDateTime time) {
        return new MonthBucket(Month.from(time));
    }

    public MonthBucket(Month month) {
        this.month = month;
    }

    @Override
    public String getName() {
        return month.toString();
    }

    @Override
    public float getLength() {
        return month.minLength();
    }

    @Override
    public String getLengthUnit() {
        return "day";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthBucket that = (MonthBucket) o;
        return month == that.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(month);
    }

    @Override
    public int compareTo(MonthBucket that) {
        return this.month.compareTo(that.month);
    }
}
