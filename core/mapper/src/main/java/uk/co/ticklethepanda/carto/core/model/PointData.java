package uk.co.ticklethepanda.carto.core.model;

import uk.co.ticklethepanda.carto.core.projection.Point;

public record PointData<T extends Point, U>(T point, U data) {

    public T getPoint() {
        return point;
    }

    public U getData() {
        return data;
    }

}
