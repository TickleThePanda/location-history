package co.uk.ticklethepanda.location.history.cartograph.heatmap;

import java.awt.*;

/**
 * Created by panda on 22/04/2017.
 */
public interface HeatmapColourPicker {

    default float normaliseValue(float unscaled, float largestNumber) {
        return 1.0f - (float) Math.log(unscaled) / (float) Math.log(largestNumber);
    }

    default Color pickColor(float unscaled, float largestNumber) {
        return pickColor(normaliseValue(unscaled, largestNumber));
    }

    Color pickColor(float percentage);

    class Greyscale implements HeatmapColourPicker {

        public static final float MAX_BRIGHTNESS = 0.2f;

        private final float maximumBrightness;

        public Greyscale() {
            this(MAX_BRIGHTNESS);
        }

        public Greyscale(float maximumBrightness) {
            this.maximumBrightness = maximumBrightness;
        }

        @Override
        public Color pickColor(float percentage) {
            float brightness = (1.0f - maximumBrightness) * percentage;
            Color color = Color.getHSBColor(0f, 0f, brightness);
            return color;
        }
    }

    class Monotone implements HeatmapColourPicker {

        public static final float MIN_BRIGHTNESS = 0.1f;
        public static final float MAX_BRIGHTNESS = 0.9f;

        private final float minBrightness;
        private final float maxBrightness;

        private final Color color;

        public Monotone(Color color) {
            this(color, MIN_BRIGHTNESS, MAX_BRIGHTNESS);
        }

        public Monotone(Color color, float minBrightness, float maxBrightness) {
            this.color = color;
            this.minBrightness = minBrightness;
            this.maxBrightness = maxBrightness;
        }

        @Override
        public Color pickColor(float percentage) {
            float brightness = (maxBrightness - minBrightness) * percentage + minBrightness;

            float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

            Color color = Color.getHSBColor(hsb[0], hsb[1], (float) brightness);
            return color;
        }
    }

    class Hue implements HeatmapColourPicker {
        public Color pickColor(float percentage) {
            float hue = percentage;
            hue = Math.min(hue, 1f) * 0.8f;
            return Color.getHSBColor(
                    (float) hue, 1f, 1f);
        }
    }

}
