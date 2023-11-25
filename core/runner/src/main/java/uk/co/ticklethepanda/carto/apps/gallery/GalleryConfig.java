package uk.co.ticklethepanda.carto.apps.gallery;

import java.awt.Color;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import uk.co.ticklethepanda.carto.core.heatmap.HeatmapColourPicker;
import uk.co.ticklethepanda.carto.core.heatmap.HeatmapImagePainter;

public class GalleryConfig {

    private String firstMonth;
    private String heatmapColorHex;

    private List<HeatmapWindow> heatmaps;

    public GalleryConfig(List<HeatmapWindow> configurations, String firstMonth) {
        this.heatmaps = configurations;
        this.firstMonth = firstMonth;
    }

    public List<HeatmapWindow> getHeatmaps() {
        return heatmaps;
    }

    public void setHeatmaps(List<HeatmapWindow> heatmaps) {
        this.heatmaps = heatmaps;
    }

    public String getFirstMonth() {
        return firstMonth;
    }

    public void setFirstMonth(String firstMonth) {
        this.firstMonth = firstMonth;
    }

    public void setHeatmapColorHex(String heatmapColorHex) {
        this.heatmapColorHex = heatmapColorHex;
    }

    public String getHeatmapColorHex() {
        return heatmapColorHex;
    }

    public Map<String, Predicate<LocalDateTime>> getFilters() {
        Map<String, Predicate<LocalDateTime>> filters = new HashMap<>();

        filters.put("all", ignored -> true);

        for (DayOfWeek dow : DayOfWeek.values()) {
            filters.put(dow.toString().toLowerCase(), date -> date.getDayOfWeek().equals(dow));
        }

        for (Month month : Month.values()) {
            filters.put(month.toString().toLowerCase(), date -> date.getMonth().equals(month));
        }

        YearMonthRange yearMonths = YearMonthRange.between(
                YearMonth.parse(this.getFirstMonth()),
                YearMonth.from(LocalDate.now())
        );

        for (YearMonth yearMonth: yearMonths) {
            filters.put(yearMonth.toString().toLowerCase(), date -> YearMonth.from(date).equals(yearMonth));
        }

        return filters;
    }
    
    public HeatmapImagePainter createPainterFromConfig() {
        String colorHexString = this.getHeatmapColorHex();
        int colorHex = Integer.parseInt(colorHexString, 16);
        return new HeatmapImagePainter(new HeatmapColourPicker.Monotone(new Color(colorHex)));
    }



}
