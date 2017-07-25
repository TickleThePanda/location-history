package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDimensions;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;

import java.io.Serializable;

public class HeatmapRequestDto implements Serializable {

    private String name;

    private Integer width;
    private Integer height;

    private Integer pixelSize;

    private Float x;
    private Float y;
    private Float scale;

    public void setName(String name) {
        this.name = name;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setPixelSize(Integer pixelSize) {
        this.pixelSize = pixelSize;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public Float getScale() {
        return scale;
    }

    public Integer getPixelSize() {
        return pixelSize;
    }

}
