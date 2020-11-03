package com.hradecek.maps.http.route;

import java.util.Arrays;
import java.util.Collections;

import io.vertx.ext.web.handler.impl.HttpStatusException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link StartLocationParamValidator}.
 */
public class StartLocationParamValidatorTest {

    // Constants
    private static final int BAD_REQUEST = 400;

    // Tested instance
    private StartLocationParamValidator validator;

    @BeforeEach
    public void beforeEach() {
        validator = new StartLocationParamValidator();
    }

    @Test
    public void zeroProvided() {
        validator.validate(Collections.emptyList());
    }

    @Test
    public void exactlyOneProvidedWithValidLatLng() {
        validator.validate(Collections.singletonList("20.00000,-21.222"));
    }

    @Test
    public void exactlyOneProvidedWithValidLatLngContainingWhitespaces() {
        validator.validate(Collections.singletonList("   20.00000   ,   -21.222   "));
    }

    @Test
    public void exactlyOneNotFloatingPoint() {
        validator.validate(Collections.singletonList("40,20"));
    }

    @Test
    public void exactlyOneWithNonNumericLongitude() {
        final var exception = assertThrows(HttpStatusException.class,
                                           () -> validator.validate(Collections.singletonList("-20.0102,asdf")));
        assertThat(exception.getStatusCode(), equalTo(BAD_REQUEST));
    }

    @Test
    public void exactlyOneWithNonNumericLatitude() {
        final var exception = assertThrows(HttpStatusException.class,
                                           () -> validator.validate(Collections.singletonList("asf,-20.0102")));
        assertThat(exception.getStatusCode(), equalTo(BAD_REQUEST));
    }

    @Test
    public void exactlyOneNotSeparatedByComma() {
        var exception = assertThrows(HttpStatusException.class,
                                     () -> validator.validate(Collections.singletonList("11.111;-20.0102")));
        assertThat(exception.getStatusCode(), equalTo(BAD_REQUEST));

        exception = assertThrows(HttpStatusException.class,
                                 () -> validator.validate(Collections.singletonList("11.111 -20.0102")));
        assertThat(exception.getStatusCode(), equalTo(BAD_REQUEST));
    }

    @Test
    public void exactlyOneLatitudeNotInRange() {
        final var exception = assertThrows(HttpStatusException.class,
                                           () -> validator.validate(Collections.singletonList("-20.0102,200.002")));
        assertThat(exception.getStatusCode(), equalTo(BAD_REQUEST));
    }

    @Test
    public void exactlyOneLongitudeNotInRange() {
        final var exception = assertThrows(HttpStatusException.class,
                                           () -> validator.validate(Collections.singletonList("-400.0102,20.002")));
        assertThat(exception.getStatusCode(), equalTo(BAD_REQUEST));
    }

    @Test
    public void moreThatOneProvided() {
        final var exception = assertThrows(HttpStatusException.class,
                                           () -> validator.validate(Arrays.asList("-20.0102,22.0222", "10.2,20.11")));
        assertThat(exception.getStatusCode(), equalTo(BAD_REQUEST));
    }
}
