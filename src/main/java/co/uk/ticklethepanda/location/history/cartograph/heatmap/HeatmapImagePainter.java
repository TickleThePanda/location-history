package co.uk.ticklethepanda.location.history.cartograph.heatmap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

public class HeatmapImagePainter {
    public static final Logger LOG = LogManager.getLogger();

    private HeatmapColourPicker colourPicker;

    public HeatmapImagePainter() {
        this(new HeatmapColourPicker.Greyscale());
    }

    public HeatmapImagePainter(HeatmapColourPicker colourPicker) {
        this.colourPicker = colourPicker;
    }

    public BufferedImage paintHeatmap(Heatmap heatmap, int blockSize) {

        BufferedImage bi = new BufferedImage(heatmap.getDimensions().getWidth() * blockSize,
                heatmap.getDimensions().getHeight() * blockSize, BufferedImage.TYPE_INT_ARGB);

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
        for (int x = 0; x < array.getDimensions().getWidth(); x++) {
            for (int y = 0; y < array.getDimensions().getHeight(); y++) {
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
        for (int x = 0; x < heatmap.getDimensions().getWidth(); x++) {
            for (int y = 0; y < heatmap.getDimensions().getHeight(); y++) {
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
