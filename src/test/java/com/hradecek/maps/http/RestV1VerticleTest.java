package com.hradecek.maps.http;

import com.hradecek.maps.config.AppConfigRetriever;
import com.hradecek.maps.config.ServerOptions;
import com.hradecek.maps.random.RandomMapsService;
import com.hradecek.maps.random.RandomMapsVerticle;
import com.hradecek.maps.types.Route;
import com.hradecek.maps.types.RouteParams;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import io.vertx.serviceproxy.ServiceBinder;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Base test class for {@link RestV1ServerVerticle REST v1} unit tests.
 */
public class RestV1VerticleTest {

    @Mock
    private RandomMapsService randomMaps;
    @Captor
    private ArgumentCaptor<Handler<AsyncResult<Route>>> routeResultHandlerCaptor;

    @BeforeEach
    protected void beforeEach(Vertx vertx, VertxTestContext context) {
        registerRandomMapsMock(vertx);
        deployTestedRestV1Verticle(vertx, context);
    }

    private void registerRandomMapsMock(Vertx vertx) {
        new ServiceBinder(vertx.getDelegate()).setAddress(RandomMapsVerticle.RANDOM_MAP_QUEUE)
                .register(RandomMapsService.class, randomMaps);
    }

    private void deployTestedRestV1Verticle(Vertx vertx, VertxTestContext context) {
        vertx.getDelegate().deployVerticle(
                new RestV1ServerVerticle(),
                new DeploymentOptions().setConfig(new ServerOptions(new AppConfigRetriever(vertx)).config()),
                context.completing());
    }

    protected class ApiV1Request {

        private final VertxTestContext context;
        private final WebClient client;
        private Runnable mockedRouteBlock;

        ApiV1Request(Vertx vertx, VertxTestContext context) {
            this.context = context;
            this.client = WebClient.create(vertx.getDelegate());
        }

        // TODO: check params?
        ApiV1Request mockRouteFailure() {
            mockedRouteBlock = () -> {
                AsyncResult<Route> routeResult = Future.failedFuture(new RuntimeException("Failure message"));
                verify(randomMaps).route(any(RouteParams.class), routeResultHandlerCaptor.capture());
                routeResultHandlerCaptor.getValue().handle(routeResult);
            };
            return this;
        }

        // TODO: check params?
        ApiV1Request mockRouteResult(final Route mockedRoute) {
            mockedRouteBlock = () -> {
                AsyncResult<Route> routeResult = Future.succeededFuture(mockedRoute);
                verify(randomMaps).route(any(RouteParams.class), routeResultHandlerCaptor.capture());
                routeResultHandlerCaptor.getValue().handle(routeResult);
            };
            return this;
        }

        void send(final String endpoint, Consumer<HttpResponse<Buffer>> assertResponse) {
            client.get(8080, "localhost", createV1Url(endpoint))
                    .send(context.succeeding(result -> context.verify(() -> {
                        assertResponse.accept(result);
                        context.completeNow();
                    })));
            try {
                context.awaitCompletion(5, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                context.failNow(ex);
            }

            mockedRouteBlock.run();
            context.completeNow();
        }

        private String createV1Url(final String endpoint) {
            return "/v1" + (endpoint.startsWith("/") ? "" : "/") + endpoint;
        }
    }
}
