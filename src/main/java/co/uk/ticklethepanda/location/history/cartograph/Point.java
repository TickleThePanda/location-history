package co.uk.ticklethepanda.location.history.cartograph;

import co.uk.ticklethepanda.location.history.cartograph.points.euclid.EuclidPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public interface Point {

    Logger LOG = LogManager.getLogger();

    static EuclidPoint getMaxBound(
            List<? extends Point> locations) {
        if (locations.size() == 0) {
            return null;
        }
        float maxX = locations.get(0).getX();
        float maxY = locations.get(0).getY();
        for (Point mapPoint : locations) {
            maxX = Math.max(maxX, mapPoint.getX());
            maxY = Math.max(maxY, mapPoint.getY());
        }
        return new EuclidPoint(maxX, maxY);
    }

    static EuclidPoint getMinBound(
            List<? extends Point> locations) {
        if (locations.size() == 0) {
            return null;
        }
        float minX = locations.get(0).getX();
        float minY = locations.get(0).getY();
        for (Point mapPoint : locations) {
            minX = Math.min(minX, mapPoint.getX());
            minY = Math.min(minY, mapPoint.getY());
            if (mapPoint.getY() == 0) {
                LOG.debug(mapPoint);
            }
        }
        return new EuclidPoint(minX, minY);
    }

    static Rectangle getBoundingRectangle(
            List<? extends Point> locations) {
        if (locations.size() == 0) {
            return null;
        }
        EuclidPoint min = Point.getMinBound(locations);
        EuclidPoint max = Point.getMaxBound(locations);

        float rectangleWidth = max.getX() - min.getX();
        float rectangleHeight = max.getY() - min.getY();

        Rectangle rect = new Rectangle(min.getX(), min.getY(),
                rectangleWidth, rectangleHeight);

        return rect;
    }

    float getX();

    float getY();
}
