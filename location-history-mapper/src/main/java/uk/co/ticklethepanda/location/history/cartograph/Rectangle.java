package uk.co.ticklethepanda.location.history.cartograph;

import uk.co.ticklethepanda.location.history.cartograph.projection.Point;

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

    public boolean contains(Point point) {
        return contains(point.getHorizontalComponent(), point.getVerticalComponent());
    }

    /**
     * Checks if a point lies within the external boundary
     * of the rectangle.
     * @param x the x coordinate of the point to check
     * @param y the y coordinate of the point to check
     * @return whether the point lies within the external boundary of this
     * rectangle
     */
    public boolean contains(float x, float y) {
        return this.x <= x && x <= maxX &&
                this.y <= y && y <= maxY;
    }

    /**
     * Checks if any point in either rectangle lies within the external
     * boundary of the other rectangle.
     * @param that the other rectangle
     * @return {@code true} if there's an intersection, {@code false} if there's not
     */
    public boolean intersects(Rectangle that) {
        return this.x <= that.maxX
                && this.maxX >= that.x
                && this.y <= that.maxY
                && this.maxY >= that.y;
    }
}
