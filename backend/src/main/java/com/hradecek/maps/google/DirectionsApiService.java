package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import io.reactivex.Single;

import static com.hradecek.maps.google.Utils.toGLatLng;

/**
 * Simple service for Directions API.
 */
public class DirectionsApiService extends MapApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectionsApiService.class);

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
     * @return route
     */
    public Single<Route> getRoute(LatLng origin, LatLng destination) {
        return Single.create(singleEmitter -> DirectionsApi.getDirections(context,
                toGLatLng(origin).toString(), toGLatLng(destination).toString()).setCallback(
                new PendingResult.Callback<>() {
                    @Override
                    public void onResult(DirectionsResult result) {
                        if (result.routes == null || result.routes.length == 0) {
                            singleEmitter.onError(createException(origin, destination));
                        }
                        singleEmitter.onSuccess(new Route(result.routes[0].overviewPolyline.getEncodedPath()));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        singleEmitter.onError(createException(origin, destination));
                    }
                })
        );
    }

    // TODO don't throw away throwable, it MUST be propagate in thrown exception
    private DirectionsApiException createException(final LatLng origin, final LatLng destination) {
        final var errorMessage = "No route has been found from " + origin + " to " + destination;
        LOGGER.error(errorMessage);

        return new DirectionsApiException(errorMessage);
    }
}
