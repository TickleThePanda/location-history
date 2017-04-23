package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLong;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLongHeatmapProjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;

@Service
public class HeatmapService {

    private CartographRepo repo;

    @Autowired
    public HeatmapService(CartographRepo repo) {
        this.repo = repo;
    }

    public Heatmap asHeatmap(
            Point2D size,
            LatLong center,
            double scale
    ) {
        LatLongHeatmapProjector converter =
                new LatLongHeatmapProjector(
                        repo.getCartograph(),
                        size,
                        center,
                        scale
                );

        return converter.project();

    }
}
