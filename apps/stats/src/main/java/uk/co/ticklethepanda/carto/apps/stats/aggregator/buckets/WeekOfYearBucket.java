package uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Objects;

public class WeekOfYearBucket implements DateBucket, Comparable<WeekOfYearBucket> {

    private final int weekOfYear;

    public static WeekOfYearBucket from(LocalDateTime time) {
        return new WeekOfYearBucket(time.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
    }

    private WeekOfYearBucket(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }
    
    @Override
    public String getName() {
        return Integer.toString(weekOfYear);
    }

    @Override
    public float getLength() {
        return 7;
    }

    @Override
    public String getLengthUnit() {
        return "day";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeekOfYearBucket that = (WeekOfYearBucket) o;
        return weekOfYear == that.weekOfYear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekOfYear);
    }

    @Override
    public int compareTo(WeekOfYearBucket that) {
        return Integer.compare(this.weekOfYear, that.weekOfYear);
    }
}
