package uk.co.ticklethepanda.location.history.cartograph.projection;

import uk.co.ticklethepanda.location.history.cartograph.Rectangle;

import java.util.List;

public interface Point {

    float getHorizontalComponent();
    float getVerticalComponent();

    static Point getMaxBound(
            List<? extends Point> locations) {
        if (locations.size() == 0) {
            return null;
        }
        float maxX = locations.get(0).getHorizontalComponent();
        float maxY = locations.get(0).getVerticalComponent();
        for (Point mapPoint : locations) {
            maxX = Math.max(maxX, mapPoint.getHorizontalComponent());
            maxY = Math.max(maxY, mapPoint.getVerticalComponent());
        }
        return new LongLat(maxX, maxY);
    }

    static Point getMinBound(
            List<? extends Point> locations) {
        if (locations.size() == 0) {
            return null;
        }
        float minX = locations.get(0).getHorizontalComponent();
        float minY = locations.get(0).getVerticalComponent();
        for (Point mapPoint : locations) {
            minX = Math.min(minX, mapPoint.getHorizontalComponent());
            minY = Math.min(minY, mapPoint.getVerticalComponent());
        }
        return new LongLat(minX, minY);
    }

    static Rectangle getBoundingRectangle(
            List<? extends Point> locations) {
        if (locations.size() == 0) {
            return null;
        }
        Point min = Point.getMinBound(locations);
        Point max = Point.getMaxBound(locations);

        float rectangleWidth = max.getHorizontalComponent() - min.getHorizontalComponent();
        float rectangleHeight = max.getVerticalComponent() - min.getVerticalComponent();

        return new Rectangle(min.getHorizontalComponent(), min.getVerticalComponent(),
                rectangleWidth, rectangleHeight);
    }
}
