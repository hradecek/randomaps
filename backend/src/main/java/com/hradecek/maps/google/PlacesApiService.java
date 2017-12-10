package com.hradecek.maps.google;

import com.google.maps.GeoApiContext;
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
        return Observable.create(emitter -> {
            PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, location).radius(50_000).await();
            if (response.results.length <= 0) {
                final String errorMessage = "No places has been found nearby " + location;
                logger.error(errorMessage);
                emitter.onError(new PlacesApiException(errorMessage));
            }
            /* TODO: Use nextPageToken for complete list of results */
            Arrays.stream(response.results).forEach(place -> emitter.onNext(place.geometry.location));
            emitter.onComplete();
        });
    }
}
