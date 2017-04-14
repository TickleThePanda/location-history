package co.uk.ticklethepanda.location.history.examples;

import co.uk.ticklethepanda.location.history.cartograph.SpatialCollection;
import co.uk.ticklethepanda.location.history.cartograph.Point;
import co.uk.ticklethepanda.location.history.cartographs.quadtree.Quadtree;
import co.uk.ticklethepanda.location.history.points.PointConverters;
import co.uk.ticklethepanda.location.history.points.ecp.EcpPoint;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocations;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.kroo.elliot.GifSequenceWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GifLineImageDriver {

    public static final Logger LOG = LogManager.getLogger();

    private static final int IMAGE_WIDTH = 800;
    private static final int IMAGE_HEIGHT = 1000;

    private static final String FILE_NAME = "input/panda-loc-hist.json";
    private static final String OUT_NAME = "panda-line-gif-out.gif";

    private static final Rectangle2D croppedImage = new Rectangle2D.Double(
            -135000,
            -2990000,
            150000,
            150000 / 800f * 1000f);

    public static void main(String[] args) throws JsonSyntaxException,
            JsonIOException, IOException {
        final List<GoogleLocation> locations = GoogleLocations.Loader
                .fromFile(FILE_NAME)
                .filterInaccurate(1000)
                .getLocations();

        List<EcpPoint> points = PointConverters
                .GOOGLE_TO_ECP
                .convertList(locations);

        SpatialCollection<EcpPoint> spatialCollection = new Quadtree<>(points);


        LOG.info("Drawing image...");

        List<Line2D> lines = new ArrayList<>();
        int count = 0;
        int pointsCount = points.size();

        ImageOutputStream stream =
                new FileImageOutputStream(new File(OUT_NAME));

        GifSequenceWriter writer = new GifSequenceWriter(stream,
                BufferedImage.TYPE_INT_ARGB,
                300,
                true);

        Point prev = points.get(0);

        for (Point mp : points) {

            Line2D newLine = getLine(prev, mp);

            lines.add(newLine);

            if (count % 1000 == 0) {
                writer.writeToSequence(generateImageForLines(lines));
                LOG.info("line " + count + " of " + pointsCount);
            }

            prev = mp;
            count++;
        }

        writer.writeToSequence(generateImageForLines(lines));

        stream.flush();
        writer.close();

    }

    private static BufferedImage generateImageForLines(List<Line2D> lines) {
        BufferedImage bi = new BufferedImage(
                IMAGE_WIDTH,
                IMAGE_HEIGHT,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bi.createGraphics();

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(1f));

        lines.forEach(graphics2D::draw);
        return bi;
    }

    private static Line2D getLine(Point prev, Point mp) {
        double prevX = prev.getX();
        double prevY = prev.getY();

        double nextX = mp.getX();
        double nextY = mp.getY();

        double imageX0 = croppedImage.getX();
        double imageY0 = croppedImage.getY();

        double prevImageX = (prevX - imageX0) / croppedImage.getWidth() * IMAGE_WIDTH;
        double prevImageY = (prevY - imageY0) / croppedImage.getHeight() * IMAGE_HEIGHT;

        double nextImageX = (nextX - imageX0) / croppedImage.getWidth() * IMAGE_WIDTH;
        double nextImageY = (nextY - imageY0) / croppedImage.getHeight() * IMAGE_HEIGHT;

        return new Line2D.Double(
                prevImageX,
                prevImageY,
                nextImageX,
                nextImageY);
    }
}
