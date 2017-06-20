package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;
import co.uk.ticklethepanda.location.history.cartograph.world.CountryPolygonConverter;
import co.uk.ticklethepanda.location.history.cartograph.world.CountryPolygonLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Repository
public class CountryRepo {

    public static final Logger LOG = LogManager.getLogger();

    private String filePath;
    private List<List<LongLat>> countries;

    @Autowired
    public CountryRepo(
            @Value("${countries.file.path}") String filePath
    ) {
        this.filePath = filePath;
    }

    @PostConstruct
    public void loadCountries() throws IOException {

        LOG.info("Loading countries from file...");

        CountryPolygonConverter converter = new CountryPolygonConverter();
        this.countries = converter.convertList(
                CountryPolygonLoader.countryPolygonLoader("input/countries.geojson"));
    }

    public List<List<LongLat>> getCountries() {
        return countries;
    }
}
