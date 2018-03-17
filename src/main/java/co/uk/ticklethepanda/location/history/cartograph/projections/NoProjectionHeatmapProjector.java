package co.uk.ticklethepanda.location.history.cartograph.projections;

import co.uk.ticklethepanda.location.history.cartograph.model.GeodeticDataCollection;
import co.uk.ticklethepanda.location.history.cartograph.Rectangle;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDimensions;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapProjector;
import co.uk.ticklethepanda.location.history.cartograph.projection.EuclidPoint;
import co.uk.ticklethepanda.location.history.cartograph.projection.LongLat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NoProjectionHeatmapProjector implements HeatmapProjector<LocalDate> {

    private static float calculateActualXScale(float scaleValue) {
        return scaleValue;
    }

    private static float calculateActualYScale(float scaleValue) {
        return -scaleValue / 2.0f;
    }

    private static Logger LOG = LogManager.getLogger();

    private final GeodeticDataCollection<LocalDate> geodeticDataCollection;
    private HeatmapDimensions size;
    private LongLat center;
    private float scale;
    private Predicate<LocalDate> filter;

    public NoProjectionHeatmapProjector(
            GeodeticDataCollection<LocalDate> geodeticDataCollection,
            HeatmapDescriptor<LocalDate> heatmapDescriptor
    ) {
        this.geodeticDataCollection = geodeticDataCollection;
        this.size = heatmapDescriptor.getDimensions();
        this.center = heatmapDescriptor.getCenter();
        this.scale = heatmapDescriptor.getScale();
        this.filter = heatmapDescriptor.getFilter().orElse(null);
    }

    @Override
    public GeodeticDataCollection<LocalDate> getGeodeticDataCollection() {
        return geodeticDataCollection;
    }

    @Override
    public HeatmapDimensions getViewSize() {
        return size;
    }

    @Override
    public void setViewSize(HeatmapDimensions size) {
        this.size = size;
    }

    @Override
    public void translate(EuclidPoint point) {
        this.center = new LongLat(
                center.getLongitude() + point.getX() * calculateActualXScale(scale),
                center.getLatitude() + point.getY() * calculateActualYScale(scale)
        );
    }

    @Override
    public void translate(LongLat point) {
        this.center = new LongLat(
                center.getLongitude() + point.getLongitude(),
                center.getLatitude() + point.getLatitude()
        );
    }

    @Override
    public void scaleBy(float scale) {
        this.scale *= scale;
    }

    @Override
    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public void setCenter(LongLat point) {
        this.center = point;
    }

    @Override
    public void scaleAround(EuclidPoint point, float scaleMult) {
        float diffScale = calculateScaleDelta(scaleMult);

        float offsetLat = (point.getX() - size.getWidth() / 2.0f) * calculateActualXScale(diffScale);
        float offsetLong = (point.getY() - size.getHeight() / 2.0f) * calculateActualYScale(diffScale);

        this.translate(new LongLat(
                offsetLat,
                offsetLong
        ));

        this.scaleBy(scaleMult);
    }

    @Override
    public void scaleAround(LongLat point, float scaleMult) {
        float scaleDetla = calculateScaleDelta(scaleMult);

        float offsetLat = point.getLongitude() * calculateScaleDelta(scaleDetla);
        float offsetLong = point.getLatitude() * calculateScaleDelta(scaleDetla);

        this.translate(new LongLat(
                offsetLat,
                offsetLong
        ));

        this.scaleBy(scaleMult);
    }

    @Override
    public void setFilter(Predicate<LocalDate> filter) {
        this.filter = filter;
    }

    @Override
    public Heatmap<LocalDate> project() {
        if (size == null) {
            throw new IllegalStateException("a size must be defined");
        }

        LOG.debug("projecting: {}", this);

        final float xScale = calculateActualXScale(scale);
        final float yScale = calculateActualYScale(scale);

        final int viewWidth = (int) Math.ceil(size.getWidth());
        final int viewHeight = (int) Math.ceil(size.getHeight());

        final int[][] projection = new int[viewWidth][viewHeight];

        final float projectionStartX = center.getLongitude() - (float) viewWidth / 2.0f * xScale;
        final float projectionStartY = center.getLatitude() - (float) viewHeight / 2.0f * yScale;

        LOG.trace("bounding rectangle: {}", geodeticDataCollection.getBoundingRectangle());
        LOG.trace("projectionStartX: {}", projectionStartX);
        LOG.trace("projectionStartY: {}", projectionStartY);

        for (int x = 0; x < viewWidth; x++) {
            for (int y = 0; y < viewHeight; y++) {
                float xStart = projectionStartX + x * xScale;
                float yStart = projectionStartY + y * yScale;

                Rectangle block = createPositiveRectangle(
                        xStart, yStart,
                        xScale, yScale
                );

                if (filter != null) {
                    projection[x][y] = geodeticDataCollection.countMatchingPoints(block, filter);
                } else {
                    projection[x][y] = geodeticDataCollection.countPoints(block);
                }

            }
        }

        LOG.trace("image values sorted: {}", () ->
                Arrays.stream(projection)
                        .flatMapToInt(Arrays::stream)
                        .boxed()
                        .sorted(Comparator.reverseOrder())
                        .filter(i -> i > 0)
                        .collect(Collectors.toList())
        );

        return new Heatmap<>(projection, center, scale);
    }

    @Override
    public String toString() {
        return "LatLongDateHeatmapProjector{" +
                "geodeticDataCollection.bounds=" + geodeticDataCollection.getBoundingRectangle() +
                ", size=" + size +
                ", center=" + center +
                ", scaleBy=" + scale +
                '}';
    }

    private Rectangle createPositiveRectangle(float x, float y, float width, float height) {
        if (width < 0) {
            x -= width;
            width = -width;
        }
        if (height < 0) {
            y -= height;
            height = -height;
        }

        return new Rectangle(x, y, width, height);
    }

    private float calculateScaleDelta(float scaleMult) {
        return scale - scale * scaleMult;
    }
}
