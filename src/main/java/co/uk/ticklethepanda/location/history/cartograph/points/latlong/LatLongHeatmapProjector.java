package co.uk.ticklethepanda.location.history.cartograph.points.latlong;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapProjector;
import co.uk.ticklethepanda.location.history.cartograph.SpatialCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class LatLongHeatmapProjector implements HeatmapProjector<LatLong> {

    private static Logger LOG = LogManager.getLogger();

    private static double calculateActualXScale(double scaleValue) {
        return scaleValue;
    }

    private static double calculateActualYScale(double scaleValue) {
        return -scaleValue / 2.0;
    }

    private final SpatialCollection<LatLong> spatialCollection;
    private Point2D size;
    private LatLong center;
    private double scale;

    public LatLongHeatmapProjector(
            SpatialCollection<LatLong> spatialCollection,
            Point2D size
    ) {
        this.spatialCollection = spatialCollection;
        this.size = size;

        this.scale = spatialCollection.getBoundingRectangle().getWidth() / size.getX();

        this.center = new LatLong(
                spatialCollection.getBoundingRectangle().getCenterX(),
                spatialCollection.getBoundingRectangle().getCenterY()
        );
    }

    public LatLongHeatmapProjector(
            SpatialCollection<LatLong> spatialCollection,
            Point2D size,
            LatLong center,
            double scale
    ) {
        this.spatialCollection = spatialCollection;
        this.size = size;
        this.center = center;
        this.scale = scale;
    }

    @Override
    public SpatialCollection<LatLong> getSpatialCollection() {
        return spatialCollection;
    }

    @Override
    public Point2D getViewSize() {
        return size;
    }

    @Override
    public void setViewSize(Point2D size) {
        this.size = size;
    }

    @Override
    public void translate(Point2D point) {
        this.center = new LatLong(
                center.getX() + point.getX() * calculateActualXScale(scale),
                center.getY() + point.getY() * calculateActualYScale(scale)
        );
    }

    @Override
    public void translate(LatLong point) {
        this.center = new LatLong(
                center.getX() + point.getX(),
                center.getY() + point.getY()
        );
    }

    @Override
    public void scaleBy(double scale) {
        this.scale *= scale;
    }

    @Override
    public void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public void setCenter(LatLong point) {
        this.center = point;
    }

    @Override
    public void scaleAround(Point2D point, double scaleMult) {
        double diffScale = calculateScaleDelta(scaleMult);

        double offsetLat = (point.getX() - size.getX() / 2.0) * calculateActualXScale(diffScale);
        double offsetLong = (point.getY() - size.getY() / 2.0) * calculateActualYScale(diffScale);

        this.translate(new LatLong(
                offsetLat,
                offsetLong
        ));

        this.scaleBy(scaleMult);
    }

    @Override
    public void scaleAround(LatLong point, double scaleMult) {
        double scaleDetla = calculateScaleDelta(scaleMult);

        double offsetLat = point.getX() * calculateScaleDelta(scaleDetla);
        double offsetLong = point.getY() * calculateScaleDelta(scaleDetla);

        this.translate(new LatLong(
                offsetLat,
                offsetLong
        ));

        this.scaleBy(scaleMult);
    }

    @Override
    public Heatmap<LatLong> project() {
        if (size == null) {
            throw new IllegalStateException("a size must be defined");
        }

        LOG.debug("projecting: {}", this);

        final double xScale = calculateActualXScale(scale);
        final double yScale = calculateActualYScale(scale);

        final int viewWidth = (int) Math.ceil(size.getX());
        final int viewHeight = (int) Math.ceil(size.getY());

        final int[][] projection = new int[viewWidth][viewHeight];

        final double projectionStartX = center.getX() - (double) viewWidth / 2.0 * xScale;
        final double projectionStartY = center.getY() - (double) viewHeight / 2.0 * yScale;

        LOG.trace("bounding rectangle: {}", spatialCollection.getBoundingRectangle());
        LOG.trace("projectionStartX: {}", projectionStartX);
        LOG.trace("projectionStartY: {}", projectionStartY);

        for (int x = 0; x < viewWidth; x++) {
            for (int y = 0; y < viewHeight; y++) {
                double xStart = projectionStartX + x * xScale;
                double yStart = projectionStartY + y * yScale;

                Rectangle2D block = createPositiveRectangle(
                        xStart, yStart,
                        xScale, yScale
                );

                projection[x][y] = spatialCollection.countPointsInside(block);
            }
        }

        LOG.trace("image values sorted: {}", () ->
                Arrays.stream(projection)
                        .flatMapToInt(arr -> Arrays.stream(arr))
                        .boxed()
                        .sorted(Comparator.reverseOrder())
                        .filter(i -> i > 0)
                        .collect(Collectors.toList())
        );

        return new Heatmap<>(projection, center, scale);
    }

    @Override
    public String toString() {
        return "LatLongHeatmapProjector{" +
                "spatialCollection.bounds=" + spatialCollection.getBoundingRectangle() +
                ", size=" + size +
                ", center=" + center +
                ", scaleBy=" + scale +
                '}';
    }

    private Rectangle2D createPositiveRectangle(double x, double y, double width, double height) {
        if (width < 0) {
            x -= width;
            width = -width;
        }
        if (height < 0) {
            y -= height;
            height = -height;
        }

        return new Rectangle2D.Double(x, y, width, height);
    }

    private double calculateScaleDelta(double scaleMult) {
        return scale - scale * scaleMult;
    }
}
