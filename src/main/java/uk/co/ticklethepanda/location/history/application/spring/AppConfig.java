package uk.co.ticklethepanda.location.history.application.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import uk.co.ticklethepanda.location.history.cartograph.heatmap.HeatmapProjector;
import uk.co.ticklethepanda.location.history.cartograph.model.PointData;
import uk.co.ticklethepanda.location.history.cartograph.projection.LongLat;
import uk.co.ticklethepanda.location.history.cartograph.projection.Projector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.GeodeticDataLoadException;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.GeodeticDataLoader;
import uk.co.ticklethepanda.location.history.data.loader.geodetic.google.GoogleLocationGeodeticDataLoader;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver commonsMultipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(-1);
        return resolver;
    }

}
