package uk.co.ticklethepanda.location.history.cartograph.model;

import uk.co.ticklethepanda.location.history.cartograph.projection.Point;

public class PointData<T extends Point, U> {

    private T point;
    private U data;

    public PointData(T point, U data) {
        this.point = point;
        this.data = data;
    }

    public T getPoint() {
        return point;
    }

    public U getData() {
        return data;
    }

}
