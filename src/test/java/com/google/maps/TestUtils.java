package com.google.maps;

import java.nio.charset.StandardCharsets;

public class TestUtils {

    public static String retrieveBody(String filename) {
        final var input = TestUtils.class.getResourceAsStream(filename);

        try (final var s = new java.util.Scanner(input, StandardCharsets.UTF_8.name())) {
            s.useDelimiter("\\A");
            final var body = s.next();
            if (body == null || body.length() == 0) {
                throw new RuntimeException("filename '" + filename + "' resulted in null or empty body");
            }

            return body;
        }
    }
}
