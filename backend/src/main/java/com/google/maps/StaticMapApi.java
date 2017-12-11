package com.google.maps;

import com.google.maps.internal.ApiConfig;
import com.google.maps.model.LatLng;

/**
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class StaticMapApi {
    static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/staticmap");

    private StaticMapApi() {}

    public static StaticMapApiRequest getStaticMap(GeoApiContext context, LatLng center, int width, int height, int zoom) {
        return new StaticMapApiRequest(context).center(center).size(width, height).zoom(zoom);
    }
}
