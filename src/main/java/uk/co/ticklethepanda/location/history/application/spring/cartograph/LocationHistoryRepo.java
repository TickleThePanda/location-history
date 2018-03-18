package uk.co.ticklethepanda.location.history.application.spring.cartograph;

import uk.co.ticklethepanda.location.history.cartograph.model.PointData;
import uk.co.ticklethepanda.location.history.cartograph.projection.LongLat;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.GeodeticDataLoadException;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.google.GoogleLocationGeodeticDataLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

@Repository
public class LocationHistoryRepo {

    private static final Logger LOG = LogManager.getLogger();

    private final GoogleLocationGeodeticDataLoader dataLoader;

    private List<PointData<LongLat, LocalDate>> history;

    @Autowired
    public LocationHistoryRepo(
            @Value("${location.history.file.path}") String filePath,
            @Value("${location.history.accuracyThreshold}") long accuracyThreshold
    ) {
        this.dataLoader = new GoogleLocationGeodeticDataLoader(filePath, accuracyThreshold);
    }

    @PostConstruct
    public void init() throws GeodeticDataLoadException {
        LOG.info("Loading map data");
        history = dataLoader.load();
        LOG.info("Loaded map data");
    }

    public List<PointData<LongLat, LocalDate>> getLocationHistory() {
        return this.history;
    }

}
