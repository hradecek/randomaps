package com.hradecek.maps;

import com.hradecek.maps.config.ConfigOptions;

import io.vertx.core.Verticle;

/**
 * Represents deployment of {@code verticle} with its {@code options}.
 */
public class VerticleDeployment {

    private final Class<? extends Verticle> verticle;
    private final ConfigOptions options;

    /**
     * Constructor.
     *
     * @param verticle verticle to be deployed
     * @param options verticles options
     */
    public VerticleDeployment(Class<? extends Verticle> verticle, ConfigOptions options) {
        this.verticle = verticle;
        this.options = options;
    }

    /**
     * Get verticle.
     *
     * @return verticle
     */
    public Class<? extends Verticle> getVerticle() {
        return verticle;
    }

    /**
     * Get deployment options.
     *
     * @return options
     */
    public ConfigOptions getOptions() {
        return options;
    }
}
