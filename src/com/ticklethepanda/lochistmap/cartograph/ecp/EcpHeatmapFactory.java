package com.ticklethepanda.lochistmap.cartograph.ecp;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.ticklethepanda.lochistmap.cartograph.Point;
import com.ticklethepanda.lochistmap.cartograph.Heatmap;
import com.ticklethepanda.lochistmap.cartograph.HeatmapFactory;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocation;

public class EcpHeatmapFactory implements HeatmapFactory {
	public static long ACCURACY_CUTOFF = 10000;

	private EcpPoint[] convertToECPPoints(GoogleLocation[] locationArray) {
		ArrayList<EcpPoint> mapPointArrayList = new ArrayList<EcpPoint>();
		for (int i = 0; i < locationArray.length; i++) {
			GoogleLocation location = locationArray[i];
			if (location.getAccuracy() < accuracyThreshold) {
				mapPointArrayList.add(EcpPoint
						.convertLocationToECP(location));
			}
		}

		EcpPoint[] mapPointList = new EcpPoint[mapPointArrayList.size()];

		for (int i = 0; i < mapPointArrayList.size(); i++) {
			mapPointList[i] = mapPointArrayList.get(i);
		}

		return mapPointList;
	}

	private final EcpPoint[] ecpMapArray;

	private final Rectangle2D boundingRectangle;

	private final long accuracyThreshold;

	public EcpHeatmapFactory(GoogleLocation[] locationArray) {
		this(locationArray, ACCURACY_CUTOFF);
	}

	public EcpHeatmapFactory(GoogleLocation[] locationArray, long accuracyThreshold) {
		this.accuracyThreshold = accuracyThreshold;
		this.ecpMapArray = convertToECPPoints(locationArray);
		this.boundingRectangle = Point.getBoundingRectangle(ecpMapArray);
	}

	public Heatmap createHeatmap(int nColumns) {

		double minX = ecpMapArray[0].getX();
		double maxX = ecpMapArray[0].getX();

		double minY = ecpMapArray[0].getY();
		double maxY = ecpMapArray[0].getY();

		for (EcpPoint mapPoint : ecpMapArray) {
			if (mapPoint.getX() < minX)
				minX = mapPoint.getX();
			if (mapPoint.getX() > maxX)
				maxX = mapPoint.getX();

			if (mapPoint.getY() < minY)
				minY = mapPoint.getY();
			if (mapPoint.getY() > maxY)
				maxY = mapPoint.getY();
		}

		double difX = maxX - minX;
		double difY = maxY - minY;

		double ratio = difY / difX;

		double height = (int) ((double) nColumns * ratio);

		double nBlocksHori = nColumns;
		double nBlocksVert = height;

		int array[][] = new int[(int) nBlocksHori + 1][(int) nBlocksVert + 1];

		for (EcpPoint mapPoint : ecpMapArray) {
			int x = (int) (((mapPoint.getX() - minX) / (difX)) * nBlocksHori);
			int y = (int) (((mapPoint.getY() - minY) / (difY)) * nBlocksVert);
			array[x][y]++;
		}
		
		return new Heatmap(array);
	}

	@Override
	public EcpPoint[] getPoints() {
		return this.ecpMapArray;
	}

	@Override
	public Rectangle2D getBoundingRectangle() {
		return this.boundingRectangle;
	}
}
