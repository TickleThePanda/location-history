package uk.co.ticklethepanda.carto.apps.stats.distance;

import uk.co.ticklethepanda.carto.apps.stats.markers.loader.Marker;
import uk.co.ticklethepanda.carto.apps.stats.aggregator.LocationChange;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class DistanceFromMarkerCalculator {

    public static final float METRES_IN_KILOMETRES = 1000f;
    public static final float SECONDS_IN_HOURS = 60f * 60f;

    private final List<PointData<LongLat, Marker>> markers;

    public DistanceFromMarkerCalculator(
            List<PointData<LongLat, Marker>> markers
    ) {
        this.markers = markers;
    }

    public DistanceFromMarker getDistanceFromMarker(LocationChange change) {
        var date = change.next().data();
        var prevDate = change.prev().data();
        var point = change.next().getPoint();

        var marker = getAnnotatedDate(date.toLocalDate());

        float distanceToHomeInKm = point.getDistanceInMetersTo(marker.getPoint()) / METRES_IN_KILOMETRES;
        float hoursSinceLastLocation = ((float) prevDate.until(date, ChronoUnit.SECONDS)) / SECONDS_IN_HOURS;
        float kmHours = Math.abs(distanceToHomeInKm * hoursSinceLastLocation);

        return new DistanceFromMarker(kmHours, 1f, marker.data());
    }

    private PointData<LongLat, Marker> getAnnotatedDate(
            LocalDate date
    ) {

        var markersBeforeDate = markers
                .stream()
                .filter(marker -> date.isAfter(marker.getData().date()))
                .collect(Collectors.toList());

        return markersBeforeDate.get(markersBeforeDate.size() - 1);
    }
}

