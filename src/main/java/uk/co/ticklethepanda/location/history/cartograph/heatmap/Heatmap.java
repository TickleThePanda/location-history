package uk.co.ticklethepanda.location.history.cartograph.heatmap;

import java.io.Serializable;

public class Heatmap<T> implements Serializable {

    private final int[][] heatmapArray;
    private final HeatmapDescriptor<T> descriptor;

    public Heatmap(HeatmapDescriptor<T> descriptor, int[][] heatmapArray) {
        this.descriptor = descriptor;
        this.heatmapArray = heatmapArray;
    }

    public int getValue(int x, int y) {
        return heatmapArray[x][y];
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
