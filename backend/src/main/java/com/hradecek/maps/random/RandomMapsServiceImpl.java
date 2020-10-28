package com.hradecek.maps.random;

import com.hradecek.maps.google.MapsApiServiceException;
import com.hradecek.maps.google.reactivex.MapsService;
import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;
import com.hradecek.maps.utils.GpsRandom;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

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

    final MapsService mapsService;

    /**
     * Constructor.
     *
     * @param mapsService Maps Service dependency
     */
    public RandomMapsServiceImpl(final MapsService mapsService) {
        this.mapsService = mapsService;
    }

    @Override
    public RandomMapsService route(Handler<AsyncResult<Route>> resultHandler) {
        randomRoute()
                .doOnSuccess(RandomMapsServiceImpl::debugLogSuccessRoute)
                .subscribe(SingleHelper.toObserver(resultHandler));
        return this;
    }

    private static void debugLogSuccessRoute(final Route route) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Route generated: number of points={}", route.decodePath().size());
        }
    }

    private Single<Route> randomRoute() {
        return randomStartLocation()
                .flatMap(mapsService::rxNearbyPlace)
                .flatMap(this::randomRouteTo)
                .retry(MAX_RETRIES, RandomMapsServiceImpl::isRecoverableFailure);
    }

    private Single<LatLng> randomStartLocation() {
        return Single.create(new RandomLocationSubscribe(mapsService));
    }

    private Single<Route> randomRouteTo(final LatLng startLocation) {
        return Single.create(new RandomEndLocationSubscribe(startLocation, mapsService))
                     .flatMap(endLocation -> mapsService.rxRoute(startLocation, endLocation));
    }

    private static boolean isRecoverableFailure(final Throwable failure) {
        return !(failure instanceof MapsApiServiceException);
    }

    private static class RandomLocationSubscribe implements SingleOnSubscribe<LatLng> {

        private final MapsService mapsService;

        public RandomLocationSubscribe(final MapsService mapsService) {
            this.mapsService = mapsService;
        }

        @Override
        public void subscribe(SingleEmitter<LatLng> emitter) {
            var randomLocation = GPS_RANDOM.nextLatLng();
            mapsService.rxIsWater(randomLocation)
                       .subscribe(isWater -> {
                           if (Boolean.TRUE.equals(isWater)) {
                               emitter.onError(new RuntimeException());
                           } else {
                               emitter.onSuccess(randomLocation);
                           }
                       }, emitter::onError);
        }
    }

    private static class RandomEndLocationSubscribe implements SingleOnSubscribe<LatLng> {

        private final LatLng startLocation;
        private final MapsService mapsService;

        public RandomEndLocationSubscribe(LatLng startLocation, MapsService mapsService) {
            this.startLocation = startLocation;
            this.mapsService = mapsService;
        }

        @Override
        public void subscribe(SingleEmitter<LatLng> emitter) {
            mapsService.rxNearbyPlace(GPS_RANDOM.nextLatLng(startLocation, 50_000, 100_000))
                       .subscribe(endLocation -> {
                           if (endLocation.equals(startLocation)) {
                               emitter.onError(createRuntimeException(startLocation));
                           } else {
                               emitter.onSuccess(endLocation);
                           }
                       }, emitter::onError);

        }

        private static RuntimeException createRuntimeException(final LatLng startLocation) {
            return new RuntimeException(String.format("No close end location found for '%s'.", startLocation));
        }
    }
}
