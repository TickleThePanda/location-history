package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapColourPicker;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapImagePainter;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class HeatmapImageService {

    public static final Logger LOG = LogManager.getLogger();

    private final Color imageBaseColor;
    private HeatmapService heatmapService;

    @Autowired
    public HeatmapImageService(
            HeatmapService heatmapService,
            @Value("${location.history.heatmap.base-color}") Integer colorHex
    ) {
        this.heatmapService = heatmapService;
        this.imageBaseColor = new Color(colorHex);
    }

    @Cacheable("heatmap-image")
    public byte[] getHeatmapImage(
            Point2D size,
            LatLong center,
            double scale,
            int pixelsPerBlock
    ) throws IOException {

        HeatmapImagePainter md = new HeatmapImagePainter(
                new HeatmapColourPicker.Monotone(imageBaseColor)
        );

        BufferedImage image = md.paintHeatmap(
                heatmapService.asHeatmap(size, center, scale),
                pixelsPerBlock);

        LOG.info("Sending image out to file...");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "png", outputStream);

        return outputStream.toByteArray();

    }
}
