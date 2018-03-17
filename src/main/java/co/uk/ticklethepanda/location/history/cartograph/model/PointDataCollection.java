package co.uk.ticklethepanda.location.history.cartograph.model;

import co.uk.ticklethepanda.location.history.cartograph.Rectangle;
import co.uk.ticklethepanda.location.history.cartograph.projection.Point;

import java.util.function.Predicate;

/**
 * A collection of projections which
 *
 */
public interface PointDataCollection<T extends Point, U> {

    /**
     * Add a point to the collection.
     *
     * @param point
     * @return
     */
    void add(PointData<T, U> point);

    /**
     * Count the number of projections that fall within the given shape.
     *
     * @param shape
     * @return
     */
    int countPoints(Rectangle shape);

    int countMatchingPoints(Rectangle shape, Predicate<U> filter);

    Rectangle getBoundingRectangle();
}
