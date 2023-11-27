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
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.google.cloud.storage.contrib.nio.CloudStorageOptions.withMimeType;

public class LocationHistoryGoogle {

    private static Logger LOG = LogManager.getLogger();

    private static final String CONFIGURATION_STORAGE_NAME = "ttp-location-history-config";
    private static final String GALLERY_STORAGE_NAME = "ttp-location-history-gallery";

    private static final CloudStorageFileSystem CONFIG_FS = CloudStorageFileSystem
            .forBucket(CONFIGURATION_STORAGE_NAME);
    private static final CloudStorageFileSystem GALLERY_FS = CloudStorageFileSystem.forBucket(GALLERY_STORAGE_NAME);

    public static void main(String[] args) throws IOException {

        LOG.info("Started Cloud Run gallery builder");

        GalleryBuilder builder = new GalleryBuilder(
            LocationHistoryGoogle::readHistory,
            LocationHistoryGoogle::loadConfig,
            LocationHistoryGoogle::writeImage
        );

        LOG.info("Building gallery");

        builder.build(new SphericalPsuedoMercatorProjector());

        LOG.info("Finished run");

    }

    private static GalleryConfig loadConfig() throws JsonSyntaxException, JsonIOException, IOException {
        LOG.info("Loading config file");
        var configPath = CONFIG_FS.getPath("config.json");

        var reader = new InputStreamReader(Files.newInputStream(configPath));

        return new Gson().fromJson(reader, GalleryConfig.class);
    }

    private static List<PointData<LongLat, LocalDateTime>> readHistory() throws IOException {
        LOG.info("Loading history file");
        var historyPath = CONFIG_FS.getPath("history.json.gz");

        var reader = new InputStreamReader(new GZIPInputStream(Files.newInputStream(historyPath)));
        var loader = new GoogleLocationGeodeticDataLoader(reader, -1);

        return loader.load();
    }
    
    private static void writeImage(GalleryImage image) throws IOException {
    
        var heatmapName = image.getImageName();
        var filterName = image.getFilterName();

        var imageKey = "location-history/" + heatmapName + "-" + filterName + ".png";
        LOG.info("Writing image " + imageKey);

        var imagePath = GALLERY_FS.getPath(imageKey);

        Files.deleteIfExists(imagePath);
        Files.write(imagePath, image.stream().readAllBytes(), withMimeType("image/png"));
        
    }

}
