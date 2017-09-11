package co.uk.ticklethepanda.location.history.cartograph.world;

import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;

public class MapDescriptor {
    private final float scale;
    private final MapDimensions dimensions;
    private final LongLat center;

    public MapDescriptor(MapDimensions dimensions, LongLat center, float scale) {
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

    public LongLat getCenter() {
        return center;
    }
}
