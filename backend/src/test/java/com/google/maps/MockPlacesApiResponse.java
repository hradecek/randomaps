package com.google.maps;

import okhttp3.mockwebserver.MockResponse;

public class MockPlacesApiResponse extends MockGoogleMapsApiResponse {

    private final MockResponse mockResponse = new MockResponse();

    public MockPlacesApiResponse(int statusCode) {
        this(statusCode, "");
    }

    public MockPlacesApiResponse(String body) {
        this(200, body);
    }

    public MockPlacesApiResponse(int statusCode, String body) {
        mockResponse.setHeader("Content-Type", "application/json");
        mockResponse.setResponseCode(statusCode);
        mockResponse.setBody(body);
    }

    @Override
    public MockResponse response() {
        return mockResponse;
    }
}

