package com.google.maps;

import okhttp3.mockwebserver.MockResponse;

public abstract class MockGoogleMapsApiResponse {

    public abstract MockResponse response();
}
