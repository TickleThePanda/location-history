package co.uk.ticklethepanda.location.history;

import co.uk.ticklethepanda.location.history.cartograph.Cartograph;
import co.uk.ticklethepanda.location.history.cartographs.heatmap.HeatmapPresenter;
import co.uk.ticklethepanda.location.history.cartographs.heatmap.HeatmapView;
import co.uk.ticklethepanda.location.history.cartographs.quadtree.Quadtree;
import co.uk.ticklethepanda.location.history.points.PointConverters;
import co.uk.ticklethepanda.location.history.points.ecp.EcpPoint;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocations;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class GUIDriver {

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

        System.out.println("Loading locations from file...");
        final List<GoogleLocation> locations = GoogleLocations.Loader.fromFile(
                "panda-loc-hist").getLocations();

        List<EcpPoint> points = PointConverters
                .GOOGLE_TO_ECP
                .convertList(locations);

        System.out.println("Converting array to Quadtree...");
        Cartograph<EcpPoint> cartograph = new Quadtree<>(points);
        WindowFactory windowFactory = new WindowFactory();
        SwingUtilities.invokeAndWait(windowFactory);
        HeatmapView heatmapView = windowFactory.createWindow();

        HeatmapPresenter heatmapPresenter = new HeatmapPresenter(heatmapView,
                cartograph);

    }

}
