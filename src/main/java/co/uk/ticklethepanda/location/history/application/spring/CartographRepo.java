package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.GeodeticData;
import co.uk.ticklethepanda.location.history.cartograph.GeodeticDataCollection;
import co.uk.ticklethepanda.location.history.cartograph.cartographs.quadtree.Quadtree;
import co.uk.ticklethepanda.location.history.cartograph.points.Converters;
import co.uk.ticklethepanda.location.history.cartograph.points.googlelocation.GoogleLocations;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Repository
public class CartographRepo {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static final Logger LOG = LogManager.getLogger();

    private final long accuracyThreshold;
    private String filePath;
    private GeodeticDataCollection<LocalDate> cartograph;

    @Autowired
    public CartographRepo(
            @Value("${location.history.file.path}") String filePath,
            @Value("${location.history.accuracyThreshold}") long accuracyThreshold
    ) {
        this.filePath = filePath;
        this.accuracyThreshold = accuracyThreshold;
    }

    @PostConstruct
    public void loadCartographs() throws FileNotFoundException {
        LOG.info("Loading locations from file...");

        GoogleLocations locations =
                GoogleLocations.Loader
                        .fromFile(filePath);

        if (accuracyThreshold != -1) {
            locations = locations.getFiltrator()
                    .removeInaccurate(accuracyThreshold)
                    .filter();
        }

        LOG.info("Generating cumulative map");
        loadCompleteCartograph(locations);
    }

    public void loadCompleteCartograph(GoogleLocations locations) throws FileNotFoundException {

        LOG.info("Generating map...");

        List<GeodeticData<LocalDate>> points =
                Converters.GOOGLE_TO_LAT_LONG
                        .convertList(locations.getLocations());

        this.cartograph = new Quadtree<>(points);
    }

    @CacheEvict("heatmap-image")
    public void save(InputStream stream) throws IOException {
        Path path = new File(filePath).toPath();
        Path backupPath = new File(filePath + "-backup").toPath();
        LOG.info("deleting old backup ({})", backupPath.toString());
        Files.deleteIfExists(backupPath);
        LOG.info("moving old data ({}) to backup ({})", path.toString(), backupPath.toString());
        Files.move(path, backupPath);
        LOG.info("saving new data ({})", path.toString());
        Files.copy(stream, path);
        LOG.info("finished saving");

        executorService.submit(() -> {
            try {
                this.loadCartographs();
            } catch (FileNotFoundException e) {
                LOG.info("Could not load cartograph after it was uploaded.");
            }
        });
    }


    public GeodeticDataCollection<LocalDate> getCartograph() {
        return this.cartograph;
    }

}
