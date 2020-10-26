package uk.co.ticklethepanda.carto.core.loader;

import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.Point;

import java.util.List;

public interface GeodeticDataLoader<T extends Point, U> {

    List<PointData<T, U>> load();

}
