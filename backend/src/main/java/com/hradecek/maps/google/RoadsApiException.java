package com.hradecek.maps.google;

/**
 * General exception related to Roads API issues.
 *
 * @see RoadsApiService
 */
public class RoadsApiException extends RuntimeException {

    /**
     * Constructor.
     */
    public RoadsApiException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message failure message
     */
    public RoadsApiException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause original cause
     */
    public RoadsApiException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message failure message
     * @param cause original cause
     */
    public RoadsApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
