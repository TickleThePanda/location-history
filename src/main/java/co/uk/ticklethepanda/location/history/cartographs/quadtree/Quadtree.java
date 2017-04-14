package co.uk.ticklethepanda.location.history.cartographs.quadtree;

import co.uk.ticklethepanda.location.history.cartograph.SpatialCollection;
import co.uk.ticklethepanda.location.history.cartograph.Point;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Quadtree<E extends Point> implements SpatialCollection<E> {
    private static final int DEFAULT_MAX_STORAGE = 1;
    private final Rectangle2D boundingRectangle;
    private final ArrayList<E> points = new ArrayList<E>();
    private Quadtree<E> northWest;
    private Quadtree<E> northEast;
    private Quadtree<E> southEast;
    private Quadtree<E> southWest;
    private int count = 0;

    public Quadtree(Rectangle2D rectangle) {
        this.boundingRectangle = rectangle;
    }

    public Quadtree(List<E> points) {
        this.boundingRectangle = Point.getBoundingRectangle(points);
        points.forEach(this::add);
    }

    public boolean add(E point) {

        // if this doesn't contain item
        if (!boundingRectangle.contains(point.getX(), point.getY()))
            return false;

        count++;

        // if this does, increase the count
        if (points.size() > DEFAULT_MAX_STORAGE) {
            if (this.isLeaf()) {
                subdivide();
            }
            if (northEast.add(point))
                return true;
            if (northWest.add(point))
                return true;
            if (southEast.add(point))
                return true;
            if (southWest.add(point))
                return true;
        } else {
            points.add(point);
        }
        return false;

    }

    /**
     * Returns number count of points within rectangle
     *
     * @param shape
     * @return
     */
    public int countPointsInside(Shape shape) {

        if (!shape.intersects(this.boundingRectangle)) {
            return 0;
        }

        if (shape.contains(boundingRectangle)) {
            return count;
        }

        int count = 0;
        for (Point mp : points) {
            if (shape.contains(mp.getX(), mp.getY())) {
                count++;
            }
        }
        if (!isLeaf()) {
            count += northWest.countPointsInside(shape);
            count += northEast.countPointsInside(shape);
            count += southWest.countPointsInside(shape);
            count += southEast.countPointsInside(shape);
        }
        return count;
    }

    @Override
    public Rectangle2D getBoundingRectangle() {
        return boundingRectangle;
    }

    private boolean isLeaf() {
        return northWest == null || northEast == null || southWest == null
                || southWest == null;
    }

    private void subdivide() {
        double newWidth = boundingRectangle.getWidth() / 2;
        double newHeight = boundingRectangle.getHeight() / 2;

        // minX, minY
        Rectangle2D.Double nwRect = new Rectangle2D.Double(
                boundingRectangle.getMinX(), boundingRectangle.getMinY(),
                newWidth, newHeight);

        // minX, cenY
        Rectangle2D.Double swRect = new Rectangle2D.Double(
                boundingRectangle.getMinX(), boundingRectangle.getCenterY(),
                newWidth, newHeight);

        // cenX, minY
        Rectangle2D.Double neRect = new Rectangle2D.Double(
                boundingRectangle.getCenterX(), boundingRectangle.getMinY(),
                newWidth, newHeight);

        // cenX, cenY
        Rectangle2D.Double seRect = new Rectangle2D.Double(
                boundingRectangle.getCenterX(), boundingRectangle.getCenterY(),
                newWidth, newHeight);

        northWest = new Quadtree<>(nwRect);
        northEast = new Quadtree<E>(neRect);
        southWest = new Quadtree<E>(swRect);
        southEast = new Quadtree<E>(seRect);
    }
}
