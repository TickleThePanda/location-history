package com.ticklethepanda.lochistmap.cartograph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

public class HeatmapPainter {

  public static final double MIN_BRIGHTNESS = 150.0;
  public static final double BRIGHTNESS_GRAPH_SCALE = 1500.0;

  private double minBrightness = MIN_BRIGHTNESS;
  private double brightnessGraphScale = BRIGHTNESS_GRAPH_SCALE;

  public HeatmapPainter() {
    this(MIN_BRIGHTNESS, BRIGHTNESS_GRAPH_SCALE);
  }

  public HeatmapPainter(double minBrightness, double brightnessGraphScale) {
    this.minBrightness = minBrightness;
    this.brightnessGraphScale = brightnessGraphScale;
  }

  public double getBrightnessGraphScale() {
    return brightnessGraphScale;
  }

  public BufferedImage paintHeatmap(Heatmap heatmap, int blockSize) {

    BufferedImage bi = new BufferedImage(heatmap.getWidth() * blockSize,
        heatmap.getHeight() * blockSize, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = bi.createGraphics();

    g2d.setBackground(Color.WHITE);

    paintMap(heatmap, blockSize, g2d);

    return bi;
  }

  private Color computeCellColor(double unscaled, double largestNumber) {
    double linearBrightness = unscaled / largestNumber;
    int scaledBrightness = (int) (minBrightness - minBrightness
        * (1.0 - Math
            .pow(2.0, -linearBrightness * brightnessGraphScale)));
    Color color = new Color(0, 0, 0, 255 - scaledBrightness);
    return color;
  }

  private static double getHighestNumber(Heatmap array) {
    double maxNumber = 0;
    for (int x = 0; x < array.getWidth(); x++) {
      for (int y = 0; y < array.getHeight(); y++) {
        if (array.getValue(x, y) > maxNumber)
          maxNumber = array.getValue(x, y);
      }
    }
    return maxNumber;
  }

  public double getMinBrightness() {
    return minBrightness;
  }

  public Graphics2D paintMap(Heatmap heatmap, int blockSize, Graphics2D g2d) {
    double maxNumber = getHighestNumber(heatmap);
    for (int x = 0; x < heatmap.getWidth(); x++) {
      for (int y = 0; y < heatmap.getHeight(); y++) {
        if (heatmap.getValue(x, y) > 0) {
          Color color = computeCellColor(
              heatmap.getValue(x, y), maxNumber);
          g2d.setColor(color);

          double drawStartX = (x) * blockSize;
          double drawStartY = (y) * blockSize;
          Double doubleRect = new Double(drawStartX, drawStartY,
              blockSize, blockSize);
          g2d.fill(doubleRect);
        }
      }
    }
    return g2d;
  }

  public void setBrightnessGraphScale(double brightnessGraphScale) {
    this.brightnessGraphScale = brightnessGraphScale;
  }

  public void setMinBrightness(double minBrightness) {
    this.minBrightness = minBrightness;
  }
}