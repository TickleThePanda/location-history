package co.uk.ticklethepanda.location.history.cartograph.model;

import co.uk.ticklethepanda.location.history.cartograph.Rectangle;

import java.util.function.Predicate;

/**
 * A collection of projections which
 *
 */
public interface GeodeticDataCollection<T> {

    /**
     * Add a point to the collection.
     *
     * @param point
     * @return
     */
    void add(GeodeticData<T> point);

    /**
     * Count the number of projections that fall within the given shape.
     *
     * @param shape
     * @return
     */
    int countPoints(Rectangle shape);

    int countMatchingPoints(Rectangle shape, Predicate<T> filter);

    Rectangle getBoundingRectangle();
}
