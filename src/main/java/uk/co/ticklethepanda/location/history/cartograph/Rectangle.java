package uk.co.ticklethepanda.location.history.cartograph;

public class Rectangle {

    private final float x;
    private final float y;
    private final float width;
    private final float height;

    private final float centerX;
    private final float centerY;
    private final float maxX;
    private final float maxY;

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.centerX = x + width / 2f;
        this.centerY = y + height / 2f;

        this.maxX = x + width;
        this.maxY = y + height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getMinX() {
        return x;
    }

    public float getMinY() {
        return y;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getCentreX() {
        return centerX;
    }

    public float getCentreY() {
        return centerY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean contains(float x, float y) {
        return this.x <= x && x < maxX &&
                this.y <= y && y < maxY;
    }

    public boolean intersects(Rectangle that) {
        return !(this.x > that.maxX ||
                this.maxX < that.x ||
                this.y > that.maxY ||
                this.maxY < that.y);
    }
}
