package uk.co.ticklethepanda.carto.apps.web.heatmap;

import java.io.Serializable;

public class HeatmapRequestDto implements Serializable {

    private String name;

    private Integer width;
    private Integer height;

    private Float pixelSize;

    private String search;
    
    private Float longitude;
    private Float latitude;
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

    public void setPixelSize(Float pixelSize) {
        this.pixelSize = pixelSize;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setSearch(String search) {
        this.search = search;
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

    public Float getLongitude() {
        return longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getScale() {
        return scale;
    }

    public Float getPixelSize() {
        return pixelSize;
    }

    public String getSearch() {
        return search;
    }
}
