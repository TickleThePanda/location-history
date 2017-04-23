package co.uk.ticklethepanda.location.history.application.spring;

import co.uk.ticklethepanda.location.history.cartograph.points.latlong.LatLong;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class HeatmapRequestDto implements Serializable {
    private Integer width;
    private Integer height;

    private Integer pixelSize;

    private Double x;
    private Double y;
    private Double scale;

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setPixelSize(Integer pixelSize) {
        this.pixelSize = pixelSize;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Point2D getSize() {
        return new Point2D.Double(width, height);
    }

    public LatLong getCenter() {
        return new LatLong(x, y);
    }

    public double getScale() {
        return scale;
    }

    public int getPixelSize() {
        return pixelSize;
    }

}
