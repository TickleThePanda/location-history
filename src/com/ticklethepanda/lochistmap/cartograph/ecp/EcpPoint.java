package com.ticklethepanda.lochistmap.cartograph.ecp;

import java.util.ArrayList;

import com.ticklethepanda.lochistmap.cartograph.Point;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.Location;

public class EcpPoint implements Point {
  private final double x;
  private final double y;
  public static long ACCURACY_CUTOFF = 100;

  public EcpPoint(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public static EcpPoint convertLocationToECP(Location location) {
    double x = location.getX() / 360.0;
    double y = -location.getY() / 180.0;
    return new EcpPoint(x, y);
  }

  @Override
  public double getX() {
    return x;
  }

  @Override
  public double getY() {
    return y;
  }

  public static EcpPoint[] convertFromLocations(Location[] locationArray) {
    ArrayList<EcpPoint> mapPointArrayList = new ArrayList<EcpPoint>();
    for (int i = 0; i < locationArray.length; i++) {
      Location location = locationArray[i];
      if (location.getAccuracy() < EcpPoint.ACCURACY_CUTOFF) {
        mapPointArrayList.add(convertLocationToECP(location));
      }
    }
  
    EcpPoint[] mapPointList = new EcpPoint[mapPointArrayList.size()];
  
    for (int i = 0; i < mapPointArrayList.size(); i++) {
      mapPointList[i] = mapPointArrayList.get(i);
    }
  
    return mapPointList;
  }
}
