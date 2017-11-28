package com.hradecek.maps.google;

import com.google.maps.GeoApiContext;
import com.google.maps.RoadsApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;
import io.reactivex.Single;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * TODO:
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class RoadsApiService {

    private static Logger logger = LoggerFactory.getLogger(RoadsApiService.class);

    private final GeoApiContext context;

    public RoadsApiService(GeoApiContext context) {
        this.context = context;
    }

    public Single<SnappedPoint[]> getNearbyRoads(LatLng point) {
        return Single.create(singleEmmiter -> {
            SnappedPoint[] snappedPoints = RoadsApi.nearestRoads(context, point).await();
            if (snappedPoints.length <= 0) {
                singleEmmiter.onError(new IllegalStateException("No nearby roads have been found"));
            }
            singleEmmiter.onSuccess(snappedPoints);
        });
    }
}
