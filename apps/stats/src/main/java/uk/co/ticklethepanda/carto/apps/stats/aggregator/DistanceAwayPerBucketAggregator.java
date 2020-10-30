package uk.co.ticklethepanda.carto.apps.stats.aggregator;

import uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets.DateBucket;
import uk.co.ticklethepanda.carto.apps.stats.markers.loader.Marker;
import uk.co.ticklethepanda.carto.apps.stats.distance.DistanceFromMarker;
import uk.co.ticklethepanda.carto.apps.stats.distance.DistanceFromMarkerCalculator;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DistanceAwayPerBucketAggregator {

    private final Function<LocalDateTime, DateBucket> grouper;

    public DistanceAwayPerBucketAggregator(
            Function<LocalDateTime, DateBucket> grouper
    ) {
        this.grouper = grouper;
    }

    public TreeMap<DateBucket, DistanceFromMarker> calculateDistanceAwayByMonth(
            List<PointData<LongLat, LocalDateTime>> locationHistory,
            List<PointData<LongLat, Marker>> markers
    ) {
        var changes = getLocationChanges(locationHistory);

        var distanceFromMarkerCalculator = new DistanceFromMarkerCalculator(markers);

        var aggregates = changes.stream()
                .parallel()
                .collect(Collectors.groupingBy(
                        c -> grouper.apply(c.next().getData()),
                        Collectors.reducing(
                                new DistanceFromMarker(),
                                distanceFromMarkerCalculator::getDistanceFromMarker,
                                DistanceFromMarker::add
                        )
                ));

        return new TreeMap<>(aggregates);
    }

    private List<LocationChange> getLocationChanges(List<PointData<LongLat, LocalDateTime>> locationHistory) {
        List<LocationChange> changes = new ArrayList<>(locationHistory.size() - 1);
        for (int i = 1; i < locationHistory.size(); i++) {
            changes.add(new LocationChange(
                    locationHistory.get(i - 1),
                    locationHistory.get(i)
            ));
        }
        return changes;
    }
}