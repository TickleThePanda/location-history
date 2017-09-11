package co.uk.ticklethepanda.location.history.application.spring;

public class NamedLocation {

    private Integer width;
    private Integer height;

    private Integer pixelSize;

    private NamedLocationLongLat center;

    private Float scale;

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setPixelSize(Integer pixelSize) {
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

    public Integer getPixelSize() {
        return pixelSize;
    }

    public NamedLocationLongLat getCenter() {
        return center;
    }

    public void setCenter(NamedLocationLongLat center) {
        this.center = center;
    }
}
