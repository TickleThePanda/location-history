package co.uk.ticklethepanda.location.history.cartograph;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A collection of points which
 * @param <E>
 */
public interface Cartograph<E extends Point>  {

    boolean add(E mp);

    int countPointsInside(Shape shape);

    Rectangle2D getBoundingRectangle();
}
