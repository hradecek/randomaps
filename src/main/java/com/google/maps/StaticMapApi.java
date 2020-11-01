package com.google.maps;

import com.google.maps.internal.ApiConfig;
import com.google.maps.model.LatLng;

/**
 * Static map API.
 */
public class StaticMapApi {

    static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/staticmap");

    private StaticMapApi() {}

    /**
     * Creates static map API request.
     *
     * @param context GeoApiContext
     * @param center center of the final static map
     * @param width width of the final static map
     * @param height height of the final static map
     * @param zoom used zoom
     * @return static map API request
     */
    public static StaticMapApiRequest getStaticMap(GeoApiContext context, LatLng center, int width, int height,
                                                   int zoom) {
        return new StaticMapApiRequest(context).center(center).size(width, height).zoom(zoom);
    }
}
