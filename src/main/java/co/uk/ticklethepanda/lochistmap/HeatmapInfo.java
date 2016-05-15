package co.uk.ticklethepanda.lochistmap;

import co.uk.ticklethepanda.lochistmap.cartograph.Heatmap;
import co.uk.ticklethepanda.lochistmap.cartograph.ecp.EcpHeatmapFactory;
import co.uk.ticklethepanda.lochistmap.cartograph.quadtree.Quadtree;
import co.uk.ticklethepanda.lochistmap.cartograph.Point;
import co.uk.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocations;

import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by panda on 15/05/16.
 */
public class HeatmapInfo {

  private static final Rectangle2D croppedImage = new Rectangle2D.Double(
      -135000,
      -2990000,
      150000,
      150000 / 800f * 1000f);

  public static void main(String[] args) throws FileNotFoundException {
    System.out.println("Loading locations from file...");
    final List<GoogleLocation> locations = GoogleLocations.Loader.fromFile(
        "panda-loc-hist").getLocations();

    System.out.println("Converting ECP coordinate system...");

    List<? extends Point> points =
        new EcpHeatmapFactory(locations).getPoints();

    Quadtree quadtree = new Quadtree(points);

    Heatmap heatmap = quadtree.convertToHeatmap(200, 200, croppedImage);

    for (int x = 0; x < heatmap.getWidth(); x++) {
      for (int y = 0; y < heatmap.getHeight(); y++) {
        if (heatmap.getValue(x, y) > 0) {
          System.out.println(heatmap.getValue(x, y));
        }
      }
    }
  }
}
