package com.hradecek.maps.config;

import io.reactivex.Single;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.Vertx;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p>Retrieve application configuration.
 * <p>Application might be configured either via (with precedence):
 * <ol>
 *     <li>json configuration,</li>
 *     <li>system properties passed,</li>
 *     <li>environment variables.</li>
 * </ol>
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class AppConfigRetriever {

    /**
     * Config path. Custom config may be passed via {@code -Dconf} system property.
     */
    private static final String CONFIG_PATH = System.getProperty("conf", "conf/config.json");

    /**
     * Base config retriever
     */
    private final ConfigRetriever configRetriever;

    /**
     * Constructor
     *
     * @param vertx Vert.x instance
     */
    public AppConfigRetriever(Vertx vertx) {
        final ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions()
                .addStore(new ConfigStoreOptions().setType("env"))
                .addStore(new ConfigStoreOptions().setType("sys"));
        if (Files.exists(Paths.get(CONFIG_PATH))) {
            configRetrieverOptions.addStore(new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("json")
                    .setConfig(new JsonObject().put("path", CONFIG_PATH)));
        }

        this.configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
    }

    /**
     * Get configuration.
     *
     * @return configuration.
     */
    public Single<JsonObject> config() {
        return configRetriever.rxGetConfig();
    }
}
