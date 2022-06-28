package com.example.captstone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.captstone.models.Review;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private List<Review> reviews;

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
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private TextView tvReviewBody;
        private TextView tvTimeAgo;
        private TextView tvReviewTitle;
        private TextView tvMediaTitle;
        private TextView tvMediaType;
        private ImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvReviewTitle = itemView.findViewById(R.id.tvReviewTitle);
            tvReviewBody = itemView.findViewById(R.id.tvReviewBody);
            tvMediaTitle = itemView.findViewById(R.id.tvMediaTitle);
            tvMediaType = itemView.findViewById(R.id.tvMediaType);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        }


        public void bind(Review review) {
            //Bind the review data to the view elements
            tvUsername.setText(review.getUser().getUsername());
            tvReviewBody.setText(review.getReviewBody());
            tvReviewTitle.setText(review.getReviewTitle());
            tvMediaTitle.setText(review.getMediaTitle());
            tvMediaType.setText(review.getMediaType());

            DateFormat formatter = new SimpleDateFormat("MMMM D");
            String date = formatter.format(review.getCreatedAt());
            int dateLength = date.length();
            tvTimeAgo.setText("Posted on " + date.substring(0, dateLength - 2) + date.substring(dateLength - 1, dateLength));

            ParseUser reviewUser = review.getUser();
            ParseFile profilePic = reviewUser.getParseFile("profilePic");
            if (profilePic != null) {
                Glide.with(context).load(profilePic.getUrl()).into(ivProfilePic);
            } else {
                Glide.with(context).load(R.drawable.default_profile_pic).into(ivProfilePic);
            }
        }
    }
}