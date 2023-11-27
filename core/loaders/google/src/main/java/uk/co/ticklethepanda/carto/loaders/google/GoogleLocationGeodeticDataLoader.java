package uk.co.ticklethepanda.carto.loaders.google;

import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.loader.GeodeticDataLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import uk.co.ticklethepanda.carto.loaders.google.internal.GoogleLocation;
import uk.co.ticklethepanda.carto.loaders.google.internal.GoogleLocations;

import java.awt.Window.Type;
import java.io.BufferedReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by panda on 4/13/17.
 */
public class GoogleLocationGeodeticDataLoader implements GeodeticDataLoader<LongLat, LocalDateTime> {

    private final BufferedReader reader;
    private long accuracyThreshold;

    public GoogleLocationGeodeticDataLoader(Reader reader) {
        this.reader = new BufferedReader(reader);

        this.accuracyThreshold = -1;
    }

    public GoogleLocationGeodeticDataLoader(Reader reader, long accuracyThreshold) {
        this.reader = new BufferedReader(reader);

        this.accuracyThreshold = accuracyThreshold;
    }

    @Override
    public List<PointData<LongLat, LocalDateTime>> load() {

        Gson gson = new GsonBuilder().registerTypeAdapter(
                LocalDateTime.class,
                (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                    ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime()
            )
            .create();
            
        GoogleLocations locations = gson.fromJson(reader, GoogleLocations.class);

        Stream<GoogleLocation> stream = locations.getLocations().stream();

        stream = stream.filter(l -> l.getX() != 0 && l.getY() != 0);

        if (accuracyThreshold != -1) {
            stream = stream.filter(l -> l.getAccuracy() < accuracyThreshold);
        }

        List<PointData<LongLat, LocalDateTime>> points = stream
                .map(p -> new PointData<>(new LongLat(p.getX() / 1e7f, p.getY() /1e7f), p.getDateTime()))
                .collect(Collectors.toList());

        return points;
    }

}
