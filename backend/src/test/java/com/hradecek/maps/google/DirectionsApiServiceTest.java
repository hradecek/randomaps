package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import java.io.IOException;
import java.util.function.Consumer;

import io.reactivex.observers.TestObserver;

import com.google.maps.GeoApiContext;
import com.google.maps.MockDirectionsApiResponse;
import com.google.maps.MockGoogleApiServer;
import com.google.maps.MockGoogleMapsApiResponse;
import com.google.maps.TestUtils;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link DirectionsApiService}.
 */
public class DirectionsApiServiceTest {

    // Sample data
    private static final String ROUTE_RESPONSE = TestUtils.retrieveBody("DirectionsApiResponse.json");
    private static final String NO_ROUTE_RESPONSE = TestUtils.retrieveBody("DirectionsApiNoRouteResponse.json");
    private static final LatLng LOCATION_1 = new LatLng(1.0D, 1.0D);
    private static final LatLng LOCATION_2 = new LatLng(2.0D, 2.0D);

    @Test
    public void getRouteFound() throws Exception {
        runWithMockedServer(new MockDirectionsApiResponse(ROUTE_RESPONSE), response ->
                response.assertSubscribed()
                        .assertNoErrors()
                        .assertValue(DirectionsApiServiceTest::assertRoute));
    }

    @Test
    public void getRouteServerFailure() throws Exception {
        runWithMockedServer(new MockDirectionsApiResponse(500), response ->
                response.assertError(DirectionsApiException.class)
                        .assertError(error -> error.getCause() != null));
    }

    @Test
    public void getRouteNoRouteCanBeFound() throws Exception {
        runWithMockedServer(new MockDirectionsApiResponse(NO_ROUTE_RESPONSE), response ->
                response.assertError(DirectionsApiException.class)
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

    private static void runWithMockedServer(final MockGoogleMapsApiResponse mockResponse,
                                            final Consumer<TestObserver<Route>> responseConsumer) {
        try (final var mockServer = new MockGoogleApiServer(mockResponse)) {
            responseConsumer.accept(getTestRoute(mockServer.context()));
        } catch (IOException exception) {
            throw new RuntimeException("Cannot start mocked API server.");
        }
    }
}
