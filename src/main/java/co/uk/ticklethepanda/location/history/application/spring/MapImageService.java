package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapColourPicker;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapImagePainter;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;
import co.uk.ticklethepanda.location.history.cartograph.world.MapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.world.MapDimensions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class MapImageService {

    public static final Logger LOG = LogManager.getLogger();

    private final Color imageBaseColor;
    private final CountryImageService countryImageService;
    private HeatmapService heatmapService;
    private HeatmapImagePainter heatmapPainter;

    @Autowired
    public MapImageService(
            HeatmapService heatmapService,
            CountryImageService countryImageService,
            @Value("${location.history.heatmap.base-color}") Integer colorHex
    ) throws IOException {
        this.heatmapService = heatmapService;
        this.imageBaseColor = new Color(colorHex);
        this.heatmapPainter = new HeatmapImagePainter(
                new HeatmapColourPicker.Monotone(imageBaseColor)
        );
        this.countryImageService = countryImageService;
    }

    @Cacheable("heatmap-image")
    public byte[] getHeatmapImage(
            HeatmapDescriptor<LongLat, LocalDate> heatmapDescriptor,
            int pixelsPerBlock
    ) throws IOException {

        BufferedImage image = heatmapPainter.paintHeatmap(
                heatmapService.asHeatmap(heatmapDescriptor),
                pixelsPerBlock);

        MapDescriptor<LongLat> mapDescriptor = new MapDescriptor<>(
                new MapDimensions(
                        heatmapDescriptor.getDimensions().getWidth() * pixelsPerBlock,
                        heatmapDescriptor.getDimensions().getHeight() * pixelsPerBlock),
                heatmapDescriptor.getCenter(),
                heatmapDescriptor.getScale() / pixelsPerBlock
        );

        Graphics2D g = (Graphics2D) image.getGraphics();

        g.drawImage(countryImageService.drawMap(mapDescriptor), 0, 0, null);

        return convertImageToArray(image);
    }

    public byte[] convertImageToArray(BufferedImage image) throws IOException {

        LOG.info("Writing image to array.");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "png", outputStream);

        return outputStream.toByteArray();
    }

}
