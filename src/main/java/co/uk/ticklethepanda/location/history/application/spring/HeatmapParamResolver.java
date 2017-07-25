package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDimensions;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.function.Predicate;

/**
 * Created by czn83431 on 2017-07-25.
 */
@Component
public class HeatmapParamResolver {

    private final Environment env;
    private Float maxScale;

    @Autowired
    public HeatmapParamResolver(
            Environment env,
            @Value("${location.history.heatmap.minScale}") Float maxScale
    ) {
        this.env = env;
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
        LongLat center = resolveLongLat(name, dto.getX(), dto.getY());
        Float scale = resolveScale(name, dto.getScale());
        Integer pixelSize = resolvePixelSize(name, dto.getPixelSize());

        return new HeatmapParams(dimensions, center, scale, pixelSize, filter);
    }

    private void verify(HeatmapRequestDto dto) {
        if(dto.getX() != null ^ dto.getY() != null) {
            throw new IllegalArgumentException("Both x and y must be specified if either is specified");
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
            return new HeatmapDimensions(getWidthFromProperty(name), getHeightFromProperty(name));
        } else {
            return new HeatmapDimensions(width, height);
        }
    }

    private LongLat resolveLongLat(String name, Float x, Float y) {
        if(x == null && y == null) {
            return new LongLat(getXFromProperty(name), getYFromProperty(name));
        } else {
            return new LongLat(x, y);
        }
    }

    private Float resolveScale(String name, Float scale) {
        if(scale == null) {
            return getScaleFromProperty(name);
        } else {
            return scale;
        }
    }

    private Integer resolvePixelSize(String name, Integer pixelSize) {
        if(pixelSize == null) {
            return getPixelSizeFromProperty(name);
        } else {
            return pixelSize;
        }
    }

    private <T> T getProperty(String name, String key, Class<T> type) {
        return env.getProperty("location.history.heatmap." + name + "." + "key", type);
    }

    private Integer getWidthFromProperty(String name) {
        return getProperty(name, "width", Integer.class);
    }

    private Integer getHeightFromProperty(String name) {
        return getProperty(name, "height", Integer.class);
    }

    private Float getXFromProperty(String name) {
        return getProperty(name, "x", Float.class);
    }

    private Float getYFromProperty(String name) {
        return getProperty(name, "y", Float.class);
    }

    private Float getScaleFromProperty(String name) {
        return getProperty(name, "scale", Float.class);
    }

    private Integer getPixelSizeFromProperty(String name) {
        return getProperty(name, "pixelSize", Integer.class);
    }

}
