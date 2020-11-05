package com.hradecek.maps.types;

import java.util.Objects;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Represents GEO location as pair of latitude and longitude.
 */
@DataObject(generateConverter = true)
public class LatLng {

    private final double lat;
    private final double lng;

    /**
     * JSON Attributes.
     */
    private enum JsonAttributes {
        LAT("lat"), LNG("lng");

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
     * Parse latitude and longitude from provided string {@code latLng}.
     * <p>
     * Note, that {@code latLng} string array is expected to be in right format and
     * no additional validation is performed.
     *
     * @param latLng array containing latitude and longitude
     */
    public static LatLng parseLatLng(String[] latLng) {
        return new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
    }

    /**
     * Constructor for JSON type.
     *
     * @param latLngJson {@link LatLng} encoded in {@link JsonObject}
     */
    public LatLng(final JsonObject latLngJson) {
        this.lat = latLngJson.getDouble(JsonAttributes.LAT.attributeName);
        this.lng = latLngJson.getDouble(JsonAttributes.LNG.attributeName);
    }

    /**
     * Constructor.
     *
     * @param lat latitude
     * @param lng longitude
     */
    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Get latitude.
     *
     * @return latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Get latitude in radians.
     *
     * @return latitude in radians
     */
    public double getLatR() {
        return Math.toRadians(lat);
    }

    /**
     * Get longitude.
     *
     * @return longitude
     */
    public double getLng() {
        return lng;
    }

    /**
     * Get longitude in radians.
     *
     * @return longitude in radians
     */
    public double getLngR() {
        return Math.toRadians(lng);
    }

    /**
     * Converts to {@link JsonObject}.
     *
     * @return JSON representation
     */
    public JsonObject toJson() {
        var jsonObject = new JsonObject();
        LatLngConverter.toJson(this, jsonObject);
        return jsonObject;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final var latLng = (LatLng) other;
        return Double.compare(latLng.lat, lat) == 0 && Double.compare(latLng.lng, lng) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", lat, lng);
    }
}
