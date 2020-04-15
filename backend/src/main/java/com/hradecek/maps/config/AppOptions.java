package com.hradecek.maps.config;

import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Aggregate all config options as a whole piece.
 */
public class AppOptions implements ConfigOptions {

    /**
     * Set of all partial configs.
     */
    private Set<ConfigOptions> options = new HashSet<>();

    /**
     * Get final config from joining partial configs.
     *
     * @return final config
     */
    public JsonObject config() {
        var merged = new JsonObject();
        for (ConfigOptions option : options) {
            merged.mergeIn(option.config());
        }

        return merged;
    }

    /**
     * Add new partial config.
     *
     * @param configOptions partial config
     * @return this config
     */
    public AppOptions add(ConfigOptions configOptions) {
        options.add(configOptions);
        return this;
    }
}
