package co.uk.ticklethepanda.lochistmap;

import co.uk.ticklethepanda.lochistmap.cartograph.ecp.EcpHeatmapFactory;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import co.uk.ticklethepanda.lochistmap.cartograph.Point;
import co.uk.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocation;
import co.uk.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocations;
import net.kroo.elliot.GifSequenceWriter;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GifLineImageDriver {

	private static final int IMAGE_WIDTH = 800;
	private static final int IMAGE_HEIGHT = 1000;

	private static final String FILE_NAME = "panda-loc-hist";
	private static final String OUT_NAME = "panda-line-gif-out.gif";

	private static final Rectangle2D croppedImage = new Rectangle2D.Double(
				-135000,
				-2990000,
				150000,
				150000 / 800f * 1000f);

	public static void main(String[] args) throws JsonSyntaxException,
			JsonIOException, IOException {
		final List<GoogleLocation> locations = GoogleLocations.Loader
				.fromFile(FILE_NAME)
				.filterInaccurate(1000)
				.getLocations();

		final EcpHeatmapFactory map = new EcpHeatmapFactory(locations);

		System.out.println(map.getBoundingRectangle().toString());

		System.out.println("Drawing image...");

		List<Line2D> lines = new ArrayList<>();
		int count = 0;
		int pointsCount = map.getPoints().size();

		ImageOutputStream stream =
				new FileImageOutputStream(new File(OUT_NAME));

		GifSequenceWriter writer = new GifSequenceWriter(stream,
				BufferedImage.TYPE_INT_ARGB,
				300,
				true);

		Point prev = map.getPoints().get(0);

		for (Point mp : map.getPoints()) {

			Line2D newLine = getLine(prev, mp);

			lines.add(newLine);

			if(count % 1000 == 0) {
				writer.writeToSequence(generateImageForLines(lines));
				System.out.println("line " + count + " of " + pointsCount);
			}

			prev = mp;
			count++;
		}

		writer.writeToSequence(generateImageForLines(lines));

		stream.flush();
		writer.close();

	}

	private static BufferedImage generateImageForLines(List<Line2D> lines) {
		BufferedImage bi = new BufferedImage(
        IMAGE_WIDTH,
        IMAGE_HEIGHT,
        BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = bi.createGraphics();

		graphics2D.setColor(Color.BLACK);
		graphics2D.setStroke(new BasicStroke(1f));

		lines.forEach(graphics2D::draw);
		return bi;
	}

	private static Line2D getLine(Point prev, Point mp) {
		double prevX = prev.getX();
		double prevY = prev.getY();

		double nextX = mp.getX();
		double nextY = mp.getY();

		double imageX0 = croppedImage.getX();
		double imageY0 = croppedImage.getY();

		double prevImageX = (prevX - imageX0) / croppedImage.getWidth() * IMAGE_WIDTH;
		double prevImageY = (prevY - imageY0) / croppedImage.getHeight() * IMAGE_HEIGHT;

		double nextImageX = (nextX - imageX0) / croppedImage.getWidth() * IMAGE_WIDTH;
		double nextImageY = (nextY - imageY0) / croppedImage.getHeight() * IMAGE_HEIGHT;

		return new Line2D.Double(
				prevImageX,
				prevImageY,
				nextImageX,
				nextImageY);
	}
}
