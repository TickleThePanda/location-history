package com.ticklethepanda.lochistmap;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.ticklethepanda.lochistmap.cartograph.Heatmap;
import com.ticklethepanda.lochistmap.cartograph.HeatmapPainter;
import com.ticklethepanda.lochistmap.cartograph.ecp.EcpHeatmapFactory;
import com.ticklethepanda.lochistmap.cartograph.ecp.EcpPoint;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocation;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocations;
import com.ticklethepanda.lochistmap.cartograph.quadtree.Quadtree;
import com.ticklethepanda.lochistmap.imagewriter.ImageWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComparisonImageDriver {

	private static final int IMAGE_WIDTH = 540;

	private static final int IMAGE_BLOCK_SIZE = 4;

	private static final double IMAGE_HORIZ_N_BLOCKS = (double) IMAGE_WIDTH
			/ (double) IMAGE_BLOCK_SIZE;

	private static final String JESS_FILE_NAME = "jess-loc-hist";
	private static final String PANDA_FILE_NAME = "panda-loc-hist";

	private static final long MAXIMUM_TIME_DIFF_MINUTES = 1;
	private static final long MAXIMUM_TIME_DIFF_SECONDS = MAXIMUM_TIME_DIFF_MINUTES * 60;
	private static final long MAXIMUM_TIME_DIFF_MILLIS = MAXIMUM_TIME_DIFF_SECONDS * 1000;

	private static final double MAXIMUM_UNIT_DISTANCE_BETWEEN_POINTS = 1000000;

	public static void main(String[] args) throws JsonSyntaxException,
			JsonIOException, IOException {
		long startTime = System.currentTimeMillis();
		System.out.println("Loading Person 1's locations from file...");

		final List<GoogleLocation> locations1 = GoogleLocations.Loader.fromFile(
				JESS_FILE_NAME).getLocations();

		System.out.printf("Person 1 points %,d\n", locations1.size());

		System.out.println("Loading Person 2's locations from file...");

		final List<GoogleLocation> locations2 = GoogleLocations.Loader.fromFile(
				PANDA_FILE_NAME).getLocations();

		System.out.printf("person 2 points %,d\n", locations2.size());

		System.out.println("Comparing Points");

		ArrayList<GoogleLocation> overlappingLocations = combineLocations(locations1,
				locations2, MAXIMUM_TIME_DIFF_MILLIS,
				MAXIMUM_UNIT_DISTANCE_BETWEEN_POINTS);

		List<GoogleLocation> locationArray = new ArrayList<>();
		{
			for (GoogleLocation loc : overlappingLocations) {
				locationArray.add(loc);
			}
		}
		System.out.printf("combination points %,d\n", locationArray.size());

		System.out.println("Converting ECP coordinate system...");
		final List<EcpPoint> points = new EcpHeatmapFactory(locationArray).getPoints();

		System.out.println("Converting array to Quadtree...");
		Quadtree quadtree = new Quadtree(points);

		System.out.println("Rendering image...");

		Heatmap heatmap = quadtree.convertToHeatmap(IMAGE_HORIZ_N_BLOCKS);
		HeatmapPainter heatmapPainter = new HeatmapPainter(
				new HeatmapPainter.HeatmapColourPicker.Greyscale(100));
		BufferedImage image = heatmapPainter.paintHeatmap(heatmap, IMAGE_BLOCK_SIZE);

		System.out.println("Outputting image...");
		ImageWriter.writeImageOut(image, "combi");

		long endTime = System.currentTimeMillis();

		long timeTaken = endTime - startTime;

		System.out.printf("Time taken to generate: %.3f s.\n",
				(double) (timeTaken) / 1000.0);

	}

	private static ArrayList<GoogleLocation> combineLocations(
			final List<GoogleLocation> locations1, final List<GoogleLocation> locations2,
			long maxTimeDiffMillis, double maxDistDiffUnits) {
		ArrayList<GoogleLocation> overlappingLocations = new ArrayList<GoogleLocation>();

		int index1 = 0;
		for (int index2 = 0; index2 < locations2.size(); index2++) {

			// get counter ahead for person 1
			while (true) {
				long timeDiff = locations1.get(index1).getTimestampMs()
						- locations2.get(index2).getTimestampMs();
				if (index1 > locations1.size() - 2
						|| timeDiff < maxTimeDiffMillis) {
					break;
				}

				index1++;
			}

			int iBackJes = index1;
			// work backwards to find match.
			while (iBackJes >= 0) {
				long timeDiff = locations1.get(iBackJes).getTimestampMs()
						- locations2.get(index2).getTimestampMs();
				// we're too far back in time now, break.
				if (timeDiff > maxTimeDiffMillis) {
					break;
				}

				double distBetweenX = locations2.get(index2).getX()
						- locations1.get(iBackJes).getX();
				double distBetweenY = locations2.get(index2).getY()
						- locations1.get(iBackJes).getY();
				double locationDiff = (long) Math.sqrt(distBetweenX
						* distBetweenX + distBetweenY * distBetweenY);
				if (locationDiff < maxDistDiffUnits) {
					overlappingLocations.add(locations2.get(index2));
				}

				// decreases index so increases time difference
				iBackJes--;
			}
		}
		return overlappingLocations;
	}
}
