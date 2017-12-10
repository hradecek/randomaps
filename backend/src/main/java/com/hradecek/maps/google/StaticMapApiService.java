package com.hradecek.maps.google;

import com.google.maps.GeoApiContext;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Simple service for Static Map API.
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class StaticMapApiService extends MapApi {

    /**
     * Logger
     */
    private static Logger logger = LoggerFactory.getLogger(StaticMapApiService.class);

    /**
     * Constructor
     *
     * @param context GeoApiContext
     */
    public StaticMapApiService(GeoApiContext context) {
        super(context);
    }
}
