package com.example.captstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.captstone.modelAdapters.ReviewsAdapter;
import com.example.captstone.models.Result;
import com.example.captstone.models.Review;
import com.google.android.material.card.MaterialCardView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class SelectedMediaActivity extends AppCompatActivity {
    private static final String TAG = "SelectedMediaActivity";

    private TextView tvTitle;
    private TextView tvMediaType;
    private TextView tvNoReviewsFound;
    private Button bAddReview;
    private Button bSubmitReview;
    private ProgressBar pbLoading;
    private EditText etReviewBody;
    private RatingBar rbRating;
    private MaterialCardView mcvAddReview;
    private RecyclerView rvReviews;
    private LinearLayoutManager manager;
    private EndlessRecyclerViewScrollListener scrollListener;
    protected ReviewsAdapter adapter;
    protected List<Review> allReviews;

    private boolean reviewsFound;

    private String mediaTitle;
    private String mediaType;
    private String mediaCreator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_media);

        getSupportActionBar().setTitle("Media Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tvMediaType = findViewById(R.id.tvMediaType);
        tvNoReviewsFound = findViewById(R.id.tvNoReviewsFound);
        bAddReview = findViewById(R.id.bAddReview);
        bSubmitReview = findViewById(R.id.bSubmitReview);
        pbLoading = findViewById(R.id.pbLoading);
        etReviewBody = findViewById(R.id.etReviewBody);
        rbRating = findViewById(R.id.rbRating);
        mcvAddReview = findViewById(R.id.mcvAddReview);
        rvReviews = findViewById(R.id.rvReviews);

        allReviews = new ArrayList<>();
        adapter = new ReviewsAdapter(this, allReviews);

        rvReviews.setAdapter(adapter);
        manager = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(manager);

        // query reviews
        queryReviews();

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            }
        };

        rvReviews.addOnScrollListener(scrollListener);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Result result = Parcels.unwrap(bundle.getParcelable("result"));
            mediaTitle = result.getTitle();
            tvTitle.setText(mediaTitle);
            mediaType = result.getMediaType();
            tvMediaType.setText(mediaType);
            mediaCreator = result.getCreator();
        }

        bAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show add review card
                rvReviews.setVisibility(View.GONE);
                mcvAddReview.setVisibility(View.VISIBLE);
            }
        });

        bSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: submitting review");
                String reviewBody = etReviewBody.getText().toString();
                if (reviewBody.isEmpty()) {
                    Toast.makeText(SelectedMediaActivity.this, "Please enter a review", Toast.LENGTH_SHORT).show();
                    return;
                }
                // save review
                saveReview(reviewBody, ParseUser.getCurrentUser(), mediaType, mediaTitle, mediaCreator, rbRating.getRating());

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void queryReviews() {
        reviewsFound = false;
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
                if (reviews.size() > 0) {
                    for (Review review : reviews) {
                        if (review.getMediaTitle().equals(mediaTitle)) {
                            allReviews.add(review);
                            reviewsFound = true;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                if (!reviewsFound) {
                    Log.i(TAG, "No reviews found");
                    tvNoReviewsFound.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void saveReview(String reviewBody, ParseUser currentUser, String mediaType, String mediaTitle, String mediaCreator, float reviewRating) {
        Review review = new Review();
        review.setReviewBody(reviewBody);
        review.setUser(currentUser);
        review.setMediaType(mediaType);
        review.setMediaTitle(mediaTitle);
        review.setMediaCreator(mediaCreator);
        review.setReviewRating(reviewRating);
        pbLoading.setVisibility(ProgressBar.VISIBLE);
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(SelectedMediaActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Review save was successful.");
                etReviewBody.setText("");
                pbLoading.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }
}