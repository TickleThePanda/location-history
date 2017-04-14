package co.uk.ticklethepanda.location.history;

import co.uk.ticklethepanda.location.history.points.PointConverters;
import co.uk.ticklethepanda.location.history.points.ecp.EcpPoint;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocations;
import co.uk.ticklethepanda.utility.images.imagewriter.ImageWriter;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.List;

public class LineImageJessDriver {

    private static final int IMAGE_WIDTH = 1000;

    private static final String FILE_NAME = "panda-loc-hist";
    private static final String OUT_NAME = "line-out-name";

    public static void main(String[] args) throws JsonSyntaxException,
            JsonIOException, FileNotFoundException {
        System.out.println("Loading locations from file...");
        final List<GoogleLocation> locations = GoogleLocations.Loader
                .fromFile(FILE_NAME).getLocations();

        List<EcpPoint> points = PointConverters.GOOGLE_TO_ECP.convertList(locations);

        System.out.println("Converting ECP coordinate system...");
        Rectangle2D boundingRectangle = co.uk.ticklethepanda.location.history.cartograph.Point.getBoundingRectangle(points);
        double aspectRatio = boundingRectangle.getWidth()
                / boundingRectangle.getHeight();
        System.out.println("Drawing image...");
        BufferedImage bi = new BufferedImage(IMAGE_WIDTH,
                (int) ((double) IMAGE_WIDTH / aspectRatio),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = (Graphics2D) bi.getGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(0.01f));

        long startTime = System.currentTimeMillis();

        co.uk.ticklethepanda.location.history.cartograph.Point prev = points.get(0);
        for (co.uk.ticklethepanda.location.history.cartograph.Point mp : points) {

            double normX1 = (prev.getX() - boundingRectangle.getX())
                    / boundingRectangle.getWidth();
            double normY1 = (prev.getY() - boundingRectangle.getY())
                    / boundingRectangle.getHeight();
            double normX2 = (mp.getX() - boundingRectangle.getX())
                    / boundingRectangle.getWidth();
            double normY2 = (mp.getY() - boundingRectangle.getY())
                    / boundingRectangle.getHeight();

            double imagX1 = normX1 * (double) bi.getWidth();
            double imagY1 = normY1 * (double) bi.getHeight();
            double imagX2 = normX2 * (double) bi.getWidth();
            double imagY2 = normY2 * (double) bi.getHeight();

            g2d.draw(new Line2D.Double(imagX1, imagY1, imagX2, imagY2));
            prev = mp;
        }

        long endTime = System.currentTimeMillis();

        System.out.printf("Time taken to draw: %.3f\n", (double) (endTime - startTime) / 1000.0);
        System.out.println("Printing out to file...");
        ImageWriter.writeImageOut(bi, OUT_NAME);
        System.out.println("Finished...");

    }
}
