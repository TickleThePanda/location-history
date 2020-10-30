package uk.co.ticklethepanda.carto.apps.stats;

import uk.co.ticklethepanda.carto.apps.stats.aggregator.*;
import uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets.*;
import uk.co.ticklethepanda.carto.apps.stats.markers.loader.MarkersLoader;
import uk.co.ticklethepanda.carto.apps.stats.output.DistanceFromMarkerPerBucketPrinter;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.loaders.google.GoogleLocationGeodeticDataLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class LocationHistoryStatsMain {
    
    public static void main(String[] args) throws FileNotFoundException {

        if (args.length != 2) {
            System.err.println("Must have two arguments: $0 <history-path> <markers-path>");
            System.exit(1);
        }

        if (Arrays.stream(args).anyMatch(s -> s.equals("--help") || s.equals("-h"))) {
            System.out.println("""
                    Usage: $0 <history-path> <markers-path>
                    """);
            System.exit(0);
        }
        
        var historyPath = args[0];
        var markersPath = args[1];

        var locationHistory = loadLocationHistory(historyPath);

        var markers = MarkersLoader.loadMarkers(markersPath);

        var groupers = Arrays.<Function<LocalDateTime, DateBucket>>asList(
                MonthDateBucket::from,
                WeekDateBucket::from,
                MonthBucket::from,
                DayOfWeekBucket::from,
                DayOfYearBucket::from,
                WeekOfYearBucket::from,
                HourOfDayBucket::from,
                MinuteOfDayBucket::from
        );
        
        for (var grouper : groupers) {

            var distanceByGroups =
                    new DistanceAwayPerBucketAggregator(grouper)
                            .calculateDistanceAwayByMonth(locationHistory, markers);

            new DistanceFromMarkerPerBucketPrinter().print(distanceByGroups);
        }
    }

    private static List<PointData<LongLat, LocalDateTime>> loadLocationHistory(String historyPath) throws FileNotFoundException {
        return new GoogleLocationGeodeticDataLoader(new FileReader(historyPath)).load();
    }


}
