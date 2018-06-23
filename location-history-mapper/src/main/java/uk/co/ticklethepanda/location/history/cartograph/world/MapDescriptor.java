package uk.co.ticklethepanda.location.history.cartograph.world;

import uk.co.ticklethepanda.location.history.cartograph.projection.LongLat;

public class MapDescriptor {
    private final float scale;
    private final ImageDimensions dimensions;
    private final LongLat center;

    public MapDescriptor(ImageDimensions dimensions, LongLat center, float scale) {
        this.dimensions = dimensions;
        this.center = center;
        this.scale = scale;
    }

    public float getPixelSizeInMapUnits() {
        return scale;
    }

    public ImageDimensions getDimensions() {
        return dimensions;
    }

    public LongLat getCenter() {
        return center;
    }

}
