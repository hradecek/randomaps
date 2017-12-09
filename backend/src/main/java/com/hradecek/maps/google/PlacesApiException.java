package com.hradecek.maps.google;

/**
 * General exception related to Places API issues.
 *
 * @see PlacesApiService
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class PlacesApiException extends RuntimeException {

    public PlacesApiException() {
        super();
    }

    public PlacesApiException(String message) {
        super(message);
    }

    public PlacesApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlacesApiException(Throwable cause) {
        super(cause);
    }
}
