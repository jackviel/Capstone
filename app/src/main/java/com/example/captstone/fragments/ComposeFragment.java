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
import com.example.captstone.modelAdapters.ResultsAdapter;
import com.example.captstone.models.Result;
import com.example.captstone.models.Review;
import com.example.captstone.net.BookResultClient;
import com.example.captstone.net.MovieResultClient;
import com.example.captstone.net.MusicResultClient;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Headers;

public class ComposeFragment extends Fragment {

    public static final String TAG = "ComposeFragment";

    private EditText etReviewTitle;
    private EditText etReviewBody;
    public SearchView svMedia;
    public RecyclerView rvResults;
    public ProgressBar pbLoading;

    private ArrayList<Result> aResults;
    private ResultsAdapter resultsAdapter;

    private final BookResultClient bookResultClient = new BookResultClient();
    private final MovieResultClient movieResultClient = new MovieResultClient();
    private final MusicResultClient musicResultClient = new MusicResultClient();

    public Result selection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etReviewTitle = view.findViewById(R.id.etReviewTitle);
        etReviewBody = view.findViewById(R.id.etReviewBody);
        Button bSubmit = view.findViewById(R.id.bSubmit);
        pbLoading = view.findViewById(R.id.pbLoading);
        svMedia = view.findViewById(R.id.svMedia);
        rvResults = view.findViewById(R.id.rvResults);

        aResults = new ArrayList<>();
        resultsAdapter = new ResultsAdapter(getContext(), aResults);

        resultsAdapter.setOnItemClickListener(new ResultsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                // set selection to selected result
                selection = aResults.get(position);
            }
        });

        rvResults.setAdapter(resultsAdapter);

        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));

        svMedia.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Remove all results from the adapter
                aResults.clear();

                fetchBooks(query);
                fetchMovies(query);
                fetchSongs(query);

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
                                // add five books into the results array
                                int jsonResultsLength = jsonResults.length();
                                final ArrayList<Result> results;
                                if (jsonResultsLength > 0) {
                                    if (jsonResultsLength > 0)
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Book").subList(0, 1));
                                    else
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Book").subList(0, 1));
                                    // Load model objects into the adapter
                                    for (Result result : results) {
                                        aResults.add(result); // add result through the adapter
                                    }
                                    resultsAdapter.notifyDataSetChanged();
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
            private void fetchMovies(String query) {
                Log.i(TAG, "FETCHING MOVIES");
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
                                // add five movies into the results array
                                int jsonResultsLength = jsonResults.length();
                                final ArrayList<Result> results;
                                if (jsonResultsLength > 0) {
                                    if (jsonResultsLength > 0)
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Movie").subList(0, 1));
                                    else
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Movie").subList(0, 1));
                                    // Load model objects into the adapter
                                    for (Result result : results) {
                                        aResults.add(result); // add result through the adapter
                                    }
                                    resultsAdapter.notifyDataSetChanged();
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
            private void fetchSongs(String query) {
                Log.i(TAG, "FETCHING SONGS");
                musicResultClient.getResults(query, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON response) {
                        try {
                            JSONArray jsonResults;
                            if (response != null) {
                                // Get jsonResults
                                jsonResults = response.jsonObject.getJSONObject("results").getJSONObject("trackmatches").getJSONArray("track");
                                // Parse json array into array of model objects
                                // final ArrayList<Result> results = Result.fromJson(docs);
                                // add five songs into the results array
                                int jsonResultsLength = jsonResults.length();
                                final ArrayList<Result> results;
                                if (jsonResultsLength > 0) {
                                    if (jsonResultsLength > 0)
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Song").subList(0, 1));
                                    else
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Song").subList(0, 1));
                                    // Load model objects into the adapter
                                    for (Result result : results) {
                                        aResults.add(result); // add result through the adapter
                                    }
                                    resultsAdapter.notifyDataSetChanged();
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
        });

        bSubmit.setOnClickListener(v -> {
            Log.i(TAG, "onClick Submit button");
            String reviewTitle = etReviewTitle.getText().toString();
            String reviewBody = etReviewBody.getText().toString();

            if (reviewTitle.isEmpty()) {
                Toast.makeText(getContext(), "Title can't be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (reviewBody.isEmpty()) {
                Toast.makeText(getContext(), "Body can't be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            ParseUser currentUser = ParseUser.getCurrentUser();
            saveReview(reviewTitle, reviewBody, currentUser, selection.getMediaType(), selection.getTitle(), selection.getCreator());
        });

    }

    private void saveReview(String reviewTitle, String reviewBody, ParseUser currentUser, String mediaType, String mediaTitle, String mediaCreator) {
        Review review = new Review();
        review.setReviewTitle(reviewTitle);
        review.setReviewBody(reviewBody);
        review.setUser(currentUser);
        review.setMediaType(mediaType);
        review.setMediaTitle(mediaTitle);
        review.setMediaCreator(mediaCreator);
        pbLoading.setVisibility(ProgressBar.VISIBLE);
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving.", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Review save was successful.");
                etReviewTitle.setText("");
                etReviewBody.setText("");
                pbLoading.setVisibility(ProgressBar.INVISIBLE);
        }
    });
}

    }
