package co.uk.ticklethepanda.location.history.points.googlelocation;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleLocations {

    public static class Loader {
        public static GoogleLocations fromFile(String fileName)
                throws JsonSyntaxException, JsonIOException, FileNotFoundException {
            Gson gson = new Gson();

            return gson.fromJson(new BufferedReader(new FileReader("input/"
                    + fileName + ".json")), GoogleLocations.class);

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

    public GoogleLocations filterInaccurate(long accuracyThreshold) {
        return new GoogleLocations(this.getLocations().stream().filter(l -> l.getAccuracy() < accuracyThreshold).collect(Collectors.toList()));
    }
}
