package uk.co.ticklethepanda.location.history.cartograph.world;

import java.awt.*;

public class MapTheme {
    private final Color fillColor;
    private final Color outlineColor;

    public MapTheme(Color outlineColor, Color fillColor) {
        this.outlineColor = outlineColor;
        this.fillColor = fillColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }
}
