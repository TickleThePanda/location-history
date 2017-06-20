package co.uk.ticklethepanda.location.history.cartograph.world;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CountryPolygonLoader {

    public static List<Polygon> countryPolygonLoader(String fileName) throws IOException {

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

        return polygons;
    }
}
