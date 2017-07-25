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
    private final LocalDateFilterFactory filters;

    private final HeatmapParamResolver heatmapParamResolver;

    @Autowired
    public HeatmapController(
            HeatmapService cartographService,
            MapImageService mapImageService,
            LocalDateFilterFactory filters,
            HeatmapParamResolver heatmapParamResolver) {
        this.heatmapService = cartographService;
        this.mapImageService = mapImageService;
        this.filters = filters;
        this.heatmapParamResolver = heatmapParamResolver;
    }

    @RequestMapping(
            params = {"img", "sum"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImage(
            @ModelAttribute("default-heatmap") HeatmapRequestDto dto
    ) throws IOException {
        HeatmapParams params = heatmapParamResolver.resolve(dto);
        return mapImageService.getHeatmapImage(
                params.getHeatmapDescriptor(),
                params.getPixelSize()
        );
    }

    @RequestMapping(
            params = {"img", "sum", "weekday"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImageByDayOfWeek(
            @ModelAttribute("default-heatmap") HeatmapRequestDto dto,
            @RequestParam("weekday") DayOfWeek dayOfWeek
    ) throws IOException {
        HeatmapParams params = heatmapParamResolver.resolve(dto, filters.get(dayOfWeek));

        return mapImageService.getHeatmapImage(
                params.getHeatmapDescriptor(),
                params.getPixelSize()
        );
    }

    @RequestMapping(
            params = {"img", "sum", "month"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImageByMonth(
            @ModelAttribute("default-heatmap") HeatmapRequestDto dto,
            @RequestParam("month") Month month
    ) throws IOException {
        HeatmapParams params = heatmapParamResolver.resolve(dto, filters.get(month));

        return mapImageService.getHeatmapImage(
                params.getHeatmapDescriptor(),
                params.getPixelSize()
        );
    }

    @RequestMapping(
            params = {"img", "sum", "yearMonth"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImageByYearMonth(
            @ModelAttribute("default-heatmap") HeatmapRequestDto dto,
            @RequestParam("yearMonth") YearMonth yearMonth
    ) throws IOException {

        YearMonth lastMonth = YearMonth.now().minusMonths(1);

        if(lastMonth.isBefore(yearMonth) || lastMonth.equals(yearMonth)) {
            throw new IllegalArgumentException("yearMonth must be before last month");
        }

        HeatmapParams params = heatmapParamResolver.resolve(dto, filters.get(lastMonth));

        return mapImageService.getHeatmapImage(
                params.getHeatmapDescriptor(),
                params.getPixelSize()
        );
    }


    @RequestMapping(
            params = {"img", "sum", "year"},
            produces = "image/png"
    )
    @ResponseBody
    public byte[] getLocationHistoryMapImageByYear(
            @ModelAttribute("default-heatmap") HeatmapRequestDto dto,
            @RequestParam("year") int year
    ) throws IOException {
        HeatmapParams params = heatmapParamResolver.resolve(dto, filters.get(year));

        return mapImageService.getHeatmapImage(
                params.getHeatmapDescriptor(),
                params.getPixelSize()
        );
    }

}
