package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;

import io.reactivex.observers.TestObserver;

import com.google.maps.GeoApiContext;
import com.google.maps.MockPlacesApiResponse;
import com.google.maps.TestUtils;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PlacesApiService}.
 */
public class PlacesApiServiceTest extends ApiServiceUnitTest {

    // Sample data
    private static final String NEARBY_PLACE_RESPONSE = TestUtils.retrieveBody("PlacesApiNearbyResponse.json");
    private static final String NEARBY_PLACE_ZERO_RESULTS_RESPONSE = TestUtils.retrieveBody("PlacesApiNearbyZeroResultsResponse.json");
    private static final LatLng LOCATION_1 = new LatLng(1.0D, 1.0D);
    private static final LatLng EXPECTED_LOCATION = new LatLng(-33.8587323, 151.2100055);

    @Test
    public void nearbyPlaceFound() {
        runWithMockedServer(new MockPlacesApiResponse(NEARBY_PLACE_RESPONSE), context ->
                getTestNearbyPlace(context)
                        .assertSubscribed()
                        .assertNoErrors()
                        .assertValue(EXPECTED_LOCATION::equals));
    }

    @Test
    public void nearbyPlaceNotFound() {
        runWithMockedServer(new MockPlacesApiResponse(NEARBY_PLACE_ZERO_RESULTS_RESPONSE), context ->
                getTestNearbyPlace(context)
                        .assertError(NearbyPlaceNotFoundException.class)
                        .assertError(error -> error.getCause() == null));
    }

    @Test
    public void nearbyPlaceServerFailure() {
        runWithMockedServer(new MockPlacesApiResponse(500), context ->
                getTestNearbyPlace(context)
                        .assertError(PlacesApiException.class)
                        .assertError(error -> error.getCause() != null));
    }

    // Helper assertions & methods
    private static TestObserver<LatLng> getTestNearbyPlace(GeoApiContext context) {
        try {
            return new PlacesApiService(context).nearbyPlace(LOCATION_1).test().await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Cannot await for API service.");
        }
    }
}
