package uk.co.ticklethepanda.location.history.application.spring.heatmap;

import uk.co.ticklethepanda.location.history.cartograph.heatmap.HeatmapColourPicker;
import uk.co.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import uk.co.ticklethepanda.location.history.cartograph.heatmap.HeatmapImagePainter;
import uk.co.ticklethepanda.location.history.cartograph.heatmap.HeatmapProjector;
import uk.co.ticklethepanda.location.history.cartograph.world.MapDescriptor;
import uk.co.ticklethepanda.location.history.cartograph.world.ImageDimensions;
import uk.co.ticklethepanda.location.history.cartograph.world.MapTheme;
import uk.co.ticklethepanda.location.history.cartograph.world.WorldMapDrawer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final HeatmapProjector<LocalDate> heatmapProjector;
    private final WorldMapDrawer worldMapDrawer;
    private final MapTheme theme;

    private HeatmapImagePainter heatmapPainter;

    @Autowired
    public MapImageService(
            HeatmapProjector<LocalDate> heatmapProjector,
            WorldMapDrawer worldMapDrawer,
            MapTheme theme
    ) throws IOException {
        this.theme = theme;
        this.heatmapPainter = new HeatmapImagePainter(
                new HeatmapColourPicker.Monotone(theme.getHeatColor())
        );

        this.heatmapProjector = heatmapProjector;
        this.worldMapDrawer = worldMapDrawer;
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

        if(theme.isCountryMapEnabled()) {
            images.add(worldMapDrawer.draw(mapDescriptor, theme));
        }
        images.add(heatmapPainter.paintHeatmap(
                heatmapProjector.project(heatmapDescriptor),
                pixelsPerBlock));

        BufferedImage all = new BufferedImage(
                imageWidth, imageHeight,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) all.getGraphics();
        images.forEach(image -> g.drawImage(image, 0, 0, null));

        return convertImageToArray(all);
    }

    private byte[] convertImageToArray(BufferedImage image) throws IOException {

        LOG.info("Writing image to array.");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(5000);

        ImageIO.write(image, "png", outputStream);

        return outputStream.toByteArray();
    }

}
