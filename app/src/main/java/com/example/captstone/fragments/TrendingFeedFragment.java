package com.example.captstone.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.captstone.EndlessRecyclerViewScrollListener;
import com.example.captstone.R;
import com.example.captstone.modelAdapters.TrendingMediaAdapter;
import com.example.captstone.models.Result;
import com.example.captstone.net.BookTrendingClient;
import com.example.captstone.net.MovieTrendingClient;
import com.example.captstone.net.MusicTrendingClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Headers;

public class TrendingFeedFragment extends Fragment {
    private static final String TAG = "TrendingFragment";

    private RecyclerView rvTrending;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager manager;

    protected TrendingMediaAdapter adapter;
    protected ArrayList<Result> allTrending;

    private final MovieTrendingClient movieTrendingClient = new MovieTrendingClient();
    private final MusicTrendingClient musicTrendingClient = new MusicTrendingClient();
    private final BookTrendingClient bookTrendingClient = new BookTrendingClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTrending = view.findViewById(R.id.rvTrending);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });

        allTrending = new ArrayList<>();
        adapter = new TrendingMediaAdapter(getContext(), allTrending);

        manager = new LinearLayoutManager(getContext());
        rvTrending.setLayoutManager(manager);
        rvTrending.setAdapter(adapter);

        queryTrending();
    }

    public void fetchTimelineAsync(int page) {
        swipeContainer.setRefreshing(true);
        adapter.clear();
        queryTrending();
        swipeContainer.setRefreshing(false);
    }

    private void fetchTrendingMovies() {
        Log.i(TAG, "fetching trending movies");
        movieTrendingClient.getTrending(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON response) {
                try {
                    JSONArray jsonResults;
                    if (response != null) {
                        // Get jsonResults
                        jsonResults = response.jsonObject.getJSONArray("results");
                        // Parse json array into array of model objects
                        // add movies into the results array
                        int jsonResultsLength = jsonResults.length();
                        final ArrayList<Result> results;
                        if (jsonResultsLength > 0) {
                            if (jsonResultsLength > 4)
                                results = new ArrayList<>(Result.fromJson(jsonResults, "Movie").subList(0, 4));
                            else
                                results = new ArrayList<>(Result.fromJson(jsonResults, "Movie").subList(0, 1));
                            // Load model objects into the adapter
                            // add result through the adapter
                            Log.i(TAG, "results size: " + results.size());
                            allTrending.addAll(results);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                    Log.i(TAG, "Error, invalid JSON format.");
                    Toast.makeText(getContext(), "Error encountered, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                // Handle failed request here
                Log.e(TAG,
                        "Request failed with code " + statusCode + ". Response message: " + response);
            }
        });
    }
    private void fetchTrendingSongs(){
        Log.i(TAG, "fetching trending songs");
        musicTrendingClient.getTrendingMusic(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON response) {
                try {
                    JSONArray jsonResults;
                    if (response != null) {
                        // Get jsonResults
                        jsonResults = response.jsonObject.getJSONObject("tracks").getJSONArray("track");
                        // Parse json array into array of model objects
                        // add songs into the results array
                        int jsonResultsLength = jsonResults.length();
                        final ArrayList<Result> results;
                        if (jsonResultsLength > 0) {
                            if (jsonResultsLength > 4)
                                results = new ArrayList<>(Result.fromJson(jsonResults, "Trending Song").subList(0, 4));
                            else
                                results = new ArrayList<>(Result.fromJson(jsonResults, "Trending Song").subList(0, 1));
                            // Load model objects into the adapter
                            for (Result result : results) {
                                allTrending.add(result); // add result through the adapter
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                    Log.i(TAG, "Error, invalid JSON format.");
                    Toast.makeText(getContext(), "Error encountered, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                // Handle failed request here
                Log.e(TAG,
                        "Request failed with code " + statusCode + ". Response message: " + response);
            }
        });
    }
    private void fetchTrendingBooks(){
        Log.i(TAG, "fetching trending books");
        bookTrendingClient.getTrendingBooks(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON response) {
                try {
                    JSONArray jsonList1;
                    JSONArray jsonList2;
                    if (response != null) {
                        // Get jsonResults
                        jsonList1 = response.jsonObject.getJSONObject("results").getJSONArray("lists").getJSONObject(0).getJSONArray("books");
                        jsonList2 = response.jsonObject.getJSONObject("results").getJSONArray("lists").getJSONObject(1).getJSONArray("books");
                        // Parse json array into array of model objects
                        // add songs into the results array
                        int jsonListLength = jsonList1.length();
                        ArrayList<Result> results;
                        if (jsonListLength > 0) {
                            if (jsonListLength > 2)
                                results = new ArrayList<>(Result.fromJson(jsonList1, "TrendingBook").subList(0, 2));
                            else
                                results = new ArrayList<>(Result.fromJson(jsonList1, "TrendingBook").subList(0, 1));
                            // Load model objects into the adapter
                            // add result through the adapter
                            allTrending.addAll(results);

                            jsonListLength = jsonList2.length();
                            if (jsonListLength > 0) {
                                if (jsonListLength > 2)
                                    results = new ArrayList<>(Result.fromJson(jsonList2, "TrendingBook").subList(0, 2));
                                else
                                    results = new ArrayList<>(Result.fromJson(jsonList2, "TrendingBook").subList(0, 1));
                                // Load model objects into the adapter
                                // add result through the adapter
                                allTrending.addAll(results);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                    Log.i(TAG, "Error, invalid JSON format.");
                    Toast.makeText(getContext(), "Error encountered, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                // Handle failed request here
                Log.e(TAG,
                        "Request failed with code " + statusCode + ". Response message: " + response);
            }
        });
    }

    private void queryTrending(){
        fetchTrendingMovies();
        fetchTrendingSongs();
        fetchTrendingBooks();
    }
}