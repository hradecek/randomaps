package com.hradecek.maps.utils;

import com.google.maps.model.LatLng;

import java.util.Random;

import static com.hradecek.maps.utils.GpsRandom.WGS84.SEMI_MAJOR_AXIS;

/**
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class GpsRandom extends Random {

    /**
     *
     */
    public static class WGS84 {
        /**
         *
         */
        public static final double SEMI_MAJOR_AXIS = 6_378_137.0;
    }

    /**
     *
     */
    public GpsRandom() {
        super();
    }

    public GpsRandom(int seed) {
        super(seed);
    }

    /**
     * Random latitude and longitude.
     *
     * @return
     */
    public LatLng nextLatLng() {
        return new LatLng(randomGpsDouble(), randomGpsDouble());
    }

    /* */
    private Double randomGpsDouble() {
        final double hours = (double) nextInt(60);
        final double minutes = (double) nextInt(60) / 60;
        final double seconds = (double) nextInt(60) / 3600;

        return hours + minutes + seconds;
    }

    /**
     *
     * @param base
     * @param maxDistance
     * @return
     */
    public LatLng nextLatLng(LatLng base, double maxDistance) {
        return nextLatLng(base, maxDistance, nextInt(360));
    }

    /**
     *
     * @param base
     * @param maxDistance
     * @param bearing
     * @return
     */
    public LatLng nextLatLng(LatLng base, double maxDistance, double bearing) {
        final double bearingR = Math.toRadians(bearing);
        final double lat1R = Math.toRadians(base.lat);
        final double lon1R = Math.toRadians(base.lng);
        final double distanceR = (nextDouble() * maxDistance) / SEMI_MAJOR_AXIS;

        final double a = Math.sin(distanceR) * Math.cos(lat1R);
        final double lat2 = Math.asin(Math.sin(lat1R) * Math.cos(distanceR) + a * Math.cos(bearingR));
        final double lon2 = lon1R + Math.atan2(Math.sin(bearingR) * a, Math.cos(distanceR) - Math.sin(lat1R) * Math.sin(lat2));

        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }
}
