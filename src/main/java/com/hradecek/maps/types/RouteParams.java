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
    private final double minDistance;
    private final double maxDistance;

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
     * Get route parameters builder.
     *
     * @return route parameters builder
     */
    public static RouteParamsBuilder builder() {
        return new RouteParamsBuilder();
    }

    private RouteParams(final LatLng startLocation, double minDistance, double maxDistance) {
        this.startLocation = startLocation;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    public static class RouteParamsBuilder {

        private LatLng startLocation;
        private double minDistance;
        private double maxDistance;

        public RouteParamsBuilder startLocation(final LatLng startLocation) {
            this.startLocation = startLocation;
            return this;
        }

        public RouteParamsBuilder minDistance(double minDistance) {
            this.minDistance = minDistance;
            return this;
        }

        public RouteParamsBuilder maxDistance(double maxDistance) {
            this.maxDistance = maxDistance;
            return this;
        }

        public RouteParams build() {
            return new RouteParams(startLocation, minDistance, maxDistance);
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
        final var json = new JsonObject();
        if (startLocation != null) {
            json.put(JsonAttributes.START_LOCATION.attributeName, startLocation.toJson());
        }

        return json.put(JsonAttributes.MIN_DISTANCE.attributeName, minDistance)
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
    public double getMinDistance() {
        return minDistance;
    }

    /**
     * Get maximal distance or default value if not specified.
     *
     * @return maximal distance
     */
    public double getMaxDistance() {
        return maxDistance;
    }
}
