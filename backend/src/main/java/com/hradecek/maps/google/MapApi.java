package com.hradecek.maps.google;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import io.reactivex.Observable;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Arrays;

/**
 * Base class for maps API services.
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public abstract class MapApi {

    /**
     * Geo API Context
     */
    protected final GeoApiContext context;

    /**
     * Constructor
     *
     * @param context GeoApiContext
     */
    public MapApi(GeoApiContext context) {
        this.context = context;
    }
}
