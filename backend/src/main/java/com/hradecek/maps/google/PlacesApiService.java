package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;

import io.reactivex.Single;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlacesSearchResponse;

import static com.hradecek.maps.google.Utils.fromGLatLng;
import static com.hradecek.maps.google.Utils.toGLatLng;

/**
 * Simple service for Places API.
 */
public class PlacesApiService extends MapApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectionsApiService.class);

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
    public Single<LatLng> nearbyPlace(LatLng location) {
        return Single.create(emitter -> PlacesApi.nearbySearchQuery(context, toGLatLng(location)).radius(50_000).setCallback(
                new PendingResult.Callback<>() {
                    @Override
                    public void onResult(PlacesSearchResponse response) {
                        if (response.results == null || response.results.length <= 0) {
                            emitter.onError(createException(location));
                        } else {
                            emitter.onSuccess(fromGLatLng(response.results[0].geometry.location));
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        emitter.onError(createException(location, throwable));
                    }
                })
        );
    }

    private static PlacesApiException createException(final LatLng location) {
        return createException(location, null);
    }

    private static PlacesApiException createException(final LatLng location, final Throwable throwable) {
        final var errorMessage = String.format("No places has been found nearby '%s'", location);
        LOGGER.error(errorMessage);

        return throwable == null
                ? new PlacesApiException(errorMessage)
                : new PlacesApiException(errorMessage, throwable);
    }
}
