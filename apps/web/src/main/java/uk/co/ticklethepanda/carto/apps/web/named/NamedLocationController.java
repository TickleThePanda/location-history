package uk.co.ticklethepanda.carto.apps.web.named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/location/names")
public class NamedLocationController {

    private final NamedLocations namedLocations;

    @Autowired
    public NamedLocationController(NamedLocations namedLocations) {
        this.namedLocations = namedLocations;
    }

    @GetMapping
    @ResponseBody
    public Map<String, NamedLocation> getNamedLocations() {
        return namedLocations.getLocations();
    }

}
