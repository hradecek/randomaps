package com.hradecek.maps;

import com.hradecek.maps.config.AppConfigRetriever;
import com.hradecek.maps.config.ConfigOptions;
import com.hradecek.maps.config.GoogleApiOptions;
import com.hradecek.maps.config.RandomMapsOptions;
import com.hradecek.maps.config.ServerOptions;
import com.hradecek.maps.google.GoogleMapsVerticle;
import com.hradecek.maps.http.RestV1ServerVerticle;
import com.hradecek.maps.random.RandomMapsVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;

import io.reactivex.Single;

/**
 * Bootstrap class.
 */
public class Bootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private static final Vertx VERTX = Vertx.vertx();

    private static final AppConfigRetriever APP_CONFIG_RETRIEVER = new AppConfigRetriever(VERTX);

    /**
     * Bootstrap method, deploy all necessary verticles.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        deploy(RandomMapsVerticle.class, Bootstrap::randomMapsOptions)
                .flatMap(id -> deploy(GoogleMapsVerticle.class, new GoogleApiOptions(APP_CONFIG_RETRIEVER)))
                .flatMap(id -> deploy(RestV1ServerVerticle.class, new ServerOptions(APP_CONFIG_RETRIEVER)))
                .subscribe();
    }

    private static JsonObject randomMapsOptions() {
        return new RandomMapsOptions().googleMapsQueue(GoogleMapsVerticle.GOOGLE_MAPS_QUEUE).config();
    }

    private static Single<String> deploy(Class<? extends Verticle> verticle, final ConfigOptions config) {
        return VERTX.rxDeployVerticle(verticle.getCanonicalName(), new DeploymentOptions().setConfig(config.config()))
                    .doOnSuccess(id -> LOGGER.info(String.format("Deployed verticle: %s", verticle)));
    }
}
