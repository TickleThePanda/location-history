package co.uk.ticklethepanda.location.history.cartograph.points;

import co.uk.ticklethepanda.location.history.cartograph.Converter;
import co.uk.ticklethepanda.location.history.cartograph.GeodeticData;
import co.uk.ticklethepanda.location.history.cartograph.points.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;

import java.time.LocalDate;

/**
 * Created by panda on 4/13/17.
 */
public class Converters {

    public static final Converter<GoogleLocation, GeodeticData<LongLat, LocalDate>> GOOGLE_TO_LAT_LONG =
            p -> {
                float x = p.getX() / 1e7f;
                float y = p.getY() / 1e7f;
                return new GeodeticData<>(new LongLat(x, y), p.getDate());
            };

}
