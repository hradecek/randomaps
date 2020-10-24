package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;

import io.reactivex.Single;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlacesSearchResponse;

import static com.hradecek.maps.google.Utils.fromGLatLng;
import static com.hradecek.maps.google.Utils.toGLatLng;

/**
 * Simple service for Places API.
 */
public class PlacesApiService extends MapApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlacesApiService.class);

    /**
     * Constructor
     *
     * @param context GeoApiContext
     */
    public PlacesApiService(GeoApiContext context) {
        super(context);
    }

    /**
     * Return all nearby places GPS coordination.
     *
     * @param location base GPS location
     * @return All found nearby places GPS coordination
     */
    public Single<LatLng> nearbyPlace(final LatLng location) {
        return Single.create(emitter -> createRequest(location).setCallback(
                new PendingResult.Callback<>() {
                    @Override
                    public void onResult(PlacesSearchResponse response) {
                        if (response.results == null || response.results.length <= 0) {
                            emitter.onError(createNoRouteException(location));
                        } else {
                            emitter.onSuccess(fromGLatLng(response.results[0].geometry.location));
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        emitter.onError(createFailureException(location, throwable));
                    }
                })
        );
    }

    private NearbySearchRequest createRequest(final LatLng location) {
        return PlacesApi.nearbySearchQuery(context, toGLatLng(location)).radius(50_000);
    }

    private static PlacesApiException createNoRouteException(final LatLng location) {
        final var errorMessage = String.format("No place has been found for location %s.", location);
        LOGGER.warn(errorMessage);

        return new PlacesApiException(errorMessage);
    }

    private static PlacesApiException createFailureException(final LatLng location, final Throwable cause) {
        final var errorMessage = String.format("No place can be found nearby %s, due to: %s",
                                               location, cause.getMessage());
        LOGGER.error(errorMessage, cause);

        return new PlacesApiException(errorMessage, cause);
    }
}
