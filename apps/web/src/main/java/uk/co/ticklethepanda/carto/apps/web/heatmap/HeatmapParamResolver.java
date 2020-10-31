package uk.co.ticklethepanda.carto.apps.web.heatmap;

import uk.co.ticklethepanda.carto.apps.web.geocoding.GeocodingClient;
import uk.co.ticklethepanda.carto.apps.web.geocoding.GeocodingClientException;
import uk.co.ticklethepanda.carto.apps.web.named.NamedLocations;
import uk.co.ticklethepanda.carto.core.heatmap.HeatmapDescriptor;
import uk.co.ticklethepanda.carto.core.heatmap.HeatmapDimensions;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Created by czn83431 on 2017-07-25.
 */
@Component
public class HeatmapParamResolver {

    private final GeocodingClient geocodingClient;
    
    private final NamedLocations namedLocations;

    private final Float maxScale;

    @Autowired
    public HeatmapParamResolver(
            NamedLocations namedLocations,
            @Value("${location.history.heatmap.minScale}") Float maxScale,
            GeocodingClient client
    ) throws IOException {
        this.namedLocations = namedLocations;
        this.maxScale = maxScale;
        this.geocodingClient = client;
    }

    public HeatmapParams resolve(HeatmapRequestDto dto) {
        return resolve(dto, null);
    }

    public HeatmapParams resolve(HeatmapRequestDto dto, Predicate<LocalDateTime> filter) {
        String name = dto.getName();
        if(name == null) {
            name = "default";
        }

        verify(dto);

        HeatmapDimensions dimensions = resolveDimensions(name, dto.getWidth(), dto.getHeight());
        LongLat center = resolveLongLat(name, dto.getSearch(), dto.getLongitude(), dto.getLatitude());
        Float scale = resolveScale(name, dto.getScale());

        HeatmapDescriptor<LocalDateTime> descriptor = new HeatmapDescriptor<>(dimensions, center, scale, filter);

        Float pixelSize = resolvePixelSize(name, dto.getPixelSize());

        return new HeatmapParams(descriptor, pixelSize);
    }

    private void verify(HeatmapRequestDto dto) {
        if (dto.getLongitude() != null ^ dto.getLatitude() != null) {
            throw new IllegalArgumentException(
                    "Both longitude and latitude must be specified if either is specified"
            );
        }
        
        if(dto.getWidth() != null ^ dto.getHeight() != null) {
            throw new IllegalArgumentException("Both width and height must be specified if either is specified");
        }

        if(dto.getScale() != null && dto.getScale() < maxScale) {
            throw new IllegalArgumentException("Scale is too low");
        }
    }

    private HeatmapDimensions resolveDimensions(String name, Integer width, Integer height) {
        if (width == null && height == null) {
            return new HeatmapDimensions(
                    namedLocations.getLocations().get(name).getWidth(),
                    namedLocations.getLocations().get(name).getHeight());
        } else {
            return new HeatmapDimensions(width, height);
        }
    }

    private LongLat resolveLongLat(String name, String query, Float longitude, Float latitude) {
        if(longitude == null && latitude == null && query == null) {
            return new LongLat(
                    namedLocations.getLocations().get(name).getCenter().getLongitude(),
                    namedLocations.getLocations().get(name).getCenter().getLatitude());
        } else if (query != null) {
            try {
                return geocodingClient.query(query);
            } catch (GeocodingClientException e) {
                throw new RuntimeException("Unable to fetch geocoding info");
            }
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

    private Float resolvePixelSize(String name, Float pixelSize) {
        if(pixelSize == null) {
            return namedLocations.getLocations().get(name).getPixelSize();
        } else {
            return pixelSize;
        }
    }

}
