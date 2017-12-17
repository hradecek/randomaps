package com.hradecek.maps;

import com.google.maps.GeoApiContext;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.hradecek.maps.google.DirectionsApiService;
import com.hradecek.maps.google.PlacesApiService;
import com.hradecek.maps.google.StaticMapApiService;
import com.hradecek.maps.utils.GpsRandom;
import com.hradecek.maps.utils.JsonUtils;
import io.reactivex.*;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;

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
        GeoApiContext context =
                new GeoApiContext.Builder().apiKey(JsonUtils.getString(config(), "google.key")).build();

        directionsService = new DirectionsApiService(context);
        placesService = new PlacesApiService(context);
        staticMapApiService = new StaticMapApiService(context);

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.get("/random").produces("application/json").handler(this::randomHandler);
        server.requestHandler(router::accept).rxListen(8080).subscribe();
    }

    private void randomHandler(RoutingContext context) {
        final RandomJourney randomJourney = new RandomJourney(placesService, directionsService);
        randomLocations()
                .skipWhile(staticMapApiService::isWater)
                .firstOrError()
                .flatMap(this::firstNearby)
                .compose(randomJourney)
                .map(this::polylineToBuffer)
                .retry()
                .subscribe(polyline -> createHttpResponse(context).end(polyline));
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
