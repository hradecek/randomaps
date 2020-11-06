package com.hradecek.maps.utils;

import java.util.stream.Stream;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests for utility class {@link NumberUtils}.
 */
public class NumberUtilsTest extends UtilsClassUnitTest<NumberUtils> {

    @Override
    protected Class<NumberUtils> getUtilityClass() {
        return NumberUtils.class;
    }

    @ParameterizedTest
    @MethodSource("validDoubleStrings")
    public void stringToDoubleForValidString(final String actualString, double expectedDouble) {
        final var converted = NumberUtils.stringToDouble(actualString);

        assertThat(converted.isPresent(), is(true));
        assertThat(converted.getAsDouble(), closeTo(expectedDouble, 0.001));
    }

    private static Stream<Arguments> validDoubleStrings() {
        return Stream.of(
                Arguments.of("1.0", 1.0d),
                Arguments.of("-0.0", 0.0d),
                Arguments.of("0.0", 0.0d),
                Arguments.of(".001", .001d),
                Arguments.of(".000000", .0d),
                Arguments.of("11.1111111111", 11.1111111111d),
                Arguments.of("-138.831", -138.831d),
                Arguments.of("10.32d", 10.32d),
                Arguments.of("10232.9872f", 10232.9872d),
                Arguments.of("255", 255.0d)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDoubleStrings")
    public void stringToDoubleForInvalidString(final String actualString) {
        final var converted = NumberUtils.stringToDouble(actualString);

        assertThat(converted.isEmpty(), is(true));
    }

    private static Stream<Arguments> invalidDoubleStrings() {
        return Stream.of(
                Arguments.of("0xA"),
                Arguments.of("NotADouble"),
                Arguments.of("."),
                Arguments.of("1a1.1"),
                Arguments.of("1,2"),
                Arguments.of("-00x.2"),
                Arguments.of("0x10P")
        );
    }

    @Test
    public void stringToDoubleValidStringTruthPredicate() {
        final var converted = NumberUtils.stringToDouble("1.0", d -> d >= 0);

        assertThat(converted.isPresent(), is(true));
        assertThat(converted.getAsDouble(), closeTo(1.0, 0.001));
    }

    @Test
    public void stringToDoubleInvalidStringWithAnyPredicate() {
        final var converted = NumberUtils.stringToDouble("NotADouble", d -> d >= 0);

        assertThat(converted.isEmpty(), is(true));
    }

    @Test
    public void stringToDoubleValidStringFalsePredicate() {
        final var converted = NumberUtils.stringToDouble("1.0", d -> d < 0);

        assertThat(converted.isEmpty(), is(true));
    }

    @ParameterizedTest
    @MethodSource("closedRangesTrue")
    public void isInRangeClosedBothTrue(int number, int low, int high) {
        var isInRange = NumberUtils.isInRangeClosedBoth(number, low, high);

        assertThat(isInRange, is(true));
    }

    private static Stream<Arguments> closedRangesTrue() {
        return Stream.of(
                Arguments.of(7, 2, 10),
                Arguments.of(5, 10, 2),
                Arguments.of(-5, -10, -2),
                Arguments.of(-5, -2, -10),
                Arguments.of(0, -1, 1),
                Arguments.of(1, 0, 2),
                Arguments.of(1, 1, 1),
                Arguments.of(0, -1, 1),
                Arguments.of(-1, -1, -1),
                Arguments.of(0, 0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("closedRangesFalse")
    public void isInRangeClosedBothFalse(int number, int low, int high) {
        var isInRange = NumberUtils.isInRangeClosedBoth(number, low, high);

        assertThat(isInRange, is(false));
    }

    private static Stream<Arguments> closedRangesFalse() {
        return Stream.of(
                Arguments.of(0, 1, 2),
                Arguments.of(0, 2, 1),
                Arguments.of(0, -1, -2),
                Arguments.of(1, 20, 20),
                Arguments.of(1, 20, 30),
                Arguments.of(-10, -9, 9),
                Arguments.of(-5, -10, -6)
        );
    }
}
