package com.hradecek.maps.google;

/**
 * Non-recoverable {@link RoadsApiService} exception.
 *
 * @see RoadsApiService
 */
public class RoadsApiException extends MapsApiServiceException {

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
