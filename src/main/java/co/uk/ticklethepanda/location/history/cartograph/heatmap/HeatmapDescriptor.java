package co.uk.ticklethepanda.location.history.cartograph.heatmap;

import co.uk.ticklethepanda.location.history.cartograph.Point;

public class HeatmapDescriptor<E extends Point> {
    private final HeatmapDimensions dimensions;

    private final E center;
    private final double scale;

    public HeatmapDescriptor(HeatmapDimensions dimensions, E center, double scale) {
        this.dimensions = dimensions;
        this.center = center;
        this.scale = scale;
    }

    public HeatmapDimensions getDimensions() {
        return dimensions;
    }

    public E getCenter() {
        return center;
    }

    public double getScale() {
        return scale;
    }
}
