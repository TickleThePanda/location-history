package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLong;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLongHeatmapProjector;
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
            HeatmapDescriptor<LatLong, LocalDate> heatmapDescriptor) {
        return new LatLongHeatmapProjector(repo.getCartograph(), heatmapDescriptor)
                .project();
    }


}
