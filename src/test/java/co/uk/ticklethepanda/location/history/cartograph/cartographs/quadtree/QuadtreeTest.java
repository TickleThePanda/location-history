package co.uk.ticklethepanda.location.history.cartograph.cartographs.quadtree;

import co.uk.ticklethepanda.location.history.cartograph.GeodeticData;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDimensions;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapProjector;
import co.uk.ticklethepanda.location.history.cartograph.points.Converters;
import co.uk.ticklethepanda.location.history.cartograph.points.googlelocation.GoogleLocations;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLong;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLongHeatmapProjector;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuadtreeTest {


    private static final int BUCKET_SIZE_MINIMUM = 48;
    private static final int BUCKET_SIZE_INCREMENT = 2;
    private static final int BUCKET_SIZE_MAXIMUM = 100;

    private static final long N_ITERATIONS = 1;
    private static final long NANO_SECONDS_IN_SECONDS = 1_000_000L;

    @Test
    @Ignore
    public void performance() throws FileNotFoundException {

        List<GeodeticData<LatLong, LocalDate>> data = loadFromFile();

        System.out.println("bucketSize, average time (ms)");

        for (int bucketSize = BUCKET_SIZE_MINIMUM;
             bucketSize <= BUCKET_SIZE_MAXIMUM;
             bucketSize += BUCKET_SIZE_INCREMENT) {

            long sum = 0;
            for (int iteration = 0; iteration < N_ITERATIONS; iteration++) {
                Quadtree<LatLong, LocalDate> quadtree = new Quadtree<>(data, bucketSize);

                long startTime = System.nanoTime();

                HeatmapProjector<LatLong, LocalDate> projector = new LatLongHeatmapProjector(
                        quadtree,
                        new HeatmapDescriptor<>(
                                new HeatmapDimensions(250, 190),
                                new LatLong(-2.51f, 52.1f),
                                0.0428f,
                                t -> t.getDayOfWeek().equals(DayOfWeek.MONDAY))
                );

                projector.project();

                long endTime = System.nanoTime();
                sum += endTime - startTime;
            }

            long average = sum / N_ITERATIONS;

            System.out.println(bucketSize + ", " + average / NANO_SECONDS_IN_SECONDS + "");
        }

    }

    private List<GeodeticData<LatLong, LocalDate>> loadFromFile() throws FileNotFoundException {
        GoogleLocations locations =
                GoogleLocations.Loader
                        .fromFile("input/location-history.json");

        return Converters.GOOGLE_TO_LAT_LONG
                .convertList(locations.getLocations());
    }

    private List<GeodeticData<LatLong, LocalDate>> generateRandomData() {
        Random random = new Random();

        List<GeodeticData<LatLong, LocalDate>> data = new ArrayList<>();

        for (int i = 0; i < 1_000_000; i++) {
            data.add(new GeodeticData<>(
                    new LatLong((float) random.nextDouble() * 1000.0f - 500.0f, (float) random.nextDouble() * 1000.0f - 500.0f),
                    LocalDate.now().plusDays((int) (random.nextDouble() * 7.0f - 3.5f))
            ));
        }
        return data;
    }

}
