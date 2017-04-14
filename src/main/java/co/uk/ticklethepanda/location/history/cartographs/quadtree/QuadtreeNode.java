package co.uk.ticklethepanda.location.history.cartographs.quadtree;

import co.uk.ticklethepanda.location.history.cartograph.Point;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

class QuadtreeNode<E extends Point> {
    private static final int DEFAULT_MAX_STORAGE = 1;

    private QuadtreeNode<E> northWest;
    private QuadtreeNode<E> northEast;
    private QuadtreeNode<E> southEast;
    private QuadtreeNode<E> southWest;

    private final Rectangle2D boundingRectangle;

    private final ArrayList<E> points = new ArrayList<E>();
    private int count = 0;

    public QuadtreeNode(Rectangle2D rectangle) {
        this.boundingRectangle = rectangle;
    }

    public boolean insert(E mp) {

        // if this doesn't contain item
        if (!boundingRectangle.contains(mp.getX(), mp.getY()))
            return false;

        count++;

        // if this does, increase the count
        if (points.size() > DEFAULT_MAX_STORAGE) {
            if (this.isLeaf()) {
                subdivide();
            }
            if (northEast.insert(mp))
                return true;
            if (northWest.insert(mp))
                return true;
            if (southEast.insert(mp))
                return true;
            if (southWest.insert(mp))
                return true;
        } else {
            points.add(mp);
        }
        return false;

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

        northWest = new QuadtreeNode<>(nwRect);
        northEast = new QuadtreeNode<E>(neRect);
        southWest = new QuadtreeNode<E>(swRect);
        southEast = new QuadtreeNode<E>(seRect);
    }

    /**
     * Returns number count of points within rectangle
     *
     * @param rect
     * @return
     */
    public int queryRange(Rectangle2D rect) {

        if (!rect.intersects(this.boundingRectangle)) {
            return 0;
        }

        if (rect.contains(boundingRectangle)) {
            return count;
        }

        int count = 0;
        for (Point mp : points) {
            if (rect.contains(mp.getX(), mp.getY())) {
                count++;
            }
        }
        if (!isLeaf()) {
            count += northWest.queryRange(rect);
            count += northEast.queryRange(rect);
            count += southWest.queryRange(rect);
            count += southEast.queryRange(rect);
        }
        return count;
    }

    public boolean isLeaf() {
        return northWest == null || northEast == null || southWest == null
                || southWest == null;
    }

}
