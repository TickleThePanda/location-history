package co.uk.ticklethepanda.location.history.cartograph.world;

import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MapDrawer {

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

    private List<List<LongLat>> polygons = new ArrayList<>();

    public MapDrawer(List<List<LongLat>> countries, MapDescriptor mapDescriptor, Color color) {
        this.polygons = countries;

        this.scale = mapDescriptor.getScale();
        this.width = mapDescriptor.getDimensions().getWidth();
        this.height = mapDescriptor.getDimensions().getHeight();
        this.startX = mapDescriptor.getCenter().getX() - (width * calculateActualXScale(scale)) / 2f;
        this.startY = mapDescriptor.getCenter().getY() - (height * calculateActualYScale(scale)) / 2f;
    }

    public BufferedImage drawMap() {

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();

        g.setColor(new Color(0x444444));

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (List<LongLat> polygon : polygons) {
            drawPolygon(g, polygon);
        }

        return bufferedImage;
    }

    private void drawPolygon(Graphics2D g, List<LongLat> polygon) {

        Point2D prevPoint = calcuate(polygon.get(polygon.size() - 1));
        for (LongLat lngLatAlt : polygon) {
            Point2D thisPoint = calcuate(lngLatAlt);
            g.draw(new Line2D.Double(prevPoint, thisPoint));

            prevPoint = thisPoint;
        }
    }

    private Point2D calcuate(LongLat longLat) {
        return new Point2D.Float((longLat.getX() - startX) / calculateActualXScale(scale),
                (longLat.getY() - startY) / calculateActualYScale(scale));
    }
}
