package com.hradecek.maps.config;

import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO:
 */
public class AppOptions implements ConfigOptions {

    /**
     * TODO:
     */
    private Set<ConfigOptions> options = new HashSet<>();

    /**
     * TODO:
     *
     * @return
     */
    public JsonObject config() {
        JsonObject merged = new JsonObject();
        for (ConfigOptions option : options) {
            merged.mergeIn(option.config());
        }

        return merged;
    }

    /**
     * TODO:
     *
     * @param configOptions
     * @return
     */
    public AppOptions add(ConfigOptions configOptions) {
        options.add(configOptions);
        return this;
    }
}
