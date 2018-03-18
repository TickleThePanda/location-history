package co.uk.ticklethepanda.location.history.application.spring.heatmap;

import co.uk.ticklethepanda.location.history.application.spring.country.CountryImageService;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapColourPicker;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapImagePainter;
import co.uk.ticklethepanda.location.history.cartograph.world.MapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.world.ImageDimensions;
import co.uk.ticklethepanda.location.history.cartograph.world.MapTheme;
import co.uk.ticklethepanda.location.history.cartograph.world.WorldMapDrawer;
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

    private static final Logger LOG = LogManager.getLogger();

    private final CountryImageService countryImageService;
    private final Color heatColor;
    private final Color waterColor;
    private Integer colorHex;
    private Integer fillHex;
    private final boolean mapEnabled;
    private HeatmapService heatmapService;
    private HeatmapImagePainter heatmapPainter;

    @Autowired
    public MapImageService(
            HeatmapService heatmapService,
            CountryImageService countryImageService,
            @Value("${map.colors.country.outline}") Integer colorHex,
            @Value("${map.colors.country.fill}") Integer fillHex,
            @Value("${map.colors.heat.base}") Integer heatHex,
            @Value("${map.colors.water}") Integer waterHex,
            @Value("${map.enabled}") boolean enabled
    ) throws IOException {
        this.colorHex = colorHex;
        this.fillHex = fillHex;
        this.mapEnabled = enabled;
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
                new ImageDimensions(imageWidth, imageHeight),
                heatmapDescriptor.getCenter(),
                heatmapDescriptor.getBoxSize() / pixelsPerBlock
        );

        List<BufferedImage> images = new ArrayList<>();

        if(mapEnabled) {
            WorldMapDrawer drawer = countryImageService.getDrawer();
            MapTheme theme = new MapTheme(new Color(colorHex), new Color(fillHex));
            images.add(drawer.draw(mapDescriptor, theme));
        }
        images.add(heatmapPainter.paintHeatmap(
                heatmapService.asHeatmap(heatmapDescriptor),
                pixelsPerBlock));

        BufferedImage all = new BufferedImage(
                imageWidth, imageHeight,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) all.getGraphics();

        if(mapEnabled) {
            g.setColor(waterColor);
            g.fillRect(0, 0, imageWidth, imageHeight);
        }
        images.forEach(image -> g.drawImage(image, 0, 0, null));

        return convertImageToArray(all);
    }

    public byte[] convertImageToArray(BufferedImage image) throws IOException {

        LOG.info("Writing image to array.");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(5000);

        ImageIO.write(image, "png", outputStream);

        return outputStream.toByteArray();
    }

}
