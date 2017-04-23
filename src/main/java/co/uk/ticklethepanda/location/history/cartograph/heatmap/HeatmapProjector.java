package co.uk.ticklethepanda.location.history.cartograph.heatmap;

import co.uk.ticklethepanda.location.history.cartograph.Point;
import co.uk.ticklethepanda.location.history.cartograph.SpatialCollection;

import java.awt.geom.Point2D;

public interface HeatmapProjector<E extends Point> {

    SpatialCollection<E> getSpatialCollection();

    Point2D getViewSize();

    void setViewSize(Point2D physicalSize);

    void setCenter(E point);

    void translate(Point2D point);

    void translate(E point);

    void setScale(double scale);

    void scaleBy(double scale);

    void scaleAround(Point2D aDouble, double v);

    void scaleAround(E point, double v);

    Heatmap<E> project();

}
