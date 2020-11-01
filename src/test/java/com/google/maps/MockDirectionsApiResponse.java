package com.google.maps;

import okhttp3.mockwebserver.MockResponse;

public class MockDirectionsApiResponse extends MockGoogleMapsApiResponse {

    private final MockResponse mockResponse = new MockResponse();

    public MockDirectionsApiResponse(int statusCode) {
        this(statusCode, "");
    }

    public MockDirectionsApiResponse(String body) {
        this(200, body);
    }

    public MockDirectionsApiResponse(int statusCode, String body) {
        mockResponse.setHeader("Content-Type", "application/json");
        mockResponse.setResponseCode(statusCode);
        mockResponse.setBody(body);
    }

    @Override
    public MockResponse response() {
        return mockResponse;
    }
}

