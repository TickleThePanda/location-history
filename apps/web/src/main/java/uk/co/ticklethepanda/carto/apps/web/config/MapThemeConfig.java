package uk.co.ticklethepanda.carto.apps.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.ticklethepanda.carto.core.world.MapTheme;

import java.awt.*;

@Configuration
public class MapThemeConfig {

    @Bean(name = "mapTheme")
    public MapTheme mapTheme(
        @Value("${map.colors.country.outline}") Integer outlineHex,
        @Value("${map.colors.country.fill}") Integer fillHex,
        @Value("${map.colors.heat.base}") Integer heatHex,
        @Value("${map.colors.heat.brightness.min:0.1}") Float heatMinBrightness,
        @Value("${map.colors.heat.brightness.max:0.9}") Float heatMaxBrightness,
        @Value("${map.colors.water}") Integer waterHex,
        @Value("${map.colors.background}") Integer backgroundHex,
        @Value("${map.countries.enabled}") boolean enabled
    ) {

        final Color heatColor = new Color(heatHex);
        final Color waterColor = new Color(waterHex);
        final Color outlineColor = new Color(outlineHex);
        final Color fillColor = new Color(fillHex);
        final Color backgroundColor = new Color(backgroundHex);

        return new MapTheme(
                new MapTheme.HeatmapTheme(heatColor, heatMinBrightness, heatMaxBrightness),
                new MapTheme.WorldMapTheme(fillColor, waterColor, outlineColor, enabled),
                backgroundColor
        );
    }
}
