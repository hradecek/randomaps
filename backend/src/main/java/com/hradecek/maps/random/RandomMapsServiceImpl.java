package com.hradecek.maps.random;

import com.hradecek.maps.google.reactivex.MapsService;
import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;
import com.hradecek.maps.utils.GpsRandom;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.SingleHelper;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Implementation of {@link RandomMapsService} using Google Map APIs.
 */
public class RandomMapsServiceImpl implements RandomMapsService {

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
        randomRoute().subscribe(SingleHelper.toObserver(resultHandler));
        return this;
    }

    private Single<Route> randomRoute() {
        return randomLocation().flatMap(mapsService::rxNearbyPlace).flatMap(this::randomRouteFrom).retry();
    }

    private Single<LatLng> randomLocation() {
        return Single.create(new RandomLocationSubscribe(mapsService)).retry();
    }

    private Single<Route> randomRouteFrom(final LatLng startLocation) {
        return Single.create(new RandomEndLocationSubscribe(startLocation, mapsService))
                     .retry()
                     .flatMap(endLocation -> mapsService.rxRoute(startLocation, endLocation));
    }

    private static class RandomLocationSubscribe implements SingleOnSubscribe<LatLng> {

        private final MapsService mapsService;

        public RandomLocationSubscribe(final MapsService mapsService) {
            this.mapsService = mapsService;
        }

        @Override
        public void subscribe(SingleEmitter<LatLng> emitter) throws Exception {
            var randomLocation = GPS_RANDOM.nextLatLng();
            mapsService.rxIsWater(randomLocation)
                       .subscribe(isWater -> {
                           if (isWater) {
                               emitter.onError(new RuntimeException());
                           } else {
                               emitter.onSuccess(randomLocation);
                           }
                       });
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
        public void subscribe(SingleEmitter<LatLng> emitter) throws Exception {
            mapsService.rxNearbyPlace(GPS_RANDOM.nextLatLng(startLocation, 50_000, 100_000))
                       .subscribe(endLocation -> {
                           if (endLocation.equals(startLocation)) {
                               emitter.onError(new RuntimeException(String.format("No close end location found for '%s'.", startLocation)));
                           } else {
                               emitter.onSuccess(endLocation);
                           }
                       }, emitter::onError);

        }
    }
}
