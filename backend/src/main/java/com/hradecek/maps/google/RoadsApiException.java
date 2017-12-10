package com.hradecek.maps.google;

/**
 * General exception related to Roads API issues.
 *
 * @see RoadsApiService
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class RoadsApiException extends RuntimeException {

    public RoadsApiException() {
        super();
    }

    public RoadsApiException(String message) {
        super(message);
    }

    public RoadsApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoadsApiException(Throwable cause) {
        super(cause);
    }
}
