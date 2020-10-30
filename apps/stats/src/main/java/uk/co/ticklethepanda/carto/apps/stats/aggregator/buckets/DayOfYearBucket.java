package uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets;

import java.time.LocalDateTime;
import java.util.Objects;

public class DayOfYearBucket implements DateBucket, Comparable<DayOfYearBucket> {

    private final int dayOfYear;

    public static DayOfYearBucket from(LocalDateTime time) {
        return new DayOfYearBucket(time.getDayOfYear());
    }

    public DayOfYearBucket(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    @Override
    public String getName() {
        return Integer.toString(dayOfYear);
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
        DayOfYearBucket that = (DayOfYearBucket) o;
        return dayOfYear == that.dayOfYear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfYear);
    }

    @Override
    public int compareTo(DayOfYearBucket that) {
        return Integer.compare(this.dayOfYear, that.dayOfYear);
    }
}
