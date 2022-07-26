package com.example.captstone.net;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MovieResultClient {
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private AsyncHttpClient client;
    private static final String API_KEY = "52da927ad32148f842f6cf9f02146b26";

    public MovieResultClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getResults(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("search/movie?api_key=" + API_KEY + "&query=");
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}