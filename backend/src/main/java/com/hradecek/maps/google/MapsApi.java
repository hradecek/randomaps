package com.hradecek.maps.google;

import com.google.maps.GeoApiContext;

/**
 * Base class for maps API services.
 */
public abstract class MapsApi {

    /**
     * Geo API Context
     */
    protected final GeoApiContext context;

    /**
     * Constructor
     *
     * @param context GeoApiContext
     */
    public MapsApi(GeoApiContext context) {
        this.context = context;
    }
}
