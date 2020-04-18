package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.reactivex.SingleHelper;

import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;

import java.io.IOException;

public class GoogleMapsServiceImpl implements MapsService {

    /**
     * Google maps APIs.
     */
    private DirectionsApiService directionsService;
    private PlacesApiService placesService;
    private StaticMapApiService staticMapApiService;

    /**
     * Constructor
     *
     * @param geoApiContext Google GEO API context
     */
    public GoogleMapsServiceImpl(final GeoApiContext geoApiContext) {
        this.directionsService = new DirectionsApiService(geoApiContext);
        this.placesService = new PlacesApiService(geoApiContext);
        this.staticMapApiService = new StaticMapApiService(geoApiContext);
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
    public MapsService isWater(LatLng location, Handler<AsyncResult<Boolean>> resultHandler) {
        try {
            resultHandler.handle(Future.succeededFuture(staticMapApiService.isWater(location)));
        } catch (InterruptedException | ApiException | IOException ex) {
            resultHandler.handle(Future.failedFuture(ex));
        }
        return this;
    }
}
