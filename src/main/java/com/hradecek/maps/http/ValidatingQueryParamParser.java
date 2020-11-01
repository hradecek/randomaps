package com.hradecek.maps.http;

import java.util.List;

/**
 * Query parameter parser, which performs validation before parsing value from provided query parameters.
 *
 * @param <T> type of returned value
 */
public class ValidatingQueryParamParser<T> extends QueryParamParserDecorator<T> {

    private final QueryParamValidator validator;

    /**
     * Constructor.
     *
     * @param validator query parameter validator
     * @param parser query parameter parser
     */
    public ValidatingQueryParamParser(QueryParamValidator validator, QueryParamParser<T> parser) {
        super(parser);
        this.validator = validator;
    }

    @Override
    public T parse(List<String> queryParam) {
        validator.validate(queryParam);
        return super.parse(queryParam);
    }
}
