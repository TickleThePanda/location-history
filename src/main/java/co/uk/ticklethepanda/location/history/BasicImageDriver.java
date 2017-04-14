package co.uk.ticklethepanda.location.history;

import co.uk.ticklethepanda.location.history.cartograph.Cartograph;
import co.uk.ticklethepanda.location.history.cartographs.heatmap.HeatmapGenerator;
import co.uk.ticklethepanda.location.history.cartographs.heatmap.HeatmapImagePainter;
import co.uk.ticklethepanda.location.history.cartographs.quadtree.Quadtree;
import co.uk.ticklethepanda.location.history.points.PointConverters;
import co.uk.ticklethepanda.location.history.points.ecp.EcpPoint;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocations;
import co.uk.ticklethepanda.utility.images.imagewriter.ImageWriter;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class BasicImageDriver {


    private static final String FILE_NAME = "panda-loc-hist";

    private static final int IMAGE_BLOCK_SIZE = 4;

    private static final Rectangle2D viewport = new Rectangle2D.Double(
            -208635,
            -3005546,
            264354,
            201649);

    public static void main(String[] args) throws JsonSyntaxException,
            JsonIOException, IOException {

        // get data
        System.out.println("Loading locations from file...");
        final List<GoogleLocation> locations = GoogleLocations.Loader.fromFile(FILE_NAME).getLocations();

        System.out.println("Generating map...");

        List<EcpPoint> points =
                PointConverters.GOOGLE_TO_ECP
                        .convertList(locations);

        Cartograph<EcpPoint> tree = new Quadtree<EcpPoint>(points);

        HeatmapImagePainter md = new HeatmapImagePainter(
                new HeatmapImagePainter.HeatmapColourPicker.Monotone()
        );

        HeatmapGenerator converter =
                new HeatmapGenerator(tree);

        BufferedImage image = md.paintHeatmap(
                converter.convert(viewport, 250.0 / viewport.getWidth()), IMAGE_BLOCK_SIZE);

        System.out.println("Sending image out to file...");
        ImageWriter.writeImageOut(image, "mine");

    }
}
