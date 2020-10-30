package uk.co.ticklethepanda.carto.apps.stats.output;

import uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets.DateBucket;
import uk.co.ticklethepanda.carto.apps.stats.distance.DistanceFromMarker;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DistanceFromMarkerPerBucketPrinter {

    public void print(TreeMap<DateBucket, DistanceFromMarker> distances) {

        List<String> units = distances.keySet().stream()
                .map(DateBucket::getLengthUnit)
                .distinct()
                .collect(Collectors.toList());

        if (units.size() > 1) {
            throw new IllegalStateException("A DateBucket must return only one type of unit but had: " + units.toString());
        }
        
        String unit = units.get(0);

        System.out.println("Year Week, km h / " + unit + ", log(km h / " + unit + "), Marker");

        for (var entry : distances.entrySet()) {
            var box = entry.getKey();
            var aggregate = entry.getValue();

            var perLength = aggregate.sum() / box.getLength();

            System.out.println(box.getName() + "," + perLength + "," + Math.log(perLength) + "," + aggregate.marker().info());
        }
    }
}
