package uk.co.ticklethepanda.carto.loaders.google;

import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.loader.GeodeticDataLoader;
import com.google.gson.Gson;
import uk.co.ticklethepanda.carto.loaders.google.internal.GoogleLocation;
import uk.co.ticklethepanda.carto.loaders.google.internal.GoogleLocations;

import java.io.BufferedReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by panda on 4/13/17.
 */
public class GoogleLocationGeodeticDataLoader implements GeodeticDataLoader<LongLat, LocalDate> {

    private final BufferedReader reader;
    private long accuracyThreshold;

    public GoogleLocationGeodeticDataLoader(Reader reader, long accuracyThreshold) {
        this.reader = new BufferedReader(reader);

        this.accuracyThreshold = accuracyThreshold;
    }

    @Override
    public List<PointData<LongLat, LocalDate>> load() {

        Gson gson = new Gson();

        GoogleLocations locations = gson.fromJson(reader, GoogleLocations.class);

        Stream<GoogleLocation> stream = locations.getLocations().stream();

        stream = stream.filter(l -> l.getX() != 0 && l.getY() != 0);

        if (accuracyThreshold != -1) {
            stream = stream.filter(l -> l.getAccuracy() < accuracyThreshold);
        }

        List<PointData<LongLat, LocalDate>> points = stream
                .map(p -> new PointData<>(new LongLat(p.getX() / 1e7f, p.getY() /1e7f), p.getDate()))
                .collect(Collectors.toList());

        return points;
    }
}
