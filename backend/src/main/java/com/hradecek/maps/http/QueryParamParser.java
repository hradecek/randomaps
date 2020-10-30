package com.hradecek.maps.http;

import java.util.List;

/**
 * Parser for query parameters.
 *
 * @param <T> type of returned value
 */
@FunctionalInterface
public interface QueryParamParser<T> {

    /**
     * Parses value from provided {@code queryParam} and converts it into class of type {@code T}.
     *
     * @param queryParam query param
     * @return parsed value
     */
    T parse(List<String> queryParam);
}
