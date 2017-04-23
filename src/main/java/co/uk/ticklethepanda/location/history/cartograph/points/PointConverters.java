package co.uk.ticklethepanda.location.history.cartograph.points;

import co.uk.ticklethepanda.location.history.cartograph.PointConverter;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLong;
import co.uk.ticklethepanda.location.history.cartograph.points.googlelocation.GoogleLocation;

/**
 * Created by panda on 4/13/17.
 */
public class PointConverters {

    public static final PointConverter<GoogleLocation, LatLong> GOOGLE_TO_LAT_LONG =
            p -> {
                double x = p.getX() / 1e7;
                double y = p.getY() / 1e7;
                return new LatLong(x, y);
            };

    public static final PointConverter<GoogleLocation, LatLong> LAT_LONG_TO_GOOGLE =
            p -> {
                double x = p.getX() * 1e7;
                double y = p.getY() * 1e7;
                return new LatLong(x, y);
            };
}
