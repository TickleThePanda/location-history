package co.uk.ticklethepanda.location.history.examples;

import co.uk.ticklethepanda.location.history.cartograph.SpatialCollection;
import co.uk.ticklethepanda.location.history.cartographs.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartographs.SpatialCollectionAnalyser;
import co.uk.ticklethepanda.location.history.cartographs.quadtree.Quadtree;
import co.uk.ticklethepanda.location.history.points.PointConverters;
import co.uk.ticklethepanda.location.history.points.ecp.EcpPoint;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by panda on 15/05/16.
 */
public class HeatmapInfo {

    public static final Logger LOG = LogManager.getLogger();

    private static final Rectangle2D viewport = new Rectangle2D.Double(
            -135000,
            -2990000,
            150000,
            150000 / 800f * 1000f);

    public static void main(String[] args) throws FileNotFoundException {
        LOG.info("Loading locations from file...");
        final List<GoogleLocation> locations = GoogleLocations.Loader.fromFile(
                "input/panda-loc-hist.json").getLocations();

        LOG.info("Converting ECP coordinate system...");

        List<EcpPoint> points = PointConverters.GOOGLE_TO_ECP.convertList(locations);

        SpatialCollection<EcpPoint> spatialCollection = new Quadtree<>(points);
        SpatialCollectionAnalyser converter = new SpatialCollectionAnalyser(
                spatialCollection
        );

        Heatmap heatmap = converter.convertToHeatmap(viewport,
                233.0 / viewport.getWidth());
        for (int x = 0; x < heatmap.getWidth(); x++) {
            for (int y = 0; y < heatmap.getHeight(); y++) {
                if (heatmap.getValue(x, y) > 0) {
                    LOG.info(heatmap.getValue(x, y));
                }
            }
        }
    }
}
