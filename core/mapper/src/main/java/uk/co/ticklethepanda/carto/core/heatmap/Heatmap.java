package uk.co.ticklethepanda.carto.core.heatmap;

import java.io.Serializable;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    public HeatmapDescriptor<T> getDescriptor() {
        return this.descriptor;
    }

    public int countPoints() {
        return Stream.of(heatmapArray).mapToInt(row -> IntStream.of(row).sum()).sum();
    }

}
