package com.google.maps;

import com.google.maps.internal.StringJoin.UrlValue;
import com.google.maps.model.LatLng;
import com.google.maps.model.PhotoResult;

import java.util.Arrays;

import static com.google.maps.StaticMapApiRequest.LocationParam.CENTER;
import static com.google.maps.StaticMapApiRequest.LocationParam.ZOOM;
import static com.google.maps.StaticMapApiRequest.MapParam.SIZE;

/**
 * Static Map API Request.
 */
public class StaticMapApiRequest extends PendingResultBase<PhotoResult, StaticMapApiRequest, PhotoRequest.Response> {

    /**
     * Location URL parameters:
     * <ul>
     *     <li>center</li>
     *     <li>zoom</li>
     * </ul>
     */
    public enum LocationParam implements UrlValue {
        ZOOM, CENTER;

        public String toUrlValue() {
            return name().toLowerCase();
        }
    }

    /**
     * Map URL parameters:
     * <ul>
     *     <li>size</li>
     *     <li>scale</li>
     *     <li>format</li>
     *     <li>region</li>
     *     <li>maptype</li>
     *     <li>language</li>
     * </ul>
     */
    public enum MapParam implements UrlValue {
        SIZE, SCALE, FORMAT, REGION, MAPTYPE, LANGUAGE;

        public String toUrlValue() {
            return name().toLowerCase();
        }
    }

    /**
     * Feature URL parameters:
     * <ul>
     *     <li>path</li>
     *     <li>style</li>
     *     <li>markers</li>
     *     <li>visible</li>
     * </ul>
     */
    public enum FeatureParam implements UrlValue {
        PATH, STYLE, MARKERS, VISIBLE;

        public String toUrlValue() {
            return name().toLowerCase();
        }
    }

    private static final UrlValue[] REQUIRED = new UrlValue[] { CENTER, ZOOM, SIZE };

    /**
     * Constructor.
     *
     * @param context GeaApiContext
     */
    public StaticMapApiRequest(GeoApiContext context) {
        super(context, StaticMapApi.API_CONFIG, PhotoRequest.Response.class);
    }

    @Override
    protected void validateRequest() {
        Arrays.stream(REQUIRED)
              .map(UrlValue::toUrlValue)
              .forEach(key -> {
                  if (!params().containsKey(key)) {
                      throw new IllegalArgumentException("Request must contain " + key);
                  }
              });
    }

    /**
     * Set center location for static map.
     *
     * @param center center of the static map
     * @return static map API request
     */
    public StaticMapApiRequest center(LatLng center) {
        return param(CENTER.toUrlValue(), center);
    }

    /**
     * Set width and height of the static map.
     *
     * @param width width of the final static map
     * @param height height of the final static map
     * @return static map API request
     */
    public StaticMapApiRequest size(int width, int height) {
        return param(SIZE.toUrlValue(), String.format("%dx%d", width, height));
    }

    /**
     * Set zoom of the static map.
     *
     * @param zoom zoom of the final static map
     * @return static map API request
     */
    public StaticMapApiRequest zoom(int zoom) {
        return param(ZOOM.toUrlValue(), String.valueOf(zoom));
    }

    /**
     * Create static map's style.
     *
     * @see StyledMap
     * @param styledMaps style
     * @return static map API request
     */
    public StaticMapApiRequest style(StyledMap styledMaps) {
        return param("style", styledMaps.toUrlValue());
    }
}
