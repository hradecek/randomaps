package com.hradecek.maps.config;

import io.vertx.core.json.JsonObject;

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
    public static final String ENV_SERVER_HOST = "SERVER_HOST";
    public static final String ENV_SERVER_PORT = "SERVER_PORT";

    /**
     * System properties
     */
    public static final String SYS_SERVER_HOST = "server.host";
    public static final String SYS_SERVER_PORT = "server.port";

    /**
     * JSON config keys, nested under root
     */
    public static final String JSON_SERVER_ROOT = "server";
    public static final String JSON_HOST = "host";
    public static final String JSON_PORT = "port";

    /**
     * Partial JSON config representing Server's options
     */
    private final JsonObject config;

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
        return new JsonObject().put(JSON_SERVER_ROOT, host(config).mergeIn(port(config)));
    }

    private JsonObject host(JsonObject config) {
        var host = DEFAULT_SERVER_HOST;
        if (config.containsKey(JSON_SERVER_ROOT) && config.getJsonObject(JSON_SERVER_ROOT).containsKey(JSON_HOST)) {
            host = config.getJsonObject(JSON_SERVER_ROOT).getString(JSON_HOST);
        } else if (config.containsKey(SYS_SERVER_HOST)) {
            host = config.getString(SYS_SERVER_HOST);
        } else if (config.containsKey(ENV_SERVER_HOST)) {
            host = config.getString(ENV_SERVER_HOST);
        }

        return new JsonObject().put(JSON_HOST, host);
    }

    private JsonObject port(JsonObject config) {
        var port = DEFAULT_SERVER_PORT;
        if (config.containsKey(JSON_SERVER_ROOT) && config.getJsonObject(JSON_SERVER_ROOT).containsKey(JSON_PORT)) {
            port = config.getJsonObject(JSON_SERVER_ROOT).getInteger(JSON_PORT);
        } else if (config.containsKey(SYS_SERVER_PORT)) {
            port = config.getInteger(SYS_SERVER_PORT);
        } else if (config.containsKey(ENV_SERVER_PORT)) {
            port = config.getInteger(ENV_SERVER_PORT);
        }

        return new JsonObject().put(JSON_PORT, port);
    }
}
