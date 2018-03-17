package co.uk.ticklethepanda.location.history.application.spring.cartograph;

import co.uk.ticklethepanda.location.history.cartograph.model.PointDataCollection;
import co.uk.ticklethepanda.location.history.cartograph.models.quadtree.Quadtree;
import co.uk.ticklethepanda.location.history.cartograph.projection.EuclidPoint;
import co.uk.ticklethepanda.location.history.cartograph.projection.LongLat;
import co.uk.ticklethepanda.location.history.data.loader.geodetic.GeodeticDataLoadException;
import co.uk.ticklethepanda.location.history.data.loader.geodetic.google.GoogleLocationGeodeticDataLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Repository
public class CartographRepo {

    private static final Logger LOG = LogManager.getLogger();

    private final GoogleLocationGeodeticDataLoader dataLoader;

    private PointDataCollection<LongLat, LocalDate> cartograph;

    @Autowired
    public CartographRepo(
            @Value("${location.history.file.path}") String filePath,
            @Value("${location.history.accuracyThreshold}") long accuracyThreshold
    ) {
        this.dataLoader = new GoogleLocationGeodeticDataLoader(filePath, accuracyThreshold);
    }

    @PostConstruct
    public void init() throws GeodeticDataLoadException {
        LOG.info("Loading map data");
        cartograph = new Quadtree<>(dataLoader.load());
        LOG.info("Loaded map data");
    }

    public PointDataCollection<LongLat, LocalDate> getCartograph() {
        return this.cartograph;
    }

}
