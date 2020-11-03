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
class DoubleParamValidator implements QueryParamValidator {

    private final String queryParamName;

    /**
     * Constructor.
     *
     * @param queryParamName name of the query parameter
     */
    public DoubleParamValidator(final String queryParamName) {
        this.queryParamName = queryParamName;
    }

    @Override
    public void validate(final List<String> doubleQueryParams) {
        assertZeroOrSingle(doubleQueryParams);

        if (doubleQueryParams.size() == 1) {
            try {
                Double.parseDouble(doubleQueryParams.get(0));
            } catch (NumberFormatException ex) {
                throw new HttpStatusException(BAD_REQUEST.code(), createBadFormatErrorMessage());
            }
        }
    }

    private void assertZeroOrSingle(final List<String> doubleQueryParams) {
        if (doubleQueryParams.size() > 1) {
            throw new HttpStatusException(BAD_REQUEST.code(), createMoreThanOneParamErrorMessage());
        }
    }

    private String createBadFormatErrorMessage() {
        return queryParamName + " must be floating point number";
    }

    private String createMoreThanOneParamErrorMessage() {
        return "None or single " + queryParamName + " query parameters were expected";
    }
}
