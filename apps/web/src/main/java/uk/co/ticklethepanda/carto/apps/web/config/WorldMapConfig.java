package uk.co.ticklethepanda.carto.apps.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.ticklethepanda.carto.apps.web.CountryPolygonLoader;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.projection.Projector;
import uk.co.ticklethepanda.carto.core.world.WorldMap;
import uk.co.ticklethepanda.carto.core.world.WorldMapDrawer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Configuration
public class WorldMapConfig {

    @Bean(name="worldMapDrawer")
    public WorldMapDrawer worldMapDrawer(
            @Value("${countries.file.path:}") String filePath,
            Projector projector
    ) throws IOException {

        if (!filePath.equals("")) {
            List<List<LongLat>> countries = CountryPolygonLoader.countryPolygonLoader(filePath);
            WorldMap worldMap = new WorldMap(countries);
            return WorldMapDrawer.createProjector(projector, worldMap);
        } else {
            return new WorldMapDrawer(projector, Collections.emptyList());
        }
    }
}
