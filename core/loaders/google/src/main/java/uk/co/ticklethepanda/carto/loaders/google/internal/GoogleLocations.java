package uk.co.ticklethepanda.carto.loaders.google.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GoogleLocations {

    public class Filterer {
        private List<Predicate<GoogleLocation>> filters = new ArrayList<>();

        public Filterer removeNullIslands() {
            filters.add(l -> l.getX() != 0 && l.getY() != 0);
            return this;
        }

        public Filterer removeInaccurate(long threshold) {
            if(threshold != -1) {
                filters.add(l -> l.getAccuracy() < threshold);
            }
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

    public Filterer getFiltrator() {
        return new Filterer();
    }
}
