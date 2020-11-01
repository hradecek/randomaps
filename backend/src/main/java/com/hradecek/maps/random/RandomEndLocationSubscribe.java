package com.hradecek.maps.random;

import com.hradecek.maps.google.reactivex.MapsService;
import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.utils.GpsRandom;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * TODO: JavaDocs
 */
class RandomEndLocationSubscribe implements SingleOnSubscribe<LatLng> {

    /**
     * Random GPS generator
     */
    private static final GpsRandom GPS_RANDOM = new GpsRandom();

    private final LatLng startLocation;
    private final double minDistance;
    private final double maxDistance;
    private final MapsService mapsService;

    public RandomEndLocationSubscribe(final LatLng startLocation,
                                      final double minDistance,
                                      final double maxDistance,
                                      final MapsService mapsService) {
        this.startLocation = startLocation;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.mapsService = mapsService;
    }

    @Override
    public void subscribe(SingleEmitter<LatLng> emitter) {
        mapsService.rxNearbyPlace(GPS_RANDOM.nextLatLng(startLocation, minDistance, maxDistance))
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
