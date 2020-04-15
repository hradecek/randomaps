package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import com.google.maps.GeoApiContext;

@ProxyGen
@VertxGen
public interface MapsService {

    @GenIgnore
    static MapsService create(final String googleApiKey) {
        return new GoogleMapsServiceImpl(new GeoApiContext.Builder().apiKey(googleApiKey).build());
    }

    @GenIgnore
    static com.hradecek.maps.google.reactivex.MapsService createProxy(Vertx vertx, String address) {
        return new com.hradecek.maps.google.reactivex.MapsService(new MapsServiceVertxEBProxy(vertx, address));
    }

    /**
     *
     * @param location
     * @param resultHandler
     * @return
     */
    @Fluent
    MapsService nearbyPlace(LatLng location, Handler<AsyncResult<LatLng>> resultHandler);

    /**
     *
     * @param startLocation
     * @param endLocation
     * @param resultHandler
     * @return
     */
    @Fluent
    MapsService route(LatLng startLocation, LatLng endLocation, Handler<AsyncResult<Route>> resultHandler);

    /**
     *
     * @param location
     * @param resultHandler
     * @return
     */
    @Fluent
    MapsService isWater(LatLng location, Handler<AsyncResult<Boolean>> resultHandler);
}
