package co.uk.ticklethepanda.location.history.application.gui;

import co.uk.ticklethepanda.location.history.cartograph.SpatialCollection;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLongHeatmapProjector;
import co.uk.ticklethepanda.location.history.cartograph.cartographs.quadtree.Quadtree;
import co.uk.ticklethepanda.location.history.cartograph.points.PointConverters;
import co.uk.ticklethepanda.location.history.cartograph.points.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.location.history.cartograph.points.googlelocation.GoogleLocations;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLong;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class GUIDriver {

    public static final Logger LOG = LogManager.getLogger();

    private static final class WindowFactory implements Runnable {
        private HeatmapView mv;

        public HeatmapView createWindow() {
            return mv;
        }

        public void run() {
            JFrame jframe = new JFrame();
            jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.mv = new HeatmapView();
            jframe.getContentPane().setLayout(new BorderLayout());
            jframe.getContentPane().add(mv, BorderLayout.CENTER);
            jframe.pack();
            jframe.setVisible(true);
        }
    }

    public static void main(String[] args) throws JsonSyntaxException,
            JsonIOException, FileNotFoundException, InvocationTargetException,
            InterruptedException {

        LOG.info("Loading locations from file...");
        final List<GoogleLocation> locations = GoogleLocations.Loader.fromFile(
                "input/location-history.json").getLocations();

        List<LatLong> points = PointConverters
                .GOOGLE_TO_LAT_LONG
                .convertList(locations);

        LOG.info("Converting array to Quadtree...");
        SpatialCollection<LatLong> spatialCollection = new Quadtree<>(points);
        WindowFactory windowFactory = new WindowFactory();
        SwingUtilities.invokeAndWait(windowFactory);
        HeatmapView heatmapView = windowFactory.createWindow();

        HeatmapPresenter<LatLong> heatmapPresenter = new HeatmapPresenter<>(
                heatmapView,
                new LatLongHeatmapProjector(spatialCollection,
                        new Point2D.Double(
                                heatmapView.getWidth() / HeatmapPresenter.DEFAULT_PIXEL_SIZE,
                                heatmapView.getHeight() / HeatmapPresenter.DEFAULT_PIXEL_SIZE
                        )
                )
        );

    }

}
