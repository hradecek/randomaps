package com.hradecek.maps.config;

import io.vertx.core.json.JsonObject;

import static com.hradecek.maps.config.ServerOptions.JsonKeys.PORT;
import static com.hradecek.maps.config.ServerOptions.JsonKeys.SERVER_ROOT;

/**
 * Class representing server options
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class ServerOptions implements ConfigOptions {

    /**
     * Default Server HTTP port
     */
    public static final int DEFAULT_SERVER_PORT = 8080;

    public static class EnvKeys {
        public static final String SERVER_PORT = "SERVER_PORT";
    }

    public static class SysKeys {
        public static final String SERVER_PORT = "server.port";
    }

    public static class JsonKeys {
        public static final String SERVER_ROOT = "server";
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
        return new JsonObject().put(JsonKeys.SERVER_ROOT, port(config));
    }

    private JsonObject port(JsonObject config) {
        int port = DEFAULT_SERVER_PORT;
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
