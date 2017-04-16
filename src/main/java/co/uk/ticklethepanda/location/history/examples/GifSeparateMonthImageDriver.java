package co.uk.ticklethepanda.location.history.examples;

import co.uk.ticklethepanda.location.history.cartograph.SpatialCollection;
import co.uk.ticklethepanda.location.history.cartographs.SpatialCollectionAnalyser;
import co.uk.ticklethepanda.location.history.cartographs.heatmap.HeatmapImagePainter;
import co.uk.ticklethepanda.location.history.cartographs.quadtree.Quadtree;
import co.uk.ticklethepanda.location.history.points.PointConverters;
import co.uk.ticklethepanda.location.history.points.ecp.EcpPoint;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocations;
import net.kroo.elliot.GifSequenceWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by panda on 15/05/16.
 */
public class GifSeparateMonthImageDriver {

    public static final Logger LOG = LogManager.getLogger();

    private static class StringMonthIterator implements Iterator<String> {

        private final String lastMonth;

        private String currentMonth;

        public StringMonthIterator(String firstMonth, String lastMonth) {
            this.currentMonth = firstMonth;
            this.lastMonth = lastMonth;
        }

        @Override
        public boolean hasNext() {
            return !currentMonth.equals(lastMonth);
        }

        @Override
        public String next() {
            String[] currentMonthYear = currentMonth.split(":");
            int month = Integer.parseInt(currentMonthYear[1]);
            int year = Integer.parseInt(currentMonthYear[0]);

            month += 1;

            if (month > 12) {
                month = 1;
                year++;
            }

            currentMonth = year + ":" + String.format("%02d", month);

            return currentMonth;
        }
    }

    private static final String DATE_FORMAT = "YYYY:MM";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private static final HeatmapImagePainter PAINTER = new HeatmapImagePainter(
            new HeatmapImagePainter.HeatmapColourPicker.Monotone(Color.getColor("#ccddaa")));

    private static final Rectangle2D viewport = new Rectangle2D.Double(
            -208635,
            -3005546,
            264354,
            201649);

    private static final String OUT_NAME = "output/panda-points-separate-out.gif";

    public static void main(String[] args) throws IOException {

        LOG.info("loading locations from file...");

        final List<GoogleLocation> locations =
                GoogleLocations.Loader
                        .fromFile("input/panda-loc-hist.json")
                        .filterInaccurate(1000)
                        .getLocations();


        LOG.info("grouping by month...");

        Map<String, List<EcpPoint>> pointsGroupedByMonth
                = locations.stream().collect(
                Collectors.groupingBy(
                        l -> FORMATTER.format(l.getDate()),
                        Collectors.mapping(
                                l -> PointConverters
                                        .GOOGLE_TO_ECP
                                        .convert(l),
                                Collectors.toList())));

        ImageOutputStream stream =
                new FileImageOutputStream(new File(OUT_NAME));

        GifSequenceWriter writer = new GifSequenceWriter(stream,
                BufferedImage.TYPE_INT_ARGB,
                300,
                true);

        List<String> months = getAllMonthsInRange(pointsGroupedByMonth);

        LOG.info("drawing...");


        BufferedImage lastImage = null;

        for (String monthIdentifier : months) {

            List<EcpPoint> points = pointsGroupedByMonth.get(monthIdentifier);

            LOG.info(monthIdentifier);

            SpatialCollection<EcpPoint> spatialCollection = new Quadtree<>(points);

            SpatialCollectionAnalyser converter = new SpatialCollectionAnalyser(
                    spatialCollection
            );

            BufferedImage image = PAINTER.paintHeatmap(
                    converter.convertToHeatmap(viewport,
                            233.0 / viewport.getWidth()),
                    3);

            Graphics2D graphics2D = image.createGraphics();

            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            graphics2D.setFont(new Font("Courier New", Font.PLAIN, 20));
            graphics2D.setColor(Color.BLACK);

            graphics2D.drawString(
                    createMonthString(monthIdentifier),
                    5,
                    25);

            writer.writeToSequence(image);

            lastImage = image;
        }

        //repeat last month
        for (int i = 0; i < 10; i++) {
            writer.writeToSequence(lastImage);
        }

        writer.close();
    }

    private static String createMonthString(String monthIdentifier) {
        String[] monthYear = monthIdentifier.split(":");
        int month = Integer.parseInt(monthYear[1]);
        int year = Integer.parseInt(monthYear[0]);

        month += 1;

        if (month > 12) {
            month = 1;
            year++;
        }

        return year + " - " + Month.of(month);
    }

    private static List<String> getAllMonthsInRange(Map<String, List<EcpPoint>> pointsGroupedByMonth) {
        List<String> months = pointsGroupedByMonth
                .keySet()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        String firstMonth = months.get(0);
        String lastMonth = months.get(months.size() - 1);

        Iterator<String> monthIterator = new StringMonthIterator(firstMonth, lastMonth);

        List<String> allMonths = new ArrayList<>();

        while (monthIterator.hasNext()) {
            allMonths.add(monthIterator.next());
        }
        return allMonths;
    }
}
