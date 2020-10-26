package uk.co.ticklethepanda.carto.core.projections;

import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.projection.EuclidPoint;
import uk.co.ticklethepanda.carto.core.projection.Projector;

public class SphericalPsuedoMercatorProjector implements Projector {

    private static final double PI_4 = Math.PI / 4.0;

    @Override
    public LongLat fromEuclidPoint(EuclidPoint point) {
        float lon = point.getX();
        float lat = (float) -(Math.atan(Math.exp(Math.toRadians(point.getY())) / PI_4 - 1.0) * 90.0);

        return new LongLat(lon, lat);
    }

    @Override
    public EuclidPoint toEuclidPoint(LongLat longLat) {
        float x = longLat.getLongitude();
        float y = (float) -Math.toDegrees(Math.log(Math.tan(((longLat.getLatitude() / 90.0 + 1.0) * PI_4))));
        return new EuclidPoint(x, y);
    }
}
