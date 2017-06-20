package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.YearMonth;
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
    private final MapImageService mapImageService;
    private final HeatmapRequestDefaults defaults;
    private final LocalDateFilterFactory filters;

    @Autowired
    public HeatmapController(
            HeatmapService cartographService,
            MapImageService mapImageService,
            HeatmapRequestDefaults defaults,
            LocalDateFilterFactory filters
    ) {
        this.heatmapService = cartographService;
        this.mapImageService = mapImageService;
        this.defaults = defaults;
        this.filters = filters;
    }

    @ModelAttribute("default-heatmap")
    public HeatmapRequestDto getHeatmapRequest(
            @RequestParam(required = false) Integer width,
            @RequestParam(required = false) Integer height,
            @RequestParam(required = false) Integer pixelSize,
            @RequestParam(required = false) Float x,
            @RequestParam(required = false) Float y,
            @RequestParam(required = false) Float scale
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
            @ModelAttribute("default-heatmap") HeatmapRequestDto param
    ) throws IOException {

        if (param.getScale() < defaults.getMinScale()) {
            throw new IllegalArgumentException("scale must be more than " + defaults.getMinScale());
        }

        return mapImageService.getHeatmapImage(
                new HeatmapDescriptor<>(param.getSize(), param.getCenter(), param.getScale()),
                param.getPixelSize()
        );
    }

    @RequestMapping(
            params = {"img", "sum", "weekday"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImageByDayOfWeek(
            @ModelAttribute("default-heatmap") HeatmapRequestDto param,
            @RequestParam("weekday") DayOfWeek dayOfWeek
    ) throws IOException {

        if (param.getScale() < defaults.getMinScale()) {
            throw new IllegalArgumentException("scale must be more than " + defaults.getMinScale());
        }

        return mapImageService.getHeatmapImage(
                new HeatmapDescriptor<>(
                        param.getSize(),
                        param.getCenter(),
                        param.getScale(),
                        filters.get(dayOfWeek)),
                param.getPixelSize()
        );
    }

    @RequestMapping(
            params = {"img", "sum", "month"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImageByMonth(
            @ModelAttribute("default-heatmap") HeatmapRequestDto param,
            @RequestParam("month") Month month
    ) throws IOException {

        if (param.getScale() < defaults.getMinScale()) {
            throw new IllegalArgumentException("scale must be more than " + defaults.getMinScale());
        }

        return mapImageService.getHeatmapImage(
                new HeatmapDescriptor<>(
                        param.getSize(),
                        param.getCenter(),
                        param.getScale(),
                        filters.get(month)),
                param.getPixelSize()
        );
    }

    @RequestMapping(
            params = {"img", "sum", "yearMonth"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImageByYearMonth(
            @ModelAttribute("default-heatmap") HeatmapRequestDto param,
            @RequestParam("yearMonth") YearMonth yearMonth
    ) throws IOException {

        if (param.getScale() < defaults.getMinScale()) {
            throw new IllegalArgumentException("scale must be more than " + defaults.getMinScale());
        }

        return mapImageService.getHeatmapImage(
                new HeatmapDescriptor<>(
                        param.getSize(),
                        param.getCenter(),
                        param.getScale(),
                        filters.get(yearMonth)),
                param.getPixelSize()
        );
    }


    @RequestMapping(
            params = {"img", "sum", "year"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImageByYear(
            @ModelAttribute("default-heatmap") HeatmapRequestDto param,
            @RequestParam("year") int year
    ) throws IOException {

        if (param.getScale() < defaults.getMinScale()) {
            throw new IllegalArgumentException("scale must be more than " + defaults.getMinScale());
        }

        return mapImageService.getHeatmapImage(
                new HeatmapDescriptor<>(
                        param.getSize(),
                        param.getCenter(),
                        param.getScale(),
                        filters.get(year)),
                param.getPixelSize()
        );
    }

    @RequestMapping
    @ResponseBody
    public Heatmap getLocationHistoryMap() throws IOException {
        return heatmapService.asHeatmap(
                new HeatmapDescriptor<>(
                        defaults.getSize(),
                        defaults.getCenter(),
                        defaults.getScale())
        );
    }

    @RequestMapping(
            params = "distribution"
    )
    @ResponseBody
    public List<Integer> getPointDistribution() throws IOException {
        Heatmap heatmap = heatmapService.asHeatmap(
                new HeatmapDescriptor<>(
                        defaults.getSize(),
                        defaults.getCenter(),
                        defaults.getScale())
        );

        return Stream.of(heatmap.getHeatmap())
                .flatMapToInt(e -> Arrays.stream(e))
                .filter(e -> e != 0)
                .distinct()
                .boxed()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

    }

}
