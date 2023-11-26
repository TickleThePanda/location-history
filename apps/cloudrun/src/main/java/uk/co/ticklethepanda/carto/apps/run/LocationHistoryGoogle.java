package uk.co.ticklethepanda.carto.apps.run;

import com.google.cloud.storage.contrib.nio.CloudStorageFileSystem;
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

    private static final String CONFIGURATION_STORAGE_NAME = "ttp-location-history-config";
    private static final String GALLERY_STORAGE_NAME = "ttp-location-history-gallery";

    private static final CloudStorageFileSystem CONFIG_FS = CloudStorageFileSystem
            .forBucket(CONFIGURATION_STORAGE_NAME);
    private static final CloudStorageFileSystem GALLERY_FS = CloudStorageFileSystem.forBucket(GALLERY_STORAGE_NAME);

    public static void main(String[] args) throws IOException {

        GalleryBuilder builder = new GalleryBuilder(
            LocationHistoryGoogle::readHistory,
            LocationHistoryGoogle::loadConfig,
            LocationHistoryGoogle::writeImage
        );

        builder.build(new SphericalPsuedoMercatorProjector());

    }

    private static GalleryConfig loadConfig() throws JsonSyntaxException, JsonIOException, IOException {
        var configPath = CONFIG_FS.getPath("config.json");

        var reader = new InputStreamReader(Files.newInputStream(configPath));

        return new Gson().fromJson(reader, GalleryConfig.class);
    }

    private static List<PointData<LongLat, LocalDateTime>> readHistory() throws IOException {
        var historyPath = CONFIG_FS.getPath("history.json");

        var reader = new InputStreamReader(Files.newInputStream(historyPath));
        var loader = new GoogleLocationGeodeticDataLoader(reader, -1);

        return loader.load();
    }
    
    private static void writeImage(GalleryImage image) throws IOException {
    
        var heatmapName = image.getImageName();
        var filterName = image.getFilterName();

        var imageKey = "location-history/" + heatmapName + "-" + filterName + ".png";

        var imagePath = GALLERY_FS.getPath(imageKey);

        Files.copy(image.stream(), imagePath);
        
    }

}
