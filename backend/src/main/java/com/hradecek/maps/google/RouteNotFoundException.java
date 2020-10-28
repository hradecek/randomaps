package com.hradecek.maps.google;

/**
 * Throw when route was not found.
 */
public class RouteNotFoundException extends Exception {

    /**
     * Constructor.
     */
    public RouteNotFoundException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message failure message
     */
    public RouteNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause reference to original cause
     */
    public RouteNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message failure message
     * @param cause reference to original cause
     */
    public RouteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
