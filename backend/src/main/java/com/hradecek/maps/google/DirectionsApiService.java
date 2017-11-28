package com.hradecek.maps.google;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.EncodedPolyline;
import io.reactivex.Single;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.IOException;

/**
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class DirectionsApiService {

    private static Logger logger = LoggerFactory.getLogger(DirectionsApiService.class);

    private final GeoApiContext context;

    public DirectionsApiService(GeoApiContext context) {
        this.context = context;
    }

    public Single<EncodedPolyline> getRoute(String origin, String destination) {
        return Single.create(singleEmitter -> {
            try {
                DirectionsResult result = DirectionsApi.getDirections(context, origin, destination).await();
                if (result.routes.length <= 0) {
                    singleEmitter.onError(
                            new IllegalStateException("No route has been found from " + origin + " to "+  destination));
                }
                singleEmitter.onSuccess(result.routes[0].overviewPolyline);
            } catch (ApiException | InterruptedException | IOException e) {
                singleEmitter.onError(e);
            }
        });
    }
}
