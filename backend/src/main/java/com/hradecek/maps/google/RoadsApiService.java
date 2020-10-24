package com.hradecek.maps.google;

import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult.Callback;
import com.google.maps.RoadsApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;

import io.reactivex.Single;

/**
 * Simple service for Roads API.
 */
public class RoadsApiService extends MapApi {

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
