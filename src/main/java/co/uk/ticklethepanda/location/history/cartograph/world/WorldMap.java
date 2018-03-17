package co.uk.ticklethepanda.location.history.cartograph.world;

import co.uk.ticklethepanda.location.history.cartograph.Rectangle;
import co.uk.ticklethepanda.location.history.cartograph.projection.LongLat;
import co.uk.ticklethepanda.location.history.cartograph.projection.Point;

import java.util.List;
import java.util.stream.Collectors;

public class WorldMap {

    private class Country {
        private final List<LongLat> countryOutline;
        private final Rectangle boundingRectangle;

        public Country(List<LongLat> countryOutline) {
            this.countryOutline = countryOutline;
            this.boundingRectangle = Point.getBoundingRectangle(countryOutline);
        }
    }

    private List<Country> countries;

    public WorldMap(List<List<LongLat>> countryOutlines) {
        this.countries = countryOutlines.stream()
                .map(Country::new)
                .collect(Collectors.toList());
    }

    public List<List<LongLat>> getCountryOutlines() {
        return countries.stream()
                .map(c -> c.countryOutline)
                .collect(Collectors.toList());
    }

    public List<List<LongLat>> getCountryOutlinesThatIntersect(Rectangle rectangle) {
        return countries.stream()
                .filter(cr -> rectangle.intersects(cr.boundingRectangle))
                .map(c -> c.countryOutline)
                .collect(Collectors.toList());
    }
}
