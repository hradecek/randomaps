package com.hradecek.maps;

import com.hradecek.maps.config.AppConfigRetriever;
import com.hradecek.maps.config.ConfigOptions;
import com.hradecek.maps.config.GoogleApiOptions;
import com.hradecek.maps.config.RandomMapsOptions;
import com.hradecek.maps.config.ServerOptions;
import com.hradecek.maps.google.GoogleMapsVerticle;
import com.hradecek.maps.http.RestV1ServerVerticle;
import com.hradecek.maps.random.RandomMapsVerticle;

import java.util.Arrays;
import java.util.List;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;

/**
 * Bootstrap class.
 */
public class Bootstrap {

    static {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4j2LogDelegateFactory");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private static final Vertx VERTX = Vertx.vertx();

    private static final AppConfigRetriever APP_CONFIG_RETRIEVER = new AppConfigRetriever(VERTX);

    private static final ConfigOptions RANDOM_MAPS_OPTIONS =
            new RandomMapsOptions().googleMapsQueue(GoogleMapsVerticle.GOOGLE_MAPS_QUEUE);

    private static final List<VerticleDeployment> VERTICLES = Arrays.asList(
            new VerticleDeployment(RandomMapsVerticle.class, RANDOM_MAPS_OPTIONS),
            new VerticleDeployment(GoogleMapsVerticle.class, new GoogleApiOptions(APP_CONFIG_RETRIEVER)),
            new VerticleDeployment(RestV1ServerVerticle.class, new ServerOptions(APP_CONFIG_RETRIEVER)));

    /**
     * Bootstrap method, deploy all necessary verticles.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new VerticleDeployer(VERTX)
                .deploy(VERTICLES)
                .doOnError(LOGGER::error)
                .subscribe(() -> {}, error -> VERTX.close());
    }
}
