package co.uk.ticklethepanda.location.history.application.gui;

import co.uk.ticklethepanda.location.history.cartograph.Point;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapColourPicker;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapImagePainter;
import co.uk.ticklethepanda.location.history.cartograph.heatmap.HeatmapProjector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.lang.Thread.State;

public class HeatmapPresenter<E extends Point> {

    private static final Logger LOG = LogManager.getLogger();

    private final class HeatmapScaler implements Runnable {

        private static final double SCALE_MULTIPLIER = 0.9;
        private Point2D mousePoint;
        private double wheelRotation;

        public HeatmapScaler(Point2D point, int rotation) {
            this.mousePoint = point;
            this.wheelRotation = rotation;
        }

        public HeatmapScaler(int rotation) {
            this(new java.awt.Point(view.getWidth() / 2, view.getHeight() / 2), rotation);
        }

        @Override
        public void run() {

            LOG.trace("mouse wheelRotation: {}", wheelRotation);

            projector.scaleAround(
                    new Point2D.Double(
                            mousePoint.getX(),
                            mousePoint.getY()
                    ),
                    wheelRotation > 0
                            ? wheelRotation / SCALE_MULTIPLIER
                            : SCALE_MULTIPLIER / -wheelRotation
            );

            updateView();
        }

    }

    private final class HeatmapTranslator implements Runnable {

        private Point2D translation;

        HeatmapTranslator(Point2D translation) {
            this.translation = translation;
        }

        @Override
        public void run() {

            projector.translate(translation);

            updateView();
        }

    }

    private final class HeatmapResizer implements Runnable {
        @Override
        public void run() {
            projector.setViewSize(
                    new Point2D.Double(
                            view.getWidth() / pixelSize,
                            view.getHeight() / pixelSize
                    )
            );

            updateView();
        }

    }

    public static final int DEFAULT_PIXEL_SIZE = 3;

    private final HeatmapView view;
    private final HeatmapProjector<E> projector;
    private final HeatmapImagePainter painter = new HeatmapImagePainter(
            new HeatmapColourPicker.Monotone(new Color(0xCCDDAA)));

    private Thread thread;

    private final int pixelSize;

    private java.awt.Point mousePoint;

    public HeatmapPresenter(
            HeatmapView view,
            HeatmapProjector<E> projector) {
        this(view, projector, DEFAULT_PIXEL_SIZE);
    }

    public HeatmapPresenter(
            HeatmapView view,
            HeatmapProjector<E> projector,
            int pixelSize) {
        this.view = view;
        this.projector = projector;
        this.pixelSize = pixelSize;

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
                    java.awt.Point windowPoint = e.getPoint();
                    java.awt.Point heatmapPoint = new java.awt.Point(
                            windowPoint.x / pixelSize,
                            windowPoint.y / pixelSize);

                    thread = new Thread(new HeatmapScaler(heatmapPoint, e
                            .getWheelRotation()));
                    thread.start();
                }
            }

        });
        view.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (thread.getState() == State.TERMINATED) {
                    double transX = (mousePoint.getX() - e.getPoint().x) / (double) pixelSize;
                    double transY = (mousePoint.getY() - e.getPoint().y) / (double) pixelSize;
                    mousePoint = e.getPoint();
                    Point2D translation = new Point2D.Double(transX, transY);
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


    private void updateView() {
        view.setHeatmap(
                painter.paintHeatmap(projector.project(), pixelSize)
        );
    }

}
