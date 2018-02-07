package com.hradecek.maps.utils;

import com.google.maps.model.LatLng;

import java.util.Random;

import static com.hradecek.maps.utils.GpsRandom.WGS84.SEMI_MAJOR_AXIS;

/**
 * Generating random GPS locations.
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class GpsRandom extends Random {

    /**
     * World Geodetic System constants
     */
    public static class WGS84 {

        /**
         * Earth's semi major axis in metres
         */
        public static final double SEMI_MAJOR_AXIS = 6_378_137.0;
    }

    /**
     * Constructor
     */
    public GpsRandom() {
        super();
    }

    /**
     * Constructor
     *
     * @param seed initial seed
     */
    public GpsRandom(int seed) {
        super(seed);
    }

    /**
     * Random latitude and longitude.
     *
     * @return
     */
    public LatLng nextLatLng() {
        final double latitude = (Math.random() * 180.0) - 90.0;
        final double longitude = (Math.random() * 360.0) - 180.0;

        return new LatLng(latitude, longitude);
    }

    /**
     * Get random point constrained by minimal and maximal distance from base location.
     *
     * @param base base location
     * @param minDistance minimal distance in metres
     * @param maxDistance maximal distance in metres
     * @return random location
     */
    public LatLng nextLatLng(LatLng base, double minDistance, double maxDistance) {
        return nextLatLng(base, minDistance, maxDistance, nextInt(360));
    }

    /**
     * Get random point constrained by bearing angle, minimal and maximal distance from base location.
     *
     * @param base base location
     * @param minDistance minimal distance in metres
     * @param maxDistance maximal distance in metres
     * @param bearing bearing angle in degrees
     * @return random location
     */
    public LatLng nextLatLng(LatLng base, double minDistance, double maxDistance, double bearing) {
        final double bearingR = Math.toRadians(bearing);
        final double lat1R = Math.toRadians(base.lat);
        final double lon1R = Math.toRadians(base.lng);
        final double distanceR = (minDistance + nextDouble() * (maxDistance - minDistance)) / SEMI_MAJOR_AXIS;

        final double a = Math.sin(distanceR) * Math.cos(lat1R);
        final double lat2 = Math.asin(Math.sin(lat1R) * Math.cos(distanceR) + a * Math.cos(bearingR));
        final double lon2 = lon1R + Math.atan2(Math.sin(bearingR) * a, Math.cos(distanceR) - Math.sin(lat1R) * Math.sin(lat2));

        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }
}
