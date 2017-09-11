package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDimensions;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.function.Predicate;

/**
 * Created by czn83431 on 2017-07-25.
 */
@Component
public class HeatmapParamResolver {

    private NamedLocations namedLocations;

    private Float maxScale;

    @Autowired
    public HeatmapParamResolver(
            NamedLocations namedLocations,
            @Value("${location.history.heatmap.minScale}") Float maxScale
    ) throws IOException {
        this.namedLocations = namedLocations;
        this.maxScale = maxScale;
    }

    public HeatmapParams resolve(HeatmapRequestDto dto) {
        return resolve(dto, null);
    }

    public HeatmapParams resolve(HeatmapRequestDto dto, Predicate<LocalDate> filter) {
        String name = dto.getName();
        if(name == null) {
            name = "default";
        }

        verify(dto);

        HeatmapDimensions dimensions = resolveDimensions(name, dto.getWidth(), dto.getHeight());
        LongLat center = resolveLongLat(name, dto.getLongitude(), dto.getLatitude());
        Float scale = resolveScale(name, dto.getScale());
        Integer pixelSize = resolvePixelSize(name, dto.getPixelSize());

        return new HeatmapParams(dimensions, center, scale, pixelSize, filter);
    }

    private void verify(HeatmapRequestDto dto) {
        if(dto.getLongitude() != null ^ dto.getLatitude() != null) {
            throw new IllegalArgumentException("Both longitude and latitude must be specified if either is specified");
        }

        if(dto.getWidth() != null ^ dto.getHeight() != null) {
            throw new IllegalArgumentException("Both width and height must be specified if either is specified");
        }

        if(dto.getScale() != null && dto.getScale() < maxScale) {
            throw new IllegalArgumentException("Scale is too low");
        }
    }

    private HeatmapDimensions resolveDimensions(String name, Integer width, Integer height) {
        if(width == null && height == null) {
            return new HeatmapDimensions(
                    namedLocations.getLocations().get(name).getWidth(),
                    namedLocations.getLocations().get(name).getHeight());
        } else {
            return new HeatmapDimensions(width, height);
        }
    }

    private LongLat resolveLongLat(String name, Float longitude, Float latitude) {
        if(longitude == null && latitude == null) {
            return new LongLat(
                    namedLocations.getLocations().get(name).getCenter().getLongitude(),
                    namedLocations.getLocations().get(name).getCenter().getLatitude());
        } else {
            return new LongLat(longitude, latitude);
        }
    }

    private Float resolveScale(String name, Float scale) {
        if(scale == null) {
            return namedLocations.getLocations().get(name).getScale();
        } else {
            return scale;
        }
    }

    private Integer resolvePixelSize(String name, Integer pixelSize) {
        if(pixelSize == null) {
            return namedLocations.getLocations().get(name).getPixelSize();
        } else {
            return pixelSize;
        }
    }

}
