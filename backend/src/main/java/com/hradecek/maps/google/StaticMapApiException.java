package com.hradecek.maps.google;

/**
 * Non-recoverable {@link StaticMapApiService} exception.
 *
 * @see StaticMapApiService
 */
public class StaticMapApiException extends MapsApiServiceException {

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
