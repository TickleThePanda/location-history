package uk.co.ticklethepanda.carto.core;

import org.junit.jupiter.api.Test;
import uk.co.ticklethepanda.carto.core.model.Rectangle;

import static org.assertj.core.api.Assertions.assertThat;

class RectangleTest {

    /**
     * <pre>
     *  ____
     * |1   |
     * |  __|__
     * |_|__|  |
     *   |   2 |
     *   |_____|
     *
     * </pre>
     */
    @Test
    public void intersects_topLeft$bottomRight_true() {
        Rectangle one = new Rectangle(0, 0, 4, 4);
        Rectangle two = new Rectangle(2, 2, 4, 4);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }

    /**
     * <pre>
     *      _____
     *     |   1 |
     *   __|__   |
     *  |  |__|__|
     *  | 2   |
     *  |_____|
     *
     * </pre>
     */
    @Test
    public void intersects_topRight$bottomLeft_true() {
        Rectangle one = new Rectangle(2, 0, 4, 4);
        Rectangle two = new Rectangle(0, 2, 4, 4);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }

    /**
     * <pre>
     *        ____
     *   ____|_  2|
     *  |1   | |  |
     *  |____|_|  |
     *       |____|
     *
     * </pre>
     */
    @Test
    public void intersects_rightSideWithin_true() {
        Rectangle one = new Rectangle(0, 1, 4, 2);
        Rectangle two = new Rectangle(2, 0, 4, 4);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }

    /**
     * <pre>
     *  ____
     * |2  _|___
     * |  | |  1|
     * |  |_|___|
     * |____|
     *
     * </pre>
     */
    @Test
    public void intersects_leftSideWithin_true() {
        Rectangle one = new Rectangle(2, 1, 4, 2);
        Rectangle two = new Rectangle(0, 0, 4, 4);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }

    /**
     * <pre>
     *    ___
     *   |1  |
     *  _|___|_
     * | |   | |
     * | |___| |
     * |     2 |
     * |_______|
     *
     * </pre>
     */
    @Test
    public void intersects_bottomSideWithin_true() {
        Rectangle one = new Rectangle(1, 0, 2, 4);
        Rectangle two = new Rectangle(0, 2, 4, 4);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }

    /**
     * <pre>
     *  _______
     * |2      |
     * |  ___  |
     * | |   | |
     * |_|___|_|
     *   |1  |
     *   |___|
     * </pre>
     */
    @Test
    public void intersects_topSideWithin_true() {
        Rectangle one = new Rectangle(1, 2, 2, 4);
        Rectangle two = new Rectangle(0, 0, 4, 4);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }


    /**
     * <pre>
     *  _______
     * |2 ___  |
     * | |1  | |
     * | |___| |
     * |_______|
     *
     * </pre>
     */
    @Test
    public void intersects_completelyWithin_true() {
        Rectangle one = new Rectangle(1, 2, 2, 4);
        Rectangle two = new Rectangle(0, 0, 4, 4);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }

    /**
     * <pre>
     *  ____ ____
     * |1   |2   |
     * |    |    |
     * |____|____|
     *
     * </pre>
     */
    @Test
    public void intersects_adjacentTouchingHorizontally_true() {
        Rectangle one = new Rectangle(0, 0, 2, 2);
        Rectangle two = new Rectangle(2, 0, 2, 2);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }

    /**
     * <pre>
     *  ____
     * |1   |
     * |    |
     * |____|
     * |2   |
     * |    |
     * |____|
     * </pre>
     */
    @Test
    public void intersects_adjacentTouchingVertically_true() {
        Rectangle one = new Rectangle(0, 0, 2, 2);
        Rectangle two = new Rectangle(0, 2, 2, 2);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }

    /**
     * <pre>
     *  ____
     * |1   |
     * |    |
     * |____|____
     *      |2   |
     *      |    |
     *      |____|
     * </pre>
     */
    @Test
    public void intersects_touchingDiagonalDownLeft_true() {
        Rectangle one = new Rectangle(0, 0, 2, 2);
        Rectangle two = new Rectangle(2, 2, 2, 2);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }

    /**
     * <pre>
     *       ____
     *      |1   |
     *      |    |
     *  ____|____|
     * |2   |
     * |    |
     * |____|
     * </pre>
     */
    @Test
    public void intersects_touchingDiagonalUpRight_true() {
        Rectangle one = new Rectangle(2, 0, 2, 2);
        Rectangle two = new Rectangle(0, 2, 2, 2);

        assertThat(one.intersects(two)).isEqualTo(true);
        assertThat(two.intersects(one)).isEqualTo(true);
    }

    /**
     * <pre>
     *  ___
     * |1  |
     * |___|
     *        ___
     *       |2  |
     *       |___|
     *
     * </pre>
     */
    @Test
    public void intersects_diagonalDownToTheLeft_false() {
        Rectangle one = new Rectangle(0, 0, 2, 2);
        Rectangle two = new Rectangle(4, 4, 2, 2);

        assertThat(one.intersects(two)).isEqualTo(false);
        assertThat(two.intersects(one)).isEqualTo(false);

    }

    /**
     * <pre>
     *       ___
     *      |2  |
     *      |___|
     *  ___
     * |1  |
     * |___|
     *
     * </pre>
     */
    @Test
    public void intersects_diagonalUpToTheRight_false() {
        Rectangle one = new Rectangle(0, 4, 2, 2);
        Rectangle two = new Rectangle(4, 0, 2, 2);

        assertThat(one.intersects(two)).isEqualTo(false);
        assertThat(two.intersects(one)).isEqualTo(false);

    }

    /**
     * <pre>
     *  ___    ___
     * |1  |  |2  |
     * |___|  |___|
     *
     * </pre>
     */
    @Test
    public void intersects_adjacentNotTouchingHorizontally_false() {

        Rectangle one = new Rectangle(0, 0, 2, 2);
        Rectangle two = new Rectangle(3, 0, 2, 2);

        assertThat(one.intersects(two)).isEqualTo(false);
        assertThat(two.intersects(one)).isEqualTo(false);
    }

    /**
     * <pre>
     *  ___
     * |1  |
     * |___|
     *  ___
     * |2  |
     * |___|
     *
     * </pre>
     */
    @Test
    public void intersects_adjacentNotTouchingVertically_false() {

        Rectangle one = new Rectangle(0, 0, 2, 2);
        Rectangle two = new Rectangle(0, 3, 2, 2);

        assertThat(one.intersects(two)).isEqualTo(false);
        assertThat(two.intersects(one)).isEqualTo(false);
    }
}