package co.uk.ticklethepanda.location.history.cartograph.points.googlelocation;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GoogleLocations {

    private static final Logger LOG = LogManager.getLogger();

    public static class Loader {
        public static GoogleLocations fromFile(String fileName)
                throws JsonSyntaxException, JsonIOException, FileNotFoundException {
            Gson gson = new Gson();

            GoogleLocations locations = gson.fromJson(
                    new BufferedReader(
                            new FileReader(fileName)
                    ), GoogleLocations.class);

            return locations.getFiltrator()
                    .removeNullIslands()
                    .filter();

        }
    }

    public class Filtrator {
        private List<Predicate<GoogleLocation>> filters = new ArrayList<>();

        public Filtrator removeNullIslands() {
            filters.add(l -> !l.isNullIsland());
            return this;
        }

        public Filtrator removeInaccurate(long threshold) {
            filters.add(l -> l.getAccuracy() < threshold);
            return this;
        }

        public Filtrator keepByDayOfWeek(DayOfWeek dayOfWeek) {
            filters.add(l -> l.getDate().getDayOfWeek().equals(dayOfWeek));
            return this;
        }

        public Filtrator keepByMonth(Month month) {
            filters.add(l -> l.getDate().getMonth().equals(month));
            return this;
        }

        public Filtrator keepByYearMonth(YearMonth month) {
            filters.add(l -> YearMonth.from(l.getDate()).equals(month));
            return this;
        }

        public Filtrator addFilter(Predicate<GoogleLocation> filter) {
            filters.add(filter);
            return this;
        }

        public GoogleLocations filter() {
            Stream<GoogleLocation> locs = locations.stream();
            for (Predicate<GoogleLocation> filter : filters) {
                locs = locs.filter(filter);
            }
            return new GoogleLocations(locs.collect(Collectors.toList()));
        }

    }

    private List<GoogleLocation> locations;

    public List<GoogleLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<GoogleLocation> locations) {
        this.locations = locations;
    }

    public GoogleLocations() {
        this(new ArrayList<>());
    }

    public GoogleLocations(List<GoogleLocation> locations) {
        this.locations = locations;
    }

    public Filtrator getFiltrator() {
        return new Filtrator();
    }
}
