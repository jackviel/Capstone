package com.example.captstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.captstone.fragments.ReviewFeedFragment;
import com.example.captstone.fragments.HomeFragment;
import com.example.captstone.fragments.ProfileFragment;
import com.example.captstone.fragments.TrendingFeedFragment;
import com.example.captstone.fragments.TrendingFragment;
import com.example.captstone.modelAdapters.ResultsAdapter;
import com.example.captstone.models.Result;
import com.example.captstone.net.BookResultClient;
import com.example.captstone.net.MovieResultClient;
import com.example.captstone.net.MusicResultClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public final static String TAG = "MainActivity";

    private RecyclerView rvResults;
    private ProgressBar pbLoading;
    private SearchView svMedia;
    private SearchManager searchManager;

    private ArrayList<Result> aResults;
    private ResultsAdapter resultsAdapter;

    private final BookResultClient bookResultClient = new BookResultClient();
    private final MovieResultClient movieResultClient = new MovieResultClient();
    private final MusicResultClient musicResultClient = new MusicResultClient();

    public Result selection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Working Title :-)");

        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_feed:
                        getSupportActionBar().setTitle("Working Title :-)");
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_trending:
                        getSupportActionBar().setTitle("Trending Now");
                        fragment = new TrendingFeedFragment();
                        break;
                    case R.id.action_profile:
                        getSupportActionBar().setTitle("Profile");
                        fragment = new ProfileFragment();
                        break;
                    default:
                        getSupportActionBar().setTitle("Working Title :-)");
                        fragment = new ReviewFeedFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_feed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_trending, menu);

        rvResults = findViewById(R.id.rvResults);

        aResults = new ArrayList<>();
        resultsAdapter = new ResultsAdapter(getApplicationContext(), aResults);

        resultsAdapter.setOnItemClickListener(new ResultsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                // set selection to selected result
                selection = aResults.get(position);
                Log.i(TAG, "Clicked on: " + selection.getTitle());
                // launch detail fragment

            }
        });

        rvResults.setAdapter(resultsAdapter);

        rvResults.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        svMedia = (SearchView) menu.findItem(R.id.action_search).getActionView();
        svMedia.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        svMedia.setQueryHint("Search");
        svMedia.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Remove all results from the adapter
                aResults.clear();
                rvResults.setVisibility(View.VISIBLE);
                fetchMovies(query);
                fetchSongs(query);
                fetchBooks(query);
                // clear focus on search view so it doesn't fetch twice
                svMedia.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.trim().isEmpty()) {
                    // No query entered, remove all results from the adapter
                    rvResults.setVisibility(View.GONE);
                }

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
                                // add books into the results array
                                int jsonResultsLength = jsonResults.length();
                                final ArrayList<Result> results;
                                if (jsonResultsLength > 0) {
                                    if (jsonResultsLength > 2)
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Book").subList(0, 1));
                                    else
                                        results = null;
                                    // Load model objects into the adapter
                                    if (results != null) {
                                        for (Result result : results) {
                                            aResults.add(result); // add result through the adapter
                                        }
                                        resultsAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            // Invalid JSON format, show appropriate error.
                            e.printStackTrace();
                            Log.i(TAG, "Error, invalid JSON format.");
                            Toast.makeText(getApplicationContext(), "Error encountered, please try again.", Toast.LENGTH_SHORT).show();
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
                                // add movies into the results array
                                int jsonResultsLength = jsonResults.length();
                                final ArrayList<Result> results;
                                if (jsonResultsLength > 0) {
                                    if (jsonResultsLength > 2)
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Movie").subList(0, 1));
                                    else
                                        results = null;
                                    // Load model objects into the adapter
                                    if (results != null) {
                                        for (Result result : results) {
                                            aResults.add(result); // add result through the adapter
                                        }
                                        resultsAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            // Invalid JSON format, show appropriate error.
                            e.printStackTrace();
                            Log.i(TAG, "Error, invalid JSON format.");
                            Toast.makeText(getApplicationContext(), "Error encountered, please try again.", Toast.LENGTH_SHORT).show();
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
                                // add songs into the results array
                                int jsonResultsLength = jsonResults.length();
                                final ArrayList<Result> results;
                                Log.i(TAG, "onSuccess: length of jsonResults: " + jsonResultsLength);
                                if (jsonResultsLength > 0) {
                                    if (jsonResultsLength > 2)
                                        results = new ArrayList<>(Result.fromJson(jsonResults, "Song").subList(0, 1));
                                    else
                                        results = null;
                                    // Load model objects into the adapter
                                    if (results != null) {
                                        for (Result result : results) {
                                            aResults.add(result); // add result through the adapter
                                        }
                                        resultsAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            // Invalid JSON format, show appropriate error.
                            e.printStackTrace();
                            Log.i(TAG, "Error, invalid JSON format.");
                            Toast.makeText(getApplicationContext(), "Error encountered, please try again.", Toast.LENGTH_SHORT).show();
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

        return true;
    }
}