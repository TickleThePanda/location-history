package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;
import co.uk.ticklethepanda.location.history.cartograph.world.MapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.world.MapDrawer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class CountryImageService {

    private final CountryRepo countryRepo;
    private final Color outlineColor;
    private final Color fillColor;

    @Autowired
    public CountryImageService(
            CountryRepo countryRepo,
            @Value("${map.colors.country.outline}") Integer colorHex,
            @Value("${map.colors.country.fill}") Integer fillHex
    ) {
        this.countryRepo = countryRepo;

        this.outlineColor = new Color(colorHex);
        this.fillColor = new Color(fillHex);
    }

    public BufferedImage drawFill(MapDescriptor mapDescriptor) {
        return new MapDrawer(countryRepo.getCountries(), mapDescriptor, outlineColor, fillColor).drawFill();
    }

    public BufferedImage drawOutline(MapDescriptor mapDescriptor) {
        return new MapDrawer(countryRepo.getCountries(), mapDescriptor, outlineColor, fillColor).drawOutline();
    }
}
