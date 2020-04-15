package com.hradecek.maps.config;

import io.vertx.core.json.JsonObject;

import static com.hradecek.maps.config.ServerOptions.JsonKeys.*;

/**
 * Class representing server options
 */
public class ServerOptions implements ConfigOptions {

    /**
     * Default Server hostname
     */
    public static final String DEFAULT_SERVER_HOST = "localhost";

    /**
     * Default Server HTTP port
     */
    public static final int DEFAULT_SERVER_PORT = 8080;

    /**
     * Environment variables
     */
    public static class EnvKeys {
        public static final String SERVER_HOST = "SERVER_HOST";
        public static final String SERVER_PORT = "SERVER_PORT";
    }

    /**
     * System properties
     */
    public static class SysKeys {
        public static final String SERVER_HOST = "server.host";
        public static final String SERVER_PORT = "server.port";
    }

    /**
     * JSON config keys, nested under root
     */
    public static class JsonKeys {
        public static final String SERVER_ROOT = "server";
        public static final String HOST = "host";
        public static final String PORT = "port";
    }

    /**
     * Partial JSON config representing Server's options
     */
    private JsonObject config;

    /**
     * Constructor
     *
     * @param configRetriever application config retriever
     */
    public ServerOptions(AppConfigRetriever configRetriever) {
        this.config = configRetriever.config().blockingGet();
    }

    /**
     * Return server's options config as JSON.
     *
     * @return json for server's options
     */
    public JsonObject config() {
        return new JsonObject().put(JsonKeys.SERVER_ROOT, host(config).mergeIn(port(config)));
    }

    private JsonObject host(JsonObject config) {
        var host = DEFAULT_SERVER_HOST;
        if (config.containsKey(SERVER_ROOT) && config.getJsonObject(SERVER_ROOT).containsKey(HOST)) {
            host = config.getJsonObject(SERVER_ROOT).getString(HOST);
        } else if (config.containsKey(SysKeys.SERVER_HOST)) {
            host = config.getString(SysKeys.SERVER_HOST);
        } else if (config.containsKey(EnvKeys.SERVER_HOST)) {
            host = config.getString(EnvKeys.SERVER_HOST);
        }

        return new JsonObject().put(HOST, host);
    }

    private JsonObject port(JsonObject config) {
        var port = DEFAULT_SERVER_PORT;
        if (config.containsKey(SERVER_ROOT) && config.getJsonObject(SERVER_ROOT).containsKey(PORT)) {
            port = config.getJsonObject(SERVER_ROOT).getInteger(PORT);
        } else if (config.containsKey(SysKeys.SERVER_PORT)) {
            port = config.getInteger(SysKeys.SERVER_PORT);
        } else if (config.containsKey(EnvKeys.SERVER_PORT)) {
            port = config.getInteger(EnvKeys.SERVER_PORT);
        }

        return new JsonObject().put(PORT, port);
    }
}
