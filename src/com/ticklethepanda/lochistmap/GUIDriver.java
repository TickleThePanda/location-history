package com.ticklethepanda.lochistmap;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.ticklethepanda.lochistmap.cartograph.Point;
import com.ticklethepanda.lochistmap.cartograph.HeatmapPresenter;
import com.ticklethepanda.lochistmap.cartograph.HeatmapView;
import com.ticklethepanda.lochistmap.cartograph.ecp.EcpHeatmapFactory;
import com.ticklethepanda.lochistmap.cartograph.ecp.EcpPoint;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.LocationHistoryLoader;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.Location;
import com.ticklethepanda.lochistmap.cartograph.quadtree.Quadtree;

public class GUIDriver {

  private static final class WindowFactory implements Runnable {
    private HeatmapView mv;

    public HeatmapView createWindow() {
      return mv;
    }

    @Override
    public void run() {
      JFrame jframe = new JFrame();
      jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.mv = new HeatmapView();
      jframe.getContentPane().setLayout(new BorderLayout());
      jframe.getContentPane().add(mv, BorderLayout.CENTER);
      jframe.pack();
      jframe.setVisible(true);
    }
  }

  public static void main(String[] args) throws JsonSyntaxException,
      JsonIOException, FileNotFoundException, InvocationTargetException,
      InterruptedException {

    System.out.println("Loading locations from file...");
    final Location[] locationHistory = LocationHistoryLoader.loadFromFile(
        "panda-loc-hist").getLocations();

    System.out.println("Converting ECP coordinate system...");
    final Point[] points = EcpPoint.convertFromLocations(locationHistory);

    System.out.println("Converting array to Quadtree...");
    Quadtree quadtree = new Quadtree(points);
    WindowFactory windowFactory = new WindowFactory();
    SwingUtilities.invokeAndWait(windowFactory);
    HeatmapView heatmapView = windowFactory.createWindow();

    HeatmapPresenter heatmapPresenter = new HeatmapPresenter(heatmapView,
        quadtree);

  }

}
