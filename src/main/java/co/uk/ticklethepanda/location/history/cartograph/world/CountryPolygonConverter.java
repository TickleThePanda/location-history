package co.uk.ticklethepanda.location.history.cartograph.world;

import co.uk.ticklethepanda.location.history.cartograph.Converter;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;
import org.geojson.LngLatAlt;
import org.geojson.Polygon;

import java.util.List;

public class CountryPolygonConverter implements Converter<Polygon, List<LongLat>> {

    private Converter<LngLatAlt, LongLat> converter =
            i -> new LongLat((float) i.getLongitude(), (float) i.getLatitude());

    @Override
    public List<LongLat> convert(Polygon input) {
        return converter.convertList(input.getExteriorRing());
    }
}
