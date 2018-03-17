package co.uk.ticklethepanda.location.history.data.loader.geodetic.google;

import co.uk.ticklethepanda.location.history.cartograph.model.GeodeticData;
import co.uk.ticklethepanda.location.history.cartograph.projection.LongLat;
import co.uk.ticklethepanda.location.history.data.loader.geodetic.GeodeticDataLoadException;
import co.uk.ticklethepanda.location.history.data.loader.geodetic.GeodeticDataLoader;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by panda on 4/13/17.
 */
public class GoogleLocationGeodeticDataLoader implements GeodeticDataLoader<LocalDate> {

    private String filePath;
    private long accuracyThreshold;

    public GoogleLocationGeodeticDataLoader(String filePath, long accuracyThreshold) {
        this.filePath = filePath;
        this.accuracyThreshold = accuracyThreshold;
    }

    @Override
    public List<GeodeticData<LocalDate>> load() throws GeodeticDataLoadException {

        Gson gson = new Gson();

        GoogleLocations locations;
        try {
            locations = gson.fromJson(
                    new BufferedReader(
                            new FileReader(filePath)
                    ), GoogleLocations.class);
        } catch (FileNotFoundException e) {
            throw new GeodeticDataLoadException(e);
        }

        Stream<GoogleLocation> stream = locations.getLocations().stream();

        stream = stream.filter(l -> l.getX() != 0 && l.getY() != 0);

        if (accuracyThreshold != -1) {
            stream = stream.filter(l -> l.getAccuracy() < accuracyThreshold);
        }

        List<GeodeticData<LocalDate>> points = stream
                .map(p -> new GeodeticData<>(new LongLat(p.getX() / 1e7f, p.getY() /1e7f), p.getDate()))
                .collect(Collectors.toList());

        return points;
    }
}
