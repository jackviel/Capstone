package com.example.captstone.fragments;

import static com.parse.Parse.getApplicationContext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.captstone.EndlessRecyclerViewScrollListener;
import com.example.captstone.ParseApplication;
import com.example.captstone.R;
import com.example.captstone.ReviewsAdapter;

import com.example.captstone.cacheModels.ReviewCache;
import com.example.captstone.cacheModels.ReviewDao;
import com.example.captstone.models.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    public static final String TAG = "FeedFragment";

    private ReviewDao reviewDao;
    private RecyclerView rvReviews;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager manager;
    private SnapHelper snapHelper;

    protected ReviewsAdapter adapter;
    protected List<Review> allReviews;

    public FeedFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });

        rvReviews = view.findViewById(R.id.rvReviews);

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvReviews);

        // initialize the array that will hold posts and create a PostsAdapter
        allReviews = new ArrayList<>();
        adapter = new ReviewsAdapter(getContext(), allReviews);

        // set the adapter on the recycler view
        rvReviews.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext());
        rvReviews.setLayoutManager(manager);
        // set the layout manager on the recycler view
        // query reviews
        queryPosts();

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts();
            }
        };

        rvReviews.addOnScrollListener(scrollListener);

        reviewDao = ((ParseApplication) getApplicationContext()).getMyDatabase().reviewDao();
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        swipeContainer.setRefreshing(true);
        adapter.clear();
        queryPosts();
        swipeContainer.setRefreshing(false);
    }

    private void queryPosts() {
        if (isNetworkAvailable()) {
            ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
            //query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
            query.include(Review.KEY_USER);
            query.addDescendingOrder("createdAt");
            query.findInBackground(new FindCallback<Review>() {
                @Override
                public void done(List<Review> reviews, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting posts", e);
                        return;
                    }
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            for (Review review : reviews) {
                                ReviewCache reviewCache = new ReviewCache(review.getReviewTitle(), review.getReviewBody(), review.getUser().getUsername(), review.getCreatedAt().toString(), review.getMediaType(), review.getMediaTitle(), review.getMediaCreator());
                                reviewDao.insert(reviewCache);
                                allReviews.addAll(reviews);
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            });
        }
        else {
            Log.i(TAG, "no network");

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    List<ReviewCache> reviewCaches = reviewDao.recentItems();
                    List<Review> reviewsFromDB = ReviewCache.getReviewsList(reviewCaches);
                    adapter.addAll(reviewsFromDB);
                }
            });
            adapter.notifyDataSetChanged();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}