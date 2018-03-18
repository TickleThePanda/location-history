package uk.co.ticklethepanda.location.history.application.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.ticklethepanda.location.history.cartograph.projection.LongLat;
import uk.co.ticklethepanda.location.history.cartograph.projection.Projector;
import uk.co.ticklethepanda.location.history.cartograph.world.WorldMap;
import uk.co.ticklethepanda.location.history.cartograph.world.WorldMapDrawer;
import uk.co.ticklethepanda.location.history.data.loader.country.CountryPolygonLoader;

import java.io.IOException;
import java.util.List;

@Configuration
public class WorldMapConfig {

    @Bean(name="worldMapDrawer")
    public WorldMapDrawer worldMapDrawer(
            @Value("${countries.file.path}") String filePath,
            Projector projector
    ) throws IOException {

        List<List<LongLat>> countries = CountryPolygonLoader.countryPolygonLoader(filePath);
        WorldMap worldMap = new WorldMap(countries);
        return WorldMapDrawer.createProjector(projector, worldMap);
    }
}
