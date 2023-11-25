package uk.co.ticklethepanda.carto.apps.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uk.co.ticklethepanda.carto.apps.gallery.GalleryBuilder;
import uk.co.ticklethepanda.carto.apps.gallery.GalleryConfig;
import uk.co.ticklethepanda.carto.core.model.PointData;
import uk.co.ticklethepanda.carto.core.projection.LongLat;
import uk.co.ticklethepanda.carto.core.projections.SphericalPsuedoMercatorProjector;
import uk.co.ticklethepanda.carto.loaders.google.GoogleLocationGeodeticDataLoader;

import java.io.*;
import java.time.*;
import java.util.List;
import java.util.zip.*;

public class LocationHistoryLambda implements RequestHandler<S3Event, Void> {

    private static final String CONFIGURATION_BUCKET = "location-history";
    private static final String RESULTS_BUCKET = "ticklethepanda-assets";

    @Override
    public Void handleRequest(S3Event s3Event, Context context) {

        try {
            AmazonS3 client = AmazonS3ClientBuilder.defaultClient();
            S3Object configObject = client.getObject(CONFIGURATION_BUCKET, "config.json");
            S3Object historyObject = client.getObject(CONFIGURATION_BUCKET, "history.json");

            GalleryBuilder builder = new GalleryBuilder(
                () -> this.loadPoints(historyObject),
                () -> this.createConfigFromObject(configObject),
                galleryImage -> {

                    var heatmapName = galleryImage.combination().config().getName();
                    var filterName = galleryImage.combination().name();

                    String imageKey = "location-history/" + heatmapName + "-" + filterName + ".png";

                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentType("image/png");
                    metadata.setCacheControl("max-age: 86400");

                    PutObjectRequest request = new PutObjectRequest(RESULTS_BUCKET, imageKey, galleryImage.stream(), metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);

                    client.putObject(request);
                }
            );

            builder.build(new SphericalPsuedoMercatorProjector());
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            
            context.getLogger().log("ERROR: unable to process location history data: " + e.toString() + '\n' + sw.toString());
        }
        
        return null;
    }

    private GalleryConfig createConfigFromObject(S3Object object) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(new InputStreamReader(object.getObjectContent()), GalleryConfig.class);
    }

    private List<PointData<LongLat, LocalDateTime>> loadPoints(S3Object object) throws IOException {
        String historyEncoding = object.getObjectMetadata().getContentEncoding();

        Reader reader;
        if ("gzip".equals(historyEncoding)) {
          reader = new InputStreamReader(new GZIPInputStream(object.getObjectContent()));
        } else {
          reader = new InputStreamReader(object.getObjectContent());
        }

        GoogleLocationGeodeticDataLoader loader = new GoogleLocationGeodeticDataLoader(reader, -1);

        return loader.load();
    }
}
