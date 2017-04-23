package co.uk.ticklethepanda.location.history.cartograph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public interface Point {

    Logger LOG = LogManager.getLogger();

    static Point2D getMaxBound(
            List<? extends Point> locations) {
        double maxX = locations.get(0).getX();
        double maxY = locations.get(0).getY();
        for (Point mapPoint : locations) {
            maxX = Math.max(maxX, mapPoint.getX());
            maxY = Math.max(maxY, mapPoint.getY());
        }
        return new Point2D.Double(maxX, maxY);
    }

    static Point2D getMinBound(
            List<? extends Point> locations) {
        double minX = locations.get(0).getX();
        double minY = locations.get(0).getY();
        for (Point mapPoint : locations) {
            minX = Math.min(minX, mapPoint.getX());
            minY = Math.min(minY, mapPoint.getY());
            if (mapPoint.getY() == 0) {
                LOG.debug(mapPoint);
            }
        }
        return new Point2D.Double(minX, minY);
    }

    static Rectangle2D getBoundingRectangle(
            List<? extends Point> locations) {
        Point2D min = Point.getMinBound(locations);
        Point2D max = Point.getMaxBound(locations);

        double rectangleWidth = max.getX() - min.getX();
        double rectangleHeight = max.getY() - min.getY();

        Rectangle2D rect = new Rectangle2D.Double(min.getX(), min.getY(),
                rectangleWidth, rectangleHeight);

        return rect;
    }

    double getX();

    double getY();
}
