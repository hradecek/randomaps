package com.hradecek.maps;

import com.hradecek.maps.config.AppConfigRetriever;
import com.hradecek.maps.config.GoogleApiOptions;
import com.hradecek.maps.config.ServerOptions;
import com.hradecek.maps.google.GoogleMapsVerticle;
import com.hradecek.maps.http.HttpServerVerticle;
import com.hradecek.maps.random.RandomMapsVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;

import io.reactivex.Observable;

/**
 * Bootstrap class.
 */
public class Bootstrap {

    /**
     * Logger
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    /**
     * Vert.x instance
     */
    private static final Vertx VERTX = Vertx.vertx();

    /**
     * Main config retriever
     */
    private static final AppConfigRetriever APP_CONFIG_RETRIEVER = new AppConfigRetriever(VERTX);

    /**
     * Verticles to deploy
     */
    private final static Observable<String> verticles = Observable.just(HttpServerVerticle.class.getCanonicalName());

    /**
     * Bootstrap method, deploy all necessary verticles.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final var googleApiOptions = new GoogleApiOptions(APP_CONFIG_RETRIEVER);
        final var httpServerOptions = new ServerOptions(APP_CONFIG_RETRIEVER);
        VERTX.rxDeployVerticle(new RandomMapsVerticle(), new DeploymentOptions().setConfig(new JsonObject().put("googlemaps.queue", "googlemaps.queue")))
             .flatMap(id -> VERTX.rxDeployVerticle(new GoogleMapsVerticle(), new DeploymentOptions().setConfig(googleApiOptions.config())))
             .flatMap(id -> VERTX.rxDeployVerticle(new HttpServerVerticle(), new DeploymentOptions().setConfig(httpServerOptions.config())))
             .subscribe();
    }

    /**
     * Create verticle's deployment options.
     *
     * @param verticle verticle to be deployed
     * @return pair of verticle name and set of deployment options
     */
//    private static Pair<String, DeploymentOptions> getDeploymentOptions(String verticle) {
//        final var options = new AppOptions().add(new GoogleApiOptions(APP_CONFIG_RETRIEVER))
//                                            .add(new ServerOptions(APP_CONFIG_RETRIEVER));
//        return Pair.of(verticle, new DeploymentOptions().setConfig(options.config()));
//    }

    /**
     * Deployable verticle with options.
     *
     * @param deploymentPair deployable pair
     * @return Single deployable verticle
     */
//    private static Single<String> deployVerticle(Pair<String, DeploymentOptions> deploymentPair)  {
//        LOGGER.debug("Deploying " + deploymentPair.getLeft() +
//                     " | options: " + deploymentPair.getRight().getConfig().encodePrettily());
//        return VERTX.rxDeployVerticle(deploymentPair.getLeft(), deploymentPair.getRight());
//    }
}
