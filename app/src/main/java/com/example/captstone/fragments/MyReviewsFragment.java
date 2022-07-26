package com.example.captstone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.captstone.EndlessRecyclerViewScrollListener;
import com.example.captstone.LoginScreenActivity;
import com.example.captstone.R;
import com.example.captstone.modelAdapters.ReviewsAdapter;
import com.example.captstone.models.Result;
import com.example.captstone.models.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MyReviewsFragment extends Fragment {
    private static final String TAG = "MyReviewsFragment";

    private RecyclerView rvReviews;
    private LinearLayoutManager manager;
    private EndlessRecyclerViewScrollListener scrollListener;
    protected ReviewsAdapter adapter;
    protected List<Review> allReviews;

    private String mediaTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvReviews = view.findViewById(R.id.rvReviews);

        allReviews = new ArrayList<>();
        adapter = new ReviewsAdapter(getContext(), allReviews);

        rvReviews.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext());
        rvReviews.setLayoutManager(manager);

        // query reviews
        queryReviews();

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            }
        };

        rvReviews.addOnScrollListener(scrollListener);
    }

    private void queryReviews() {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_USER);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> reviews, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Review review : reviews) {
                    if (review.getUser().getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                        allReviews.add(review);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}