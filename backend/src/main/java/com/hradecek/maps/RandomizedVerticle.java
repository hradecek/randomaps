package com.hradecek.maps;

import com.google.maps.GeoApiContext;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.hradecek.maps.google.DirectionsApiService;
import com.hradecek.maps.google.RoadsApiService;
import com.hradecek.maps.utils.GpsRandom;
import com.hradecek.maps.utils.JsonUtils;
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
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class RandomizedVerticle extends AbstractVerticle{

    /** */
    private static final GpsRandom gpsRandom = new GpsRandom();

    /** TODO: @Inject */
    private DirectionsApiService directionsService;

    /** TODO: @Inject */
    private RoadsApiService roadsService;

    @Override
    public void start() throws Exception {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(JsonUtils.getString(config(), "google.key")).build();
        roadsService = new RoadsApiService(context);
        directionsService = new DirectionsApiService(context);

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.get("/random").produces("application/json").handler(this::randomHandler);
        server.requestHandler(router::accept).rxListen(8080).subscribe();
    }

    private void randomHandler(RoutingContext context) {
        LatLng base = gpsRandom.nextLatLng();
        directionsService.getRoute(base.toString(), gpsRandom.nextLatLng(base, 100000).toString())
                .map(this::polylineToJson)
                .map(JsonObject::toString)
                .map(Buffer::buffer)
                .subscribe(buffer -> createHttpResponse(context).end(buffer));
    }

    private HttpServerResponse createHttpResponse(RoutingContext context) {
        return context.response()
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS.toString(), "Content-Type")
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS.toString(), "GET")
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN.toString(), "*")
                .putHeader("content-type", "application/json");
    }

    private JsonObject polylineToJson(EncodedPolyline polyline) {
        JsonArray jsonArray = new JsonArray();
        polyline.decodePath().forEach(elem -> jsonArray.add(JsonObject.mapFrom(elem)));

        return new JsonObject().put("polyline", jsonArray);
    }
}
