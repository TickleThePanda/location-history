package com.ticklethepanda.lochistmap.cartograph;

public class Heatmap {

  private final int[][] heatmapArray;
  private final int width;
  private final int height;

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

}
