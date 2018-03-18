package uk.co.ticklethepanda.location.history.application.spring.heatmap;

import uk.co.ticklethepanda.location.history.application.spring.cartograph.LocationHistoryRepo;
import uk.co.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import uk.co.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import uk.co.ticklethepanda.location.history.cartograph.heatmap.HeatmapProjector;
import uk.co.ticklethepanda.location.history.cartograph.projection.Projector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
