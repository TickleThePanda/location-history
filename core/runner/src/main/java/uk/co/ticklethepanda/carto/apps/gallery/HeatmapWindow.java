package uk.co.ticklethepanda.carto.apps.gallery;

import uk.co.ticklethepanda.carto.core.heatmap.HeatmapDescriptor;
import uk.co.ticklethepanda.carto.core.heatmap.HeatmapDimensions;
import uk.co.ticklethepanda.carto.core.projection.LongLat;

import java.util.function.Predicate;

public class HeatmapWindow {

    private String name;
    private HeatmapDimensions dimensions;
    private LongLat center;
    private float boxSize;

    public HeatmapWindow(HeatmapDimensions dimensions, LongLat center, float boxSize) {
        this.dimensions = dimensions;
        this.center = center;
        this.boxSize = boxSize;
    }

    public <T> HeatmapDescriptor<T> with(Predicate<T> filter) {
        return new HeatmapDescriptor<T>(dimensions, center, boxSize, filter);
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

    @Override
    public String toString() {
        return "HeatmapWindow [name=" + name + ", dimensions=" + dimensions + ", center=" + center + ", boxSize="
                + boxSize + "]";
    }

    
}
