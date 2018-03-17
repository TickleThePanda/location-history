package co.uk.ticklethepanda.location.history.cartograph.models.flat;

import co.uk.ticklethepanda.location.history.cartograph.model.GeodeticData;
import co.uk.ticklethepanda.location.history.cartograph.model.GeodeticDataCollection;
import co.uk.ticklethepanda.location.history.cartograph.Rectangle;
import co.uk.ticklethepanda.location.history.cartograph.projection.LongLat;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlatCartograph<T> implements GeodeticDataCollection<T> {

    private static <T> Rectangle getBoundingRectangle(List<GeodeticData<T>> points) {
        return LongLat.getBoundingRectangle(points.stream().map(GeodeticData::getPoint).collect(Collectors.toList()));
    }


    private final List<GeodeticData<T>> data;

    public FlatCartograph(List<GeodeticData<T>> data) {
        this.data = data;
    }

    @Override
    public void add(GeodeticData<T> point) {
        data.add(point);
    }

    @Override
    public int countPoints(Rectangle shape) {
        return countMatchingPoints(shape, t -> true);
    }

    @Override
    public int countMatchingPoints(Rectangle shape, Predicate<T> filter) {
        int count = 0;
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                GeodeticData<T> point = data.get(i);
                if (filter.test(point.getData()) && shape.contains(
                        point.getPoint().getLongitude(),
                        point.getPoint().getLatitude())) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return getBoundingRectangle(data);
    }
}
