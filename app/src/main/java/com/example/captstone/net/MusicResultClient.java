package com.example.captstone.net;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MusicResultClient {
    private static final String API_BASE_URL = "https://ws.audioscrobbler.com/";
    private AsyncHttpClient client;
    private static final String API_KEY = "95439fc74a43f6fa7a17702331fec8c8";

    public MusicResultClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getResults(final String query, JsonHttpResponseHandler handler) {
        String url = getApiUrl("/2.0/?method=track.search&track=" + query + "&api_key=" + API_KEY + "&format=json");
        client.get(url, handler);
    }
}