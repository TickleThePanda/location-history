package co.uk.ticklethepanda.location.history.application.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "location.history.heatmap")
public class NamedLocations {

    private Map<String, NamedLocation> locations;

    public Map<String, NamedLocation> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, NamedLocation> namedLocations) {
        this.locations = namedLocations;
    }
}
