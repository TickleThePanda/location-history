package uk.co.ticklethepanda.location.history.application.spring;

import org.springframework.stereotype.Service;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class LocalDateFilterFactory {

    private Map<Object, Predicate<LocalDate>> filterMap = new HashMap<>();

    public Predicate<LocalDate> get(DayOfWeek dayOfWeek) {
        if (filterMap.containsKey(dayOfWeek)) {
            return filterMap.get(dayOfWeek);
        }

        Predicate<LocalDate> filter = localDate -> localDate.getDayOfWeek().equals(dayOfWeek);
        filterMap.put(dayOfWeek, filter);

        return filter;
    }

    public Predicate<LocalDate> get(Month month) {
        if (filterMap.containsKey(month)) {
            return filterMap.get(month);
        }

        Predicate<LocalDate> filter = localDate -> localDate.getMonth().equals(month);
        filterMap.put(month, filter);

        return filter;
    }

    public Predicate<LocalDate> get(int year) {
        if (filterMap.containsKey(year)) {
            return filterMap.get(year);
        }

        Predicate<LocalDate> filter = localDate -> localDate.getYear() == year;
        filterMap.put(year, filter);

        return filter;
    }

    public Predicate<LocalDate> get(YearMonth yearMonth) {
        if (filterMap.containsKey(yearMonth)) {
            return filterMap.get(yearMonth);
        }

        Predicate<LocalDate> filter = localDate -> YearMonth.from(localDate).equals(yearMonth);
        filterMap.put(yearMonth, filter);

        return filter;
    }
}
