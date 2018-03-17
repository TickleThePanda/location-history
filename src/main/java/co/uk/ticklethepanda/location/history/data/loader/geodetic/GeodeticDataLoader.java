package co.uk.ticklethepanda.location.history.data.loader.geodetic;

import co.uk.ticklethepanda.location.history.cartograph.model.PointData;
import co.uk.ticklethepanda.location.history.cartograph.projection.Point;

import java.io.IOException;
import java.util.List;

public interface GeodeticDataLoader<T extends Point, U> {

    List<PointData<T, U>> load() throws IOException, GeodeticDataLoadException;

}
