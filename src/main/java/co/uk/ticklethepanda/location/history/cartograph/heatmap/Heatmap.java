package co.uk.ticklethepanda.location.history.cartograph.heatmap;

import co.uk.ticklethepanda.location.history.cartograph.projection.LongLat;

import java.io.Serializable;

public class Heatmap<T> implements Serializable {

    private final HeatmapDimensions dimensions;
    private final int[][] heatmapArray;
    private final HeatmapDescriptor<T> descriptor;

    public Heatmap(
            int[][] heatmapArray,
            LongLat centre,
            float scale
    ) {
        this.heatmapArray = heatmapArray;
        dimensions = new HeatmapDimensions(heatmapArray.length, heatmapArray[0].length);

        for (int[] row : heatmapArray) {
            if (row.length != dimensions.getHeight()) {
                throw new IllegalArgumentException(
                        "The heatmap array must contain"
                                + " columns of equal length.");
            }
        }

        this.descriptor = new HeatmapDescriptor<>(
                dimensions, centre, scale
        );
    }

    public int getValue(int x, int y) {
        return heatmapArray[x][y];
    }

    public HeatmapDimensions getDimensions() {
        return dimensions;
    }

    /**
     * @return a copy of the heatmap's array.
     */
    public int[][] getHeatmap() {
        return heatmapArray == null ? null : heatmapArray.clone();
    }

    public HeatmapDescriptor getDescriptor() {
        return this.descriptor;
    }

}
