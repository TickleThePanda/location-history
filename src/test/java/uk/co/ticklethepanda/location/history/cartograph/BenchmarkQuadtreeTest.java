package uk.co.ticklethepanda.location.history.cartograph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import uk.co.ticklethepanda.location.history.cartograph.model.PointData;
import uk.co.ticklethepanda.location.history.cartograph.models.quadtree.Quadtree;
import uk.co.ticklethepanda.location.history.cartograph.projection.LongLat;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.GeodeticDataLoadException;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.GeodeticDataLoader;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.google.GoogleLocationGeodeticDataLoader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BenchmarkQuadtreeTest {

    private static final Logger LOG = LogManager.getLogger();

    private static Quadtree<LongLat, LocalDate> quadtree;

    @BeforeAll
    public static void loadData() throws GeodeticDataLoadException {

        GeodeticDataLoader<LongLat, LocalDate> loader = new GoogleLocationGeodeticDataLoader("./input/location-history.json", -1);
        List<PointData<LongLat, LocalDate>> loaded = loader.load();

        quadtree = new Quadtree<>(loaded);
    }

    @Test
    @Tag("slow")
    public void benchmarkQueryWithoutFilter() {
        LocalDateTime start = LocalDateTime.now();
        for (int i = 0; i < 1000; i++) {
            quadtree.countPoints(quadtree.getBoundingRectangle());
        }

        long elapsed = start.until(LocalDateTime.now(), ChronoUnit.MILLIS);

        LOG.info("Time taken {}ms", elapsed);
    }
}
