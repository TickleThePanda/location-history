package com.ticklethepanda.lochistmap;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.ticklethepanda.lochistmap.cartograph.HeatmapPainter;
import com.ticklethepanda.lochistmap.cartograph.ecp.EcpHeatmapFactory;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocationArray;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocation;
import com.ticklethepanda.lochistmap.cartograph.quadtree.Quadtree;
import com.ticklethepanda.lochistmap.imagewriter.ImageWriter;

public class LayerImageDriver {

	private static final int IMAGE_WIDTH = 5000;

	private static final int N_LAYERS = 5;

	private static final String FILE_NAME = "panda-loc-hist";

	private static final int SCALE = 20;

	private static final int OFFSET = 1;

	public static void main(String[] args) throws JsonSyntaxException,
			JsonIOException, IOException {

		BufferedImage combined = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);

		// get data
		System.out.println("Loading locations from file...");
		final GoogleLocation[] locations = GoogleLocationArray
				.loadFromFile(FILE_NAME).getLocations();

		System.out.println("Generating map...");
		EcpHeatmapFactory map = new EcpHeatmapFactory(locations);

		Quadtree tree = new Quadtree(map.getPoints());

		for (int i = 1; i <= N_LAYERS; i++) {
			System.out.println("Generating visual layer " + i + "...");
			int pixelWidth = N_LAYERS * SCALE - i * SCALE + OFFSET;
			int nColumns = IMAGE_WIDTH / pixelWidth;

			HeatmapPainter md = new HeatmapPainter();

			BufferedImage newImage = md.paintHeatmap(
					tree.convertToHeatmap(nColumns), pixelWidth);
			BufferedImage oldImage = combined;

			RescaleOp rescaleOp = new RescaleOp(
					new float[] { 1f, 1f, 1f, 0.3f }, new float[] { 0f, 0f, 0f,
							0f }, null);
			oldImage = rescaleOp.filter(oldImage, oldImage);

			int maxWidth = Math.max(newImage.getWidth(), oldImage.getWidth());
			int maxHeight = Math
					.max(newImage.getHeight(), oldImage.getHeight());

			combined = new BufferedImage(maxWidth, maxHeight,
					BufferedImage.TYPE_INT_ARGB);

			Graphics g = combined.getGraphics();

			g.drawImage(oldImage, 0, 0, maxWidth, maxHeight, null);
			g.drawImage(newImage, 0, 0, maxWidth, maxHeight, null);
		}

		System.out.println("Sending image out to file...");
		ImageWriter.writeImageOut(combined, "mine");

	}
}
