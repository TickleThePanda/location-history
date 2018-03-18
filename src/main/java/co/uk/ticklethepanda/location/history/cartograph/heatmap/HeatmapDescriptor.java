package co.uk.ticklethepanda.location.history.cartograph.heatmap;

import co.uk.ticklethepanda.location.history.cartograph.projection.LongLat;

import java.util.Optional;
import java.util.function.Predicate;

public class HeatmapDescriptor<T> {

    private final HeatmapDimensions dimensions;
    private final LongLat center;
    private final float boxSize;
    private final Optional<Predicate<T>> filter;

    public HeatmapDescriptor(
            HeatmapDimensions dimensions,
            LongLat center,
            float boxSize,
            Predicate<T> filter) {
        this.dimensions = dimensions;
        this.center = center;
        this.boxSize = boxSize;
        this.filter = Optional.ofNullable(filter);
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

    public Optional<Predicate<T>> getFilter() {
        return filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeatmapDescriptor<?> that = (HeatmapDescriptor<?>) o;

        if (Float.compare(that.boxSize, boxSize) != 0) return false;
        if (dimensions != null ? !dimensions.equals(that.dimensions) : that.dimensions != null)
            return false;
        if (center != null ? !center.equals(that.center) : that.center != null) return false;
        return filter != null ? filter.equals(that.filter) : that.filter == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = dimensions != null ? dimensions.hashCode() : 0;
        result = 31 * result + (center != null ? center.hashCode() : 0);
        temp = Float.floatToIntBits(boxSize);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (filter != null ? filter.hashCode() : 0);
        return result;
    }
}
