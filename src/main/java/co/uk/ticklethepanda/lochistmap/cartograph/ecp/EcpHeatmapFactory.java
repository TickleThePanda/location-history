package co.uk.ticklethepanda.lochistmap.cartograph.ecp;

import co.uk.ticklethepanda.lochistmap.cartograph.Heatmap;
import co.uk.ticklethepanda.lochistmap.cartograph.HeatmapFactory;
import co.uk.ticklethepanda.lochistmap.cartograph.Point;
import co.uk.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocationToEcpConverter;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class EcpHeatmapFactory implements HeatmapFactory {
	private final List<EcpPoint> ecpPoints;

	private final Rectangle2D boundingRectangle;

	public EcpHeatmapFactory(List<GoogleLocation> locationArray) {
		this.ecpPoints =
				GoogleLocationToEcpConverter
						.convertToECPPoints(locationArray);

		this.boundingRectangle =
				Point.getBoundingRectangle(ecpPoints);
	}

	public Heatmap createHeatmap(int nColumns) {

		double minX = ecpPoints.get(0).getX();
		double maxX = ecpPoints.get(0).getX();

		double minY = ecpPoints.get(0).getY();
		double maxY = ecpPoints.get(0).getY();

		for (EcpPoint mapPoint : ecpPoints) {
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

		for (EcpPoint mapPoint : ecpPoints) {
			int x = (int) (((mapPoint.getX() - minX) / (difX)) * nBlocksHori);
			int y = (int) (((mapPoint.getY() - minY) / (difY)) * nBlocksVert);
			array[x][y]++;
		}
		
		return new Heatmap(array);
	}

	@Override
	public List<EcpPoint> getPoints() {
		return this.ecpPoints;
	}

	@Override
	public Rectangle2D getBoundingRectangle() {
		return this.boundingRectangle;
	}
}
