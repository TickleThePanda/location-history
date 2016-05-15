package com.ticklethepanda.lochistmap.imagewriter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
