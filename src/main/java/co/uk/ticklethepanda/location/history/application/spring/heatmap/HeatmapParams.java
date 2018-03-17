package co.uk.ticklethepanda.location.history.application.spring.heatmap;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDimensions;
import co.uk.ticklethepanda.location.history.cartograph.projection.LongLat;

import java.time.LocalDate;
import java.util.function.Predicate;

public class HeatmapParams {
    private HeatmapDimensions heatmapDimensions;
    private LongLat center;
    private float scale;
    private int pixelSize;
    private Predicate<LocalDate> filter;

    public HeatmapParams(HeatmapDimensions heatmapDimensions, LongLat center, float scale, int pixelSize, Predicate<LocalDate> filter) {
        this.heatmapDimensions = heatmapDimensions;
        this.center = center;
        this.scale = scale;
        this.pixelSize = pixelSize;
        this.filter = filter;
    }

    public HeatmapDimensions getHeatmapDimensions() {
        return heatmapDimensions;
    }

    public void setHeatmapDimensions(HeatmapDimensions heatmapDimensions) {
        this.heatmapDimensions = heatmapDimensions;
    }

    public LongLat getCenter() {
        return center;
    }

    public void setCenter(LongLat center) {
        this.center = center;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public void setPixelSize(int pixelSize) {
        this.pixelSize = pixelSize;
    }
    public Predicate<LocalDate> getFilter() {
        return filter;
    }

    public void setFilter(Predicate<LocalDate> filter) {
        this.filter = filter;
    }

    public HeatmapDescriptor<LocalDate> getHeatmapDescriptor() {
        return new HeatmapDescriptor<>(
                heatmapDimensions,
                center,
                scale,
                filter
        );
    }

}
