package com.hradecek.maps.config;

import io.vertx.core.json.JsonObject;

/**
 * Google API options.
 */
public class GoogleApiOptions implements ConfigOptions {

    /**
     * Environment variables
     */
    public static final String ENV_API_KEY = "GOOGLE_API_KEY";

    /**
     * System properties
     */
    public static final String SYS_API_KEY = "google.api.key";

    /**
     * JSON configuration file
     */
    public static final String JSON_ROOT = "google";
    public static final String JSON_API_KEY = "key";

    private final JsonObject config;

    /**
     * Constructor.
     *
     * @param configRetriever configuration retriever
     */
    public GoogleApiOptions(AppConfigRetriever configRetriever) {
        this.config = configRetriever.config().blockingGet();
    }

    /**
     * Get the config
     *
     * @return resulting config
     */
    public JsonObject config() {
        return new JsonObject().put(JSON_ROOT, getApi(config));
    }

    private JsonObject getApi(JsonObject config) {
        String api;
        if (config.containsKey(JSON_ROOT) && config.getJsonObject(JSON_ROOT).containsKey(JSON_API_KEY) &&
                !config.getJsonObject(JSON_ROOT).getString(JSON_API_KEY).isEmpty()) {
            api = config.getJsonObject(JSON_ROOT).getString(JSON_API_KEY);
        } else if (config.containsKey(SYS_API_KEY) && !config.getString(SYS_API_KEY).isEmpty()) {
            api = config.getString(SYS_API_KEY);
        } else if (config.containsKey(ENV_API_KEY) && !config.getString(ENV_API_KEY).isEmpty()) {
            api = config.getString(ENV_API_KEY);
        } else {
            throw new ConfigException("No Google API Key has been defined: See ...");
        }

        return new JsonObject().put(JSON_API_KEY, api);
    }
}
