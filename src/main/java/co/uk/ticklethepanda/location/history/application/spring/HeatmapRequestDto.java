package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapDimensions;
import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LongLat;

import java.io.Serializable;

public class HeatmapRequestDto implements Serializable {
    private Integer width;
    private Integer height;

    private Integer pixelSize;

    private Float x;
    private Float y;
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

    public void setX(Float x) {
        this.x = x;
    }

    public void setY(Float y) {
        this.y = y;
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

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public HeatmapDimensions getSize() {
        return new HeatmapDimensions(width, height);
    }

    public LongLat getCenter() {
        return new LongLat(x, y);
    }

    public float getScale() {
        return scale;
    }

    public int getPixelSize() {
        return pixelSize;
    }

}
