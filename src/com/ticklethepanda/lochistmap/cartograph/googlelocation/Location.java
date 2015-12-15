package com.ticklethepanda.lochistmap.cartograph.googlelocation;

import com.ticklethepanda.lochistmap.cartograph.Point;

public class Location implements Point {
  private long timestampMs;
  private long latitudeE7;
  private long longitudeE7;
  private long accuracy;

  public long getTimestampMs() {
    return timestampMs;
  }

  public double getY() {
    return latitudeE7;
  }

  public double getX() {
    return longitudeE7;
  }

  public long getAccuracy() {
    return accuracy;
  }
}
