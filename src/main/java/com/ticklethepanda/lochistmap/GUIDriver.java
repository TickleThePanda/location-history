package com.ticklethepanda.lochistmap;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.ticklethepanda.lochistmap.cartograph.HeatmapPresenter;
import com.ticklethepanda.lochistmap.cartograph.HeatmapView;
import com.ticklethepanda.lochistmap.cartograph.Point;
import com.ticklethepanda.lochistmap.cartograph.ecp.EcpHeatmapFactory;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocation;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocations;
import com.ticklethepanda.lochistmap.cartograph.quadtree.Quadtree;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class GUIDriver {

	private static final class WindowFactory implements Runnable {
		private HeatmapView mv;

		public HeatmapView createWindow() {
			return mv;
		}

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
		final List<GoogleLocation> locations = GoogleLocations.Loader.fromFile(
				"panda-loc-hist").getLocations();

		System.out.println("Converting ECP coordinate system...");
		final List<? extends Point> points = new EcpHeatmapFactory(locations).getPoints();

		System.out.println("Converting array to Quadtree...");
		Quadtree quadtree = new Quadtree(points);
		WindowFactory windowFactory = new WindowFactory();
		SwingUtilities.invokeAndWait(windowFactory);
		HeatmapView heatmapView = windowFactory.createWindow();

		HeatmapPresenter heatmapPresenter = new HeatmapPresenter(heatmapView,
				quadtree);

	}

}
