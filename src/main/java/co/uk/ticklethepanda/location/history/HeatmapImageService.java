package co.uk.ticklethepanda.location.history;

import co.uk.ticklethepanda.location.history.cartographs.heatmap.HeatmapImagePainter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class HeatmapImageService {

    public static final Logger LOG = LogManager.getLogger();


    private final Color imageBaseColor;
    private CartographService heatmapService;

    @Autowired
    public HeatmapImageService(
            CartographService heatmapService,
            @Value("${location.history.heatmap.base-color}") Integer colorHex
    ) {
        this.heatmapService = heatmapService;
        this.imageBaseColor = new Color(colorHex);
    }

    public byte[] getHeatmapImage(
            Rectangle2D viewport,
            double widthInBlocks,
            int pixelsPerBlock) throws IOException {

        HeatmapImagePainter md = new HeatmapImagePainter(
                new HeatmapImagePainter.HeatmapColourPicker.Monotone(imageBaseColor)
        );

        BufferedImage image = md.paintHeatmap(
                heatmapService.asHeatmap(viewport, widthInBlocks),
                pixelsPerBlock);

        LOG.info("Sending image out to file...");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "png", outputStream);

        return outputStream.toByteArray();

    }
}
