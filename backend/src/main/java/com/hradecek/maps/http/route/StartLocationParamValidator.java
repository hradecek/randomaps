package com.hradecek.maps.http.route;

import com.hradecek.maps.http.QueryParamValidator;
import com.hradecek.maps.utils.GpsUtils;

import java.util.List;

import io.vertx.ext.web.handler.impl.HttpStatusException;

import static com.hradecek.maps.utils.NumberUtils.stringToDouble;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

/**
 * Validates 'startLocation' query parameter.
 *
 * <p>If validation is not successful {@link HttpStatusException} is thrown.
 */
class StartLocationParamValidator implements QueryParamValidator {

    private static final String ERROR_MORE_THAN_ONE_START_LOCATION =
            "Zero or exactly one 'startLocation' must be provided";
    private static final String ERROR_MUST_CONTAIN_TWO_NUMBERS =
            "'startLocation' parameter must contains two numbers in format (lat,lng)";

    @Override
    public void validate(final List<String> startLocationParam) {
        assertZeroOrSingle(startLocationParam);

        if (startLocationParam.size() == 1) {
            final var latLngArray = startLocationParam.get(0).split(",");
            assertExactlyTwoComponents(latLngArray);
            assertLatLngFormat(latLngArray[0], latLngArray[1]);
        }
    }

    private static void assertZeroOrSingle(final List<String> startLocationParam) {
        if (startLocationParam.size() > 1) {
            throw new HttpStatusException(BAD_REQUEST.code(), ERROR_MORE_THAN_ONE_START_LOCATION);
        }
    }

    private static void assertExactlyTwoComponents(final String[] latLngArray) {
        if (latLngArray.length != 2) {
            throw new HttpStatusException(BAD_REQUEST.code(), ERROR_MUST_CONTAIN_TWO_NUMBERS);
        }
    }

    private static void assertLatLngFormat(final String latString, final String lngString) {
        final var lat = stringToDouble(latString, GpsUtils::isValidCoordinate);
        final var lng = stringToDouble(lngString, GpsUtils::isValidCoordinate);
        if (lat.isEmpty() || lng.isEmpty()) {
            throw new HttpStatusException(BAD_REQUEST.code(), createFormatErrorMessage(lat.isEmpty(), lng.isEmpty()));
        }
    }

    private static String createFormatErrorMessage(boolean isLatErr, boolean isLngErr) {
        return (isLatErr ? "latitude " : "") +
               (isLatErr && isLngErr ? " and " : "") +
               (isLngErr ? "longitude " : "") +
               "must be in number in from interval <-180;+180>";
    }
}
