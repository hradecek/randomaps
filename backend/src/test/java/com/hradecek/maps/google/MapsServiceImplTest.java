package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import java.io.IOException;

import io.reactivex.Single;

import io.vertx.core.AsyncResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GoogleMapsServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class MapsServiceImplTest {

    // Sample data
    private static final LatLng LOCATION_1 = new LatLng(1.0D, 1.0D);
    private static final LatLng LOCATION_2 = new LatLng(2.0D, 2.0D);
    private static final LatLng PLACE_1 = new LatLng(3.0D, 3.0D);
    private static final Route ROUTE_1 = new Route("encodedString");

    // Mock dependencies
    @Mock
    private DirectionsApiService directionsService;
    @Mock
    private PlacesApiService placesService;
    @Mock
    private StaticMapApiService staticMapService;

    // Tested instance
    private MapsService googleMapsService;

    @BeforeEach
    public void beforeEach() {
        googleMapsService = new GoogleMapsServiceImpl(directionsService, placesService, staticMapService);
    }

    @Test
    public void nearbyPlaceSuccess() {
        when(placesService.nearbyPlace(any(LatLng.class))).thenReturn(Single.just(PLACE_1));

        googleMapsService.nearbyPlace(LOCATION_1, result -> {
            assertResultSuccess(result);
            assertLatLng(result, PLACE_1);
        });
    }

    @Test
    public void nearbyPlaceFailure() {
        when(placesService.nearbyPlace(any(LatLng.class))).thenReturn(singleError());

        googleMapsService.nearbyPlace(LOCATION_1, MapsServiceImplTest::assertResultFailure);
    }

    @Test
    public void routeFailure() {
        when(directionsService.getRoute(LOCATION_1, LOCATION_2)).thenReturn(singleError());

        googleMapsService.route(LOCATION_1, LOCATION_2, MapsServiceImplTest::assertResultFailure);
    }

    @Test
    public void routeSuccess() {
        when(directionsService.getRoute(LOCATION_1, LOCATION_2)).thenReturn(Single.just(ROUTE_1));

        googleMapsService.route(LOCATION_1, LOCATION_2, MapsServiceImplTest::assertResultSuccess);
    }

    @Test
    public void isWaterFailure() throws Exception {
        when(staticMapService.isWater(LOCATION_1)).thenThrow(new IOException());

        googleMapsService.isWater(LOCATION_1, MapsServiceImplTest::assertResultFailure);
    }

    @Test
    public void isWaterSuccess() throws Exception {
        when(staticMapService.isWater(LOCATION_1)).thenReturn(true);
        googleMapsService.isWater(LOCATION_1, result -> {
            assertResultSuccess(result);
            assertThat(result.result(), is(true));
        });

        when(staticMapService.isWater(LOCATION_2)).thenReturn(false);
        googleMapsService.isWater(LOCATION_2, result -> {
            assertResultSuccess(result);
            assertThat(result.result(), is(false));
        });
    }

    // Helper methods & assertions
    private static void assertLatLng(AsyncResult<LatLng> result, LatLng expectedLatLng) {
        final LatLng nearbyPlace = result.result();
        assertThat(nearbyPlace.getLat(), is(expectedLatLng.getLat()));
        assertThat(nearbyPlace.getLng(), is(expectedLatLng.getLng()));
    }

    private static <T> void assertResultSuccess(AsyncResult<T> result) {
        assertThat(result.failed(), is(false));
        assertThat(result.cause(), is(nullValue()));
        assertThat(result.result(), not(nullValue()));
    }

    private static <T> void assertResultFailure(AsyncResult<T> result) {
        assertThat(result.failed(), is(true));
        assertThat(result.cause(), not(nullValue()));
        assertThat(result.result(), is(nullValue()));
    }

    private static <T> Single<T> singleError() {
        return Single.error(new RuntimeException("Single Error"));
    }
}
