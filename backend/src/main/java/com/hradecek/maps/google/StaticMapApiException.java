package com.hradecek.maps.google;

/**
 * General exception related to Static Map API issues.
 *
 * @see StaticMapApiService
 */
public class StaticMapApiException extends RuntimeException {

    /**
     * Constructor.
     */
    public StaticMapApiException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message failure message
     */
    public StaticMapApiException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause original cause
     */
    public StaticMapApiException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message failure message
     * @param cause original cause
     */
    public StaticMapApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
