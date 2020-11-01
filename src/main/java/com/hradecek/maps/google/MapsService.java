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

/**
 * Provides API containing all maps related functionality.
 */
@ProxyGen
@VertxGen
public interface MapsService {

    @GenIgnore
    static MapsService create(final String googleApiKey) {
        final var geoApiContext = new GeoApiContext.Builder().apiKey(googleApiKey).build();

        return new GoogleMapsServiceImpl(new DirectionsApiService(geoApiContext),
                                         new PlacesApiService(geoApiContext),
                                         new StaticMapApiService(geoApiContext));
    }

    @GenIgnore
    static com.hradecek.maps.google.reactivex.MapsService createProxy(Vertx vertx, String address) {
        return new com.hradecek.maps.google.reactivex.MapsService(new MapsServiceVertxEBProxy(vertx, address));
    }

    /**
     * Get the nearest place for specified {@code location}.
     *
     * @param location location for the nearest place
     * @param resultHandler handler
     * @return {@link MapsService}
     */
    @Fluent
    MapsService nearbyPlace(LatLng location, Handler<AsyncResult<LatLng>> resultHandler);

    /**
     * Generate route from {@code startLocation} to {@code endLocation}.
     *
     * @param startLocation start location
     * @param endLocation end location
     * @param resultHandler handler
     * @return {@link MapsService}
     */
    @Fluent
    MapsService route(LatLng startLocation, LatLng endLocation, Handler<AsyncResult<Route>> resultHandler);

    /**
     * Check whether specified {@code location} is water.
     *
     * @param location location to be checked
     * @param resultHandler handler
     * @return {@link MapsService}
     */
    @Fluent
    MapsService isWater(LatLng location, Handler<AsyncResult<Boolean>> resultHandler);
}
