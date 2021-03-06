package uk.co.ticklethepanda.carto.core.heatmap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.ticklethepanda.carto.core.model.Rectangle;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.models.quadtree.Quadtree;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.projection.EuclidPoint;
import uk.co.ticklethepanda.carto.core.projection.Projector;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HeatmapProjector<T> {

    private static final Logger LOG = LogManager.getLogger();

    public static <T> HeatmapProjector<T> createProjection(Projector projector, List<PointData<LongLat, T>> points) {
        List<PointData<EuclidPoint, T>> euclidPoints = points.stream()
                .map(p -> new PointData<>(projector.toEuclidPoint(p.getPoint()), p.getData()))
                .collect(Collectors.toList());

        Quadtree<EuclidPoint, T> projection = new Quadtree<>(euclidPoints);

        LOG.trace("Projection contains {} after being given {}",
                () -> projection.countPoints(projection.getBoundingRectangle()),
                points::size);

        return new HeatmapProjector<>(projector, projection);
    }

    private static Rectangle createPositiveRectangle(float x, float y, float width, float height) {
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

    private final Quadtree<EuclidPoint, T> quadtree;
    private final Projector projector;

    private HeatmapProjector(Projector projector, Quadtree<EuclidPoint, T> quadtree) {
        this.projector = projector;
        this.quadtree = quadtree;
    }

    public Heatmap<T> project(HeatmapDescriptor<T> descriptor) {
        final LongLat centre = descriptor.getCenter();
        final HeatmapDimensions dimensions = descriptor.getDimensions();
        final float boxSize = descriptor.getBoxSize();
        final Predicate<T> filter = descriptor.getFilter().orElse(null);

        final EuclidPoint imageCentre = projector.toEuclidPoint(centre);

        final float widthInPointUnits = boxSize * (float) dimensions.getWidth();
        final float heightInPointUnits = boxSize * (float) dimensions.getHeight();

        final float startXInPointUnits = imageCentre.getX() - widthInPointUnits / 2f;
        final float startYInPointUnits = imageCentre.getY() - heightInPointUnits / 2f;

        final int[][] projection = new int[dimensions.getWidth()][dimensions.getHeight()];

        for (int x = 0; x < dimensions.getWidth(); x++) {
            for (int y = 0; y < dimensions.getHeight(); y++) {
                float xBoxStart = startXInPointUnits + (float) x * boxSize;
                float yBoxStart = startYInPointUnits + (float) y * boxSize;

                /*
                 * Because the rectangle intersects and contains is inclusive,
                 * we don't want the inside of the boxes to be slightly smaller.
                 */
                float insideBoxSize = Math.nextDown(boxSize);

                Rectangle block = createPositiveRectangle(
                        xBoxStart, yBoxStart,
                        insideBoxSize, insideBoxSize
                );

                if (filter != null) {
                    projection[x][y] = quadtree.countMatchingPoints(block, filter);
                } else {
                    projection[x][y] = quadtree.countPoints(block);
                }

            }
        }

        return new Heatmap<>(descriptor, projection);

    }

}
