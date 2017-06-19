package co.uk.ticklethepanda.location.history.cartograph.heatmap;

import co.uk.ticklethepanda.location.history.cartograph.Point;
import co.uk.ticklethepanda.location.history.cartograph.GeodeticDataCollection;
import co.uk.ticklethepanda.location.history.cartograph.points.euclid.EuclidPoint;

import java.util.function.Predicate;

public interface HeatmapProjector<E extends Point, T> {

    GeodeticDataCollection<E, T> getGeodeticDataCollection();

    HeatmapDimensions getViewSize();

    void setViewSize(HeatmapDimensions physicalSize);

    void setCenter(E point);

    void translate(EuclidPoint point);

    void translate(E point);

    void setScale(float scale);

    void scaleBy(float scale);

    void scaleAround(EuclidPoint aFloat, float v);

    void scaleAround(E point, float v);

    void setFilter(Predicate<T> filter);

    Heatmap<E, T> project();

}
