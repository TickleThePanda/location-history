package co.uk.ticklethepanda.location.history.cartograph.cartographs.flat;

import co.uk.ticklethepanda.location.history.cartograph.GeodeticData;
import co.uk.ticklethepanda.location.history.cartograph.GeodeticDataCollection;
import co.uk.ticklethepanda.location.history.cartograph.Point;
import co.uk.ticklethepanda.location.history.cartograph.Rectangle;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlatCartograph<E extends Point, T> implements GeodeticDataCollection<E, T> {

    private static <E extends Point, T> Rectangle getBoundingRectangle(List<GeodeticData<E, T>> points) {
        return Point.getBoundingRectangle(points.stream().map(GeodeticData::getPoint).collect(Collectors.toList()));
    }


    private final List<GeodeticData<E, T>> data;

    public FlatCartograph(List<GeodeticData<E, T>> data) {
        this.data = data;
    }

    @Override
    public void add(GeodeticData<E, T> point) {
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
                GeodeticData<E, T> point = data.get(i);
                if (filter.test(point.getData()) && shape.contains(
                        point.getPoint().getX(),
                        point.getPoint().getY())) {
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
