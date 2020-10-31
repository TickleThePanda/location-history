package uk.co.ticklethepanda.carto.apps.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.ticklethepanda.carto.core.projection.Projector;

import java.lang.reflect.InvocationTargetException;

@Configuration
public class ProjectionConfig {

    @Bean(name = "projector")
    public Projector projector(
            @Value("${location.history.heatmap.projector}") String projectorName
    ) throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException {

        Class<?> projectorClass = Class.forName(projectorName);

        Object projector = projectorClass.getDeclaredConstructor().newInstance();

        if (!(projector instanceof Projector)) {
            throw new IllegalArgumentException("\"location.history.heatmap.projector\" must be a type of Projector");
        }

        return (Projector) projector;

    }
}
