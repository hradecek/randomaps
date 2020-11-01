package com.hradecek.maps.google;

import java.io.IOException;
import java.util.function.Consumer;

import com.google.maps.GeoApiContext;
import com.google.maps.MockGoogleApiServer;
import com.google.maps.MockGoogleMapsApiResponse;

public abstract class ApiServiceUnitTest {

    protected void runWithMockedServer(final MockGoogleMapsApiResponse mockResponse,
                                       final Consumer<GeoApiContext> contextConsumer) {
        try (final var mockServer = new MockGoogleApiServer(mockResponse)) {
            contextConsumer.accept(mockServer.context());
        } catch (IOException exception) {
            throw new RuntimeException("Cannot start mocked API server.");
        }
    }
}
