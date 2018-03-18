package co.uk.ticklethepanda.location.history.application.spring.country;

import co.uk.ticklethepanda.location.history.cartograph.projection.Projector;
import co.uk.ticklethepanda.location.history.cartograph.world.MapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.world.WorldMapDrawer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.*;

@Service
public class CountryImageService {

    private final CountryRepo countryRepo;
    private final Projector projector;

    private WorldMapDrawer worldMapDrawer;

    @Autowired
    public CountryImageService(
            CountryRepo countryRepo,
            Projector projector
    ) {
        this.countryRepo = countryRepo;
        this.projector = projector;
    }

    @PostConstruct
    public void init() {
        this.worldMapDrawer = WorldMapDrawer.createProjector(projector, countryRepo.getCountries());
    }

    public WorldMapDrawer getDrawer() {
        return worldMapDrawer;
    }
}
