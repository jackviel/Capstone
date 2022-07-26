package com.example.captstone.net;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MovieTrendingClient {
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private AsyncHttpClient client;
    private static final String API_KEY = "52da927ad32148f842f6cf9f02146b26";

    public MovieTrendingClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getTrending(JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("trending/all/day?api_key=" + API_KEY);
            client.get(url, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}