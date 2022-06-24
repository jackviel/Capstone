package com.example.captstone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.captstone.R;
import com.example.captstone.ResultAdapter;
import com.example.captstone.models.Result;
import com.example.captstone.models.Review;
import com.example.captstone.net.BookResultClient;
import com.example.captstone.net.MovieResultClient;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Headers;

public class ComposeFragment extends Fragment {

    public static final String TAG = "ComposeFragment";

    private EditText etBody;
    private Button bSubmit;
    public ProgressBar pb;
    public SearchView svMedia;
    public RecyclerView rvResults;
    private ArrayList<Result> aResults;
    private ResultAdapter resultAdapter;
    private BookResultClient bookResultClient;
    private MovieResultClient movieResultClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etBody = view.findViewById(R.id.etBody);
        bSubmit = view.findViewById(R.id.bSubmit);
        pb = view.findViewById(R.id.pbLoading);
        svMedia = view.findViewById(R.id.svMedia);
        rvResults = view.findViewById(R.id.rvResults);

        aResults = new ArrayList<>();
        resultAdapter = new ResultAdapter(getContext(), aResults);

        resultAdapter.setOnItemClickListener(new ResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(
                        getContext(),
                        "An item at position " + position + " clicked!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        rvResults.setAdapter(resultAdapter);

        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));

        svMedia.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Remove all results from the adapter
                aResults.clear();

                fetchBooks(query);
                fetchMovies(query);

                // clearing focus on Search View so the function doesn't run twice
                svMedia.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //fetchBooks(newText);
                return false;
            }

            private void fetchBooks(String query) {
                Log.i(TAG, "FETCHING BOOKS");
                bookResultClient = new BookResultClient();
                bookResultClient.getResults(query, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON response) {
                        try {
                            JSONArray jsonResults;
                            if (response != null) {
                                // Get the jsonResults
                                jsonResults = response.jsonObject.getJSONArray("docs");
                                // Parse json array into array of model objects
                                // final ArrayList<Result> results = Result.fromJson(docs);
                                // add five books bookResults array
                                int jsonResultsLength = jsonResults.length();
                                final ArrayList<Result> results;
                                if (jsonResultsLength > 0) {
                                    if (jsonResultsLength > 4)
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Book").subList(0, 5));
                                    else
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Book").subList(0, jsonResultsLength));
                                    // Load model objects into the adapter
                                    for (Result result : results) {
                                        aResults.add(result); // add result through the adapter
                                    }
                                    resultAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            // Invalid JSON format, show appropriate error.
                            e.printStackTrace();
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
            private void fetchMovies(String query) {
                Log.i(TAG, "FETCHING MOVIES");
                movieResultClient = new MovieResultClient();
                movieResultClient.getResults(query, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON response) {
                        try {
                            JSONArray jsonResults;
                            if (response != null) {
                                // Get jsonResults
                                jsonResults = response.jsonObject.getJSONArray("results");
                                // Parse json array into array of model objects
                                // final ArrayList<Result> results = Result.fromJson(docs);
                                // add five books bookResults array
                                int jsonResultsLength = jsonResults.length();
                                final ArrayList<Result> results;
                                if (jsonResultsLength > 0) {
                                    if (jsonResultsLength > 4)
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Movie").subList(0, 5));
                                    else
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Movie").subList(0, jsonResultsLength));
                                    // Load model objects into the adapter
                                    for (Result result : results) {
                                        aResults.add(result); // add result through the adapter
                                    }
                                    resultAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            // Invalid JSON format, show appropriate error.
                            e.printStackTrace();
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
        });

        bSubmit.setOnClickListener(v -> {
            Log.i(TAG, "onClick Submit button");
            String body = etBody.getText().toString();
            if (body.isEmpty()) {
                Toast.makeText(getContext(), "Description can't be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            ParseUser currentUser = ParseUser.getCurrentUser();
            savePost(body, currentUser);
        });

    }

    private void savePost(String description, ParseUser currentUser) {
        Review review = new Review();
        review.setBody(description);
        review.setUser(currentUser);
        pb.setVisibility(ProgressBar.VISIBLE);
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving.", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Review save was successful.");
                etBody.setText("");
                pb.setVisibility(ProgressBar.INVISIBLE);
        }
    });
}

    }
