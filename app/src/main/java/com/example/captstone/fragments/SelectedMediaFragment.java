package com.example.captstone.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.captstone.EndlessRecyclerViewScrollListener;
import com.example.captstone.R;
import com.example.captstone.modelAdapters.ReviewsAdapter;
import com.example.captstone.models.Result;
import com.example.captstone.models.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class SelectedMediaFragment extends Fragment {
    public static final String TAG = "SelectedMediaFragment";

    private TextView tvTitle;
    private TextView tvMediaType;

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
        return inflater.inflate(R.layout.fragment_selected_media, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = view.findViewById(R.id.tvTitle);
        tvMediaType = view.findViewById(R.id.tvMediaType);
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


        Bundle args = getArguments();
        if (args != null) {
            Result result = new Result();
            result = Parcels.unwrap(args.getParcelable("result"));
            tvTitle.setText(result.getTitle());
            mediaTitle = result.getTitle();
            tvMediaType.setText(result.getMediaType());
            Log.d(TAG, "onViewCreated: " + result.getTitle());
        }

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
                    if (review.getMediaTitle().equals(mediaTitle)) {
                        allReviews.add(review);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
