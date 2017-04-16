package co.uk.ticklethepanda.location.history;

import co.uk.ticklethepanda.location.history.cartographs.heatmap.Heatmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/location")
public class HeatmapController {
    public static final Logger LOG = LogManager.getLogger();

    private static final int PIXELS_PER_BLOCK = 1;
    private static final double WIDTH_IN_BLOCKS = 1000;
    private static final Rectangle2D viewport = new Rectangle2D.Double(
            -208635,
            -3005546,
            264354,
            201649);

    private final CartographService cartographService;
    private final HeatmapImageService heatmapImageService;

    @Autowired
    public HeatmapController(
            CartographService cartographService,
            HeatmapImageService heatmapImageService
    ) {
        this.cartographService = cartographService;
        this.heatmapImageService = heatmapImageService;
    }

    @RequestMapping(
            params = {"img", "sum"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImage() throws IOException {
        return heatmapImageService.getHeatmapImage(viewport, WIDTH_IN_BLOCKS, PIXELS_PER_BLOCK);
    }

    @RequestMapping
    @ResponseBody
    public Heatmap getLocationHistoryMap() throws IOException {

        return cartographService.asHeatmap(viewport, 250);
    }

    @RequestMapping(
            params = "distribution"
    )
    @ResponseBody
    public List<Integer> getPointDistribution() throws IOException {
        Heatmap heatmap = cartographService.asHeatmap(viewport, 250);

        return Stream.of(heatmap.getHeatmap())
                .flatMapToInt(e -> Arrays.stream(e))
                .filter(e -> e != 0)
                .distinct()
                .boxed()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

    }

}
