package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;

import com.google.maps.internal.PolylineEncoding;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static com.google.maps.model.LatLng toGLatLng(final LatLng latLng) {
        return new com.google.maps.model.LatLng(latLng.getLat(), latLng.getLng());
    }

    public static LatLng fromGLatLng(final com.google.maps.model.LatLng latLng) {
        return new LatLng(latLng.lat, latLng.lng);
    }

    public static List<LatLng> fromEncodedPoints(final String encodedPoints) {
        return PolylineEncoding.decode(encodedPoints)
                               .stream()
                               .map(Utils::fromGLatLng)
                               .collect(Collectors.toList());
    }
}
