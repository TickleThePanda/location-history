package co.uk.ticklethepanda.location.history.cartograph.heatmap;

import java.awt.*;

/**
 * Created by panda on 22/04/2017.
 */
public interface HeatmapColourPicker {

    default double normaliseValue(double unscaled, double largestNumber) {
        return 1.0 - Math.log(unscaled) / Math.log(largestNumber);
    }

    default Color pickColor(double unscaled, double largestNumber) {
        return pickColor(normaliseValue(unscaled, largestNumber));
    }

    Color pickColor(double percentage);

    class Greyscale implements HeatmapColourPicker {

        public static final double MAX_BRIGHTNESS = 0.2;

        private final double maximumBrightness;

        public Greyscale() {
            this(MAX_BRIGHTNESS);
        }

        public Greyscale(double maximumBrightness) {
            this.maximumBrightness = maximumBrightness;
        }

        @Override
        public Color pickColor(double percentage) {
            double brightness = (1.0 - maximumBrightness) * percentage;
            Color color = Color.getHSBColor(0f, 0f, (float) brightness);
            return color;
        }
    }

    class Monotone implements HeatmapColourPicker {

        public static final double MIN_BRIGHTNESS = 0.1;
        public static final double MAX_BRIGHTNESS = 0.9;

        private final double minBrightness;
        private final double maxBrightness;

        private final Color color;

        public Monotone(Color color) {
            this(color, MIN_BRIGHTNESS, MAX_BRIGHTNESS);
        }

        public Monotone(Color color, double minBrightness, double maxBrightness) {
            this.color = color;
            this.minBrightness = minBrightness;
            this.maxBrightness = maxBrightness;
        }

        @Override
        public Color pickColor(double percentage) {
            double brightness = (maxBrightness - minBrightness) * percentage + minBrightness;

            float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

            Color color = Color.getHSBColor(hsb[0], hsb[1], (float) brightness);
            return color;
        }
    }

    class Hue implements HeatmapColourPicker {
        public Color pickColor(double percentage) {
            double hue = percentage;
            hue = Math.min(hue, 1f) * 0.8f;
            return Color.getHSBColor(
                    (float) hue, 1f, 1f);
        }
    }

}
