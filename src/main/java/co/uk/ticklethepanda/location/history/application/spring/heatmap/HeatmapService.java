package co.uk.ticklethepanda.location.history.application.spring.heatmap;

import co.uk.ticklethepanda.location.history.application.spring.cartograph.CartographRepo;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.Heatmap;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDescriptor;
import co.uk.ticklethepanda.location.history.cartograph.projections.NoProjectionHeatmapProjector;
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
            HeatmapDescriptor<LocalDate> heatmapDescriptor) {
        return new NoProjectionHeatmapProjector(repo.getCartograph(), heatmapDescriptor)
                .project();
    }


}
