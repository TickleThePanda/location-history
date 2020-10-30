package uk.co.ticklethepanda.carto.apps.stats.aggregator;

import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;

import java.time.LocalDateTime;

public record LocationChange(PointData<LongLat, LocalDateTime> prev, PointData<LongLat, LocalDateTime> next) {

}
