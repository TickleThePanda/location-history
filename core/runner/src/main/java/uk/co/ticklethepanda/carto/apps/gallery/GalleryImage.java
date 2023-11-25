package uk.co.ticklethepanda.carto.apps.gallery;

import java.io.InputStream;

public record GalleryImage(GalleryImageDefinition combination, InputStream stream) {}