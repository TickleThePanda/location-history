package co.uk.ticklethepanda.location.history.cartograph.heatmap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.Rectangle2D;
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

        HeatmapDimensions dimensions = heatmap.getDescriptor().getDimensions();

        BufferedImage bi = new BufferedImage(dimensions.getWidth() * blockSize,
                dimensions.getHeight() * blockSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bi.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        paintMap(heatmap, blockSize, g2d);

        return bi;
    }

    private static float getHighestNumber(Heatmap array) {
        HeatmapDimensions dimensions = array.getDescriptor().getDimensions();
        float maxNumber = 0;
        for (int x = 0; x < dimensions.getWidth(); x++) {
            for (int y = 0; y < dimensions.getHeight(); y++) {
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
        HeatmapDimensions dimensions = heatmap.getDescriptor().getDimensions();
        float maxNumber = getHighestNumber(heatmap);
        for (int x = 0; x < dimensions.getWidth(); x++) {
            for (int y = 0; y < dimensions.getHeight(); y++) {
                if (heatmap.getValue(x, y) > 0) {
                    Color color = colourPicker.pickColor(
                            heatmap.getValue(x, y), maxNumber);
                    g2d.setColor(color);

                    float drawStartX = (x) * blockSize;
                    float drawStartY = (y) * blockSize;
                    Rectangle2D.Float floatRect = new Rectangle2D.Float(drawStartX, drawStartY,
                            blockSize, blockSize);
                    g2d.fill(floatRect);
                }
            }
        }
        return g2d;
    }

}
