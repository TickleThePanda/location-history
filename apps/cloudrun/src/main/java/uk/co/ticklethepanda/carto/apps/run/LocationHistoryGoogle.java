package uk.co.ticklethepanda.carto.apps.run;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import uk.co.ticklethepanda.carto.apps.gallery.GalleryBuilder;
import uk.co.ticklethepanda.carto.apps.gallery.GalleryConfig;
import uk.co.ticklethepanda.carto.apps.gallery.GalleryImage;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.projections.SphericalPsuedoMercatorProjector;
import uk.co.ticklethepanda.carto.loaders.google.GoogleLocationGeodeticDataLoader;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class LocationHistoryGoogle {

    private static final String CONFIGURATION_STORAGE_NAME = "location-history-config";
    private static final String GALLERY_STORAGE_NAME = "location-history-gallery";

    public static void main(String[] args) throws IOException {

        GalleryBuilder builder = new GalleryBuilder(
            LocationHistoryGoogle::readHistory,
            LocationHistoryGoogle::loadConfig,
            LocationHistoryGoogle::writeImage
        );

        builder.build(new SphericalPsuedoMercatorProjector());

    }

    private static GalleryConfig loadConfig() throws JsonSyntaxException, JsonIOException, IOException {
        var configLocation = String.format("gs://%s/config.json", CONFIGURATION_STORAGE_NAME);
        var configUri = URI.create(configLocation);
        var configPath = Paths.get(configUri);

        var reader = new InputStreamReader(Files.newInputStream(configPath));

        return new Gson().fromJson(reader, GalleryConfig.class);
    }

    private static List<PointData<LongLat, LocalDateTime>> readHistory() throws IOException {
        var historyLocation = String.format("gs://%s/history.json", CONFIGURATION_STORAGE_NAME);
        var historyUri = URI.create(historyLocation);
        var historyPath = Paths.get(historyUri);

        var reader = new InputStreamReader(Files.newInputStream(historyPath));
        var loader = new GoogleLocationGeodeticDataLoader(reader, -1);

        return loader.load();
    }
    
    private static void writeImage(GalleryImage image) throws IOException {
    
        var heatmapName = image.getImageName();
        var filterName = image.getFilterName();

        var imageKey = "location-history/" + heatmapName + "-" + filterName + ".png";
        var imageLocation = "gs://" + GALLERY_STORAGE_NAME + "/" + imageKey;
        var imageUri = URI.create(imageLocation);
        var imagePath = Paths.get(imageUri);

        Files.copy(image.stream(), imagePath);
        
    }

}
