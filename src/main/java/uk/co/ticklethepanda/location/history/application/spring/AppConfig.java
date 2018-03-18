package uk.co.ticklethepanda.location.history.application.spring;

import uk.co.ticklethepanda.location.history.cartograph.projection.Projector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class AppConfig {

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver commonsMultipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(-1);
        return resolver;
    }

    @Bean(name = "projector")
    public Projector projector(
        @Value("${location.history.heatmap.projector}") String projectorName
    ) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        Class<?> projectorClass = Class.forName(projectorName);

        Object projector = projectorClass.newInstance();

        if (!(projector instanceof Projector)) {
            throw new IllegalArgumentException("\"location.history.heatmap.projector\" must be a type of Projector");
        }

        return (Projector) projector;

    }
}
