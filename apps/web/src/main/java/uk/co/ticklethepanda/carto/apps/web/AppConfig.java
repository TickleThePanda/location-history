package uk.co.ticklethepanda.carto.apps.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import uk.co.ticklethepanda.carto.apps.web.geocoding.GeocodingClient;
import uk.co.ticklethepanda.carto.apps.web.geocoding.OpenCageDataGeocodingClient;

import java.util.Objects;

@Configuration
public class AppConfig {

    private final String openCageDataApiKey;

    public AppConfig(
            @Value("${opencagedata.api.key}") String openCageDataApiKey
    ) {
        Objects.requireNonNull(openCageDataApiKey);
        this.openCageDataApiKey = openCageDataApiKey;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver commonsMultipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(-1);
        return resolver;
    }

    @Bean(name = "geocodingClient")
    public GeocodingClient geocodingClient() {
        return new OpenCageDataGeocodingClient(openCageDataApiKey);
    }

}
