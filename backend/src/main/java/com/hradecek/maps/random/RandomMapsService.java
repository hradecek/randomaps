package com.hradecek.maps.random;

import com.hradecek.maps.google.reactivex.MapsService;
import com.hradecek.maps.types.Route;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

@ProxyGen
@VertxGen
public interface RandomMapsService {

    @GenIgnore
    static RandomMapsService create(MapsService mapsService) {
        return new RandomMapsServiceImpl(mapsService);
    }

    @GenIgnore
    static com.hradecek.maps.random.reactivex.RandomMapsService createProxy(Vertx vertx, String address) {
        return new com.hradecek.maps.random.reactivex.RandomMapsService(new RandomMapsServiceVertxEBProxy(vertx, address));
    }

    @Fluent
    RandomMapsService route(Handler<AsyncResult<Route>> resultHandler);
}
