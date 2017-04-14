package co.uk.ticklethepanda.location.history.cartograph;

import java.awt.geom.Rectangle2D;

public interface Cartograph<E extends Point> {

    boolean insert(E mp);

    int queryRange(Rectangle2D rect);

    Rectangle2D getBoundingRectangle();
}
