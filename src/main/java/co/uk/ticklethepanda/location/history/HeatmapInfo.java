package co.uk.ticklethepanda.location.history;

import co.uk.ticklethepanda.location.history.cartograph.Cartograph;
import co.uk.ticklethepanda.location.history.cartographs.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartographs.heatmap.HeatmapGenerator;
import co.uk.ticklethepanda.location.history.cartographs.quadtree.Quadtree;
import co.uk.ticklethepanda.location.history.points.PointConverters;
import co.uk.ticklethepanda.location.history.points.ecp.EcpPoint;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocations;

import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by panda on 15/05/16.
 */
public class HeatmapInfo {

    private static final Rectangle2D viewport = new Rectangle2D.Double(
            -135000,
            -2990000,
            150000,
            150000 / 800f * 1000f);

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Loading locations from file...");
        final List<GoogleLocation> locations = GoogleLocations.Loader.fromFile(
                "panda-loc-hist").getLocations();

        System.out.println("Converting ECP coordinate system...");

        List<EcpPoint> points = PointConverters.GOOGLE_TO_ECP.convertList(locations);

        Cartograph<EcpPoint> cartograph = new Quadtree<>(points);
        HeatmapGenerator converter = new HeatmapGenerator(
                cartograph
        );

        Heatmap heatmap = converter.convert(viewport,
                233.0 / viewport.getWidth());
        for (int x = 0; x < heatmap.getWidth(); x++) {
            for (int y = 0; y < heatmap.getHeight(); y++) {
                if (heatmap.getValue(x, y) > 0) {
                    System.out.println(heatmap.getValue(x, y));
                }
            }
        }
    }
}
