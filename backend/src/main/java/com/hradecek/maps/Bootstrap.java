package com.hradecek.maps;

import com.hradecek.maps.config.AppConfigRetriever;
import com.hradecek.maps.config.ConfigOptions;
import com.hradecek.maps.config.GoogleApiOptions;
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
     *
     * @param deploymentPair
     * @return
     */
    private static Single<String> deployVerticle(Pair<String, DeploymentOptions> deploymentPair)  {
        logger.debug("Deploying " + deploymentPair.getLeft() +
                     " | options: " + deploymentPair.getRight().getConfig().encodePrettily());
        return vertx.rxDeployVerticle(deploymentPair.getLeft(), deploymentPair.getRight());
    }

    /**
     *
     * @param verticle
     * @return
     */
    private static Pair<String, DeploymentOptions> getDeploymentOptions(String verticle) {
        ConfigOptions options = new GoogleApiOptions(appConfigRetriever);
        return Pair.of(verticle, new DeploymentOptions().setConfig(options.config()));
    }
}
