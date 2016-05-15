package co.uk.ticklethepanda.lochistmap;

import co.uk.ticklethepanda.lochistmap.cartograph.ecp.EcpPoint;
import co.uk.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocations;
import co.uk.ticklethepanda.lochistmap.cartograph.quadtree.Quadtree;
import co.uk.ticklethepanda.lochistmap.cartograph.HeatmapPainter;
import net.kroo.elliot.GifSequenceWriter;

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
public class GifPointImageDriver {

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

      if(month > 12) {
        month = 1;
        year++;
      }

      currentMonth = year + ":" + String.format("%02d", month);

      return currentMonth;
    }
  }

  private static final String DATE_FORMAT = "YYYY:MM";

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

  private static final HeatmapPainter PAINTER = new HeatmapPainter(
      new HeatmapPainter.HeatmapColourPicker.Monotone());

  private static final Rectangle2D croppedImage = new Rectangle2D.Double(
      -135000,
      -2990000,
      150000,
      150000 / 800f * 1000f);

  private static final String OUT_NAME = "panda-points-out.gif";

  public static void main(String[] args) throws IOException {

    System.out.println("loading locations from file...");

    final List<GoogleLocation> locations =
        GoogleLocations.Loader
            .fromFile("panda-loc-hist")
            .filterInaccurate(1000)
            .getLocations();


    System.out.println("grouping by month...");

    Map<String, List<EcpPoint>> pointsGroupedByMonth
        = locations.stream().collect(
            Collectors.groupingBy(
                l -> FORMATTER.format(l.getDate()),
                Collectors.mapping(
                    l -> EcpPoint.convertLocationToECP(l),
                    Collectors.toList())));

    ImageOutputStream stream =
        new FileImageOutputStream(new File(OUT_NAME));

    GifSequenceWriter writer = new GifSequenceWriter(stream,
        BufferedImage.TYPE_INT_ARGB,
        300,
        true);

    List<String> months = getAllMonthsInRange(pointsGroupedByMonth);

    System.out.println("drawing...");

    List<EcpPoint> cumulativePoints = new ArrayList<>();
    for(String monthIdentifier : months) {

      if(pointsGroupedByMonth.containsKey(monthIdentifier)) {
        cumulativePoints.addAll(
            pointsGroupedByMonth.get(monthIdentifier));

        System.out.println(monthIdentifier);
      } else {
        System.out.println(monthIdentifier + " (missing)");
      }

      Quadtree quadtree = new Quadtree(cumulativePoints);

      BufferedImage image = PAINTER.paintHeatmap(
          quadtree.convertToHeatmap(
              233,
              233,
              croppedImage),
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
    }

    writer.close();
  }

  private static String createMonthString(String monthIdentifier) {
    String[] monthYear = monthIdentifier.split(":");
    int month = Integer.parseInt(monthYear[1]);
    int year = Integer.parseInt(monthYear[0]);

    month += 1;

    if(month > 12) {
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

    while(monthIterator.hasNext()) {
      allMonths.add(monthIterator.next());
    }
    return allMonths;
  }
}
