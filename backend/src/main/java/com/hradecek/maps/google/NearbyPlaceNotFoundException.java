package com.hradecek.maps.google;

/**
 * Throw when near-by place was not found.
 */
public class NearbyPlaceNotFoundException extends Exception {

    /**
     * Constructor.
     */
    public NearbyPlaceNotFoundException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message failure message
     */
    public NearbyPlaceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause reference to original cause
     */
    public NearbyPlaceNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message failure message
     * @param cause reference to original cause
     */
    public NearbyPlaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
