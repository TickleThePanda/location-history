package uk.co.ticklethepanda.carto.core.projection;

public interface Projector {

    LongLat fromEuclidPoint(EuclidPoint point);

    EuclidPoint toEuclidPoint(LongLat longLat);

}
