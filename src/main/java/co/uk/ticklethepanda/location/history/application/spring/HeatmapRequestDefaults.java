package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLong;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.geom.Point2D;

@Component
public class HeatmapRequestDefaults {
    @Value("${location.history.heatmap.width}")
    private int heatmapWidth;

    @Value("${location.history.heatmap.height}")
    private int heatmapHeight;

    @Value("${location.history.heatmap.pixelSize}")
    private int pixelSize;

    @Value("${location.history.heatmap.center.lat}")
    private double centerLat;

    @Value("${location.history.heatmap.center.long}")
    private double centerLong;

    @Value("${location.history.heatmap.scale}")
    private double scale;

    @Value("${location.history.heatmap.minScale}")
    private double minScale;

    public int getHeatmapWidth() {
        return heatmapWidth;
    }

    public int getHeatmapHeight() {
        return heatmapHeight;
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public double getCenterLat() {
        return centerLat;
    }

    public double getCenterLong() {
        return centerLong;
    }

    public double getMinScale() {
        return minScale;
    }

    public double getScale() {
        return scale;
    }

    public LatLong getCenter() {
        return new LatLong(centerLat, centerLong);
    }

    public Point2D getSize() {
        return new Point2D.Double(heatmapWidth, heatmapHeight);
    }

}
