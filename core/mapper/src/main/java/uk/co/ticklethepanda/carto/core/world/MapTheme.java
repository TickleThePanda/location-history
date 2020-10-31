package uk.co.ticklethepanda.carto.core.world;

import java.awt.*;

public class MapTheme {

    public static class HeatmapTheme {
        private final Color heatColor;
        private final float minBrightness;
        private final float maxBrightness;

        public HeatmapTheme(Color heatColor, float minBrightness, float maxBrightness) {
            this.heatColor = heatColor;
            this.minBrightness = minBrightness;
            this.maxBrightness = maxBrightness;
        }
    }

    public static class WorldMapTheme {
        private final Color fillColor;
        private final Color waterColor;
        private final Color outlineColor;
        private final boolean worldMapEnabled;

        public WorldMapTheme(Color fillColor, Color waterColor, Color outlineColor, boolean worldMapEnabled) {
            this.fillColor = fillColor;
            this.waterColor = waterColor;
            this.outlineColor = outlineColor;
            this.worldMapEnabled = worldMapEnabled;
        }
    }

    private final HeatmapTheme heatmapTheme;
    private final WorldMapTheme worldMapTheme;
    private final Color backgroundColor;

    public MapTheme(HeatmapTheme heatmapTheme, WorldMapTheme worldMapTheme, Color backgroundColor) {
        this.heatmapTheme = heatmapTheme;
        this.worldMapTheme = worldMapTheme;
        this.backgroundColor = backgroundColor;
    }

    public Color getFillColor() {
        return worldMapTheme.fillColor;
    }

    public Color getOutlineColor() {
        return worldMapTheme.outlineColor;
    }

    public Color getWaterColor() {
        return worldMapTheme.waterColor;
    }

    public Color getHeatColor() {
        return heatmapTheme.heatColor;
    }
    public float getMinBrightness() {
        return heatmapTheme.minBrightness;
    }
    public float getMaxBrightness() {
        return heatmapTheme.maxBrightness;
    }

    public boolean isCountryMapEnabled() {
        return worldMapTheme.worldMapEnabled;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }


}
