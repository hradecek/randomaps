package com.hradecek.maps.http;

import com.hradecek.maps.random.RandomMapsService;
import com.hradecek.maps.random.RandomMapsVerticle;
import com.hradecek.maps.types.Route;
import com.hradecek.maps.utils.JsonUtils;

import io.reactivex.Single;

import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;

/**
 * Create REST v1 server.
 */
public class RestV1ServerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestV1ServerVerticle.class);

    private static final String API_SPEC_FILE_PATH = "src/main/resources/api/v1/random-gps.yml";

    private static final String OPERATION_ID_ROUTE = "route";
    private static final String JSON_KEY_ROUTE = "route";

    private HttpServer httpServer;
    private com.hradecek.maps.random.reactivex.RandomMapsService randomMapService;

    @Override
    public void start(final Promise<Void> startPromise) {
        randomMapService = RandomMapsService.createProxy(vertx.getDelegate(), RandomMapsVerticle.RANDOM_MAP_QUEUE);

        OpenAPI3RouterFactory
                .rxCreate(vertx, API_SPEC_FILE_PATH)
                .map(routerFactory ->
                        routerFactory.addHandlerByOperationId(OPERATION_ID_ROUTE, this::routeHandler)
                                     .addFailureHandlerByOperationId(OPERATION_ID_ROUTE, this::routeFailureHandler))
                .flatMap(routerFactory -> httpListen(routerFactory.getRouter()))
                .doOnSuccess(httpsServer -> this.httpServer = httpsServer)
                .ignoreElement()
                .subscribe(startPromise::complete, startPromise::fail);
    }

    @Override
    public void stop() {
        httpServer.rxClose()
                  .subscribe(() -> LOGGER.info("REST v1 server has been stopped."),
                             throwable -> LOGGER.warn("Could not stop REST v1 server.", throwable));
    }

    private Single<HttpServer> httpListen(final Router router) {
        final var host = JsonUtils.getString(config(), "server.host");
        final var port = JsonUtils.getInteger(config(), "server.port");

        return vertx.createHttpServer(new HttpServerOptions().setHost(host).setPort(port))
                    .requestHandler(router)
                    .rxListen()
                    .doOnSuccess(server -> LOGGER.info(String.format("REST v1 server started at %s:%d", host, port)));
    }

    private void routeHandler(RoutingContext context) {
        randomMapService.rxRoute()
                        .flatMapCompletable(route -> createHttpResponse(context).rxEnd(routeToBuffer(route)))
                        .subscribe(() -> {}, context::fail);
    }

    private static Buffer routeToBuffer(final Route route) {
        final var jsonArray = new JsonArray();
        route.decodePath().forEach(elem -> jsonArray.add(JsonObject.mapFrom(elem)));

        return Buffer.buffer(new JsonObject().put(JSON_KEY_ROUTE, jsonArray).toString());
    }

    private HttpServerResponse createHttpResponse(RoutingContext context) {
        return context.response()
                      .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS.toString(), HttpHeaders.CONTENT_TYPE)
                      .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS.toString(), HttpHeaders.GET)
                      .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN.toString(), "*")
                      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    private void routeFailureHandler(RoutingContext context) {
        LOGGER.error(context.failure());
        context.response().setStatusCode(500).end();
    }
}
