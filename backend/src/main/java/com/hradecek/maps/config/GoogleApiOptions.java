package com.hradecek.maps.config;

import io.vertx.core.json.JsonObject;

/**
 * Google API options
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class GoogleApiOptions implements ConfigOptions {

    public static class EnvKeys {
        public static final String API_KEY = "GOOGLE_API_KEY";
    }

    public static class SysKeys {
        public static final String API_KEY = "google.api.key";
    }

    public static class JsonKeys {
        public static final String ROOT = "google";
        public static final String API_KEY = "key";
    }

    private JsonObject config;

    public GoogleApiOptions(AppConfigRetriever configRetriever) {
        this.config = configRetriever.config().blockingGet();
    }

    public JsonObject config() {
        return new JsonObject().put(JsonKeys.ROOT, getApi(config));
    }

    private JsonObject getApi(JsonObject config) {
        String api;
        if (config.containsKey(JsonKeys.ROOT) && config.getJsonObject(JsonKeys.ROOT).containsKey(JsonKeys.API_KEY) &&
                !config.getJsonObject(JsonKeys.ROOT).getString(JsonKeys.API_KEY).isEmpty()) {
            api = config.getJsonObject(JsonKeys.ROOT).getString(JsonKeys.API_KEY);
        } else if (config.containsKey(SysKeys.API_KEY) && !config.getString(SysKeys.API_KEY).isEmpty()) {
            api = config.getString(SysKeys.API_KEY);
        } else if (config.containsKey(EnvKeys.API_KEY) && !config.getString(EnvKeys.API_KEY).isEmpty()) {
            api = config.getString(EnvKeys.API_KEY);
        } else {
            throw new ConfigException("No Google API Key has been defined: See ...");
        }

        return new JsonObject().put(JsonKeys.API_KEY, api);
    }
}
