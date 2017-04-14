package co.uk.ticklethepanda.location.history.cartographs.quadtree;

import co.uk.ticklethepanda.location.history.cartograph.Cartograph;
import co.uk.ticklethepanda.location.history.cartograph.Point;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class Quadtree<E extends Point> implements Cartograph<E> {
    private final QuadtreeNode<E> root;
    private final Rectangle2D rectangle;

    public Quadtree(Rectangle2D rectangle) {
        this.rectangle = rectangle;
        root = new QuadtreeNode<>(rectangle);
    }

    public Quadtree(List<E> points) {
        this(Point.getBoundingRectangle(points));
        for (E mp : points) {
            this.insert(mp);
        }
    }

    @Override
    public boolean insert(E mp) {
        return root.insert(mp);
    }

    @Override
    public int queryRange(Rectangle2D rect) {
        return root.queryRange(rect);
    }

    @Override
    public Rectangle2D getBoundingRectangle() {
        return rectangle;
    }

}
