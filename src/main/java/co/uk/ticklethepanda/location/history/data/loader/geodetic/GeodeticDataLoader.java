package co.uk.ticklethepanda.location.history.data.loader.geodetic;

import co.uk.ticklethepanda.location.history.cartograph.model.GeodeticData;

import java.io.IOException;
import java.util.List;

public interface GeodeticDataLoader<T> {

    List<GeodeticData<T>> load() throws IOException, GeodeticDataLoadException;

}
