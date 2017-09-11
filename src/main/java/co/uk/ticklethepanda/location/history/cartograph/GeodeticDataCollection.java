package co.uk.ticklethepanda.location.history.cartograph;

import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;

import java.util.function.Predicate;

/**
 * A collection of points which
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
     * Count the number of points that fall within the given shape.
     *
     * @param shape
     * @return
     */
    int countPoints(Rectangle shape);

    int countMatchingPoints(Rectangle shape, Predicate<T> filter);

    Rectangle getBoundingRectangle();
}
