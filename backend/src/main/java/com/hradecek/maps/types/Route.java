package com.hradecek.maps.types;

import com.hradecek.maps.google.Utils;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Represents encoded route.
 */
@DataObject
public class Route {

    private final String points;

    /**
     * JSON Attributes
     */
    private enum JsonAttributes {
        POINTS("points");

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
     * @param routeJson {@link Route} encoded in {@link JsonObject}
     */
    public Route(final JsonObject routeJson) {
        this.points = routeJson.getString(JsonAttributes.POINTS.attributeName);
    }

    /**
     * Constructor - for encoded route.
     *
     * @param encodedPoints encoded route
     */
    public Route(String encodedPoints) {
        this.points = encodedPoints;
    }

    /**
     * Decoded path to list of {@link LatLng}.
     *
     * @return list of {@link LatLng}
     */
    public List<LatLng> decodePath() {
        return Utils.fromEncodedPoints(points);
    }

    /**
     * Converts to {@link JsonObject}.
     *
     * @return JSON representation
     */
    public JsonObject toJson() {
        return new JsonObject().put(JsonAttributes.POINTS.attributeName, points);
    }
}
