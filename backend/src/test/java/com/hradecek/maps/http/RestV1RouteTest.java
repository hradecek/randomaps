package com.hradecek.maps.http;

import com.hradecek.maps.types.Route;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Unit tests for {@link RestV1ServerVerticle}.
 * <p>Tested endpoints:
 * <ul>
 *     <li>{@code /route}
 * </ul>
 */
@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class RestV1RouteTest extends RestV1VerticleTest {

    // Sample data
    private static final String ENCODED_ROUTE_1 = "quniGv|icNa@NQHMFKDwAp@A?aAd@KDOFE@IDODG@KB]JE@C@A@A@A@?BAH";

    private static final String ROUTE_ENDPOINT = "/route";

    @Test
    public void routeSuccessfullyGenerated(Vertx vertx, VertxTestContext context) {
        new ApiV1Request(vertx, context)
                .mockRouteResult(new Route(ENCODED_ROUTE_1))
                .send(ROUTE_ENDPOINT, RestV1RouteTest::assertSuccess);
    }

    @Test
    @Disabled
    public void routeNoRouteWasFound(Vertx vertx, VertxTestContext context) {
        // TODO
    }

    @Test
    public void routeRouteGenerationFailure(Vertx vertx, VertxTestContext context) {
        new ApiV1Request(vertx, context)
                .mockRouteFailure()
                .send(ROUTE_ENDPOINT, result -> assertFailure(result, 500));
    }

    private static void assertFailure(HttpResponse<Buffer> result, int statusCode) {
        assertThat(result.statusCode(), equalTo(statusCode));
    }

    private static void assertSuccess(HttpResponse<Buffer> result) {
        final var body = result.body();
        assertThat(body, not(nullValue()));

        try {
            final var bodyJson = body.toJsonObject();
            assertThat(bodyJson.containsKey("route"), is(true));

            final var routePoints = bodyJson.getJsonArray("route");
            assertThat(routePoints.isEmpty(), is(false));
        } catch (DecodeException exception) {
            assertThat("Response was not valid JSON. Response was:\n\t" + body.toString(), false);
        }
    }
}
