package com.hradecek.maps.random;

import com.hradecek.maps.google.reactivex.MapsService;
import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import java.util.Random;

import io.reactivex.Single;

import io.vertx.core.AsyncResult;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Unit tests for {@link RandomMapsServiceImpl}.
 */
@ExtendWith({MockitoExtension.class, VertxExtension.class})
public class RandomMapsServiceImplTest {

    private static final Random RANDOM = new Random();

    @Mock
    private MapsService mapsService;

    // Tested instance
    private static final int MAX_TRIES = 5;
    private RandomMapsService randomMapsService;

    @BeforeEach
    public void beforeEach() {
        randomMapsService = new RandomMapsServiceImpl(mapsService);
    }

    @Test
    public void routeAllButLastRouteWereFound(VertxTestContext context) {
        int routeNotFoundCount = MAX_TRIES - 1;
        when(mapsService.rxIsWater(any(LatLng.class))).thenReturn(Single.just(false));
        when(mapsService.rxNearbyPlace(any(LatLng.class))).thenAnswer(new RandomNearbyPlaceAnswer());
        when(mapsService.rxRoute(any(LatLng.class), any(LatLng.class))).thenAnswer(new RouteNotFoundAnswer(routeNotFoundCount));

        randomMapsService.route(result -> context.verify(() -> {
            assertSuccess(result);
            verify(mapsService, times(MAX_TRIES)).rxIsWater(any(LatLng.class));
            verify(mapsService, times(2*MAX_TRIES)).rxNearbyPlace(any(LatLng.class));
            verify(mapsService, times(MAX_TRIES)).rxRoute(any(LatLng.class), any(LatLng.class));
            context.completeNow();
        }));
    }

    private static class RouteNotFoundAnswer implements Answer<Single<Route>> {

        private final int notFoundRoundsCount;
        private int callCount = 0;

        private RouteNotFoundAnswer(int notFoundRoundsCount) {
            this.notFoundRoundsCount = notFoundRoundsCount;
        }

        @Override
        public Single<Route> answer(InvocationOnMock invocationOnMock) {
            return ++callCount <= notFoundRoundsCount
                    ? Single.error(new RuntimeException())
                    : Single.just(new Route(String.valueOf(RANDOM.nextDouble())));
        }
    }

    @Test
    public void routeAllButLastNearbyPlaceWereFound(VertxTestContext context) {
        when(mapsService.rxIsWater(any(LatLng.class))).thenReturn(Single.just(false));
        when(mapsService.rxNearbyPlace(any(LatLng.class))).thenAnswer(new NearbyPlaceNotFoundAnswer(MAX_TRIES - 1));
        when(mapsService.rxRoute(any(LatLng.class), any(LatLng.class))).thenReturn(Single.just(new Route("route")));

        randomMapsService.route(result -> context.verify(() -> {
            assertSuccess(result);
            verify(mapsService, times(MAX_TRIES)).rxIsWater(any(LatLng.class));
            verify(mapsService, times(MAX_TRIES + 1)).rxNearbyPlace(any(LatLng.class)); // extra one for next call in successful rxRoute
            verify(mapsService, times(1)).rxRoute(any(LatLng.class), any(LatLng.class));
            context.completeNow();
        }));
    }

    private static class NearbyPlaceNotFoundAnswer implements Answer<Single<LatLng>> {

        private final int notFoundPlacesCount;
        private int callCount = 0;

        public NearbyPlaceNotFoundAnswer(int notFoundPlacesCount) {
            this.notFoundPlacesCount = notFoundPlacesCount;
        }

        @Override
        public Single<LatLng> answer(InvocationOnMock invocationOnMock) {
            return ++callCount <= notFoundPlacesCount
                ? Single.error(new RuntimeException())
                : Single.just(new LatLng(RANDOM.nextDouble(), RANDOM.nextDouble()));
        }

    }

    @Test
    public void routeAllButLastTryIsWater(VertxTestContext context) {
        when(mapsService.rxIsWater(any(LatLng.class))).thenAnswer(new IsWaterAnswer(MAX_TRIES - 1));
        when(mapsService.rxNearbyPlace(any(LatLng.class))).thenAnswer(new RandomNearbyPlaceAnswer());
        when(mapsService.rxRoute(any(LatLng.class), any(LatLng.class))).thenReturn(Single.just(new Route("route")));

        randomMapsService.route(result -> context.verify(() -> {
            assertSuccess(result);
            verify(mapsService, times(MAX_TRIES)).rxIsWater(any(LatLng.class));
            verify(mapsService, times(2)).rxNearbyPlace(any(LatLng.class));
            verify(mapsService, times(1)).rxRoute(any(LatLng.class), any(LatLng.class));
            context.completeNow();
        }));
    }

    private static class IsWaterAnswer implements Answer<Single<Boolean>> {

        private final int isWaterCount;
        private int callCount = 0;

        public IsWaterAnswer(int isWaterCount) {
            this.isWaterCount = isWaterCount;
        }

        @Override
        public Single<Boolean> answer(InvocationOnMock invocationOnMock) {
            return Single.just(++callCount <= isWaterCount);
        }
    }

    @Test
    public void routeOnlyWaterIsGenerated(VertxTestContext context) {
        when(mapsService.rxIsWater(any(LatLng.class))).thenReturn(Single.just(true));

        randomMapsService.route(result -> context.verify(() -> {
            assertFailure(result);
            verify(mapsService, times(MAX_TRIES)).rxIsWater(any(LatLng.class));
            context.completeNow();
        }));
    }

    @Test
    public void routeRouteApiFailure(VertxTestContext context) {
        final String expectedErrorMessage = "ExpectedErrorMessage";
        when(mapsService.rxIsWater(any(LatLng.class))).thenReturn(Single.just(false));
        when(mapsService.rxNearbyPlace(any(LatLng.class))).thenAnswer(new RandomNearbyPlaceAnswer());
        when(mapsService.rxRoute(any(LatLng.class), any(LatLng.class))).thenReturn(Single.error(new RuntimeException(expectedErrorMessage)));

        randomMapsService.route(result -> context.verify(() -> {
            assertFailure(result, expectedErrorMessage);
            verify(mapsService, times(MAX_TRIES)).rxIsWater(any(LatLng.class));
            verify(mapsService, times(2*MAX_TRIES)).rxNearbyPlace(any(LatLng.class));
            verify(mapsService, times(MAX_TRIES)).rxRoute(any(LatLng.class), any(LatLng.class));
            context.completeNow();
        }));
    }

    private static class RandomNearbyPlaceAnswer implements Answer<Single<LatLng>> {

        @Override
        public Single<LatLng> answer(InvocationOnMock invocationOnMock) {
            return Single.just(new LatLng(RANDOM.nextDouble(), RANDOM.nextDouble()));
        }
    }

    @Test
    public void routeNearbyPlaceApiFailure(VertxTestContext context) {
        final String expectedErrorMessage = "ExpectedErrorMessage";
        when(mapsService.rxIsWater(any(LatLng.class))).thenReturn(Single.just(false));
        when(mapsService.rxNearbyPlace(any(LatLng.class))).thenReturn(Single.error(new RuntimeException(expectedErrorMessage)));

        randomMapsService.route(result -> context.verify(() -> {
            assertFailure(result, expectedErrorMessage);
            verify(mapsService, times(MAX_TRIES)).rxIsWater(any(LatLng.class));
            verify(mapsService, times(MAX_TRIES)).rxNearbyPlace(any(LatLng.class));
            context.completeNow();
        }));
    }

    @Test
    public void routeIsWaterApiFailure(VertxTestContext context) {
        final String expectedErrorMessage = "ExpectedErrorMessage";
        when(mapsService.rxIsWater(any(LatLng.class))).thenReturn(Single.error(new Exception(expectedErrorMessage)));

        randomMapsService.route(result -> context.verify(() -> {
            assertFailure(result, expectedErrorMessage);
            verify(mapsService, times(MAX_TRIES)).rxIsWater(any(LatLng.class));
            context.completeNow();
        }));
    }

    // Helper assertions & methods
    private static <T> void assertFailure(AsyncResult<T> result, String expectedErrorMessage) {
        assertFailure(result);
        assertThat(result.cause().getMessage(), equalTo(expectedErrorMessage));
    }

    private static <T> void assertFailure(AsyncResult<T> result) {
        assertThat(result.failed(), is(true));
        assertThat(result.cause(), not(nullValue()));
    }

    private static <T> void assertSuccess(AsyncResult<T> result) {
        assertThat(result.failed(), is(false));
        assertThat(result.result(), not(nullValue()));
    }
}
