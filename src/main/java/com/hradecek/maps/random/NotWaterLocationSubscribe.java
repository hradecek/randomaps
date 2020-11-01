package com.hradecek.maps.random;

import com.hradecek.maps.google.reactivex.MapsService;
import com.hradecek.maps.types.LatLng;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * TODO: JavaDocs
 */
class NotWaterLocationSubscribe implements SingleOnSubscribe<LatLng> {

    private final LatLng startLocation;
    private final MapsService mapsService;

    public NotWaterLocationSubscribe(final LatLng startLocation, final MapsService mapsService) {
        this.startLocation = startLocation;
        this.mapsService = mapsService;
    }

    @Override
    public void subscribe(SingleEmitter<LatLng> emitter) {
        mapsService.rxIsWater(startLocation)
                   .subscribe(isWater -> {
                       if (Boolean.TRUE.equals(isWater)) {
                           emitter.onError(createRuntimeException(startLocation));
                       } else {
                           emitter.onSuccess(startLocation);
                       }
                   }, emitter::onError);
    }

    private static RuntimeException createRuntimeException(final LatLng startLocation) {
        return new RuntimeException(String.format("Start location '%s' cannot be on water.", startLocation));
    }
}
