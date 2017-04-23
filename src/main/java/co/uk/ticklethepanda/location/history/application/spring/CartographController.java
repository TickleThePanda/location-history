package co.uk.ticklethepanda.location.history.application.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping("/location/source")
public class CartographController {

    private static final Logger LOG = LogManager.getLogger();

    private final CartographRepo repo;

    @Autowired
    public CartographController(
            CartographRepo repo
    ) {
        this.repo = repo;
    }

    @RequestMapping(
            method = RequestMethod.POST
    )
    @ResponseBody
    public String saveLocations(@RequestParam("locations") MultipartFile file) throws IOException {
        ZipInputStream inputStream = new ZipInputStream(file.getInputStream());

        boolean complete = false;

        ZipEntry entry;
        while ((entry = inputStream.getNextEntry()) != null) {
            LOG.info("found entry: {}", entry.getName());
            if (!entry.isDirectory()
                    && entry.getName().equals("Takeout/Location History/LocationHistory.json")) {
                repo.save(inputStream);
                complete = true;
            }
        }

        return complete ? "save successful" : "could not find the LocationHistory.json file in zip";
    }
}
