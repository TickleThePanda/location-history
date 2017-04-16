package co.uk.ticklethepanda.location.history.cartographs.heatmap;

import java.io.Serializable;

public class Heatmap implements Serializable {

    private final int width;
    private final int height;
    private final int[][] heatmapArray;

    public Heatmap(int[][] heatmapArray) {
        this.heatmapArray = heatmapArray;
        this.width = heatmapArray.length;
        this.height = heatmapArray[0].length;

        for (int[] row : heatmapArray) {
            if (row.length != height) {
                throw new IllegalArgumentException(
                        "The heatmap array must contain"
                                + " columns of equal length.");
            }
        }
    }

    public int getValue(int x, int y) {
        return heatmapArray[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * @return a copy of the heatmap's array.
     */
    public int[][] getHeatmap() {
        return heatmapArray == null ? null : heatmapArray.clone();
    }

}
