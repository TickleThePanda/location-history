package uk.co.ticklethepanda.location.history.application.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.co.ticklethepanda.location.history.cartograph.heatmap.*;
import uk.co.ticklethepanda.location.history.cartograph.model.PointData;
import uk.co.ticklethepanda.location.history.cartograph.projection.LongLat;
import uk.co.ticklethepanda.location.history.cartograph.projections.SphericalPsuedoMercatorProjector;
import uk.co.ticklethepanda.location.history.loader.geodetic.google.GoogleLocationGeodeticDataLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public class LocationHistoryLambda implements RequestHandler<S3Event, Void> {

    private static final String CONFIGURATION_BUCKET = "location-history";
    private static final String RESULTS_BUCKET = "ticklethepanda-assets";

    @Override
    public Void handleRequest(S3Event s3Event, Context context) {

        AmazonS3Client client = new AmazonS3Client();

        S3Object configObject = client.getObject(CONFIGURATION_BUCKET, "config.json");
        S3Object historyObject = client.getObject(CONFIGURATION_BUCKET, "history.json");

        context.getLogger().log("INFO: got history and config objects");

        InputStreamReader reader = new InputStreamReader(historyObject.getObjectContent());

        HeatmapLambdaConfiguration configuration = createConfigFromObject(configObject);

        HeatmapImagePainter painter = createPainterFromConfig(configuration);
        HeatmapProjector<LocalDate> projector = createProjectorFromObject(historyObject);
        Map<String, Predicate<LocalDate>> filters = getFilters(configuration);

        context.getLogger().log("INFO: finished loading and setup");

        for (HeatmapConfiguration config : configuration.getHeatmaps()) {
            for(Map.Entry<String, Predicate<LocalDate>> filter : filters.entrySet()) {

                String imageKey = "location-history/" + config.getName() + "-" + filter.getKey() + ".png";

                context.getLogger().log("INFO: generating heatmap - " + imageKey);

                HeatmapDescriptor<LocalDate> descriptor = config.with(filter.getValue());

                Heatmap<LocalDate> heatmap = projector.project(descriptor);

                context.getLogger().log("INFO: generated heatmap, painting image - " + imageKey);

                BufferedImage image = painter.paintHeatmap(heatmap, 4);

                context.getLogger().log("INFO: generated image, writing to stream - " + imageKey);

                InputStream stream = getImageAsStream(image);

                context.getLogger().log("INFO: about to write image - " + imageKey);

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType("image/png");
                metadata.setCacheControl("max-age: 86400");

                PutObjectRequest request = new PutObjectRequest(RESULTS_BUCKET, imageKey, stream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);

                client.putObject(request);

            }
        }

        return null;
    }

    private HeatmapLambdaConfiguration createConfigFromObject(S3Object object) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(new InputStreamReader(object.getObjectContent()), HeatmapLambdaConfiguration.class);
    }

    private HeatmapProjector<LocalDate> createProjectorFromObject(S3Object object) {

        Reader reader = new InputStreamReader(object.getObjectContent());

        GoogleLocationGeodeticDataLoader loader = new GoogleLocationGeodeticDataLoader(reader, -1);

        List<PointData<LongLat, LocalDate>> points = loader.load();

        return HeatmapProjector.createProjection(
                new SphericalPsuedoMercatorProjector(), points
        );
    }

    private HeatmapImagePainter createPainterFromConfig(HeatmapLambdaConfiguration config) {
        String colorHexString = config.getHeatmapColorHex();
        int colorHex = Integer.parseInt(colorHexString, 16);
        return new HeatmapImagePainter(new HeatmapColourPicker.Monotone(new Color(colorHex)));
    }

    private Map<String, Predicate<LocalDate>> getFilters(HeatmapLambdaConfiguration config) {
        Map<String, Predicate<LocalDate>> filters = new HashMap<>();

        filters.put("all", ignored -> true);

        for (DayOfWeek dow : DayOfWeek.values()) {
            filters.put(dow.toString().toLowerCase(), date -> date.getDayOfWeek().equals(dow));
        }

        for (Month month : Month.values()) {
            filters.put(month.toString().toLowerCase(), date -> date.getMonth().equals(month));
        }

        YearMonthRange yearMonths = YearMonthRange.between(
                YearMonth.parse(config.getFirstMonth()),
                YearMonth.from(LocalDate.now())
        );

        for (YearMonth yearMonth: yearMonths) {
            filters.put(yearMonth.toString().toLowerCase(), date -> YearMonth.from(date).equals(yearMonth));
        }

        return filters;
    }

    private InputStream getImageAsStream(BufferedImage image) {
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(5000);

            ImageIO.write(image, "png", outputStream);

            byte[] bytes = outputStream.toByteArray();

            return new ByteArrayInputStream(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write images.", e);
        }
    }
}
