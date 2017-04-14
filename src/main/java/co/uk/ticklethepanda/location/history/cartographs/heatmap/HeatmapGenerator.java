package co.uk.ticklethepanda.location.history.cartographs.heatmap;

import co.uk.ticklethepanda.location.history.cartograph.Cartograph;

import java.awt.geom.Rectangle2D;

public class HeatmapGenerator {

    private final Cartograph<co.uk.ticklethepanda.location.history.points.ecp.EcpPoint> cartograph;

    /**
     * @param cartograph
     */
    public HeatmapGenerator(Cartograph<co.uk.ticklethepanda.location.history.points.ecp.EcpPoint> cartograph) {
        this.cartograph = cartograph;
    }

    /**
     * Converts the Quadtree to a density array, adding the sums within the
     * locations, using a Rectangle to defines the size and location of the
     * window, the number of columns for the array map, and the number for the
     * array. The size of each location in the map is defined by the ratio
     * between the window width and the number of columns: (rectangle width) /
     * nCols.
     *
     * @return an array that represents the density of the Quadtree from a particular view.
     */
    public Heatmap convert(final Rectangle2D viewport,
                           final double blocksPerUnit) {

        final double unitsPerBlock = 1 / blocksPerUnit;

        double widthInBlocks = viewport.getWidth() * blocksPerUnit;
        double heightInBlocks = viewport.getHeight() * blocksPerUnit;

        int[][] image = new int[(int) widthInBlocks + 1][(int) heightInBlocks + 1];

        for (int x = 0; x < widthInBlocks; x++) {
            for (int y = 0; y < heightInBlocks; y++) {
                double xStart = x * unitsPerBlock + viewport.getMinX();
                double yStart = y * unitsPerBlock + viewport.getMinY();

                Rectangle2D block = new Rectangle2D.Double(
                        xStart,
                        yStart,
                        unitsPerBlock,
                        unitsPerBlock);
                image[x][y] = cartograph.queryRange(block);
            }
        }
        return new Heatmap(image);
    }
}
