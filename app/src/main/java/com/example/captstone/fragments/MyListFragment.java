package com.example.captstone.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.captstone.EndlessRecyclerViewScrollListener;
import com.example.captstone.R;
import com.example.captstone.modelAdapters.ResultsAdapter;
import com.example.captstone.modelAdapters.ReviewsAdapter;
import com.example.captstone.models.Result;
import com.example.captstone.models.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class MyListFragment extends Fragment {
    private static final String TAG = "MyListFragment";

    private RecyclerView rvMyList;
    private LinearLayoutManager manager;
    private EndlessRecyclerViewScrollListener scrollListener;
    protected ResultsAdapter adapter;
    protected ArrayList<Result> myList;

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

        rvMyList = view.findViewById(R.id.rvReviews);

        myList = new ArrayList<>();
        adapter = new ResultsAdapter(getContext(), myList);

        rvMyList.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext());
        rvMyList.setLayoutManager(manager);

        // query my list
        queryMyList();

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            }
        };

        rvMyList.addOnScrollListener(scrollListener);
    }

    private void queryMyList() {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_USER);
        query.addDescendingOrder("createdAt");
        ArrayList<String> userMediaList = new ArrayList(Objects.requireNonNull(ParseUser.getCurrentUser().getList("userMediaList")));
        Log.i(TAG, "usermedialist: " + userMediaList);
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> reviews, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                HashSet<String> addedMedia = new HashSet<>();
                for (Review review : reviews) {
                    if (userMediaList.contains(review.getObjectId())) {
                        if (!addedMedia.contains(review.getMediaTitle())) {
                            addedMedia.add(review.getMediaTitle());
                            Log.i(TAG, "Review added to myList: " + review.getObjectId());
                            myList.add(Result.fromReview(review));
                        }
                        else {
                            Log.i(TAG, "Media already in myList: " + review.getObjectId());
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}