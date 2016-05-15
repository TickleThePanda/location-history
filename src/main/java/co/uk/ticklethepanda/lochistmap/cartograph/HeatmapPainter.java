package co.uk.ticklethepanda.lochistmap.cartograph;

import java.awt.*;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

public class HeatmapPainter {

	private HeatmapColourPicker colourPicker;

	public interface HeatmapColourPicker {

		Color computeCellColor(double unscaled, double largestNumber);

		public static class Greyscale implements HeatmapColourPicker {

			public static final double MIN_BRIGHTNESS = 0.2;

			private final double minBrightness;

			public Greyscale() {
				this(MIN_BRIGHTNESS);
			}

			public Greyscale(double minBrightness) {
				this.minBrightness = minBrightness;
			}

			@Override
			public Color computeCellColor(double unscaled, double largestNumber) {
				double brightness = Math.min((1.0 / unscaled) * (1.0 - minBrightness), 1f);
				Color color = Color.getHSBColor(0f, 0f, (float) brightness);
				return color;
			}
		}

		public static class Monotone implements HeatmapColourPicker {

			public static final double MIN_BRIGHTNESS = 0.2;
			public static final double MAX_BRIGHTNESS = 0.6;

			private final double minBrightness;
			private final double maxBrightness;

			public Monotone() {
				this(MIN_BRIGHTNESS, MAX_BRIGHTNESS);
			}

			public Monotone(double minBrightness, double maxBrightness) {
				this.minBrightness = minBrightness;
				this.maxBrightness = maxBrightness;
			}

			@Override
			public Color computeCellColor(double unscaled, double largestNumber) {
				double brightness = Math.min((1.0 / unscaled) * (maxBrightness - minBrightness) + 1 - MAX_BRIGHTNESS, 1f);
				Color color = Color.getHSBColor(80f/255f, 0.43f, (float) brightness);
				return color;
			}
		}

		public static class Hue implements HeatmapColourPicker {
			public Color computeCellColor(double unscaled, double largestNumber) {
				double inverseScale = 7f;
				double hue = largestNumber
						/ (inverseScale * largestNumber - inverseScale * unscaled);
				hue = Math.min(hue, 1f) * 0.8f;
				return Color.getHSBColor(
						(float) hue, 1f, 1f);
			}
		}

	}


	public HeatmapPainter() {
		this(new HeatmapColourPicker.Greyscale());
	}

	public HeatmapPainter(HeatmapColourPicker colourPicker) {
		this.colourPicker = colourPicker;
	}

	public BufferedImage paintHeatmap(Heatmap heatmap, int blockSize) {

		BufferedImage bi = new BufferedImage(heatmap.getWidth() * blockSize,
				heatmap.getHeight() * blockSize, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = bi.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d.setBackground(Color.WHITE);

		g2d.setPaint ( Color.WHITE );
		g2d.fillRect ( 0, 0, bi.getWidth(), bi.getHeight() );

		paintMap(heatmap, blockSize, g2d);

		return bi;
	}

	private static double getHighestNumber(Heatmap array) {
		double maxNumber = 0;
		for (int x = 0; x < array.getWidth(); x++) {
			for (int y = 0; y < array.getHeight(); y++) {
				if (array.getValue(x, y) > maxNumber)
					maxNumber = array.getValue(x, y);
			}
		}
		return maxNumber;
	}

	public Graphics2D paintMap(Heatmap heatmap, int blockSize, Graphics2D g2d) {
		double maxNumber = getHighestNumber(heatmap);
		for (int x = 0; x < heatmap.getWidth(); x++) {
			for (int y = 0; y < heatmap.getHeight(); y++) {
				if (heatmap.getValue(x, y) > 0) {
					Color color = colourPicker.computeCellColor(
							heatmap.getValue(x, y), maxNumber);
					g2d.setColor(color);

					double drawStartX = (x) * blockSize;
					double drawStartY = (y) * blockSize;
					Double doubleRect = new Double(drawStartX, drawStartY,
							blockSize, blockSize);
					g2d.fill(doubleRect);
				}
			}
		}
		return g2d;
	}

}