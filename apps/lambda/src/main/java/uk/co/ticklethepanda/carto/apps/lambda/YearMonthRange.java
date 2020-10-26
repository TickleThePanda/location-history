package uk.co.ticklethepanda.carto.apps.lambda;

import java.time.YearMonth;
import java.util.Iterator;

class YearMonthRange implements Iterable<YearMonth> {

    public static YearMonthRange between(YearMonth start, YearMonth end) {
        return new YearMonthRange(start, end);
    }

    private final YearMonth start;
    private final YearMonth end;

    public YearMonthRange(YearMonth start, YearMonth end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Iterator<YearMonth> iterator() {
        return new Iterator<YearMonth>() {

            private YearMonth current = start;

            @Override
            public boolean hasNext() {
                return current.plusMonths(1).isBefore(end);
            }

            @Override
            public YearMonth next() {
                YearMonth next = current;
                current = current.plusMonths(1);
                return next;
            }
        };
    }
}
