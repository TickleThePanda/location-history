package uk.co.ticklethepanda.location.history.cartograph.world;

import java.awt.*;

public class MapTheme {
    private final boolean countryMapEnabled;

    private final Color fillColor;
    private final Color waterColor;
    private final Color heatColor;
    private final Color outlineColor;

    public MapTheme(Color outlineColor, Color fillColor, Color waterColor, Color heatColor, boolean countryMapEnabled) {
        this.outlineColor = outlineColor;
        this.fillColor = fillColor;
        this.waterColor = waterColor;
        this.heatColor = heatColor;
        this.countryMapEnabled = countryMapEnabled;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public Color getWaterColor() {
        return waterColor;
    }

    public Color getHeatColor() {
        return heatColor;
    }

    public boolean isCountryMapEnabled() {
        return countryMapEnabled;
    }
}
