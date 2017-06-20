package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDimensions;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HeatmapRequestDefaults {
    @Value("${location.history.heatmap.width}")
    private int heatmapWidth;

    @Value("${location.history.heatmap.height}")
    private int heatmapHeight;

    @Value("${location.history.heatmap.pixelSize}")
    private int pixelSize;

    @Value("${location.history.heatmap.center.lat}")
    private float centerLat;

    @Value("${location.history.heatmap.center.long}")
    private float centerLong;

    @Value("${location.history.heatmap.scale}")
    private float scale;

    @Value("${location.history.heatmap.minScale}")
    private float minScale;

    public int getHeatmapWidth() {
        return heatmapWidth;
    }

    public int getHeatmapHeight() {
        return heatmapHeight;
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public float getCenterLat() {
        return centerLat;
    }

    public float getCenterLong() {
        return centerLong;
    }

    public float getMinScale() {
        return minScale;
    }

    public float getScale() {
        return scale;
    }

    public LongLat getCenter() {
        return new LongLat(centerLat, centerLong);
    }

    public HeatmapDimensions getSize() {
        return new HeatmapDimensions(heatmapWidth, heatmapHeight);
    }

}
