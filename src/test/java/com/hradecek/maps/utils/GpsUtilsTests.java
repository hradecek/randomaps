package com.hradecek.maps.utils;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests for {@link GpsUtils}.
 */
public class GpsUtilsTests {

    @ParameterizedTest
    @MethodSource("validCoordinates")
    public void isValidCoordinateTrue(double coordinate) {
        final boolean isValid = GpsUtils.isValidCoordinate(coordinate);
        assertThat(isValid, is(true));
    }

    private static Stream<Arguments> validCoordinates() {
        return Stream.of(
                Arguments.of(180.0),
                Arguments.of(-180.0),
                Arguments.of(-0.0),
                Arguments.of(0.0),
                Arguments.of(92.22222),
                Arguments.of(-123.0),
                Arguments.of(-179.999999),
                Arguments.of(179.999999)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidCoordinates")
    public void isValidCoordinatesFalseInvalidLng(double coordinate) {
        final boolean isValid = GpsUtils.isValidCoordinate(coordinate);
        assertThat(isValid, is(false));
    }

    private static Stream<Arguments> invalidCoordinates() {
        return Stream.of(
                Arguments.of(180.00000001),
                Arguments.of(-180.00000001),
                Arguments.of(-181),
                Arguments.of(181),
                Arguments.of(2000000.132),
                Arguments.of(666)
        );
    }
}

