package co.uk.ticklethepanda.utility.images.imagewriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {
    public static void writeImageOut(BufferedImage bi, String imageName) {
        File outputFile = new File("output/" + imageName + ".png");

        try {
            ImageIO.write(bi, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
