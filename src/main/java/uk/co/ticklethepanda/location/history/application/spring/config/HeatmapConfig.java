package uk.co.ticklethepanda.location.history.application.spring.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.ticklethepanda.location.history.cartograph.heatmap.HeatmapProjector;
import uk.co.ticklethepanda.location.history.cartograph.model.PointData;
import uk.co.ticklethepanda.location.history.cartograph.projection.LongLat;
import uk.co.ticklethepanda.location.history.cartograph.projection.Projector;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.GeodeticDataLoadException;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.GeodeticDataLoader;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.google.GoogleLocationGeodeticDataLoader;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class HeatmapConfig {

    private static final Logger LOG = LogManager.getLogger();

    @Bean(name = "heatmapProjector")
    public HeatmapProjector<LocalDate> heatmapProjector(
            @Value("${location.history.file.path}") String filePath,
            @Value("${location.history.accuracyThreshold}") long accuracyThreshold,
            Projector projector
    ) throws GeodeticDataLoadException {
        GeodeticDataLoader<LongLat, LocalDate> dataLoader = new GoogleLocationGeodeticDataLoader(filePath, accuracyThreshold);

        LOG.info("Loading map data");
        List<PointData<LongLat, LocalDate>> history = dataLoader.load();
        LOG.info("Loaded map data");

        LOG.info("Creating projection using " + projector.getClass().getName());
        HeatmapProjector<LocalDate> p = HeatmapProjector.createProjection(projector, history);
        LOG.info("Created projection");

        return p;
    }
}
