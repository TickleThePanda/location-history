package co.uk.ticklethepanda.location.history.cartograph.world;

import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MapDrawer {

    private final List<Path2D> paths;
    private final Color outline;
    private final Color fill;

    private static float calculateActualXScale(float scaleValue) {
        return scaleValue;
    }

    private static float calculateActualYScale(float scaleValue) {
        return -scaleValue / 2.0f;
    }

    private final float startX;
    private final float startY;
    private final int width;
    private final int height;
    private final float scale;

    public MapDrawer(List<List<LongLat>> countries, MapDescriptor mapDescriptor, Color outline, Color fill) {

        this.scale = mapDescriptor.getScale();
        this.width = mapDescriptor.getDimensions().getWidth();
        this.height = mapDescriptor.getDimensions().getHeight();
        this.startX = mapDescriptor.getCenter().getLongitude() - (width * calculateActualXScale(scale)) / 2f;
        this.startY = mapDescriptor.getCenter().getLatitude() - (height * calculateActualYScale(scale)) / 2f;
        this.outline = outline;
        this.fill = fill;

        this.paths = new ArrayList<>();

        for (List<LongLat> polygon : countries) {
            Path2D path = new Path2D.Double();
            Point2D last = calcuate(polygon.get(polygon.size() - 1));
            path.moveTo(last.getX(), last.getY());
            for (LongLat lngLatAlt : polygon) {
                Point2D point = calcuate(lngLatAlt);

                path.lineTo(point.getX(), point.getY());

            }
            path.closePath();
            paths.add(path);
        }
    }

    public BufferedImage drawOutline() {
        return renderMap(false);
    }

    public BufferedImage drawFill() {
        return renderMap(true);
    }

    private BufferedImage renderMap(boolean fill) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (fill) {
            drawFill(g);
        } else {
            drawOutline(g);
        }

        return bufferedImage;
    }

    private void drawOutline(Graphics2D g) {
        g.setColor(outline);
        paths.forEach(g::draw);
    }

    private void drawFill(Graphics2D g) {
        g.setColor(fill);
        paths.forEach(g::fill);
    }

    private Point2D calcuate(LongLat longLat) {
        return new Point2D.Float((longLat.getLongitude() - startX) / calculateActualXScale(scale),
                (longLat.getLatitude() - startY) / calculateActualYScale(scale));
    }
}
