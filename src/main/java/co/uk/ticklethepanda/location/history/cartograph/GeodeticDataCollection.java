package co.uk.ticklethepanda.location.history.cartograph;

import java.util.function.Predicate;

/**
 * A collection of points which
 *
 * @param <E>
 */
public interface GeodeticDataCollection<E extends Point, T> {

    /**
     * Add a point to the collection.
     *
     * @param point
     * @return
     */
    void add(GeodeticData<E, T> point);

    /**
     * Count the number of points that fall within the given shape.
     *
     * @param shape
     * @return
     */
    int countPoints(Rectangle shape);

    int countMatchingPoints(Rectangle shape, Predicate<T> filter);

    Rectangle getBoundingRectangle();
}
