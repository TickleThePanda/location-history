package com.ticklethepanda.lochistmap.cartograph;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface Point {

	public static Point2D getMaxBound(Point[] locations) {
		double maxX = locations[0].getX();
		double maxY = locations[0].getY();
		for (Point mapPoint : locations) {
			if (mapPoint.getX() > maxX)
				maxX = mapPoint.getX();
			if (mapPoint.getY() > maxY)
				maxY = mapPoint.getY();
		}
		return new Point2D.Double(maxX, maxY);
	}

	public static Point2D getMinBound(Point[] locations) {
		double minX = locations[0].getX();
		double minY = locations[0].getY();
		for (Point mapPoint : locations) {
			if (mapPoint.getX() < minX)
				minX = mapPoint.getX();
			if (mapPoint.getY() < minY)
				minY = mapPoint.getY();
		}
		return new Point2D.Double(minX, minY);
	}

	public static Rectangle2D getBoundingRectangle(Point[] locations) {
		Point2D min = Point.getMinBound(locations);
		Point2D max = Point.getMaxBound(locations);

		double rectangleWidth = max.getX() - min.getX();
		double rectangleHeight = max.getY() - min.getY();

		Rectangle2D rect = new Rectangle2D.Double(min.getX(), min.getY(),
				rectangleWidth, rectangleHeight);

		return rect;
	}

	public abstract double getX();

	public abstract double getY();
}
