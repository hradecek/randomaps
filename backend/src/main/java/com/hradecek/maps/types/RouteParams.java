package com.hradecek.maps.types;

import java.util.Optional;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Represents possible parameters and options for route generation.
 */
@DataObject
public class RouteParams {

    private static final long DEFAULT_MIN_DISTANCE = 50_000L;
    private static final long DEFAULT_MAX_DISTANCE = 100_000L;

    private final LatLng startLocation;
    private final long minDistance;
    private final long maxDistance;

    /**
     * JSON Attributes
     */
    private enum JsonAttributes {
        START_LOCATION("startLocation"),
        MIN_DISTANCE("minDistance"),
        MAX_DISTANCE("maxDistance");

        private final String attributeName;

        /**
         * Constructor.
         *
         * @param attributeName attribute name
         */
        JsonAttributes(final String attributeName) {
            this.attributeName = attributeName;
        }
    }

     /**
     * Constructor for JSON type.
     *
     * @param routeParamsJson encoded in {@link JsonObject}
     */
    public RouteParams(final JsonObject routeParamsJson) {
        this.startLocation = getLatLngOrNull(routeParamsJson);
        this.minDistance = routeParamsJson.getLong(JsonAttributes.MIN_DISTANCE.attributeName, DEFAULT_MIN_DISTANCE);
        this.maxDistance = routeParamsJson.getLong(JsonAttributes.MAX_DISTANCE.attributeName, DEFAULT_MAX_DISTANCE);
    }

    private static LatLng getLatLngOrNull(final JsonObject json) {
        final var startLocation = json.getJsonObject(JsonAttributes.START_LOCATION.attributeName);
        return startLocation != null ? new LatLng(startLocation) : null;
    }

    /**
     * Converts to {@link JsonObject}.
     *
     * @return JSON representation
     */
    public JsonObject toJson() {
        return new JsonObject().put(JsonAttributes.START_LOCATION.attributeName, startLocation.toJson())
                               .put(JsonAttributes.MIN_DISTANCE.attributeName, minDistance)
                               .put(JsonAttributes.MAX_DISTANCE.attributeName, maxDistance);
    }

    /**
     * Get start location parameter, {@code Optional.empty()} if no start location has been specified.
     *
     * @return start location
     */
    public Optional<LatLng> getStartLocation() {
        return Optional.ofNullable(startLocation);
    }

    /**
     * Get minimal distance or default value if not specified.
     *
     * @return minimal distance
     */
    public long getMinDistance() {
        return minDistance;
    }

    /**
     * Get maximal distance or default value if not specified.
     *
     * @return maximal distance
     */
    public long getMaxDistance() {
        return maxDistance;
    }
}
