package com.hradecek.maps.http.route;

import com.hradecek.maps.http.QueryParamValidator;

import java.util.List;

import io.vertx.ext.web.handler.impl.HttpStatusException;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

/**
 * Validates query parameter in double format.
 *
 * <p>If validation is not successful {@link HttpStatusException} is thrown.
 */
// TODO: Refactor, take as argument query param name
// TODO: Create base class
class DoubleParamValidator implements QueryParamValidator {

    private static final String ERROR_BAD_FORMAT = "Query param must be floating point number";
    private static final String ERROR_MORE_THAN_ONE_PARAM = "None or single query param was expected";

    @Override
    public void validate(final List<String> distanceParam) {
        assertZeroOrSingle(distanceParam);

        if (distanceParam.size() == 1) {
            try {
                Double.parseDouble(distanceParam.get(0));
            } catch (NumberFormatException ex) {
                throw new HttpStatusException(BAD_REQUEST.code(), ERROR_BAD_FORMAT);
            }

        }
    }

    private static void assertZeroOrSingle(final List<String> startLocationParam) {
        if (startLocationParam.size() > 1) {
            throw new HttpStatusException(BAD_REQUEST.code(), ERROR_MORE_THAN_ONE_PARAM);
        }
    }
}
