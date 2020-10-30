package com.hradecek.maps.http;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * Represents base for all REST v1 endpoints.
 *
 * <p>Every endpoint must be also {@link Handler} for {@link RoutingContext}.
 */
public abstract class RestV1Endpoint implements Handler<RoutingContext> {

    /**
     * Create HTTP response in JSON format for provided {@code context}.
     *
     * @param context {@link RoutingContext}
     * @return JSON HTTP response
     */
    protected HttpServerResponse createHttpJsonResponse(RoutingContext context) {
        return context.response()
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS.toString(), HttpHeaders.CONTENT_TYPE)
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS.toString(), HttpHeaders.GET)
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN.toString(), "*")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    }
}
