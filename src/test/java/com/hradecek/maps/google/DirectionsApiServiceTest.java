package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import io.reactivex.observers.TestObserver;

import com.google.maps.GeoApiContext;
import com.google.maps.MockDirectionsApiResponse;
import com.google.maps.TestUtils;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link DirectionsApiService}.
 */
public class DirectionsApiServiceTest extends ApiServiceUnitTest {

    // Sample data
    private static final String ROUTE_RESPONSE = TestUtils.retrieveBody("DirectionsApiResponse.json");
    private static final String NO_ROUTE_RESPONSE = TestUtils.retrieveBody("DirectionsApiNoRouteResponse.json");
    private static final LatLng LOCATION_1 = new LatLng(1.0D, 1.0D);
    private static final LatLng LOCATION_2 = new LatLng(2.0D, 2.0D);

    @Test
    public void getRouteFound() {
        runWithMockedServer(new MockDirectionsApiResponse(ROUTE_RESPONSE), context ->
                getTestRoute(context)
                        .assertSubscribed()
                        .assertNoErrors()
                        .assertValue(DirectionsApiServiceTest::assertRoute));
    }

    @Test
    public void getRouteServerFailure() {
        runWithMockedServer(new MockDirectionsApiResponse(500), context ->
                getTestRoute(context)
                        .assertError(DirectionsApiException.class)
                        .assertError(error -> error.getCause() != null));
    }

    @Test
    public void getRouteNoRouteCanBeFound() {
        runWithMockedServer(new MockDirectionsApiResponse(NO_ROUTE_RESPONSE), context ->
                getTestRoute(context)
                        .assertError(RouteNotFoundException.class)
                        .assertError(error -> error.getCause() == null));
    }

    // Helper assertions & methods
    private static boolean assertRoute(final Route actualRoute) {
        return actualRoute != null;
    }

    private static TestObserver<Route> getTestRoute(GeoApiContext context) {
        try {
            return new DirectionsApiService(context).getRoute(LOCATION_1, LOCATION_2).test().await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Cannot await for API service.");
        }
    }
}
