package uk.co.ticklethepanda.carto.apps.lambda;

import uk.co.ticklethepanda.carto.core.heatmap.HeatmapDescriptor;
import uk.co.ticklethepanda.carto.core.heatmap.HeatmapDimensions;
import uk.co.ticklethepanda.carto.core.projection.LongLat;

import java.time.LocalDate;
import java.util.function.Predicate;

public class HeatmapConfiguration {

    private String name;
    private HeatmapDimensions dimensions;
    private LongLat center;
    private float boxSize;

    public HeatmapConfiguration(HeatmapDimensions dimensions, LongLat center, float boxSize) {
        this.dimensions = dimensions;
        this.center = center;
        this.boxSize = boxSize;
    }

    public HeatmapDescriptor<LocalDate> with(Predicate<LocalDate> filter) {
        return new HeatmapDescriptor<>(dimensions, center, boxSize, filter);
    }

    public HeatmapDimensions getDimensions() {
        return dimensions;
    }

    public LongLat getCenter() {
        return center;
    }

    public float getBoxSize() {
        return boxSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
