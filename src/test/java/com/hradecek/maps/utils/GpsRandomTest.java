package com.hradecek.maps.utils;

import com.hradecek.maps.types.LatLng;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link GpsRandom}.
 */
public class GpsRandomTest {

    // Sample data
    private static final LatLng SAMPLE_LOCATION = new LatLng(40.7127837, -74.0059413);

    // Tested instance
    private GpsRandom gpsRandom;

    @BeforeEach
    public void beforeEach() {
        gpsRandom = new GpsRandom();
    }

    @Test
    public void gpsRandomWithSeedCanBeCreated() {
        final var gpsRandomWithSeed = new GpsRandom(10);
        assertThat(gpsRandomWithSeed.nextLatLng().getLng(), not(nullValue()));
    }

    @Test
    public void nextLatLngNotNull() {
        final var location = gpsRandom.nextLatLng();

        assertThat(location, not(nullValue()));
        assertThat(location.getLat(), lessThan(180.0));
        assertThat(location.getLat(), greaterThan(-180.0));
        assertThat(location.getLng(), lessThan(180.0));
        assertThat(location.getLng(), greaterThan(-180.0));
    }

    @Test
    public void nextLatLngMinDistanceBiggerThatMaxDistance() {
        assertThrows(IllegalArgumentException.class, () -> gpsRandom.nextLatLng(SAMPLE_LOCATION, 20.0, 10.0));
    }

    @Test
    public void nextLatLngMinDistanceNegative() {
        assertThrows(IllegalArgumentException.class, () -> gpsRandom.nextLatLng(SAMPLE_LOCATION, -20.0, 10.0));
    }

    @ParameterizedTest
    @MethodSource("distancesSource")
    public void nextLatLngMinMaxDistance(double minDistance, double maxDistance) {
        final var location = gpsRandom.nextLatLng(SAMPLE_LOCATION, minDistance, maxDistance);

        final var actualDistance = getDistance(location, SAMPLE_LOCATION);
        assertThat(actualDistance, lessThanOrEqualTo(maxDistance));
        assertThat(actualDistance, greaterThanOrEqualTo(minDistance));
    }

    private static Stream<Arguments> distancesSource() {
        return Stream.of(
                Arguments.of(0, 5),
                Arguments.of(500, 600),
                Arguments.of(10_000, 10_500),
                Arguments.of(10_000, 11_500),
                Arguments.of(10_000, 20_000),
                Arguments.of(100_000, 200_000),
                Arguments.of(100_000, 100_100),
                Arguments.of(1_000_000, 2_000_000)
        );
    }

    // Helper methods
    private static final double R = 6_378_137.0; // Radius of the Earth in metres

    private static double getDistance(final LatLng point1, final LatLng point2) {
        final var haversine = sin2(latDiff(point1, point2) / 2) + cos(point1.getLatR()) * cos(point2.getLatR()) * sin2(lngDiff(point1, point2) / 2);
        final var c = 2 * atan2(sqrt(haversine), sqrt(1 - haversine));

        return R * c;
    }

    private static double sin2(double num) {
        return Math.pow(Math.sin(num), 2);
    }

    private static double latDiff(final LatLng point1, final LatLng point2) {
        return point1.getLatR() - point2.getLatR();
    }

    private static double lngDiff(final LatLng point1, final LatLng point2) {
        return point1.getLngR() - point2.getLngR();
    }
}
