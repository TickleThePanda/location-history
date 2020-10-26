package uk.co.ticklethepanda.carto.apps.stats;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.loaders.google.GoogleLocationGeodeticDataLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class LocationHistoryStatsMain {
    
    private static final String AGGREGATE_FORMAT = "YYYY-MM";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(AGGREGATE_FORMAT);
    
    private static class LocalDateAdapter implements JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDate.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_DATE);
        }
    }
    
    private static record AnnotatedDate(String info, LocalDate date) {}

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

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

        var loader = new GoogleLocationGeodeticDataLoader(new FileReader(historyPath));

        var locationHistory = loader.load();

        List<PointData<LongLat, AnnotatedDate>> markers =  GSON.fromJson(
                new FileReader(markersPath),
                new TypeToken<List<PointData<LongLat, AnnotatedDate>>>(){}.getType()
        );
        
        var aggregates = new TreeMap<String, DistanceAggregate>();
        
        var lastLocation = locationHistory.get(0).data();
        
        for (var historyEntry : locationHistory) {
            var date = historyEntry.data();
            var aggregateName = date.format(FORMATTER);
            var monthLength = YearMonth.from(date).lengthOfMonth();
            var point = historyEntry.getPoint();

            var marker = getAnnotatedDate(markers, date.toLocalDate());

            aggregates.putIfAbsent(aggregateName, new DistanceAggregate());

            var aggregate = aggregates.get(aggregateName);

            var distanceToHomeInKm = Math.abs(point.getDistanceInMetersTo(marker.getPoint()) / 1000f);
            var hoursSinceLastLocation = Math.abs(lastLocation.until(date, ChronoUnit.SECONDS) / (60f * 60f));
            var kmHours = distanceToHomeInKm * hoursSinceLastLocation;

            aggregate.count++;
            aggregate.sum += kmHours / (float) monthLength;
            aggregate.marker = marker.getData().info;

            lastLocation = date;
        }

        System.out.println("Year Week, Kilometer Hours, log(km h), Marker");

        for (var entry : aggregates.entrySet()) {
            var box = entry.getKey();
            var aggregate = entry.getValue();
            System.out.println(box + "," + aggregate.sum + "," + Math.log(aggregate.sum) + "," + aggregate.marker);
        }

    }

    private static PointData<LongLat, AnnotatedDate> getAnnotatedDate(
            List<PointData<LongLat, AnnotatedDate>> markers,
            LocalDate date
    ) {
        
        var markersBeforeDate = markers
                .stream()
                .filter(marker -> date.isAfter(marker.getData().date))
                .collect(Collectors.toList());

        return markersBeforeDate.get(markersBeforeDate.size() - 1);
    }

}
