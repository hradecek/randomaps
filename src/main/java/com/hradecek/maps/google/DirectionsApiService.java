package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import io.reactivex.Single;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;

import static com.hradecek.maps.google.Utils.toGLatLng;

/**
 * Simple service for Directions API.
 */
public class DirectionsApiService extends MapsApi {

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
     * Get route from start location to end destination form of encoded poly-line.
     * If there's no route error is emitted.
     *
     * @param origin start location
     * @param destination end location
     * @return route
     */
    public Single<Route> getRoute(final LatLng origin, final LatLng destination) {
        return Single.create(singleEmitter -> createRequest(origin, destination).setCallback(
                new PendingResult.Callback<>() {
                    @Override
                    public void onResult(DirectionsResult result) {
                        if (result.routes == null || result.routes.length == 0) {
                            singleEmitter.onError(createNoRouteFoundException(origin, destination));
                        } else {
                            singleEmitter.onSuccess(new Route(result.routes[0].overviewPolyline.getEncodedPath()));
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        singleEmitter.onError(createFailureException(throwable, origin, destination));
                    }
                })
        );
    }

    private DirectionsApiRequest createRequest(final LatLng origin, final LatLng destination) {
        return DirectionsApi.getDirections(context, toGLatLng(origin).toString(), toGLatLng(destination).toString());
    }

    private RouteNotFoundException createNoRouteFoundException(final LatLng origin, final LatLng destination) {
        final var errorMessage = String.format("No route has been found from %s to %s.", origin, destination);
        LOGGER.warn(errorMessage);

        return new RouteNotFoundException(errorMessage);
    }

    private DirectionsApiException createFailureException(final Throwable cause,
                                                          final LatLng origin,
                                                          final LatLng destination) {
        final var errorMessage = String.format("Route from %s to %s cannot be retrieved, due to: %s",
                                               origin, destination, cause.getMessage());
        LOGGER.error(errorMessage, cause);

        return new DirectionsApiException(errorMessage, cause);
    }
}
