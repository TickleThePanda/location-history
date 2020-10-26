package uk.co.ticklethepanda.carto.core.world;

import uk.co.ticklethepanda.carto.core.model.Rectangle;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.projection.Point;

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
