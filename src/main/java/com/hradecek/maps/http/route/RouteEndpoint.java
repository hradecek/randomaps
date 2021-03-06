package com.hradecek.maps.http.route;

import com.hradecek.maps.http.QueryParamParser;
import com.hradecek.maps.http.RestV1Endpoint;
import com.hradecek.maps.http.ValidatingQueryParamParser;
import com.hradecek.maps.random.reactivex.RandomMapsService;
import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;
import com.hradecek.maps.types.RouteParams;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * Represents '/route' endpoint.
 */
public class RouteEndpoint extends RestV1Endpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteEndpoint.class);

    private static final String JSON_KEY_ROUTE = "route";
    private static final String ROUTE_QUERY_START_LOCATION = "startLocation";
    private static final String ROUTE_QUERY_MIN_DISTANCE = "minDistance";
    private static final String ROUTE_QUERY_MAX_DISTANCE = "minDistance";

    private static final QueryParamParser<LatLng> START_LOCATION_PARSER =
            new ValidatingQueryParamParser<>(
                    new StartLocationParamValidator(),
                    param -> LatLng.parseLatLng(param.get(0).split(",")));

    private static final QueryParamParser<Double> MIN_DISTANCE_PARSER = createDistanceParser(ROUTE_QUERY_MIN_DISTANCE);
    private static final QueryParamParser<Double> MAX_DISTANCE_PARSER = createDistanceParser(ROUTE_QUERY_MAX_DISTANCE);

    private static QueryParamParser<Double> createDistanceParser(final String queryParamName) {
        return new ValidatingQueryParamParser<>(
                new DoubleParamValidator(queryParamName),
                param -> Double.parseDouble(param.get(0)));
    }

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
        randomMapService.rxRoute(createRouteParams(context))
                        .flatMapCompletable(route -> createHttpJsonResponse(context).rxEnd(routeToBuffer(route)))
                        .doOnError(error -> LOGGER.error("Cannot handle /route.", error))
                        .subscribe(() -> {}, context::fail);
    }

    // TODO: Remove duplicates
    private static RouteParams createRouteParams(final RoutingContext context) {
        final var routeParamsBuilder = RouteParams.builder();

        final var startLocationParam = context.queryParam(ROUTE_QUERY_START_LOCATION);
        if (!startLocationParam.isEmpty()) {
            routeParamsBuilder.startLocation(START_LOCATION_PARSER.parse(startLocationParam));
        }

        final var minDistance = context.queryParam(ROUTE_QUERY_MIN_DISTANCE);
        if (!minDistance.isEmpty()) {
            routeParamsBuilder.minDistance(MIN_DISTANCE_PARSER.parse(minDistance));
        }

        final var maxDistance = context.queryParam(ROUTE_QUERY_MAX_DISTANCE);
        if (!maxDistance.isEmpty()) {
            routeParamsBuilder.maxDistance(MAX_DISTANCE_PARSER.parse(maxDistance));
        }

        return routeParamsBuilder.build();
    }

    private static Buffer routeToBuffer(final Route route) {
        final var jsonArray = new JsonArray();
        route.decodePath().forEach(elem -> jsonArray.add(JsonObject.mapFrom(elem)));

        return Buffer.buffer(new JsonObject().put(JSON_KEY_ROUTE, jsonArray).toString());
    }
}
