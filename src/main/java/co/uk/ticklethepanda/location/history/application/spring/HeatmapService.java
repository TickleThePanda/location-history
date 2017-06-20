package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLatHeatmapProjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HeatmapService {

    private CartographRepo repo;

    @Autowired
    public HeatmapService(CartographRepo repo) {
        this.repo = repo;
    }

    public Heatmap asHeatmap(
            HeatmapDescriptor<LongLat, LocalDate> heatmapDescriptor) {
        return new LongLatHeatmapProjector(repo.getCartograph(), heatmapDescriptor)
                .project();
    }


}
