package co.uk.ticklethepanda.location.history.application.spring.heatmap;

import co.uk.ticklethepanda.location.history.application.spring.cartograph.LocationHistoryRepo;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapProjector;
import co.uk.ticklethepanda.location.history.cartograph.projection.Projector;
import co.uk.ticklethepanda.location.history.cartograph.projections.NoProjectorProjector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.time.LocalDate;

@Service
public class HeatmapService {

    private static final Logger LOG = LogManager.getLogger();

    private final Projector projector;
    private HeatmapProjector<LocalDate> projection;
    private final LocationHistoryRepo repo;

    @Autowired
    public HeatmapService(LocationHistoryRepo repo, Projector projector) {
        this.repo = repo;
        this.projector = projector;
    }

    @PostConstruct
    public void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        LOG.info("Creating projection using " + projector.getClass().getName());
        this.projection = HeatmapProjector.createProjection(projector, repo.getLocationHistory());
        LOG.info("Created projection");
    }

    public Heatmap asHeatmap(
            HeatmapDescriptor<LocalDate> heatmapDescriptor) {
        return projection.project(heatmapDescriptor);
    }


}
