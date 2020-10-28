package com.hradecek.maps.google;

/**
 * Represents non-recoverable exception state for {@link MapsService}.
 */
public class MapsApiServiceException extends RuntimeException {

    /**
     * Constructor.
     */
    public MapsApiServiceException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message failure message
     */
    public MapsApiServiceException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause reference to original cause
     */
    public MapsApiServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message failure message
     * @param cause reference to original cause
     */
    public MapsApiServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
