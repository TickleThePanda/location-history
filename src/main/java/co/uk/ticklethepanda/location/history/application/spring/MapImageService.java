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
import java.util.ArrayList;
import java.util.List;

@Service
public class MapImageService {

    public static final Logger LOG = LogManager.getLogger();

    private final CountryImageService countryImageService;
    private final Color heatColor;
    private final Color waterColor;
    private HeatmapService heatmapService;
    private HeatmapImagePainter heatmapPainter;

    @Autowired
    public MapImageService(
            HeatmapService heatmapService,
            CountryImageService countryImageService,
            @Value("${map.colors.heat.base}") Integer heatHex,
            @Value("${map.colors.water}") Integer waterHex
    ) throws IOException {
        this.heatmapService = heatmapService;
        this.heatColor = new Color(heatHex);
        this.waterColor = new Color(waterHex);
        this.heatmapPainter = new HeatmapImagePainter(
                new HeatmapColourPicker.Monotone(heatColor)
        );
        this.countryImageService = countryImageService;
    }

    @Cacheable("heatmap-image")
    public byte[] getHeatmapImage(
            HeatmapDescriptor<LocalDate> heatmapDescriptor,
            int pixelsPerBlock
    ) throws IOException {

        int imageWidth = heatmapDescriptor.getDimensions().getWidth() * pixelsPerBlock;
        int imageHeight = heatmapDescriptor.getDimensions().getHeight() * pixelsPerBlock;

        MapDescriptor mapDescriptor = new MapDescriptor(
                new MapDimensions(imageWidth, imageHeight),
                heatmapDescriptor.getCenter(),
                heatmapDescriptor.getScale() / pixelsPerBlock
        );

        List<BufferedImage> images = new ArrayList<>();

        images.add(countryImageService.drawFill(mapDescriptor));
        images.add(heatmapPainter.paintHeatmap(
                heatmapService.asHeatmap(heatmapDescriptor),
                pixelsPerBlock));
        images.add(countryImageService.drawOutline(mapDescriptor));

        BufferedImage all = new BufferedImage(
                imageWidth, imageHeight,
                BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g = (Graphics2D) all.getGraphics();

        g.setColor(waterColor);
        g.fillRect(0, 0, imageWidth, imageHeight);
        images.forEach(image -> g.drawImage(image, 0, 0, null));

        return convertImageToArray(all);
    }

    public byte[] convertImageToArray(BufferedImage image) throws IOException {

        LOG.info("Writing image to array.");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "png", outputStream);

        return outputStream.toByteArray();
    }

}
