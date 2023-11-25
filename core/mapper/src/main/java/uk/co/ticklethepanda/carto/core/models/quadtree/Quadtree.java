package uk.co.ticklethepanda.carto.core.models.quadtree;

import uk.co.ticklethepanda.carto.core.model.Rectangle;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.model.PointDataCollection;
import uk.co.ticklethepanda.carto.core.projection.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Quadtree<P extends Point, E> implements PointDataCollection<P, E> {

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

    private Map<P, List<E>> points = new HashMap<>();

    private Quadtree<P, E>[] children = null;

    public Quadtree(Rectangle rectangle) {
        this(rectangle, DEFAULT_MAX_STORAGE);
    }

    public Quadtree(List<PointData<P, E>> points) {
        this(points, DEFAULT_MAX_STORAGE);
    }

    public Quadtree(Rectangle rectangle, int nodeMaxStorage) {
        this.nodeMaxStorage = nodeMaxStorage;
        this.boundingRectangle = rectangle;
    }

    public Quadtree(List<PointData<P, E>> points, int nodeMaxStorage) {
        this(getBoundingRectangle(points), nodeMaxStorage);

        points.forEach(this::add);
    }

    public void add(PointData<P, E> point) {
        add(point.getPoint(), point.getData());
    }

    public void add(P point, E data) {

        // if this doesn't contain item
        if (!boundingRectangle.contains(point.getHorizontalComponent(), point.getVerticalComponent()))
            return;

        if (points == null || points.size() >= nodeMaxStorage) {
            if (children == null) {
                subdivide();
            }
            children[NW].add(point, data);
            children[NE].add(point, data);
            children[SE].add(point, data);
            children[SW].add(point, data);
        } else {
            List<E> pointList = points.computeIfAbsent(point, p -> new ArrayList<>());
            pointList.add(data);
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
    public int countMatchingPoints(final Rectangle shape, final Predicate<E> filter) {
        if (!shape.intersects(this.boundingRectangle)) {
            return 0;
        }
        
        int count = 0;

        if (points != null) {
            for (Map.Entry<P, List<E>> entry: points.entrySet()) {
                P point = entry.getKey();
                if (shape.contains(point.getHorizontalComponent(), point.getVerticalComponent())) {
                    for (E data : entry.getValue()) {
                        if (filter.test(data)) {
                            count++;
                        }

                    }
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

        // the extra width made by these should be ignored from the parent
        float extraCentreX = Math.nextAfter(boundingRectangle.getCentreX(), Float.MAX_VALUE);
        float extraCentreY = Math.nextAfter(boundingRectangle.getCentreY(), Float.MAX_VALUE);

        // minX, minY
        Rectangle nwRect = new Rectangle(
                boundingRectangle.getMinX(), boundingRectangle.getMinY(),
                newWidth, newHeight);

        // minX, cenY
        Rectangle swRect = new Rectangle(
                boundingRectangle.getMinX(), extraCentreY,
                newWidth, newHeight);

        // cenX, minY
        Rectangle neRect = new Rectangle(
                extraCentreX, boundingRectangle.getMinY(),
                newWidth, newHeight);

        // cenX, cenY
        Rectangle seRect = new Rectangle(
                extraCentreX, extraCentreY,
                newWidth, newHeight);

        @SuppressWarnings("unchecked")
        Quadtree<P, E>[] _children = new Quadtree[4];
        this.children = _children;

        children[NW] = new Quadtree<>(nwRect, nodeMaxStorage);
        children[NE] = new Quadtree<>(neRect, nodeMaxStorage);
        children[SE] = new Quadtree<>(seRect, nodeMaxStorage);
        children[SW] = new Quadtree<>(swRect, nodeMaxStorage);

        for (Map.Entry<P, List<E>> point : points.entrySet()) {

            for (E data : point.getValue()) {
                children[NW].add(point.getKey(), data);
                children[NE].add(point.getKey(), data);
                children[SE].add(point.getKey(), data);
                children[SW].add(point.getKey(), data);
            }
        }

        this.points = null;

    }
}
