package co.uk.ticklethepanda.lochistmap.cartograph.quadtree;

import co.uk.ticklethepanda.lochistmap.cartograph.Heatmap;
import co.uk.ticklethepanda.lochistmap.cartograph.Point;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;

public class Quadtree {
	private final QuadtreeNode root;
	private final Rectangle2D rectangle;

	public Quadtree(Rectangle2D rectangle) {
		this.rectangle = rectangle;
		root = new QuadtreeNode(rectangle);
	}

	public Quadtree(List<? extends Point> points) {
		this(Point.getBoundingRectangle(points));
		for (Point mp : points) {
			this.insert(mp);
		}
	}

	public boolean insert(Point mp) {
		return root.insert(mp);
	}

	public int queryRange(Rectangle2D rect) {
		return root.queryRange(rect);
	}

	public Rectangle2D getBoundingRectangle() {
		return rectangle;
	}

	public Heatmap convertToHeatmap(double nCols) {
		return convertToHeatmap(nCols,
				rectangle.getHeight() / (rectangle.getWidth() / nCols),
				rectangle);
	}

	public Heatmap convertToHeatmap(double nCols, double nRows) {
		return convertToHeatmap(nCols, nRows, rectangle);
	}

	/**
	 * Converts the Quadtree to a density array, adding the sums within the
	 * locations, using a Rectangle to defines the size and location of the
	 * window, the number of columns for the array map, and the number for the
	 * array. The size of each location in the map is defined by the ratio
	 * between the window width and the number of columns: (rectangle width) /
	 * nCols.
	 * 
	 * @param nCols
	 *            the number of columns in the density array
	 * @param nRows
	 *            the number of rows in the density array
	 * @param windowRectangle
	 *            the rectangle defining the size and location of the window in
	 *            the map
	 * @return an array that represents the density of the Quadtree from a particular view.
	 */
	public Heatmap convertToHeatmap(double nCols, double nRows,
			Rectangle2D windowRectangle) {

		double realBlockSize = windowRectangle.getWidth() / nCols;

		int[][] image = new int[(int) nCols + 1][(int) nRows + 1];

		for (int x = 0; x < nCols; x++) {
			for (int y = 0; y < nRows; y++) {
				double xStart = x * realBlockSize + windowRectangle.getMinX();
				double yStart = y * realBlockSize + windowRectangle.getMinY();

				Rectangle2D block = new Double(xStart, yStart, realBlockSize,
						realBlockSize);
				image[x][y] = queryRange(block);
			}
		}
		return new Heatmap(image);
	}
}
