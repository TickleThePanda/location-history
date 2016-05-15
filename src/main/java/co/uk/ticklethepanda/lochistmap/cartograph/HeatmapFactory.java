package co.uk.ticklethepanda.lochistmap.cartograph;

import java.awt.geom.Rectangle2D;
import java.util.List;

public interface HeatmapFactory {	
	Heatmap createHeatmap(int nColumns);
	
	Rectangle2D getBoundingRectangle();

	List<? extends Point> getPoints();
}
