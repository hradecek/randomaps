package com.hradecek.maps.random;

import com.hradecek.maps.google.MapsApiServiceException;
import com.hradecek.maps.google.reactivex.MapsService;
import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;
import com.hradecek.maps.types.RouteParams;
import com.hradecek.maps.utils.GpsRandom;

import io.reactivex.Single;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.SingleHelper;

/**
 * Implementation of {@link RandomMapsService} using Google Map APIs.
 */
public class RandomMapsServiceImpl implements RandomMapsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomMapsVerticle.class);

    /**
     * Maximal number of retry attempts in case of any failure.
     */
    public static final int MAX_RETRIES = 4;

    /**
     * Random GPS generator
     */
    private static final GpsRandom GPS_RANDOM = new GpsRandom();

    private final MapsService mapsService;

    /**
     * Constructor.
     *
     * @param mapsService Maps Service dependency
     */
    public RandomMapsServiceImpl(final MapsService mapsService) {
        this.mapsService = mapsService;
    }

    @Override
    public RandomMapsService route(final RouteParams routeParams, Handler<AsyncResult<Route>> resultHandler) {
        randomRouteFrom(createStartLocation(routeParams), routeParams.getMinDistance(), routeParams.getMaxDistance())
                .subscribe(SingleHelper.toObserver(resultHandler));

        return this;
    }

    private Single<LatLng> createStartLocation(final RouteParams routeParams) {
        return routeParams.getStartLocation().map(this::exactStartLocation).orElseGet(this::randomStartLocation);
    }

    private Single<LatLng> exactStartLocation(final LatLng startLocation) {
        return Single.create(new NotWaterLocationSubscribe(startLocation, mapsService))
                     .onErrorResumeNext(error -> Single.error(new IllegalArgumentException(error)));
    }

    private Single<LatLng> randomStartLocation() {
        return Single.create(new NotWaterLocationSubscribe(GPS_RANDOM.nextLatLng(), mapsService));
    }

    private Single<Route> randomRouteFrom(Single<LatLng> startLocation, double minDistance, double maxDistance) {
        return startLocation
                .flatMap(mapsService::rxNearbyPlace)
                .flatMap(location -> randomRouteTo(location, minDistance, maxDistance))
                .retry(MAX_RETRIES, RandomMapsServiceImpl::isRecoverableFailure)
                .doOnSuccess(RandomMapsServiceImpl::debugLogSuccessRoute);

    }

    private Single<Route> randomRouteTo(final LatLng startLocation, double minDistance, double maxDistance) {
        return Single.create(new RandomEndLocationSubscribe(startLocation, minDistance, maxDistance, mapsService))
                .flatMap(endLocation -> mapsService.rxRoute(startLocation, endLocation));
    }

    private static boolean isRecoverableFailure(final Throwable failure) {
        return !(failure instanceof MapsApiServiceException) && !(failure instanceof IllegalArgumentException);
    }

    private static void debugLogSuccessRoute(final Route route) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Route generated: number of points={}", route.decodePath().size());
        }
    }
}
