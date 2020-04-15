package com.hradecek.maps.google;

import com.google.maps.GeoApiContext;

/**
 * Base class for maps API services.
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
