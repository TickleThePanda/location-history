package co.uk.ticklethepanda.location.history.application.spring.heatmap;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;

import java.time.LocalDate;

public class HeatmapParams {
    private final HeatmapDescriptor<LocalDate> heatmapDescriptor;
    private final int pixelSize;

    public HeatmapParams(HeatmapDescriptor<LocalDate> heatmapDescriptor, int pixelSize) {
        this.heatmapDescriptor = heatmapDescriptor;
        this.pixelSize = pixelSize;
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public HeatmapDescriptor<LocalDate> getHeatmapDescriptor() {
        return heatmapDescriptor;
    }
}
