package com.hradecek.maps.google;

/**
 * Non-recoverable {@link DirectionsApiService} exception.
 *
 * @see DirectionsApiService
 */
public class DirectionsApiException extends MapsApiServiceException {

    /**
     * Constructor.
     */
    public DirectionsApiException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message failure message
     */
    public DirectionsApiException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause reference to original cause
     */
    public DirectionsApiException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message failure message
     * @param cause reference to original cause
     */
    public DirectionsApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
