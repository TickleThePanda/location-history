package co.uk.ticklethepanda.location.history.points;

import co.uk.ticklethepanda.location.history.cartograph.PointConverter;
import co.uk.ticklethepanda.location.history.points.ecp.EcpPoint;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocation;

/**
 * Created by panda on 4/13/17.
 */
public class PointConverters {

    public static final PointConverter<GoogleLocation, EcpPoint> GOOGLE_TO_ECP =
            p -> {
                double x = p.getX() / 360.0;
                double y = -p.getY() / 180.0;
                return new EcpPoint(x, y);
            };

    public static final PointConverter<GoogleLocation, EcpPoint> ECP_TO_GOOGLE =
            p -> {
                double x = p.getX() * 360.0;
                double y = -p.getY() * 180.0;
                return new EcpPoint(x, y);
            };
}
