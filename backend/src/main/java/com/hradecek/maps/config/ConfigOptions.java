package com.hradecek.maps.config;

import io.vertx.core.json.JsonObject;

/**
 * Represents portion of application config with defined options.
 */
public interface ConfigOptions {

    /**
     * Get particular part of config as {@link JsonObject JSON}.
     *
     * @return config
     */
    JsonObject config();
}
