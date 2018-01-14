package com.hradecek.maps;

import com.google.maps.GeoApiContext;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.hradecek.maps.config.ServerOptions;
import com.hradecek.maps.google.DirectionsApiService;
import com.hradecek.maps.google.PlacesApiService;
import com.hradecek.maps.google.StaticMapApiService;
import com.hradecek.maps.utils.GpsRandom;
import com.hradecek.maps.utils.JsonUtils;
import io.reactivex.*;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;
import org.apache.commons.lang3.tuple.Pair;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Verticle for randomized GPS functions.
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class RandomizedVerticle extends AbstractVerticle {

    /**
     * Random GPS generator
     */
    private static final GpsRandom gpsRandom = new GpsRandom();

    /**
     * TODO: @Inject
     */
    private DirectionsApiService directionsService;

    /**
     * TODO:
     */
    private PlacesApiService placesService;

    /**
     * TODO:
     */
    private StaticMapApiService staticMapApiService;

    @Override
    public void start() throws Exception {
        final int serverPort = JsonUtils.getInteger(config(), "server.port");
        final GeoApiContext context =
                new GeoApiContext.Builder().apiKey(JsonUtils.getString(config(), "google.key")).build();

        directionsService = new DirectionsApiService(context);
        placesService = new PlacesApiService(context);
        staticMapApiService = new StaticMapApiService(context);

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.get("/random").produces("application/json").handler(this::randomHandler);
        router.get("/realtime")
                .handler(this::publishEvent);

        /* Event Bus */
        SockJSHandlerOptions sockJsOptions = new SockJSHandlerOptions();
        BridgeOptions bridgeOptions = new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("random"))
                .addOutboundPermitted(new PermittedOptions().setAddress("random"));
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx, sockJsOptions).bridge(bridgeOptions);
        router.get("/eventbus/*").handler(sockJSHandler);

        server.requestHandler(router::accept).rxListen(serverPort).subscribe();
    }

    private void publishEvent(RoutingContext context) {
        // context.queryParam("client"); TODO: Get client ID, use for dedicated eventbus address
        context.response().putHeader("Access-Control-Allow-Origin", "*").end();
        final RandomJourney randomJourney = new RandomJourney(placesService, directionsService);
        randomLocations()
                .skipWhile(staticMapApiService::isWater)
                .firstOrError()
                .flatMap(this::firstNearby)
                .compose(randomJourney)
                .retry()
                .map(polyline -> polyline.decodePath().stream().map(location -> Pair.of(polyline, location)).collect(Collectors.toList()))
                .flatMapObservable(Observable::fromIterable)
                .map(this::latLngToJson)
                .zipWith(Observable.interval(50, TimeUnit.MILLISECONDS), (point, interval) -> point)
                .subscribe(point -> {
                    System.out.println("Publishing " + point);
                    vertx.eventBus().publish("random", point);
                });
    }

    private JsonObject latLngToJson(Pair<EncodedPolyline, LatLng> location) {
        return new JsonObject().put("id", location.getLeft().getEncodedPath())
                .put("lat", location.getRight().lat).put("lng", location.getRight().lng);
    }

    private void randomHandler(RoutingContext context) {
        randomRoute().subscribe(polyline -> createHttpResponse(context).end(polyline));
    }

    private Single<Buffer> randomRoute() {
        final RandomJourney randomJourney = new RandomJourney(placesService, directionsService);
        return randomLocations()
                .skipWhile(staticMapApiService::isWater)
                .firstOrError()
                .flatMap(this::firstNearby)
                .compose(randomJourney)
                .map(this::polylineToBuffer)
                .retry();
    }

    private Flowable<LatLng> randomLocations() {
        return Flowable.fromCallable(gpsRandom::nextLatLng).repeat();
    }

    private Single<LatLng> firstNearby(LatLng location) {
        return placesService.nearbyPlaces(location).firstOrError();
    }

    public static class RandomJourney implements SingleTransformer<LatLng, EncodedPolyline> {

        private PlacesApiService placesService;
        private DirectionsApiService directionsService;

        public RandomJourney(PlacesApiService placesService, DirectionsApiService directionsService) {
            this.placesService = placesService;
            this.directionsService = directionsService;
        }

        @Override
        public SingleSource<EncodedPolyline> apply(Single<LatLng> start) {
            return start.flatMap(startPlace ->
                    placesService.nearbyPlaces(gpsRandom.nextLatLng(startPlace, 50_000, 100_000))
                            .skipWhile(endPlace -> sameLocation(endPlace, startPlace))
                            .firstOrError()
                            .flatMap(end -> directionsService.getRoute(startPlace.toString(), end.toString())));
        }

        private static boolean sameLocation(LatLng location1, LatLng location2) {
            return location1.lat == location2.lat && location1.lng == location2.lng;
        }
    }

    private Buffer polylineToBuffer(EncodedPolyline polyline) {
        JsonArray jsonArray = new JsonArray();
        polyline.decodePath().forEach(elem -> jsonArray.add(JsonObject.mapFrom(elem)));
        String jsonString = new JsonObject().put("polyline", jsonArray).toString();

        return Buffer.buffer(jsonString);
    }

    private HttpServerResponse createHttpResponse(RoutingContext context) {
        return context.response()
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS.toString(), "Content-Type")
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS.toString(), "GET")
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN.toString(), "*")
                .putHeader("content-type", "application/json");
    }
}
