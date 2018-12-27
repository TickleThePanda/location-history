package uk.co.ticklethepanda.location.history.application.lambda;

import java.time.YearMonth;
import java.util.List;

public class HeatmapLambdaConfiguration {

    private String firstMonth;
    private String heatmapColorHex;

    private List<HeatmapConfiguration> heatmaps;

    public HeatmapLambdaConfiguration(List<HeatmapConfiguration> configurations, String firstMonth) {
        this.heatmaps = configurations;
        this.firstMonth = firstMonth;
    }

    public List<HeatmapConfiguration> getHeatmaps() {
        return heatmaps;
    }

    public void setHeatmaps(List<HeatmapConfiguration> heatmaps) {
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
}
