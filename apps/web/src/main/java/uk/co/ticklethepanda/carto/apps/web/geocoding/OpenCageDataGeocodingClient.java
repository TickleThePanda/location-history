package uk.co.ticklethepanda.carto.apps.web.geocoding;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.tags.form.LabelTag;
import uk.co.ticklethepanda.carto.core.projection.LongLat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class OpenCageDataGeocodingClient implements GeocodingClient {
    
    private static final Logger LOG = LogManager.getLogger();

    private static final Gson GSON = new Gson();

    private final String apiKey;

    public OpenCageDataGeocodingClient(String apiKey) {
        this.apiKey = apiKey;
    }
    
    @Override
    public LongLat query(String query) throws GeocodingClientException {
        record OpenCageGeometry(float lat, float lng){};
        record OpenCageResult(OpenCageGeometry geometry){};
        record OpenCageResponse(List<OpenCageResult> results){
            public OpenCageResult getResult() {
                return results.get(0);
            }
        };
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://api.opencagedata.com/geocode/v1/json" +
                                    "?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()) +
                                    "&key=" + apiKey +
                                    "&no_annotations=1" +
                                    "&limit=1"
                    ))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            LOG.info("Result from open cage: " + body);
            
            OpenCageResponse resp = GSON.fromJson(body, OpenCageResponse.class);
            OpenCageGeometry geom = resp.getResult().geometry;

            return new LongLat(geom.lng, geom.lat);

        } catch (InterruptedException | IOException e) {
            throw new GeocodingClientException(e);
        }

    }
}
