package uk.co.ticklethepanda.carto.apps.web;

import org.springframework.stereotype.Service;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class LocalDateFilterFactory {

    private Map<Object, Predicate<LocalDateTime>> filterMap = new HashMap<>();

    public Predicate<LocalDateTime> get(DayOfWeek dayOfWeek) {
        if (filterMap.containsKey(dayOfWeek)) {
            return filterMap.get(dayOfWeek);
        }

        Predicate<LocalDateTime> filter = localDate -> localDate.getDayOfWeek().equals(dayOfWeek);
        filterMap.put(dayOfWeek, filter);

        return filter;
    }

    public Predicate<LocalDateTime> get(Month month) {
        if (filterMap.containsKey(month)) {
            return filterMap.get(month);
        }

        Predicate<LocalDateTime> filter = localDate -> localDate.getMonth().equals(month);
        filterMap.put(month, filter);

        return filter;
    }

    public Predicate<LocalDateTime> get(int year) {
        if (filterMap.containsKey(year)) {
            return filterMap.get(year);
        }

        Predicate<LocalDateTime> filter = localDate -> localDate.getYear() == year;
        filterMap.put(year, filter);

        return filter;
    }

    public Predicate<LocalDateTime> get(YearMonth yearMonth) {
        if (filterMap.containsKey(yearMonth)) {
            return filterMap.get(yearMonth);
        }

        Predicate<LocalDateTime> filter = localDate -> YearMonth.from(localDate).equals(yearMonth);
        filterMap.put(yearMonth, filter);

        return filter;
    }
}
