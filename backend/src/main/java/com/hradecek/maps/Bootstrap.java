package com.hradecek.maps;

import com.hradecek.maps.config.*;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Bootstrap class.
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class Bootstrap {

    /**
     * Logger
     */
    private final static Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    /**
     * Vert.x instance
     */
    private static final Vertx vertx = Vertx.vertx();

    /**
     * Main config retriever
     */
    private static final AppConfigRetriever appConfigRetriever = new AppConfigRetriever(vertx);

    /**
     * Verticles to deploy
     */
    private final static Observable<String> verticles = Observable.just(RandomizedVerticle.class.getCanonicalName());

    /**
     * Bootstrap method, deploy all necessary verticles.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        verticles.map(Bootstrap::getDeploymentOptions).flatMapSingle(Bootstrap::deployVerticle).subscribe();
    }

    /**
     * Create verticle's deployment options.
     *
     * @param verticle verticle to be deployed
     * @return pair of verticle name and set of deployment options
     */
    private static Pair<String, DeploymentOptions> getDeploymentOptions(String verticle) {
        AppOptions options = new AppOptions()
                .add(new GoogleApiOptions(appConfigRetriever))
                .add(new ServerOptions(appConfigRetriever));
        return Pair.of(verticle, new DeploymentOptions().setConfig(options.config()));
    }

    /**
     * Deployable verticle with options.
     *
     * @param deploymentPair deployable pair
     * @return Single deployable verticle
     */
    private static Single<String> deployVerticle(Pair<String, DeploymentOptions> deploymentPair)  {
        logger.debug("Deploying " + deploymentPair.getLeft() +
                     " | options: " + deploymentPair.getRight().getConfig().encodePrettily());
        return vertx.rxDeployVerticle(deploymentPair.getLeft(), deploymentPair.getRight());
    }
}
