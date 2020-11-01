package com.google.maps;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;

public class MockGoogleApiServer implements AutoCloseable {

    private static final String BASE_URL = "http://127.0.0.1";

    private final MockWebServer server = new MockWebServer();
    private final GeoApiContext context;

    public MockGoogleApiServer(MockGoogleMapsApiResponse... mockResponses) throws IOException {
        for (MockGoogleMapsApiResponse mockResponse : mockResponses) {
            server.enqueue(mockResponse.response());
        }
        server.start();
        context = createGeoApiContext(getBaseUrl());
    }

    private static GeoApiContext createGeoApiContext(final String baseUrl) {
        return new GeoApiContext.Builder().apiKey("AIzaFakeKey").baseUrlOverride(baseUrl).build();
    }

    protected String getBaseUrl() {
        return BASE_URL + ":" + server.getPort();
    }

    public GeoApiContext context() {
        return context;
    }

    @Override
    public void close() {
        try {
            server.shutdown();
        } catch (IOException ex) {
            System.err.println("Failed to close server: " + ex);
        }
    }
}
