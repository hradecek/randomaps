package com.google.maps;

import com.google.maps.internal.StringJoin.UrlValue;
import com.google.maps.model.LatLng;
import com.google.maps.model.PhotoResult;

import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;

import static com.google.maps.StaticMapApiRequest.LocationParam.CENTER;
import static com.google.maps.StaticMapApiRequest.LocationParam.ZOOM;
import static com.google.maps.StaticMapApiRequest.MapParam.SIZE;

/**
 * TODO:
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class StaticMapApiRequest
        extends PendingResultBase<PhotoResult, StaticMapApiRequest, PhotoRequest.Response> {

    public enum LocationParam implements UrlValue {
        ZOOM,
        CENTER;

        public String toUrlValue() {
            return name().toLowerCase();
        }
    }

    public enum MapParam implements UrlValue {
        SIZE,
        SCALE,
        FORMAT,
        REGION,
        MAPTYPE,
        LANGUAGE;

        public String toUrlValue() {
            return name().toLowerCase();
        }
    }

    public enum FeatureParam implements UrlValue {
        PATH,
        STYLE,
        MARKERS,
        VISIBLE;

        public String toUrlValue() {
            return name().toLowerCase();
        }
    }
    private static UrlValue[] required = new UrlValue[] { CENTER, ZOOM, SIZE };

    public StaticMapApiRequest(GeoApiContext context) {
        super(context, StaticMapApi.API_CONFIG, PhotoRequest.Response.class);
    }

    @Override
    protected void validateRequest() {
        Arrays.stream(required).map(UrlValue::toUrlValue).forEach(key -> {
            if (!params().containsKey(key)) {
                throw new IllegalArgumentException("Request must contain " + key);
            }
        });
    }

    public StaticMapApiRequest center(LatLng center) {
        return param(CENTER.toUrlValue(), center);
    }

    public StaticMapApiRequest size(int width, int height) {
        return param(SIZE.toUrlValue(), String.format("%dx%d", width, height));
    }

    public StaticMapApiRequest zoom(int zoom) {
        return param(ZOOM.toUrlValue(), String.valueOf(zoom));
    }

    public StaticMapApiRequest style(StyledMap styledMaps) {
        return param("style", styledMaps.toUrlValue());
    }
}
