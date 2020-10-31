package uk.co.ticklethepanda.carto.apps.web.geocoding;

import uk.co.ticklethepanda.carto.core.projection.LongLat;

public interface GeocodingClient {

    LongLat query(String query) throws GeocodingClientException;

}
