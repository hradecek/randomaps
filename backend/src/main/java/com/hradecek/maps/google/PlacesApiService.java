package com.hradecek.maps.google;

import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import io.reactivex.*;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Arrays;

/**
 * Simple service for Places API.
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class PlacesApiService extends MapApi {

    /**
     * Logger
     */
    private static Logger logger = LoggerFactory.getLogger(PlacesApiService.class);

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
    public Observable<LatLng> nearbyPlaces(LatLng location) {
        return Observable.create(emitter -> PlacesApi.nearbySearchQuery(context, location).radius(50_000).setCallback(
                new PendingResult.Callback<PlacesSearchResponse>() {
                    @Override
                    public void onResult(PlacesSearchResponse response) {
                        // TODO: Use nextPageToken for complete list of results
                        Arrays.stream(response.results).forEach(place -> emitter.onNext(place.geometry.location));
                        emitter.onComplete();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        final String errorMessage = "No places has been found nearby " + location;
                        emitter.onError(new PlacesApiException(errorMessage));
                    }
                })
        );
    }
}
