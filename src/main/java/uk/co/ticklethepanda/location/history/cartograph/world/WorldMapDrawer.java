package uk.co.ticklethepanda.location.history.cartograph.world;

import uk.co.ticklethepanda.location.history.cartograph.projection.EuclidPoint;
import uk.co.ticklethepanda.location.history.cartograph.projection.Projector;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class WorldMapDrawer {

    private static class WrappedPath {
        private Path2D.Float path = new Path2D.Float();
        private int length = 0;
    }

    private static final Collector<Point2D.Float, WrappedPath, Path2D.Float> COLLECT_TO_PATH = Collector.of(WrappedPath::new,
            (path, p) -> {
                if(path.length == 0) {
                    path.path.moveTo(p.x, p.y);
                } else {
                    path.path.lineTo(p.x, p.y);
                }
                path.length++;
            },
            (p1, p2) -> {
                p1.path.append(p2.path.getPathIterator(null), true);
                return p1;
            },
            (wrapped) -> {
                wrapped.path.closePath();
                return wrapped.path;
            },
            Collector.Characteristics.CONCURRENT);

    private final Projector projector;
    private final List<Path2D> projectedPaths;

    public static WorldMapDrawer createProjector(Projector projector, WorldMap worldMap) {

        List<Path2D> paths = worldMap.getCountryOutlines().stream()
                .map(country -> country.stream()
                        .map(projector::toEuclidPoint)
                        .map(p -> new Point2D.Float(p.getX(), p.getY()))
                        .collect(COLLECT_TO_PATH)
                ).collect(Collectors.toList());

        return new WorldMapDrawer(projector, paths);
    }

    private WorldMapDrawer(Projector projector, List<Path2D> projectedPaths) {
        this.projector = projector;
        this.projectedPaths = projectedPaths;
    }

    public BufferedImage draw(MapDescriptor mapDescriptor, MapTheme theme) {

        int width = mapDescriptor.getDimensions().getWidth();
        int height = mapDescriptor.getDimensions().getHeight();
        BufferedImage image = new BufferedImage(
                width, height,
                BufferedImage.TYPE_INT_ARGB);

        EuclidPoint point = projector.toEuclidPoint(mapDescriptor.getCenter());

        List<Shape> shapes = transformPath(mapDescriptor, point);

        Graphics2D g = getGraphics(image);

        g.setColor(theme.getWaterColor());
        g.fillRect(0, 0, width, height);

        Color fillColor = theme.getFillColor();
        Color outlineColor = theme.getOutlineColor();

        g.setColor(fillColor);
        shapes.forEach(g::fill);

        g.setColor(outlineColor);
        shapes.forEach(g::draw);

        return image;
    }

    private Graphics2D getGraphics(BufferedImage image) {
        Graphics2D g = (Graphics2D) image.getGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return g;
    }

    private List<Shape> transformPath(MapDescriptor mapDescriptor, EuclidPoint point) {
        AffineTransform transform = new AffineTransform(1f / mapDescriptor.getPixelSizeInMapUnits(), 0f, 0f, 1f / mapDescriptor.getPixelSizeInMapUnits(),
                (float) mapDescriptor.getDimensions().getWidth() /2f - point.getX() * 1f / mapDescriptor.getPixelSizeInMapUnits(),
                (float) mapDescriptor.getDimensions().getHeight() /2f - point.getY() * 1f / mapDescriptor.getPixelSizeInMapUnits());

        return projectedPaths.stream()
                .map(p -> p.createTransformedShape(transform))
                .collect(Collectors.toList());
    }
}
