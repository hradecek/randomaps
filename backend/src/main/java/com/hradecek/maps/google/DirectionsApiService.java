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
 * Simple service for Directions API.
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class DirectionsApiService extends MapApi {

    /**
     * Logger
     */
    private static Logger logger = LoggerFactory.getLogger(DirectionsApiService.class);

    /**
     * Constructor
     *
     * @param context GeoApiContext
     */
    public DirectionsApiService(GeoApiContext context) {
        super(context);
    }

    /**
     * Get route from start location to end destination form of encoded polyline.
     * If there's no route error is emitted.
     *
     * @param origin start location
     * @param destination end location
     * @return Encoded polyline
     */
    public Single<EncodedPolyline> getRoute(String origin, String destination) {
        return Single.create(singleEmitter -> {
            try {
                DirectionsResult result = DirectionsApi.getDirections(context, origin, destination).await();
                if (result.routes.length <= 0) {
                    final String errorMessage = "No route has been found from " + origin + " to "+  destination;
                    logger.error(errorMessage);
                    singleEmitter.onError(new DirectionsApiException(errorMessage));
                }
                singleEmitter.onSuccess(result.routes[0].overviewPolyline);
            } catch (ApiException | InterruptedException | IOException e) {
                singleEmitter.onError(new DirectionsApiException(e));
            }
        });
    }
}
