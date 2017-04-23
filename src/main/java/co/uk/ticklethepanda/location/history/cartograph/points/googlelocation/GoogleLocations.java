package co.uk.ticklethepanda.location.history.cartograph.points.googlelocation;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleLocations {

    private static final Logger LOG = LogManager.getLogger();

    public static class Loader {
        public static GoogleLocations fromFile(String fileName)
                throws JsonSyntaxException, JsonIOException, FileNotFoundException {
            Gson gson = new Gson();

            GoogleLocations locations = gson.fromJson(
                    new BufferedReader(
                            new FileReader(fileName)
                    ), GoogleLocations.class);

            return locations.filterNullIslands();

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
        this.locations = new ArrayList<>();
    }

    public GoogleLocations(List<GoogleLocation> locations) {
        this.locations = locations;
    }

    public GoogleLocations filterNullIslands() {
        GoogleLocations noNullIslands = new GoogleLocations(
                this.getLocations().stream()
                        .filter(l -> !l.isNullIsland())
                        .collect(Collectors.toList())
        );

        LOG.info("found {} null island(s)", locations.size() - noNullIslands.getLocations().size());

        return noNullIslands;
    }

    public GoogleLocations filterInaccurate(long accuracyThreshold) {
        return new GoogleLocations(
                this.getLocations().stream()
                        .filter(l -> l.getAccuracy() < accuracyThreshold)
                        .collect(Collectors.toList())
        );
    }
}
