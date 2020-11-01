package com.hradecek.maps.utils;

import static com.hradecek.maps.utils.NumberUtils.isInRangeClosedBoth;

/**
 * Various utility functions for GPS manipulation.
 */
public class GpsUtils {

    private GpsUtils() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }

    /**
     * Check whether coordinate part (either latitude or longitude) is in right range.
     *
     * @param coordinate coordinate to be checked
     * @return true if coordinate is in interval <-180;180>, otherwise false.
     */
    public static boolean isValidCoordinate(double coordinate) {
        return isInRangeClosedBoth(coordinate, -180.0d, 180.0d);
    }
}
