package com.hradecek.maps;

import com.hradecek.maps.config.ConfigOptions;

import java.util.List;

import io.reactivex.Completable;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;

/**
 * Deploy all provided {@link VerticleDeployment}s into specified instance of {@link Vertx}.
 */
public class VerticleDeployer {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerticleDeployer.class);

    private final Vertx vertx;

    /**
     * Constructor.
     *
     * @param vertx Vert.x instance
     */
    public VerticleDeployer(final Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * Deploy provided {@code verticleDeployments}.
     *
     * @param verticleDeployments to be deployed
     * @return {@link Completable}
     */
    public Completable deploy(final List<VerticleDeployment> verticleDeployments) {
        return verticleDeployments
                .stream()
                .map(this::deferDeployment)
                .reduce(Completable.complete(), Completable::andThen);
    }

    private Completable deferDeployment(final VerticleDeployment verticleDeployment) {
        return Completable.defer(() -> deploy(verticleDeployment.getVerticle(), verticleDeployment.getOptions()));
    }

    private Completable deploy(Class<? extends Verticle> verticle, final ConfigOptions config) {
        return vertx.rxDeployVerticle(verticle.getCanonicalName(), new DeploymentOptions().setConfig(config.config()))
                    .doOnSuccess(id -> LOGGER.info(String.format("Deployed verticle: %s", verticle)))
                    .ignoreElement();
    }
}
