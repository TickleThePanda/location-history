package uk.co.ticklethepanda.location.history.cartograph.projections;

import uk.co.ticklethepanda.location.history.cartograph.projection.EuclidPoint;
import uk.co.ticklethepanda.location.history.cartograph.projection.LongLat;
import uk.co.ticklethepanda.location.history.cartograph.projection.Projector;

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
