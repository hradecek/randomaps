package com.hradecek.maps.http;

import com.hradecek.maps.random.RandomMapsService;
import com.hradecek.maps.random.RandomMapsVerticle;
import com.hradecek.maps.utils.JsonUtils;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.RoutingContext;

import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import org.apache.commons.lang3.tuple.Pair;

/**
 */
public class HttpServerVerticle extends AbstractVerticle {

    /**
     * API version
     */
    public static final String API_VERSION = "v1";

    /**
     * OpenAPI specification path
     */
    private static final String API_SPEC_FILE_PATH = "src/main/resources/api/v1/random-gps.yml";

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    private com.hradecek.maps.random.reactivex.RandomMapsService randomMapService;

    @Override
    public void start() {
        final String serverHost = JsonUtils.getString(config(), "server.host");
        final int serverPort = JsonUtils.getInteger(config(), "server.port");

        randomMapService = RandomMapsService.createProxy(vertx.getDelegate(), RandomMapsVerticle.RANDOM_MAP_QUEUE);
        randomMapService.rxRoute().subscribe(route -> System.out.println(route.decodePath()), System.out::println);

//        HttpServer server = vertx.createHttpServer(new HttpServerOptions().setHost(serverHost).setPort(serverPort));
//        OpenAPI3RouterFactory
//                .rxCreate(vertx, API_SPEC_FILE_PATH)
//                .map(routerFactory -> routerFactory.addHandlerByOperationId("route", this::randomHandler))
//                .map(routerFactory -> routerFactory.addHandlerByOperationId("route", ctx -> ctx.response().end()))
//                .flatMap(routerFactory -> server.requestHandler(routerFactory.getRouter()).rxListen())
//                .subscribe(httpServer -> LOGGER.info(String.format("HTTP server started at %s:%d", serverHost, serverPort)),
//                           Throwable::printStackTrace);

        // router.get("/realtime").handler(this::publishEvent);
        // router.get("/eventbus/*").handler(sockJsHandler());
    }

//    private SockJSHandler sockJsHandler() {
//        SockJSHandlerOptions sockJsOptions = new SockJSHandlerOptions();
//        BridgeOptions bridgeOptions = new BridgeOptions()
//                .addInboundPermitted(new PermittedOptions().setAddress("random"))
//                .addOutboundPermitted(new PermittedOptions().setAddress("random"));
//        return SockJSHandler.create(vertx, sockJsOptions).bridge(bridgeOptions);
//    }

    private void publishEvent(RoutingContext context) {
        // context.queryParam("client"); TODO: Get client ID, use for dedicated eventbus address
//        context.response().putHeader("Access-Control-Allow-Origin", "*").end();
//        final RandomJourney randomJourney = new RandomJourney(placesService, directionsService);
//        randomLocations()
//                .skipWhile(staticMapApiService::isWater)
//                .firstOrError()
//                .flatMap(this::firstNearby)
//                .compose(randomJourney)
//                .retry()
//                .map(polyline -> polyline.decodePath().stream().map(location -> Pair.of(polyline, location)).collect(Collectors.toList()))
//                .flatMapObservable(Observable::fromIterable)
//                .map(this::latLngToJson)
//                .zipWith(Observable.interval(50, TimeUnit.MILLISECONDS), (point, interval) -> point)
//                .subscribe(point -> {
//                    System.out.println("Publishing " + point);
//                    vertx.eventBus().publish("random", point);
//                });
    }

    private JsonObject latLngToJson(Pair<EncodedPolyline, LatLng> location) {
        return new JsonObject().put("id", location.getLeft().getEncodedPath())
                .put("lat", location.getRight().lat).put("lng", location.getRight().lng);
    }

//    private void randomHandler(RoutingContext context) {
//        randomRoute().subscribe(polyline -> createHttpResponse(context).end(polyline));
//    }

    private Buffer polylineToBuffer(EncodedPolyline polyline) { JsonArray jsonArray = new JsonArray();
        polyline.decodePath().forEach(elem -> jsonArray.add(JsonObject.mapFrom(elem)));
        String jsonString = new JsonObject().put("polyline", jsonArray).toString();

        return Buffer.buffer(jsonString);
    }

    private HttpServerResponse createHttpResponse(RoutingContext context) {
        return context.response()
                      .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS.toString(), HttpHeaders.CONTENT_TYPE)
                      .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS.toString(), HttpHeaders.GET)
                      .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN.toString(), "*")
                      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    }
}
