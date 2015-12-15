package com.ticklethepanda.lochistmap.cartograph;

import java.awt.geom.Rectangle2D;

public interface HeatmapFactory {	
	public abstract Heatmap createHeatmap(int nColumns);
	
	public abstract Rectangle2D getBoundingRectangle();

	public abstract Point[] getPoints();
}
