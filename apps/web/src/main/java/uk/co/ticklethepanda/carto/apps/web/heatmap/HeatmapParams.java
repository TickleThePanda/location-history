package uk.co.ticklethepanda.carto.apps.web.heatmap;

import uk.co.ticklethepanda.carto.core.heatmap.HeatmapDescriptor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HeatmapParams {
    private final HeatmapDescriptor<LocalDateTime> heatmapDescriptor;
    private final float pixelSize;

    public HeatmapParams(HeatmapDescriptor<LocalDateTime> heatmapDescriptor, float pixelSize) {
        this.heatmapDescriptor = heatmapDescriptor;
        this.pixelSize = pixelSize;
    }

    public float getPixelSize() {
        return pixelSize;
    }

    public HeatmapDescriptor<LocalDateTime> getHeatmapDescriptor() {
        return heatmapDescriptor;
    }
}
