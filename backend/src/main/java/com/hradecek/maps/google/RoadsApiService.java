package com.hradecek.maps.google;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;
import io.reactivex.Single;

/**
 * Simple service for Roads API.
 */
public class RoadsApiService extends MapApi {

    /**
     * Logger
     */
    private static Logger logger = LoggerFactory.getLogger(RoadsApiService.class);

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
//        return Single.create(singleEmmiter -> {
//            RoadsApi.nearestRoads(context, location).setCallback(new PendingResult.Callback<SnappedPoint[]>() {
//                @Override
//                public void onResult(SnappedPoint[] snappedPoints) {
//                    singleEmmiter.onSuccess(snappedPoints);
//                }
//
//                @Override
//                public void onFailure(Throwable throwable) {
//                    singleEmmiter.onError(new IllegalStateException("No roads have been found near by " + location));
//                }
//            });
//        });
        return Single.just(null);
    }
}
