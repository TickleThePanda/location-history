package uk.co.ticklethepanda.carto.apps.web.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.ticklethepanda.carto.core.heatmap.HeatmapProjector;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.projection.Projector;
import uk.co.ticklethepanda.carto.core.loader.GeodeticDataLoadException;
import uk.co.ticklethepanda.carto.core.loader.GeodeticDataLoader;
import uk.co.ticklethepanda.carto.loaders.google.GoogleLocationGeodeticDataLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
    ) throws GeodeticDataLoadException, FileNotFoundException {
        GeodeticDataLoader<LongLat, LocalDate> dataLoader = new GoogleLocationGeodeticDataLoader(new FileReader(filePath), accuracyThreshold);

        LOG.info("Loading map data");
        List<PointData<LongLat, LocalDate>> history = dataLoader.load();
        LOG.info("Loaded map data");

        LOG.info("Creating projection using " + projector.getClass().getName());
        HeatmapProjector<LocalDate> p = HeatmapProjector.createProjection(projector, history);
        LOG.info("Created projection");

        return p;
    }
}
