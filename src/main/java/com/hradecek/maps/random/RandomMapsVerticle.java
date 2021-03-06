package com.hradecek.maps.random;

import com.hradecek.maps.config.RandomMapsOptions;
import com.hradecek.maps.google.MapsService;

import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;

/**
 * RandomMap verticle.
 * <p>
 * Registers services:
 * <ul>
 *     <li>{@link RandomMapsService}
 * </ul>
 */
public class RandomMapsVerticle extends AbstractVerticle {

    /**
     * RandomMap service event bus address.
     */
    public static final String RANDOM_MAP_QUEUE = "randommap.queue";

    @Override
    public void start() {
        final var mapsService = MapsService.createProxy(vertx, config().getString(RandomMapsOptions.GOOGLE_MAPS_QUEUE));
        new ServiceBinder(vertx).setAddress(RANDOM_MAP_QUEUE)
                                .register(RandomMapsService.class, RandomMapsService.create(mapsService));
    }
}
