package uk.co.ticklethepanda.location.history.data.loader.geodetic;

import uk.co.ticklethepanda.location.history.cartograph.model.PointData;
import uk.co.ticklethepanda.location.history.cartograph.projection.Point;

import java.io.IOException;
import java.util.List;

public interface GeodeticDataLoader<T extends Point, U> {

    List<PointData<T, U>> load() throws GeodeticDataLoadException;

}
