package com.hradecek.maps.config;

import io.vertx.core.json.JsonObject;

/**
 * Represents portion of application config with defined options.
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public interface ConfigOptions {

    /**
     * Get particular part of config as {@link JsonObject JSON}.
     *
     * @return config
     */
    JsonObject config();
}
