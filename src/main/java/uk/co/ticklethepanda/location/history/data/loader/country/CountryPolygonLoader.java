package uk.co.ticklethepanda.location.history.data.loader.country;

import uk.co.ticklethepanda.location.history.cartograph.projection.LongLat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CountryPolygonLoader {

    public static List<List<LongLat>> countryPolygonLoader(String fileName) throws IOException {

        List<Polygon> polygons = new ArrayList<>();

        FeatureCollection featureCollection =
                new ObjectMapper().readValue(new FileInputStream(new File(fileName)), FeatureCollection.class);

        for (Feature feature : featureCollection.getFeatures()) {
            GeoJsonObject geometry = feature.getGeometry();
            if (geometry instanceof Polygon) {
                polygons.add((Polygon) geometry);
            } else if (geometry instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) geometry;
                for (List<List<LngLatAlt>> lists : multiPolygon.getCoordinates()) {
                    polygons.add(new Polygon(lists.get(0)));
                }
            }
        }

        return polygons.stream()
                .map(p -> p.getExteriorRing().stream()
                            .map(i -> new LongLat((float) i.getLongitude(), (float) i.getLatitude()))
                            .collect(Collectors.toList()))
                .collect(Collectors.toList());

    }
}
