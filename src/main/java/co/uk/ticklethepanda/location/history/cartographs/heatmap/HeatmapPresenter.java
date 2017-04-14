package co.uk.ticklethepanda.location.history.cartographs.heatmap;

import co.uk.ticklethepanda.location.history.cartograph.*;
import co.uk.ticklethepanda.location.history.cartographs.SpatialCollectionAnalyser;

import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.Thread.State;

public class HeatmapPresenter<E extends Point> {

    private final class HeatmapScaler implements Runnable {

        private static final double SCALE_MULTIPLIER = 0.9;
        private java.awt.Point mousePoint;
        private double wheelRotation;

        public HeatmapScaler(java.awt.Point point, int rotation) {
            this.mousePoint = point;
            this.wheelRotation = rotation;
        }

        public HeatmapScaler(int rotation) {
            this(new java.awt.Point(view.getWidth() / 2, view.getHeight() / 2), rotation);
        }

        @Override
        public void run() {

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

            Rectangle2D newWindow = new Rectangle2D.Double(newX, newY,
                    newWidth, newHeight);

            updateView(newWindow);
        }

    }

    private final class HeatmapTranslator implements Runnable {

        private java.awt.Point translation;

        HeatmapTranslator(java.awt.Point translation) {
            this.translation = translation;
        }

        @Override
        public void run() {

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

            updateView(newWindow);
        }

    }

    private final class HeatmapResizer implements Runnable {
        @Override
        public void run() {
            double oldRatio = (double) heatmapWindow.getWidth()
                    / (double) heatmapWindow.getHeight();
            double newRatio = (double) view.getWidth()
                    / (double) view.getHeight();

            Rectangle2D newWindow = new Rectangle2D.Double(heatmapWindow.getX(),
                    heatmapWindow.getY(), heatmapWindow.getWidth(),
                    heatmapWindow.getHeight() * oldRatio / newRatio);

            updateView(newWindow);
        }

    }

    private static final int DEFAULT_PIXEL_SIZE = 3;

    private final HeatmapView view;
    private final SpatialCollectionAnalyser<E> cartographToHeatmap;
    private final HeatmapImagePainter painter = new HeatmapImagePainter();

    private Rectangle2D heatmapWindow;

    private Thread thread;

    private final int pixelSize;

    private java.awt.Point mousePoint;

    public HeatmapPresenter(
            HeatmapView view,
            SpatialCollection<E> model) {
        this(view, model, DEFAULT_PIXEL_SIZE);
    }

    public HeatmapPresenter(
            HeatmapView view,
            SpatialCollection<E> model,
            int pixelSize) {
        this.view = view;
        this.cartographToHeatmap = new SpatialCollectionAnalyser<E>(model);
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
                    java.awt.Point translation = new java.awt.Point(transX, transY);
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
                        thread = new Thread(new HeatmapTranslator(new java.awt.Point(10,
                                0)));
                        thread.start();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        thread = new Thread(new HeatmapTranslator(new java.awt.Point(
                                -10, 0)));
                        thread.start();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        thread = new Thread(new HeatmapTranslator(new java.awt.Point(0,
                                10)));
                        thread.start();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        thread = new Thread(new HeatmapTranslator(new java.awt.Point(0,
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

    private void updateView(Rectangle2D newViewport) {
        BufferedImage image = painter.paintHeatmap(cartographToHeatmap.convertToHeatmap(heatmapWindow,
                (view.getWidth() / pixelSize) / heatmapWindow.getWidth()), pixelSize);

        this.heatmapWindow = newViewport;
        view.setHeatmap(image);
    }

}
