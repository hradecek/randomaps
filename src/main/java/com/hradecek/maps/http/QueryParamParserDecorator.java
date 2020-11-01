package com.hradecek.maps.http;

import java.util.List;

/**
 * Decorator for {@link QueryParamParser}.
 *
 * @param <T> type of returned value
 */
public class QueryParamParserDecorator<T> implements QueryParamParser<T> {

    protected final QueryParamParser<T> decoratee;

    /**
     * Constructor.
     *
     * @param decoratee decorated {@link QueryParamParser}
     */
    public QueryParamParserDecorator(final QueryParamParser<T> decoratee) {
        this.decoratee = decoratee;
    }

    @Override
    public T parse(List<String> queryParam) {
        return decoratee.parse(queryParam);
    }
}
