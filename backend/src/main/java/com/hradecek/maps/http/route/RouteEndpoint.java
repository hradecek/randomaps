package com.hradecek.maps.http.route;

import com.hradecek.maps.http.QueryParamParser;
import com.hradecek.maps.http.RestV1Endpoint;
import com.hradecek.maps.http.ValidatingQueryParamParser;
import com.hradecek.maps.random.reactivex.RandomMapsService;
import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import io.reactivex.Single;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * Represents '/route' endpoint.
 */
public class RouteEndpoint extends RestV1Endpoint {

    private static final String JSON_KEY_ROUTE = "route";
    private static final String ROUTE_QUERY_START_LOCATION = "startLocation";

    private static final QueryParamParser<LatLng> START_LOCATION_PARSER =
            new ValidatingQueryParamParser<>(
                    new StartLocationParamValidator(),
                    param -> LatLng.parseLatLng(param.get(0).split(",")));

    private final RandomMapsService randomMapService;

    /**
     * Constructor.
     *
     * @param randomMapService {@link RandomMapsService} reference
     */
    public RouteEndpoint(RandomMapsService randomMapService) {
        this.randomMapService = randomMapService;
    }

    @Override
    public void handle(RoutingContext context) {
        final var startLocationParam = context.queryParam(ROUTE_QUERY_START_LOCATION);
        final Single<Route> routeSingle;
        if (startLocationParam != null) {
            final var startLocation = START_LOCATION_PARSER.parse(startLocationParam);
            routeSingle = randomMapService.rxRouteForStartLocation(startLocation);
        } else {
            routeSingle = randomMapService.rxRoute();
        }
        routeSingle.flatMapCompletable(route -> createHttpJsonResponse(context).rxEnd(routeToBuffer(route)))
                   .subscribe(() -> {}, context::fail);
    }

    private static Buffer routeToBuffer(final Route route) {
        final var jsonArray = new JsonArray();
        route.decodePath().forEach(elem -> jsonArray.add(JsonObject.mapFrom(elem)));

        return Buffer.buffer(new JsonObject().put(JSON_KEY_ROUTE, jsonArray).toString());
    }
}
