package com.example.captstone.modelAdapters;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.captstone.R;
import com.example.captstone.models.Result;
import com.example.captstone.models.Review;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private static final String TAG = "ReviewsAdapter";

    private Context context;
    private List<Review> reviews;
    private HashSet<String> myList;
    private ArrayList<String> userMediaList;

    public ReviewsAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(review);
    }

    // Clean all elements of the recycler
    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Review> list) {
        reviews.addAll(list);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private TextView tvReviewBody;
        private TextView tvTimeAgo;
        private TextView tvMediaTitle;
        private TextView tvMediaType;
        private RatingBar rbRating;
        private ImageView ivProfilePic;
        private Button bAddToList;
        private ImageButton ibAddToList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
//            tvReviewBody = itemView.findViewById(R.id.tvReviewBody);
            tvMediaTitle = itemView.findViewById(R.id.tvMediaTitle);
            tvMediaType = itemView.findViewById(R.id.tvMediaType);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            bAddToList = itemView.findViewById(R.id.bAddToList);
            ibAddToList = itemView.findViewById(R.id.ibAddToList);
        }


        public void bind(Review review) {
            //Bind the review data to the view elements
            tvUsername.setText(review.getUser().getUsername());
//            tvReviewBody.setText(review.getReviewBody());
            tvMediaTitle.setText(review.getMediaTitle());
            tvMediaType.setText(review.getMediaType());
            rbRating.setRating(review.getReviewRating());

            ParseUser currentUser = ParseUser.getCurrentUser();
            myList = new HashSet<String>();
            userMediaList = new ArrayList<>(Objects.requireNonNull(currentUser.getList("userMediaList")));

            for (Review curReview : reviews) {
                if (userMediaList.contains(curReview.getObjectId())) {
                    myList.add(curReview.getMediaTitle());
                }
            }

            if (myList.contains(review.getMediaTitle())) {
                ibAddToList.setBackgroundResource(R.drawable.check_icon_material);
            } else {
                ibAddToList.setBackgroundResource(R.drawable.add_icon_material);
            }


            //Set the time ago text
            DateFormat df = new SimpleDateFormat("MMMM dd");
            String date = df.format(review.getCreatedAt());
            tvTimeAgo.setText("Review posted on " + date);

            ParseUser reviewUser = review.getUser();
            ParseFile profilePic = reviewUser.getParseFile("profilePic");
            if (profilePic != null) {
                Glide.with(context).load(profilePic.getUrl()).into(ivProfilePic);
            } else {
                Glide.with(context).load(R.drawable.default_profile_pic).into(ivProfilePic);
            }

            // double tap add to user's list
            bAddToList.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        Log.i(TAG, "DoubleTap");
                        String reviewId = review.getObjectId();

                        // add media review to list
                        if (!userMediaList.contains(reviewId)) {
                            userMediaList.add(review.getObjectId());

                            // refresh current ParseUser
                            currentUser.fetchInBackground();
                            // put new array with the new list entry
                            currentUser.put("userMediaList", userMediaList);
                            Log.i(TAG, "CurrentUser's updated list: " + userMediaList);
                            // save changes into parse
                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Error while saving", e);
                                    }
                                    Log.i(TAG, "Current User save was successful.");
                                    ibAddToList.setBackgroundResource(R.drawable.check_icon_material);
                                }
                            });
                        }
                        else
                            Log.i(TAG, "Media already on list!, nothing changed.");
                        return super.onDoubleTap(e);
                    }
                });
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return false;
                }
            });
            ibAddToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String reviewId = review.getObjectId();
                    // add media review to list
                    if (!userMediaList.contains(reviewId)) {
                        userMediaList.add(review.getObjectId());
                        // refresh current ParseUser
                        currentUser.fetchInBackground();
                        // put new array with the new list entry
                        currentUser.put("userMediaList", userMediaList);
                        Log.i(TAG, "CurrentUser's updated list: " + userMediaList);
                        // save changes into parse
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error while saving", e);
                                }
                                Log.i(TAG, "Current User save was successful.");
                                ibAddToList.setBackgroundResource(R.drawable.check_icon_material);
                            }
                        });
                    }
                    else
                        Log.i(TAG, "Media already on list!, nothing changed.");
                }
            });
        }
    }
    }