package com.hradecek.maps.google;

import com.hradecek.maps.utils.JsonUtils;

import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;

public class GoogleMapsVerticle extends AbstractVerticle {

    /**
     * RandomMap service event bus address.
     */
    public static final String CONFIG_GOOGLE_MAPS_QUEUE = "googlemaps.queue";

    @Override
    public void start() {
        final var googleApiKey = JsonUtils.getString(config(), "google.key");
        new ServiceBinder(vertx).setAddress(CONFIG_GOOGLE_MAPS_QUEUE)
                                .register(MapsService.class, MapsService.create(googleApiKey));
    }
}
