package co.uk.ticklethepanda.location.history.cartographs.heatmap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

public class HeatmapImagePainter {
    public static final Logger LOG = LogManager.getLogger();

    private HeatmapColourPicker colourPicker;

    public interface HeatmapColourPicker {

        default double normaliseValue(double unscaled, double largestNumber) {
            return 1.0 - Math.log(unscaled) / Math.log(largestNumber);
        }

        default Color pickColor(double unscaled, double largestNumber) {
            return pickColor(normaliseValue(unscaled, largestNumber));
        }

        Color pickColor(double percentage);

        class Greyscale implements HeatmapColourPicker {

            public static final double MAX_BRIGHTNESS = 0.2;

            private final double maximumBrightness;

            public Greyscale() {
                this(MAX_BRIGHTNESS);
            }

            public Greyscale(double maximumBrightness) {
                this.maximumBrightness = maximumBrightness;
            }

            @Override
            public Color pickColor(double percentage) {
                double brightness = (1.0 - maximumBrightness) * percentage;
                Color color = Color.getHSBColor(0f, 0f, (float) brightness);
                return color;
            }
        }

        class Monotone implements HeatmapColourPicker {

            public static final double MIN_BRIGHTNESS = 0.1;
            public static final double MAX_BRIGHTNESS = 0.9;

            private final double minBrightness;
            private final double maxBrightness;

            private final Color color;

            public Monotone(Color color) {
                this(color, MIN_BRIGHTNESS, MAX_BRIGHTNESS);
            }

            public Monotone(Color color, double minBrightness, double maxBrightness) {
                this.color = color;
                this.minBrightness = minBrightness;
                this.maxBrightness = maxBrightness;
            }

            @Override
            public Color pickColor(double percentage) {
                double brightness = (maxBrightness - minBrightness) * percentage + minBrightness;

                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

                Color color = Color.getHSBColor(hsb[0], hsb[1], (float) brightness);
                return color;
            }
        }

        class Hue implements HeatmapColourPicker {
            public Color pickColor(double percentage) {
                double hue = percentage;
                hue = Math.min(hue, 1f) * 0.8f;
                return Color.getHSBColor(
                        (float) hue, 1f, 1f);
            }
        }

    }

    public HeatmapImagePainter() {
        this(new HeatmapColourPicker.Greyscale());
    }

    public HeatmapImagePainter(HeatmapColourPicker colourPicker) {
        this.colourPicker = colourPicker;
    }

    public BufferedImage paintHeatmap(Heatmap heatmap, int blockSize) {

        BufferedImage bi = new BufferedImage(heatmap.getWidth() * blockSize,
                heatmap.getHeight() * blockSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bi.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setBackground(Color.WHITE);

        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight());

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

    public Graphics2D paintMap(
            Heatmap heatmap,
            int blockSize,
            Graphics2D g2d) {
        double maxNumber = getHighestNumber(heatmap);
        for (int x = 0; x < heatmap.getWidth(); x++) {
            for (int y = 0; y < heatmap.getHeight(); y++) {
                if (heatmap.getValue(x, y) > 0) {
                    Color color = colourPicker.pickColor(
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
