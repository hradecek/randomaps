package com.hradecek.maps.config;

import io.vertx.core.json.JsonObject;

/**
 * Class representing options for {@link com.hradecek.maps.random.RandomMapsVerticle}.
 */
public class RandomMapsOptions implements ConfigOptions {

    /**
     * Config key for google maps service.
     */
    public static final String GOOGLE_MAPS_QUEUE = "googlemaps.queue";

    private final JsonObject config = new JsonObject();

    @Override
    public JsonObject config() {
        return config;
    }

    public RandomMapsOptions googleMapsQueue(final String googleMapsQueue) {
        config.put(GOOGLE_MAPS_QUEUE, googleMapsQueue);
        return this;
    }
}
