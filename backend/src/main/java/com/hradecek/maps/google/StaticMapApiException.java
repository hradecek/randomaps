package com.hradecek.maps.google;

/**
 * General exception related to Static Map API issues.
 *
 * @see StaticMapApiService
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class StaticMapApiException extends RuntimeException {

    public StaticMapApiException() {
        super();
    }

    public StaticMapApiException(String message) {
        super(message);
    }

    public StaticMapApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public StaticMapApiException(Throwable cause) {
        super(cause);
    }
}
