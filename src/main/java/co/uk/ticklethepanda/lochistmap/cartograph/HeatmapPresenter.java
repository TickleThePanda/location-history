package co.uk.ticklethepanda.lochistmap.cartograph;

import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.Thread.State;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import co.uk.ticklethepanda.lochistmap.cartograph.quadtree.Quadtree;

public class HeatmapPresenter {

	private final class HeatmapScaler extends SwingWorker<BufferedImage, Void> {

		private static final double SCALE_MULTIPLIER = 0.9;
		private Point mousePoint;
		private double wheelRotation;

		public HeatmapScaler(Point point, int rotation) {
			this.mousePoint = point;
			this.wheelRotation = rotation;
		}

		public HeatmapScaler(int rotation) {
			this(new Point(view.getWidth() / 2, view.getHeight() / 2), rotation);
		}

		@Override
		protected BufferedImage doInBackground() throws Exception {

			final double xNormalised = mousePoint.getX()
					/ (double) view.getWidth();
			final double yNormalised = mousePoint.getY()
					/ (double) view.getHeight();

			final double selectedX = heatmapWindow.getWidth() * xNormalised
					+ heatmapWindow.getX();
			final double selectedY = heatmapWindow.getHeight() * yNormalised
					+ heatmapWindow.getY();

			final double newWidth = heatmapWindow.getWidth()
					* Math.pow(SCALE_MULTIPLIER, wheelRotation);
			final double newHeight = heatmapWindow.getHeight()
					* Math.pow(SCALE_MULTIPLIER, wheelRotation);

			final double newX = selectedX - newWidth * xNormalised;
			final double newY = selectedY - newHeight * yNormalised;

			Rectangle2D newHeatmapWindow = new Rectangle2D.Double(newX, newY,
					newWidth, newHeight);

			BufferedImage heatmapImage = painter.paintHeatmap(
					model.convertToHeatmap(view.getWidth() / pixelSize,
							view.getHeight() / pixelSize, newHeatmapWindow),
					pixelSize);
			

			heatmapWindow = newHeatmapWindow;

			return heatmapImage;
		}

		@Override
		protected void done() {
			try {
				view.setHeatmap(get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

	}

	private final class HeatmapTranslator extends SwingWorker<BufferedImage, Void> {

		private Point translation;

		HeatmapTranslator(Point translation) {
			this.translation = translation;
		}

		@Override
		protected BufferedImage doInBackground() throws Exception {

			final double xDiffNormalised = (double) translation.getX()
					/ view.getWidth();
			final double yDiffNormalised = (double) translation.getY()
					/ view.getHeight();

			final double xDiffHeatmap = heatmapWindow.getWidth()
					* xDiffNormalised;
			final double yDiffHeatmap = heatmapWindow.getHeight()
					* yDiffNormalised;

			double xNewHeatmap = heatmapWindow.getX() + xDiffHeatmap;
			double yNewHeatmap = heatmapWindow.getY() + yDiffHeatmap;

			Rectangle2D newWindow = new Rectangle2D.Double(xNewHeatmap,
					yNewHeatmap, heatmapWindow.getWidth(),
					heatmapWindow.getHeight());

			Heatmap heatmap = model.convertToHeatmap(view.getWidth()
					/ pixelSize, view.getHeight() / pixelSize, newWindow);

			BufferedImage image = painter.paintHeatmap(heatmap, pixelSize);

			heatmapWindow = newWindow;
			return image;
		}

		@Override
		protected void done() {
			try {
				view.setHeatmap(get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	private final class HeatmapResizer extends SwingWorker<BufferedImage, Void> {
		@Override
		protected BufferedImage doInBackground() throws Exception {
			double oldRatio = (double) heatmapWindow.getWidth()
					/ (double) heatmapWindow.getHeight();
			double newRatio = (double) view.getWidth()
					/ (double) view.getHeight();

			heatmapWindow = new Rectangle2D.Double(heatmapWindow.getX(),
					heatmapWindow.getY(), heatmapWindow.getWidth(),
					heatmapWindow.getHeight() * oldRatio / newRatio);

			return painter.paintHeatmap(model.convertToHeatmap(view.getWidth()
					/ pixelSize + 1, view.getHeight() / pixelSize + 1,
					heatmapWindow), pixelSize);
		}

		@Override
		protected void done() {
			try {
				view.setHeatmap(get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	
	private static final int DEFAULT_PIXEL_SIZE = 3;

	protected static final int KEYPAD_TRANSLATION_DISTANCE = 20;

	private final HeatmapView view;
	private final Quadtree model;
	private Thread thread;
	private final HeatmapPainter painter = new HeatmapPainter();

	private Rectangle2D heatmapWindow;
	private final int pixelSize;

	private Point mousePoint;

	public HeatmapPresenter(HeatmapView view, Quadtree model, int pixelSize) {
		this.view = view;
		this.model = model;
		this.pixelSize = pixelSize;
		Rectangle2D bounding = model.getBoundingRectangle();

		this.heatmapWindow = new Rectangle2D.Double(bounding.getX(),
				bounding.getY(), bounding.getWidth(), (double) view.getHeight()
						* bounding.getWidth() / (double) view.getWidth());

		thread = new Thread(new HeatmapResizer());
		thread.start();

		view.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				if (!thread.isAlive()) {
					thread = new Thread(new HeatmapResizer());
					thread.start();
				}
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});

		view.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mousePoint = e.getPoint();
			}
		});

		view.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (thread.getState() == State.TERMINATED) {
					thread = new Thread(new HeatmapScaler(e.getPoint(), e
							.getWheelRotation()));
					thread.start();
				}
			}

		});
		view.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (thread.getState() == State.TERMINATED) {
					int transX = (int) mousePoint.getX() - (int) e.getPoint().x;
					int transY = (int) mousePoint.getY()
							- (int) e.getPoint().getY();
					mousePoint = e.getPoint();
					Point translation = new Point(transX, transY);
					thread = new Thread(new HeatmapTranslator(translation));
					thread.start();
				}

			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});

		view.setFocusable(true);
		view.requestFocus();
		view.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

				if (thread.getState() == State.TERMINATED) {
					if (e.getKeyChar() == '+' || e.getKeyChar() == '=') {
						thread = new Thread(new HeatmapScaler(1));
						thread.start();
					} else if (e.getKeyChar() == '-' || e.getKeyChar() == '_') {
						thread = new Thread(new HeatmapScaler(-1));
						thread.start();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (thread.getState() == State.TERMINATED) {
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						thread = new Thread(new HeatmapTranslator(new Point(10,
								0)));
						thread.start();
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						thread = new Thread(new HeatmapTranslator(new Point(
								-10, 0)));
						thread.start();
					}
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						thread = new Thread(new HeatmapTranslator(new Point(0,
								10)));
						thread.start();
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						thread = new Thread(new HeatmapTranslator(new Point(0,
								-10)));
						thread.start();
					}
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}

	public HeatmapPresenter(HeatmapView view, Quadtree model) {
		this(view, model, DEFAULT_PIXEL_SIZE);
	}
}
