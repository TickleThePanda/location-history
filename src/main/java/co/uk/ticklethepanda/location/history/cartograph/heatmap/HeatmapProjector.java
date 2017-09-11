package co.uk.ticklethepanda.location.history.cartograph.heatmap;

import co.uk.ticklethepanda.location.history.cartograph.GeodeticDataCollection;
import co.uk.ticklethepanda.location.history.cartograph.points.euclid.EuclidPoint;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;

import java.util.function.Predicate;

public interface HeatmapProjector<T> {

    GeodeticDataCollection<T> getGeodeticDataCollection();

    HeatmapDimensions getViewSize();

    void setViewSize(HeatmapDimensions physicalSize);

    void setCenter(LongLat point);

    void translate(EuclidPoint point);

    void translate(LongLat point);

    void setScale(float scale);

    void scaleBy(float scale);

    void scaleAround(EuclidPoint aFloat, float v);

    void scaleAround(LongLat point, float v);

    void setFilter(Predicate<T> filter);

    Heatmap<T> project();

}
