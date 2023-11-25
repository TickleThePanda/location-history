package uk.co.ticklethepanda.carto.core.projections;

import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.projection.EuclidPoint;
import uk.co.ticklethepanda.carto.core.projection.Projector;

public class NoProjectorProjector implements Projector {

    @Override
    public LongLat fromEuclidPoint(EuclidPoint point) {
        return new LongLat(point.getX(), -point.getY() / 2f);
    }

    @Override
    public EuclidPoint toEuclidPoint(LongLat longLat) {
        return new EuclidPoint(longLat.getLongitude(), -longLat.getLatitude() * 2f);
    }
}
