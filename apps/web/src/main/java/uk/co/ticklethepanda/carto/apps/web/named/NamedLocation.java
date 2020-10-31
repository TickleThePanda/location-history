package uk.co.ticklethepanda.carto.apps.web.named;

public class NamedLocation {

    private Integer width;
    private Integer height;

    private Float pixelSize;

    private NamedLocationLongLat center;

    private Float scale;

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setPixelSize(Float pixelSize) {
        this.pixelSize = pixelSize;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Float getScale() {
        return scale;
    }

    public Float getPixelSize() {
        return pixelSize;
    }

    public NamedLocationLongLat getCenter() {
        return center;
    }

    public void setCenter(NamedLocationLongLat center) {
        this.center = center;
    }
}
