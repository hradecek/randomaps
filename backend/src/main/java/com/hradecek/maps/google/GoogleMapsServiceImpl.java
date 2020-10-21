package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import java.io.IOException;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.reactivex.SingleHelper;

import com.google.maps.errors.ApiException;

/**
 * Service aggregating Google Maps API calls.
 */
public class GoogleMapsServiceImpl implements MapsService {

    /**
     * Google maps APIs.
     */
    private final DirectionsApiService directionsService;
    private final PlacesApiService placesService;
    private final StaticMapApiService staticMapApiService;

    /**
     * Constructor
     *
     * @param directionsService {@link DirectionsApiService} reference
     * @param placesService {@link PlacesApiService} reference
     * @param staticMapService {@link StaticMapApiService} reference
     */
    public GoogleMapsServiceImpl(final DirectionsApiService directionsService,
                                 final PlacesApiService placesService,
                                 final StaticMapApiService staticMapService) {
        this.directionsService = directionsService;
        this.placesService = placesService;
        this.staticMapApiService = staticMapService;
    }

    @Override
    public MapsService nearbyPlace(LatLng location, Handler<AsyncResult<LatLng>> resultHandler) {
        placesService.nearbyPlace(location)
                     .subscribe(SingleHelper.toObserver(resultHandler));
        return this;
    }

    @Override
    public MapsService route(LatLng startLocation, LatLng endLocation, Handler<AsyncResult<Route>> resultHandler) {
        directionsService.getRoute(startLocation, endLocation)
                         .subscribe(SingleHelper.toObserver(resultHandler));
        return this;
    }

    @Override
    @SuppressWarnings("java:S2142")
    public MapsService isWater(LatLng location, Handler<AsyncResult<Boolean>> resultHandler) {
        try {
            resultHandler.handle(Future.succeededFuture(staticMapApiService.isWater(location)));
        } catch (InterruptedException | ApiException | IOException ex) {
            resultHandler.handle(Future.failedFuture(ex));
        }
        return this;
    }
}
