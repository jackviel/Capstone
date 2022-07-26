package com.example.captstone.net;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BookTrendingClient {
    private static final String API_BASE_URL = "https://api.nytimes.com/";
    private AsyncHttpClient client;
    private static final String API_KEY = "6VyfPV4mnlFfb738jrsvgvYu0ELGdG3g";

    public BookTrendingClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getTrendingBooks(JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("/svc/books/v3/lists/overview.json?api-key=" + API_KEY);
            client.get(url, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
