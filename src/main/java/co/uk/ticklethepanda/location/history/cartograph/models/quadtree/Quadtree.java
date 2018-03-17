package co.uk.ticklethepanda.location.history.cartograph.models.quadtree;

import co.uk.ticklethepanda.location.history.cartograph.model.PointData;
import co.uk.ticklethepanda.location.history.cartograph.model.PointDataCollection;
import co.uk.ticklethepanda.location.history.cartograph.Rectangle;
import co.uk.ticklethepanda.location.history.cartograph.projection.LongLat;
import co.uk.ticklethepanda.location.history.cartograph.projection.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Quadtree<T extends Point, U> implements PointDataCollection<T, U> {

    private static <T extends Point, U> Rectangle getBoundingRectangle(List<PointData<T, U>> points) {
        return Point.getBoundingRectangle(points.stream().map(PointData::getPoint).collect(Collectors.toList()));
    }

    private static final int DEFAULT_MAX_STORAGE = 75;

    private static final int NW = 0;
    private static final int NE = 1;
    private static final int SE = 2;
    private static final int SW = 3;

    private final Rectangle boundingRectangle;
    private final int nodeMaxStorage;

    private List<PointData<T, U>> points = new ArrayList<>();

    private Quadtree<T, U>[] children = null;

    public Quadtree(Rectangle rectangle) {
        this(rectangle, DEFAULT_MAX_STORAGE);
    }

    public Quadtree(List<PointData<T, U>> points) {
        this(points, DEFAULT_MAX_STORAGE);
    }

    public Quadtree(Rectangle rectangle, int nodeMaxStorage) {
        this.nodeMaxStorage = nodeMaxStorage;
        this.boundingRectangle = rectangle;
    }

    public Quadtree(List<PointData<T, U>> points, int nodeMaxStorage) {
        this(getBoundingRectangle(points), nodeMaxStorage);

        points.forEach(this::add);
    }

    public void add(PointData<T, U> point) {

        // if this doesn't contain item
        if (!boundingRectangle.contains(point.getPoint().getHorizontalComponent(), point.getPoint().getVerticalComponent()))
            return;

        if (points == null || points.size() >= nodeMaxStorage) {
            if (children == null) {
                subdivide();
            }
            children[NW].add(point);
            children[NE].add(point);
            children[SE].add(point);
            children[SW].add(point);
        } else {
            points.add(point);
        }
    }

    /**
     * Returns number count of projections within rectangle
     *
     * @param shape
     * @return
     */
    public int countPoints(Rectangle shape) {
        return countMatchingPoints(shape, p -> true);
    }

    @Override
    public int countMatchingPoints(final Rectangle shape, final Predicate<U> filter) {
        if (!shape.intersects(this.boundingRectangle)) {
            return 0;
        }

        int count = 0;

        if (points != null) {
            // natural for loop for performance
            for (int i = 0; i < points.size(); i++) {
                PointData<T, U> point = points.get(i);
                if (filter.test(point.getData()) && shape.contains(
                        point.getPoint().getHorizontalComponent(),
                        point.getPoint().getVerticalComponent())) {
                    count++;
                }
            }
        }

        if (children != null) {
            count += children[NE].countMatchingPoints(shape, filter);
            count += children[NW].countMatchingPoints(shape, filter);
            count += children[SE].countMatchingPoints(shape, filter);
            count += children[SW].countMatchingPoints(shape, filter);
        }
        return count;
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return boundingRectangle;
    }

    private void subdivide() {
        float newWidth = boundingRectangle.getWidth() / 2f;
        float newHeight = boundingRectangle.getHeight() / 2f;

        // minX, minY
        Rectangle nwRect = new Rectangle(
                boundingRectangle.getMinX(), boundingRectangle.getMinY(),
                newWidth, newHeight);

        // minX, cenY
        Rectangle swRect = new Rectangle(
                boundingRectangle.getMinX(), boundingRectangle.getCentreY(),
                newWidth, newHeight);

        // cenX, minY
        Rectangle neRect = new Rectangle(
                boundingRectangle.getCentreX(), boundingRectangle.getMinY(),
                newWidth, newHeight);

        // cenX, cenY
        Rectangle seRect = new Rectangle(
                boundingRectangle.getCentreX(), boundingRectangle.getCentreY(),
                newWidth, newHeight);

        children = new Quadtree[4];

        children[NW] = new Quadtree<>(nwRect, nodeMaxStorage);
        children[NE] = new Quadtree<>(neRect, nodeMaxStorage);
        children[SE] = new Quadtree<>(seRect, nodeMaxStorage);
        children[SW] = new Quadtree<>(swRect, nodeMaxStorage);

        for (int i = 0; i < points.size(); i++) {
            children[NW].add(points.get(i));
            children[NE].add(points.get(i));
            children[SE].add(points.get(i));
            children[SW].add(points.get(i));
        }

        this.points = null;

    }
}
