package uk.co.ticklethepanda.carto.apps.gallery;

import java.time.LocalDateTime;
import java.util.function.Predicate;

public record GalleryImageDefinition(HeatmapWindow config, String name, Predicate<LocalDateTime> filter) {}
