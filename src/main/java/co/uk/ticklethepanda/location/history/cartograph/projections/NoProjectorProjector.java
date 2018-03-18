package co.uk.ticklethepanda.location.history.cartograph.projections;

import co.uk.ticklethepanda.location.history.cartograph.projection.EuclidPoint;
import co.uk.ticklethepanda.location.history.cartograph.projection.LongLat;
import co.uk.ticklethepanda.location.history.cartograph.projection.Projector;

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
