package com.hradecek.maps.utils;

import com.hradecek.maps.types.LatLng;

import java.util.Random;

/**
 * Generating random GPS locations.
 */
public class GpsRandom extends Random {

    /**
     * World Geodetic System constants.
     *
     * Earth's semi major axis in metres
     */
    public static final double WGS84_SEMI_MAJOR_AXIS = 6_378_137.0;

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
        var latitude = (Math.random() * 180.0) - 90.0;
        var longitude = (Math.random() * 360.0) - 180.0;

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
        var bearingR = Math.toRadians(bearing);
        var lat1R = Math.toRadians(base.getLat());
        var lon1R = Math.toRadians(base.getLng());
        var distanceR = (minDistance + nextDouble() * (maxDistance - minDistance)) / WGS84_SEMI_MAJOR_AXIS;

        var a = Math.sin(distanceR) * Math.cos(lat1R);
        var lat2 = Math.asin(Math.sin(lat1R) * Math.cos(distanceR) + a * Math.cos(bearingR));
        var lon2 = lon1R + Math.atan2(Math.sin(bearingR) * a, Math.cos(distanceR) - Math.sin(lat1R) * Math.sin(lat2));

        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }
}
