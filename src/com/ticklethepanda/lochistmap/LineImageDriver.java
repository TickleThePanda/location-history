package com.ticklethepanda.lochistmap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.ticklethepanda.lochistmap.cartograph.Point;
import com.ticklethepanda.lochistmap.cartograph.ecp.EcpHeatmapFactory;
import com.ticklethepanda.lochistmap.cartograph.ecp.EcpPoint;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.LocationHistoryLoader;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.Location;
import com.ticklethepanda.lochistmap.imagewriter.ImageWriter;

public class LineImageDriver {

  private static final int IMAGE_WIDTH = 5000;

  private static final String FILE_NAME = "panda-loc-hist";
  private static final String OUT_NAME = "panda-line-out";

  private static final int BORDER_SIZE = 10;

  public static void main(String[] args) throws JsonSyntaxException,
      JsonIOException, FileNotFoundException {
    System.out.println("Loading locations from file...");
    final Location[] locations = LocationHistoryLoader
        .loadFromFile(FILE_NAME).getLocations();

    System.out.println("Converting ECP coordinate system...");
    final EcpHeatmapFactory map = new EcpHeatmapFactory(EcpPoint.convertFromLocations(locations));
    double aspectRatio = map.getBoundingRectangle().getWidth()
        / map.getBoundingRectangle().getHeight();

    System.out.println("Drawing image...");
    BufferedImage bi = new BufferedImage(IMAGE_WIDTH,
        (int) ((double) IMAGE_WIDTH / aspectRatio),
        BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = (Graphics2D) bi.getGraphics();

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(0.01f));

    long startTime = System.currentTimeMillis();

    Point prev = map.getPoints()[0];
    for (Point mp : map.getPoints()) {

      double normX1 = (prev.getX() - map.getBoundingRectangle().getX())
          / map.getBoundingRectangle().getWidth();
      double normY1 = (prev.getY() - map.getBoundingRectangle().getY())
          / map.getBoundingRectangle().getHeight();
      double normX2 = (mp.getX() - map.getBoundingRectangle().getX())
          / map.getBoundingRectangle().getWidth();
      double normY2 = (mp.getY() - map.getBoundingRectangle().getY())
          / map.getBoundingRectangle().getHeight();

      double imagX1 = normX1 * (double) bi.getWidth();
      double imagY1 = normY1 * (double) bi.getHeight();
      double imagX2 = normX2 * (double) bi.getWidth();
      double imagY2 = normY2 * (double) bi.getHeight();

      g2d.draw(new Line2D.Double(imagX1, imagY1, imagX2, imagY2));
      prev = mp;
    }

    long endTime = System.currentTimeMillis();

    BufferedImage borderedImage = new BufferedImage(IMAGE_WIDTH + 2 * BORDER_SIZE,
        (int) ((double) IMAGE_WIDTH / aspectRatio + 2 * BORDER_SIZE),
        BufferedImage.TYPE_INT_ARGB);

    g2d = (Graphics2D) borderedImage.getGraphics();
    g2d.drawImage(bi, null, BORDER_SIZE, BORDER_SIZE);

    System.out.printf("Time taken to draw: %.3f\n", (double) (endTime - startTime) / 1000.0);
    System.out.println("Printing out to file...");
    ImageWriter.writeImageOut(borderedImage, OUT_NAME);
    System.out.println("Finished...");

  }
}
