package uk.co.ticklethepanda.carto.apps.gallery;

import java.io.InputStream;

public record GalleryImage(GalleryImageDefinition combination, InputStream stream) {

    public String getImageName() {
        return combination.config().getName();
    }

    public String getFilterName() {
        return combination.name();
    }

}