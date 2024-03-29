// convert this from using lambda to cloud run
package uk.co.ticklethepanda.carto.apps.gallery;
import uk.co.ticklethepanda.carto.core.heatmap.*;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.projection.Projector;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.*;
import java.time.*;
import java.util.*;
import java.util.List;

public class GalleryBuilder {

    private static final Logger LOG = LogManager.getLogger();

    @FunctionalInterface
    public static interface HistoryProvider {
        public List<PointData<LongLat, LocalDateTime>> getPoints() throws IOException;
    }

    @FunctionalInterface
    public static interface ConfigProvider {
        public GalleryConfig getConfig() throws IOException;
    }

    @FunctionalInterface
    public static interface GalleryImageWriter {
        public void writeImage(GalleryImage image) throws IOException;
    }

    private GalleryImageWriter imageWriter;
    private ConfigProvider configProvider;
    private HistoryProvider pointsProvider;

    public GalleryBuilder(
        HistoryProvider dataProvider,
        ConfigProvider configProvider,
        GalleryImageWriter imageWriter
    ) {
        this.pointsProvider = dataProvider;
        this.configProvider = configProvider;
        this.imageWriter = imageWriter;
    }

    public void build(Projector projector) throws IOException {
        var config = configProvider.getConfig();
        var points = pointsProvider.getPoints();

        class RuntimeIOException extends RuntimeException {
            public RuntimeIOException(GalleryImage image, IOException e) {
                super("Unable to write image " + image, e);
            }
        }

        try {
            this
                .getImageIterator(
                    projector,
                    points,
                    config
                )
                .forEachRemaining(image -> {
                    try {
                        imageWriter.writeImage(image);
                    } catch (IOException e) {
                        throw new RuntimeIOException(image, e);
                    }
                });
        } catch (RuntimeIOException ioException) {
            throw new IOException("Failed to write image", ioException.getCause());
        }
    }

    public Iterator<GalleryImage> getImageIterator(
            Projector projector,
            List<PointData<LongLat, LocalDateTime>> points,
            GalleryConfig configuration) throws IOException {

        var projection = HeatmapProjector.createProjection(projector, points);
        var filters = configuration.getFilters();
        var painter = configuration.createPainterFromConfig();

        return configuration
            .getHeatmaps()
            .stream()
            .flatMap(heatmap ->
                filters
                    .entrySet()
                    .stream()
                    .map(filter ->
                        new GalleryImageDefinition(
                            heatmap,
                            filter.getKey(),
                            filter.getValue()
                        )
                    )
            )
            .map(imageCombination -> {
                LOG.info("Generating image for " + imageCombination);
                var descriptor = imageCombination.config().with(imageCombination.filter());
                var heatmap = projection.project(descriptor);
                LOG.info("Found " + heatmap.countPoints() + " points in heatmap.");
                BufferedImage bufferedImage = painter.paintHeatmap(heatmap, 4);
                InputStream stream = getImageAsStream(bufferedImage);
                return new GalleryImage(imageCombination, stream);    
            })
            .iterator();
    }


    private InputStream getImageAsStream(BufferedImage image) {
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(5000);

            ImageIO.write(image, "png", outputStream);

            byte[] bytes = outputStream.toByteArray();

            return new ByteArrayInputStream(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write images.", e);
        }
    }
}
