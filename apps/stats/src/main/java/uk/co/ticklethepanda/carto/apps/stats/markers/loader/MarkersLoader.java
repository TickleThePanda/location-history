package uk.co.ticklethepanda.carto.apps.stats.markers.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;

public class MarkersLoader {
    public static List<PointData<LongLat, Marker>> loadMarkers(String markersPath) throws FileNotFoundException {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        return gson.fromJson(
                new FileReader(markersPath),
                new TypeToken<List<PointData<LongLat, Marker>>>() {
                }.getType()
        );
    }
}