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

    @Autowired
    public CountryImageService(
            CountryRepo countryRepo,
            @Value("${countries.outline.color}") Integer colorHex
    ) {
        this.countryRepo = countryRepo;

        this.outlineColor = new Color(colorHex);
    }


    public BufferedImage drawMap(MapDescriptor<LongLat> mapDescriptor) {

        return new MapDrawer(countryRepo.getCountries(), mapDescriptor, outlineColor).drawMap();
    }
}
