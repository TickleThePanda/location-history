package co.uk.ticklethepanda.location.history;

import co.uk.ticklethepanda.location.history.cartograph.SpatialCollection;
import co.uk.ticklethepanda.location.history.cartographs.SpatialCollectionAnalyser;
import co.uk.ticklethepanda.location.history.cartographs.heatmap.HeatmapImagePainter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/location/heatmap")
public class HeatmapController {

    public static final Logger LOG = LogManager.getLogger();

    private static final int IMAGE_BLOCK_SIZE = 4;

    private static final Rectangle2D viewport = new Rectangle2D.Double(
            -208635,
            -3005546,
            264354,
            201649);

    private final HeatmapService heatmapService;

    @Autowired
    public HeatmapController(HeatmapService heatmapService) {
        this.heatmapService = heatmapService;
    }

    @RequestMapping(
            params = {"img"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] locationHistoryMap() throws IOException {

        SpatialCollection<?> model = heatmapService.getCartograph();

        HeatmapImagePainter md = new HeatmapImagePainter(
                new HeatmapImagePainter.HeatmapColourPicker.Monotone()
        );

        SpatialCollectionAnalyser<?> converter =
                new SpatialCollectionAnalyser<>(model);

        BufferedImage image = md.paintHeatmap(
                converter.convertToHeatmap(viewport, 250.0 / viewport.getWidth()), IMAGE_BLOCK_SIZE);

        LOG.info("Sending image out to file...");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "png", outputStream);

        return outputStream.toByteArray();
    }


}
