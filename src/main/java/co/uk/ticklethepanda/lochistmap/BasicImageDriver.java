package co.uk.ticklethepanda.lochistmap;

import co.uk.ticklethepanda.lochistmap.cartograph.HeatmapPainter;
import co.uk.ticklethepanda.lochistmap.cartograph.ecp.EcpHeatmapFactory;
import co.uk.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocations;
import co.uk.ticklethepanda.lochistmap.cartograph.quadtree.Quadtree;
import co.uk.ticklethepanda.lochistmap.imagewriter.ImageWriter;
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
        EcpHeatmapFactory map = new EcpHeatmapFactory(locations);

        Quadtree tree = new Quadtree(map.getPoints());

        HeatmapPainter md = new HeatmapPainter(
                new HeatmapPainter.HeatmapColourPicker.Monotone()
        );

        BufferedImage image = md.paintHeatmap(
                tree.convertToHeatmap(250,
                        viewport), IMAGE_BLOCK_SIZE);

        System.out.println("Sending image out to file...");
        ImageWriter.writeImageOut(image, "mine");

    }
}
