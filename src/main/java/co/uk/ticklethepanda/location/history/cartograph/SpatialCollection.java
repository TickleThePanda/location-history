package co.uk.ticklethepanda.location.history.cartograph;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A collection of points which
 * @param <E>
 */
public interface SpatialCollection<E extends Point>  {

    /**
     * Add a point to the collection.
     * @param point
     * @return
     */
    boolean add(E point);

    /**
     * Count the number of points that fall within the given shape.
     * @param shape
     * @return
     */
    int countPointsInside(Shape shape);

    Rectangle2D getBoundingRectangle();
}
