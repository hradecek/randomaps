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
 * Unit tests for {@link DoubleParamValidator}.
 */
public class DoubleParamValidatorTest {

    // Sample data & constants
    private static final int BAD_REQUEST = 400;
    private static final String QUERY_PARAM_NAME = "queryParam";

    // Tested instance
    private DoubleParamValidator validator;

    @BeforeEach
    public void beforeEach() {
        validator = new DoubleParamValidator(QUERY_PARAM_NAME);
    }

    @Test
    public void noQueryParameter() {
        validator.validate(Collections.emptyList());
    }

    @Test
    public void exactlyOneDoubleFormatQueryParam() {
        validator.validate(Collections.singletonList("1.0"));
    }

    @Test
    public void exactlyOneNotInDoubleFormatQueryParam() {
        final var exception = assertThrows(HttpStatusException.class,
                                           () -> validator.validate(Collections.singletonList("NotADouble")));
        assertThat(exception.getStatusCode(), equalTo(BAD_REQUEST));
    }

    @Test
    public void moreThanOneQueryParameterProvided() {
        final var exception = assertThrows(HttpStatusException.class,
                                           () -> validator.validate(Arrays.asList("1.0", "2.0")));
        assertThat(exception.getStatusCode(), equalTo(BAD_REQUEST));
    }
}
