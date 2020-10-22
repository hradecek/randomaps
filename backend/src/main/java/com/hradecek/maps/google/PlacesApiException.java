package com.hradecek.maps.google;

/**
 * General exception related to Places API issues.
 *
 * @see PlacesApiService
 */
public class PlacesApiException extends RuntimeException {

    /**
     * Constructor.
     */
    public PlacesApiException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message failure message
     */
    public PlacesApiException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause original {@link Throwable cause}
     */
    public PlacesApiException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message failure message
     * @param cause original {@link Throwable cause}
     */
    public PlacesApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
