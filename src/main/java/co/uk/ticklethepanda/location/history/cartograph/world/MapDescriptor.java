package co.uk.ticklethepanda.location.history.cartograph.world;

import co.uk.ticklethepanda.location.history.cartograph.Point;

public class MapDescriptor<T extends Point> {
    private final float scale;
    private final MapDimensions dimensions;
    private final T center;

    public MapDescriptor(MapDimensions dimensions, T center, float scale) {
        this.dimensions = dimensions;
        this.center = center;
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public MapDimensions getDimensions() {
        return dimensions;
    }

    public T getCenter() {
        return center;
    }
}
