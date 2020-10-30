package com.hradecek.maps.http;

import java.util.List;

/**
 * Validator for query parameters.
 */
@FunctionalInterface
public interface QueryParamValidator {

    /**
     * Validates {@code queryParam}.
     *
     * <p>If param is not valid, exception might be thrown.
     *
     * @param queryParam query param to be validated
     */
    void validate(List<String> queryParam);
}
