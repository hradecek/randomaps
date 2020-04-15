package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;

import com.google.maps.GeoApiContext;
import io.reactivex.Single;

/**
 * Simple service for Places API.
 */
public class PlacesApiService extends MapApi {

    /**
     * Constructor
     *
     * @param context GeoApiContext
     */
    public PlacesApiService(GeoApiContext context) {
        super(context);
    }

    /**
     * Return all nearby places GPS coordination.
     *
     * @param location base GPS location
     * @return All found nearby places GPS coordination
     */
    private static final LatLng MOCKED_NEARBY_1 = new LatLng(66.50394779999999, 25.7293906);
    private static final LatLng MOCKED_NEARBY_2 = new LatLng(66.7754906, 66.7754906);

    int i = 0;
    public Single<LatLng> nearbyPlace(LatLng location) {
//        return Observable.create(emitter -> PlacesApi.nearbySearchQuery(context, toGLatLng(location)).radius(50_000).setCallback(
//                new PendingResult.Callback<>() {
//                    @Override
//                    public void onResult(PlacesSearchResponse response) {
//                        // TODO: Use nextPageToken for complete list of results
//                        Arrays.stream(response.results).forEach(place -> emitter.onNext(fromGLatLng(place.geometry.location)));
//                        emitter.onComplete();
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                        // TODO don't throw away throwable
//                        final String errorMessage = "No places has been found nearby " + location;
//                        emitter.onError(new PlacesApiException(errorMessage));
//                    }
//                })
//        );
        return Single.create(emitter -> {
            if (i == 0) {
                emitter.onSuccess(MOCKED_NEARBY_1);
                ++i;
            } else {
                emitter.onSuccess(MOCKED_NEARBY_2);
            }
        });
    }
}
