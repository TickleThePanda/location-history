package uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MonthDateBucket implements DateBucket, Comparable<MonthDateBucket> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final YearMonth yearMonth;
    
    public static MonthDateBucket from(LocalDateTime time) {
        return new MonthDateBucket(YearMonth.from(time));
    }

    public MonthDateBucket(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    @Override
    public String getName() {
        return FORMATTER.format(yearMonth);
    }

    @Override
    public float getLength() {
        return yearMonth.lengthOfMonth();
    }

    @Override
    public String getLengthUnit() {
        return "day";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthDateBucket that = (MonthDateBucket) o;
        return Objects.equals(yearMonth, that.yearMonth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(yearMonth);
    }

    @Override
    public int compareTo(MonthDateBucket that) {
        return this.yearMonth.compareTo(that.yearMonth);
    }
}
