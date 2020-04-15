package com.hradecek.maps.google;

/**
 * General exception related to Directions API issues.
 *
 * @see DirectionsApiService
 */
public class DirectionsApiException extends RuntimeException {

    public DirectionsApiException() {
        super();
    }

    public DirectionsApiException(String message) {
        super(message);
    }

    public DirectionsApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectionsApiException(Throwable cause) {
        super(cause);
    }
}
