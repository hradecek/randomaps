package com.hradecek.maps.google;

import io.reactivex.Single;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult.Callback;
import com.google.maps.RoadsApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;

/**
 * Simple service for Roads API.
 */
public class RoadsApiService extends MapApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoadsApiService.class);

    /**
     * Constructor
     *
     * @param context GeoApiContext
     */
    public RoadsApiService(GeoApiContext context) {
        super(context);
    }

    /**
     *
     * @param location
     * @return
     */
    public Single<SnappedPoint[]> getNearbyRoads(LatLng location) {
        return Single.create(emitter -> RoadsApi.nearestRoads(context, location).setCallback(new Callback<>() {
                @Override
                public void onResult(SnappedPoint[] snappedPoints) {
                    emitter.onSuccess(snappedPoints);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    emitter.onError(new IllegalStateException("No roads have been found near by " + location));
                }
            }));
    }
}
