package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping(
        value = "/location",
        method = RequestMethod.GET)
public class HeatmapController {

    private static final Logger LOG = LogManager.getLogger();

    private final HeatmapService heatmapService;
    private final HeatmapImageService heatmapImageService;
    private final HeatmapRequestDefaults defaults;

    @Autowired
    public HeatmapController(
            HeatmapService cartographService,
            HeatmapImageService heatmapImageService,
            HeatmapRequestDefaults defaults
    ) {
        this.heatmapService = cartographService;
        this.heatmapImageService = heatmapImageService;
        this.defaults = defaults;
    }

    @ModelAttribute("default-heatmap")
    public HeatmapRequestDto getHeatmapRequest(
            @RequestParam(required = false) Integer width,
            @RequestParam(required = false) Integer height,
            @RequestParam(required = false) Integer pixelSize,
            @RequestParam(required = false) Double x,
            @RequestParam(required = false) Double y,
            @RequestParam(required = false) Double scale
    ) {
        if (width != null ^ height != null) {
            throw new IllegalArgumentException("expected neither or both width and height to be specifed");
        }
        if (x != null ^ y != null) {
            throw new IllegalArgumentException("expected neither or both x and y to be specifed");
        }

        HeatmapRequestDto defaultRequest = new HeatmapRequestDto();
        defaultRequest.setWidth(width != null ? width : defaults.getHeatmapWidth());
        defaultRequest.setHeight(height != null ? height : defaults.getHeatmapHeight());
        defaultRequest.setPixelSize(pixelSize != null ? pixelSize : defaults.getPixelSize());
        defaultRequest.setX(x != null ? x : defaults.getCenterLat());
        defaultRequest.setY(y != null ? y : defaults.getCenterLong());
        defaultRequest.setScale(scale != null ? scale : defaults.getScale());
        return defaultRequest;
    }

    @RequestMapping(
            params = {"img", "sum"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImage(
            @ModelAttribute("default-heatmap") HeatmapRequestDto param) throws IOException {

        if (param.getScale() < defaults.getMinScale()) {
            throw new IllegalArgumentException("scale must be more than " + defaults.getMinScale());
        }

        return heatmapImageService.getHeatmapImage(
                param.getSize(),
                param.getCenter(),
                param.getScale(),
                param.getPixelSize()
        );
    }

    @RequestMapping
    @ResponseBody
    public Heatmap getLocationHistoryMap() throws IOException {
        return heatmapService.asHeatmap(
                defaults.getSize(),
                defaults.getCenter(),
                defaults.getScale());
    }

    @RequestMapping(
            params = "distribution"
    )
    @ResponseBody
    public List<Integer> getPointDistribution() throws IOException {
        Heatmap heatmap = heatmapService.asHeatmap(
                defaults.getSize(),
                defaults.getCenter(),
                defaults.getScale());

        return Stream.of(heatmap.getHeatmap())
                .flatMapToInt(e -> Arrays.stream(e))
                .filter(e -> e != 0)
                .distinct()
                .boxed()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

    }

}
