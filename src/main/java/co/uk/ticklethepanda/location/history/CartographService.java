package co.uk.ticklethepanda.location.history;

import co.uk.ticklethepanda.location.history.cartograph.Point;
import co.uk.ticklethepanda.location.history.cartograph.SpatialCollection;
import co.uk.ticklethepanda.location.history.cartographs.SpatialCollectionAnalyser;
import co.uk.ticklethepanda.location.history.cartographs.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartographs.quadtree.Quadtree;
import co.uk.ticklethepanda.location.history.points.PointConverters;
import co.uk.ticklethepanda.location.history.points.ecp.EcpPoint;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.location.history.points.googlelocation.GoogleLocations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.List;

@Service
public class CartographService {

    public static final Logger LOG = LogManager.getLogger();

    private final String filePath;

    @Autowired
    public CartographService(
            @Value("${location.history.file.path}")
                    String filePath
    ) {
        this.filePath = filePath;
    }

    private Quadtree<EcpPoint> cartograph;

    @PostConstruct
    public void loadModel() throws FileNotFoundException {
        LOG.info("Loading locations from file...");
        final List<GoogleLocation> locations =
                GoogleLocations.Loader
                        .fromFile(filePath)
                        .getLocations();

        LOG.info("Generating map...");

        List<EcpPoint> points =
                PointConverters.GOOGLE_TO_ECP
                        .convertList(locations);

        this.cartograph = new Quadtree<>(points);
    }

    public SpatialCollection<? extends Point> getCartograph() {
        return this.cartograph;
    }

    public Heatmap asHeatmap(
            Rectangle2D viewport,
            double widthInBlocks) {
        SpatialCollectionAnalyser<?> converter =
                new SpatialCollectionAnalyser<>(getCartograph());

        return converter.convertToHeatmap(viewport, widthInBlocks / viewport.getWidth());

    }
}
